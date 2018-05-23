---
title: "Hi vs. Low APIs"
permalink: /docs/hi-vs-low/
toc: true
---
Below are tests for the High Level API and Low Level API on how to write to a pipe and verify its output.

### High-Level API
```java
@Test
void checkHiAPIConsoleJSONDumpFromPrepopulatedPipeData() throws IOException {

    GraphManager gm = new GraphManager();

    // Create the pipe that we will write to
    Pipe<SchemaOneSchema> inputPipe = SchemaOneSchema.instance.newPipe(50, 500);

    /* --------- Writing to inputPipe --------- */

    inputPipe.initBuffers(); // this is always required on a new pipe!

    //Do not mix High and low!
    if(PipeWriter.tryWriteFragment(inputPipe, SchemaOneSchema.MSG_SOMEOTHERMESSAGE_2)) {

         // Assign value to your fields defined in your schema
         // This is where the hi-level API shine - assign in any order, plus readable field names.
         PipeWriter.writeLong(inputPipe, SchemaOneSchema.MSG_SOMEOTHERMESSAGE_2_FIELD_ASIGNEDLONG_202, 500000);
         PipeWriter.writeInt(inputPipe, SchemaOneSchema.MSG_SOMEOTHERMESSAGE_2_FIELD_ASIGNEDINT_103, 1000);

         // Publish the results
         PipeWriter.publishWrites(inputPipe);

         PipeWriter.publishEOF(inputPipe);

    } else {
         fail("There was no room in the pipe for a write in hi-level");
    }

    /* --------- Example stage that dumps inputPipe and writes to StringBuilder --------- */

    StringBuilder sb = new StringBuilder();

    PronghornStage consoleJSONDumpStage = ConsoleJSONDumpStage.newInstance(gm, inputPipe, sb);

    consoleJSONDumpStage.startup();
    consoleJSONDumpStage.run();
    consoleJSONDumpStage.shutdown();

    // Wait until consoleJSONDumpStage is done
    GraphManager.blockUntilStageTerminated(gm, consoleJSONDumpStage);

    /* --------- Assert that the message is correctly being dumped --------- */

    assertEquals("{\"SomeOtherMessage\":  {\"ASignedInt\":1000}  {\"ASignedLong\":500000}}\n", sb.toString());

}
```

### Low-Level API
```java
@Test
void checkLowAPIConsoleJSONDumpFromPrepopulatedPipeData() throws IOException {

    GraphManager gm = new GraphManager();

    // Create the pipe that we will write to
    Pipe<SchemaOneSchema> inputPipe = SchemaOneSchema.instance.newPipe(50, 500);

    /* --------- Writing to inputPipe --------- */

    inputPipe.initBuffers(); // this is always required on a new pipe!

    // First, assert that we have room to actually write
    assertTrue(Pipe.hasRoomForWrite(inputPipe));

    // Get the size of the schema for the inputPipe
    int size = Pipe.addMsgIdx(inputPipe, SchemaOneSchema.MSG_SOMEOTHERMESSAGE_2);

    // Write values to the pipe. Since we are using the low-level API, these need to be written in-order!
    Pipe.addIntValue(1000, inputPipe);
    Pipe.addLongValue(500000, inputPipe);

    // Confirm & publish
    Pipe.confirmLowLevelWrite(inputPipe, size);
    Pipe.publishWrites(inputPipe);

    Pipe.publishEOF(inputPipe);

    /* --------- Example stage that dumps inputPipe and writes to StringBuilder --------- */

    StringBuilder sb = new StringBuilder();

    PronghornStage consoleJSONDumpStage = ConsoleJSONDumpStage.newInstance(gm, inputPipe, sb);

    consoleJSONDumpStage.startup();
    consoleJSONDumpStage.run();
    consoleJSONDumpStage.shutdown();

    // Wait until consoleJSONDumpStage is done
    GraphManager.blockUntilStageTerminated(gm, consoleJSONDumpStage);

    /* --------- Assert that the message is correctly being dumped --------- */

    assertEquals("{\"SomeOtherMessage\":  {\"ASignedInt\":1000}  {\"ASignedLong\":500000}}\n", sb.toString());

}
```