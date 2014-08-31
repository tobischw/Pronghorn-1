package com.ociweb.jfast.mux;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.Pipe;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

import com.ociweb.jfast.catalog.loader.ClientConfig;
import com.ociweb.jfast.catalog.loader.TemplateLoader;
import com.ociweb.jfast.generator.DispatchLoader;
import com.ociweb.jfast.primitive.FASTInput;
import com.ociweb.jfast.primitive.PrimitiveReader;
import com.ociweb.jfast.primitive.adapter.FASTInputByteBuffer;
import com.ociweb.jfast.primitive.adapter.FASTInputSourceChannel;
import com.ociweb.jfast.stream.FASTDecoder;
import com.ociweb.jfast.stream.FASTInputReactor;
import com.ociweb.jfast.stream.FASTRingBuffer;
import com.ociweb.jfast.stream.RingBuffers;

public class MuxTest {

    
    @Test
    public void loadFile() {
        
        byte[] catBytes = buildRawCatalogData(new ClientConfig());
        
        URL sourceData = getClass().getResource("/performance/complex30000.dat");
        File sourceDataFile = new File(sourceData.getFile().replace("%20", " "));
       
        
        //Note: need better aproach
        //need to have these bytes read in while parseing the preamble. the 
        //FASTInput objects (n of these) need to be given the new location in the array to read from
        //TODO  B, this is sequential and use FASTInput that can use NIO changes that are made up of multiple sources.
        //TODO B, must compute max parallel in config, Minimum readers is 1 what is the maximum?
        //Must be less than cores or acceptable threads.
        //Must be limited by the maximum parallel structure implied by the catalog. need new compute in catalog for this.
        //Any reset message is isolated and does not limit, any messages that share dictionary fields must be done in one decoder.
        //once decoders read the fields the ring buffers must reconstruct the messages in order.
        //the decode must write to ring buffer the ids of each ring buffer used then this meta buffer is read to read in the right order by the conusmer.
        //TODO: B, need fake ring buffer that merges others, Zip FlatMap?
        
        //FASTInputByteBuffer input = buildInputForTestingByteBuffer(sourceDataFile);
        
        
        //TODO: put these into decoder
        int channels = 4;        
        FASTInput[] targetChannel = new FASTInput[channels];        
        WritableByteChannel[] writableChannel = new WritableByteChannel[channels];
        
        
        int reactors = 3;
        final ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(reactors);       
   //     final AtomicBoolean isAlive = reactor.start(executor, reader);
        
        
        
        FASTRingBuffer decoderIdQueue = new FASTRingBuffer((byte)20,(byte)22,null,null);
        
        
        
        Pipe pipe;
        try {
            pipe = Pipe.open();
        
            // gets the pipe's sink channel
            writableChannel[0] = pipe.sink();          
            
            targetChannel[0] = new FASTInputSourceChannel(pipe.source());
            
            int maxPMapCountInBytes=32;
            PrimitiveReader reader = new PrimitiveReader(2048, targetChannel[0], maxPMapCountInBytes);
            
            FASTDecoder readerDispatch = DispatchLoader.loadDispatchReader(catBytes); 
            FASTInputReactor reactor = new FASTInputReactor(readerDispatch,reader);
            
            reactor.start(executor, reader);
        
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        
        int loops = 100;
        
        long totalTestBytes = sourceDataFile.length();
        System.err.println("File size:"+totalTestBytes);
        
        long begin = System.nanoTime();
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(sourceDataFile, "r");
            randomAccessFile.seek(0);
            
            begin = System.nanoTime();
            int preamble;
            
            FileChannel fc = randomAccessFile.getChannel();
            MappedByteBuffer mem = fc.map(FileChannel.MapMode.READ_ONLY, 0, totalTestBytes);
                            
            
            
           int j = loops;
           while (--j>=0) {
            
                mem.position(0);
                
                do { 
                    //this should be the size of this block
                    preamble = mem.get();
                    preamble |= mem.get()<<8;
                    preamble |= mem.get()<<16;
                    preamble |= mem.get()<<24;
                    int newPos = mem.position()+preamble;
                    //peek ahead at the templateId which will be after the pmap
                    
                    int pmap =  mem.get();
                    int templateId = readIntegerUnsigned(mem);
                    
                    //message 1 resets so do them separate
                    if (templateId==1) {
                        //round robin on 3 of these
                                                
                        
                        //TODO: will only work if encoder is, consuming the bytes from FASTInput
                         //      fc.transferTo(mem.position(), preamble, writableChannel);//does zero copy when it can
                        
                    } else {  //all others 
                        //TODO: will only work if encoder is, consuming the bytes from FASTInput
                            //   fc.transferTo(mem.position(), preamble, writableChannel);//does zero copy when it can    
                        
                    }
                    //store which encoder was used here so that the reader can join this data together.
                    //decoderIdQueue

                    
                    
                    mem.position(newPos);//now at start of next block or end of file
                                      
                    
                
                   // System.err.println(mem.remaining());
                }while (mem.remaining()>0);
                
           }         
            
            fc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        long duration = System.nanoTime()-begin;
        
        long bytes = totalTestBytes*loops;
        
        //need MB per second
        double bytesPerSecond = ( 1000000000d * bytes)/(double)duration;
        System.err.println((bytesPerSecond/ (1<<20)) + " MBps");
        System.err.println(duration+" ns");
        
    }
    
    
    public static int readIntegerUnsigned(MappedByteBuffer mem) {//Invoked 100's of millions of times, must be tight.
        byte v = mem.get();
        return (v < 0) ? (v & 0x7F) : readIntegerUnsignedTail(v,mem);

    }

    //TODO: C, add overflow flag to support optional int that is outside 32 bits. Without this we dont quite match the spec.
    
    //recursive use of the stack turns out to be a good way to unroll this loop.
    private static int readIntegerUnsignedTail(int a, MappedByteBuffer mem) {
        byte v = mem.get();
        return (v<0) ? (a << 7) | (v & 0x7F) : readIntegerUnsignedTail((a<<7)|v,mem);
    }
    
    private FASTInputByteBuffer buildInputForTestingByteBuffer(File sourceDataFile) {
        long totalTestBytes = sourceDataFile.length();
        FASTInputByteBuffer fastInput = null;
        try {
            FileChannel fc = new RandomAccessFile(sourceDataFile, "r").getChannel();
            MappedByteBuffer mem = fc.map(FileChannel.MapMode.READ_ONLY, 0, totalTestBytes);
            fastInput = new FASTInputByteBuffer(mem);
            fc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fastInput;
    }
    
    
    public static byte[] buildRawCatalogData(ClientConfig clientConfig) {
        //this example uses the preamble feature
        clientConfig.setPreableBytes((short)4);

        ByteArrayOutputStream catalogBuffer = new ByteArrayOutputStream(4096);
        try {
            TemplateLoader.buildCatalog(catalogBuffer, "/performance/example.xml", clientConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue("Catalog must be built.", catalogBuffer.size() > 0);

        byte[] catalogByteArray = catalogBuffer.toByteArray();
        return catalogByteArray;
    }
    
}
