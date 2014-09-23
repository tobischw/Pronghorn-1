package com.ociweb.jfast.catalog.extraction;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.ociweb.jfast.error.FASTException;
import com.ociweb.jfast.field.TokenBuilder;
import com.ociweb.jfast.generator.DispatchLoader;
import com.ociweb.jfast.primitive.FASTOutput;
import com.ociweb.jfast.primitive.PrimitiveWriter;
import com.ociweb.jfast.primitive.adapter.FASTOutputTotals;
import com.ociweb.jfast.stream.FASTDynamicWriter;
import com.ociweb.jfast.stream.FASTEncoder;
import com.ociweb.jfast.stream.FASTRingBuffer;

public class ExtractorTest {
	
	private File testFile;
	
	
	@Before
	public void setupTestFile() {
		
		try {
			File f = File.createTempFile(this.getClass().getSimpleName(), "test");
			f.deleteOnExit();
			
			
			FileOutputStream out = new FileOutputStream(f);
			
			out.write("0,0,0,2.3,2.4,2.5,\"alpha\"\r\n".getBytes());
			out.write(",0,0,2.4,2.4,3.05,\"alpha\"\r\n".getBytes());
			out.write(",0,0,2.5,2.4,4.05,\r\n".getBytes());
			out.write("0,0,0,2.6,2.4,5.005,\"alpha\"\r\n".getBytes());
			
			out.write("0,0,0,2.6,2.4,\"alpha\"\r\n".getBytes());
			out.write("0,0,0,2.6,2.4,\"alpha\"\r\n".getBytes());
			out.write("0,0,0,2.6,2.4,\"alpha\"\r\n".getBytes());
			
			//TODO: build out all the test examples we need... 				
	
			
			out.close();
		
			testFile = f;
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	

    @Test
    public void extractTest() throws FileNotFoundException {
        
      //  FieldTypeVisitor visitor = new FieldTypeVisitor();
        
        int fieldDelimiter = (int)',';
                
        byte[] recordDelimiter = new byte[]{'\n'};
        
        int openQuote = (int)'"';        
        int closeQuote = (int)'"';
        
        int escape = (int)'/';
        
        
        ExtractionVisitor visitor = new ExtractionVisitor() {
            
            @Override
            public void closeFrame() {
            }
            
            @Override
            public void closeRecord(int startPos) {
            }
            
            @Override
            public void closeField() {
            }
            
            @Override
            public void appendContent(MappedByteBuffer mappedBuffer, int start, int limit, boolean contentQuoted) {
                                
                byte[] target = new byte[limit-start];
                ByteBuffer dup = mappedBuffer.duplicate();
                dup.position(start);
                dup.limit(limit);
                dup.get(target,0,limit-start);

                //TODO: add a test here
                
            }

            @Override
            public void openFrame() {
                // TODO Auto-generated method stub
                
            }
        };
              	
        FileChannel fileChannel = new RandomAccessFile(testFile, "rw").getChannel();
        
        Extractor ex = new Extractor(fieldDelimiter, recordDelimiter, openQuote, closeQuote, escape, 3); //8 byte page size
        
        try {
            ex.extract(fileChannel, visitor);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }


    }
    
    @Test
    public void fieldTypeExtractionTest() throws FileNotFoundException {
        
        
        int fieldDelimiter = (int)',';
                
        byte[] recordDelimiter = new byte[]{'\r','\n'};
        
        int openQuote = (int)'"';        
        int closeQuote = (int)'"';
        
        //Not using escape in this test file
        int escape = Integer.MIN_VALUE;
        
        RecordFieldExtractor typeAccum = new RecordFieldExtractor(RecordFieldValidator.ALL_VALID);   
        FieldTypeVisitor visitor = new FieldTypeVisitor(typeAccum);

        FileChannel fileChannel = new RandomAccessFile(testFile, "rw").getChannel();
        
        Extractor ex = new Extractor(fieldDelimiter, recordDelimiter, openQuote, closeQuote, escape, 20);
        
        try {
            ex.extract(fileChannel, visitor);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }                
          

    }
    
    @Test
    public void dataExtractionTest() throws FileNotFoundException {
        
        
        int fieldDelimiter = (int)',';
                
        byte[] recordDelimiter = new byte[]{'\r','\n'};
        
        int openQuote = (int)'"';        
        int closeQuote = (int)'"';
        
        //Not using escape in this test file
        int escape = Integer.MIN_VALUE;
        
        RecordFieldExtractor typeAccum = new RecordFieldExtractor(RecordFieldValidator.ALL_VALID);   
        
        final FieldTypeVisitor visitor1 = new FieldTypeVisitor(typeAccum); 
        
        byte[] catBytes = typeAccum.memoizeCatBytes();
        
        
        int writeBuffer = 16384;
        boolean minimizeLatency = false;
        FASTOutputTotals fastOutput =  new FASTOutputTotals();
		PrimitiveWriter writer = new PrimitiveWriter(writeBuffer, fastOutput , minimizeLatency);      		
		FASTEncoder writerDispatch = DispatchLoader.loadDispatchWriter(catBytes); //this is the first catalog that only knows catalogs
		
        
        System.err.println("Empty catalog before startup: "+ typeAccum.buildCatalog(true));
        
       
        FASTRingBuffer ringBuffer = new FASTRingBuffer((byte)20, (byte)24, null, null); //TODO: produce from catalog.
        final StreamingVisitor visitor2 = new StreamingVisitor(typeAccum, ringBuffer);

        final FileChannel fileChannel = new RandomAccessFile(testFile, "rw").getChannel();
        
        final Extractor ex = new Extractor(fieldDelimiter, recordDelimiter, openQuote, closeQuote, escape, 29);
        
        ///TOOD: need writer to extract ring buffer and write to stream.
        
        ExecutorService executor = Executors.newSingleThreadExecutor();
        
        Runnable extractRunnable = new Runnable() {
			@Override
			public void run() {
				try {
					ex.extract(fileChannel, visitor1, visitor2);  
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
			}        	
        };
        
        executor.execute(extractRunnable);
        
        
        executor.shutdown();
        
        while (!executor.isTerminated() || FASTRingBuffer.contentRemaining(ringBuffer)>0) {
        	FASTRingBuffer.dump(ringBuffer);
	        //if (FASTRingBuffer.moveNext(ringBuffer)) {
     //          if (queue.consumerData.isNewMessage()) {
        				//TODO: if this is a new catalog must load it and replace the dynamicWriter code
                    	///    FASTDynamicWriter dynamicWriter = new FASTDynamicWriter(writer, ringBuffer, writerDispatch);
            	//}
        	
		//          try{   
		//          dynamicWriter.write();
		//      } catch (FASTException e) {
		//          System.err.println("ERROR: cursor at "+writerDispatch.getActiveScriptCursor()+" "+TokenBuilder.tokenToString(queue.from.tokens[writerDispatch.getActiveScriptCursor()]));
		//          throw e;
		//      }    
	       // }
        }
        

        
        
        


    }
    
    
}
