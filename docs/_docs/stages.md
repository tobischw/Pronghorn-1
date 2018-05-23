---
title: "Building Stages"
permalink: /docs/stages/
toc: true
---
![](https://github.com/objectcomputing/Pronghorn/raw/master/static/graph-update.png)

In graph theory, **stages** are better known as vertices or nodes, while **pipes** are known as edges or lines. These concepts are an integral part in the [Actor model](https://en.wikipedia.org/wiki/Actor_model). As an actor framework, Pronghorn has **stages** that can receive messages, make local decisions, process data, and respond to messages with new messages. Messages are passed through **pipes**, which allow for data to be shared between stages.

Thus, every stage can have an input and output pipe. The `PronghornStage` class is an abstract class that provides a protocol for creating custom stages. Please follow the guidelines below for writing correct Pronghorn code.

## Create your first Stage
Add a new Java class to your existing Pronghorn project (or [create a new project](https://github.com/objectcomputing/Pronghorn/wiki/getting-started)). If you prefer, create a new package in your project called "Stages" and place it there. Use the code below to help you get started:

```java
package com.ociweb.stages;

import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.RawDataSchema;
import com.ociweb.pronghorn.stage.PronghornStage;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;

public class ExampleStage extends PronghornStage {

    Pipe<RawDataSchema> input, output;

    public ExampleStage(GraphManager gm, Pipe<RawDataSchema> input, Pipe<RawDataSchema>  output) {

        super(gm, input, output);

        this.input = input;
        this.output = output;

    }

    public ExampleStage newInstance(GraphManager gm, Pipe<RawDataSchema> input, Pipe<RawDataSchema>  output) {

        return new ExampleStage(gm, input, output);

    }

    @Override
    public void startup() {

        // Initialize your objects and values here

    }

    @Override
    public void run() {

        // Put your stage logic here

    }

    // Only override this when you are certain that clean up needs to be performed before shutdown
    // Otherwise, do not use this
    @Override
    public void shutdown() {

        // Clean up

    }

}
```
Depending on the functionality of your stage, you may need to change ```RawDataSchema``` to the schema that your stage will be processing. More on this can be found on the [Schema](https://github.com/objectcomputing/Pronghorn/wiki/adding-custom-schemas) page.

* The constructor should be responsible for saving references to your pipes and defining [notas](#notas).

Every Pronghorn stage requires you to set the GraphManager. You are not, however, required to have output or input pipes - "Dangling pipes" are supported. If you choose to do so, `super()` will have to look similar to this:
```java
super(gm, NONE, NONE);
```
where `NONE` is a constant declaring an empty pipe.

* The static new instance method is Pronghorn convention for instantiating new stages. You are not required to have this, it exists primarily for readability reasons.

* The `startup()` method gets visited when your stage is requested to start.

Instead of allocating and initializing local objects and variables in the class constructor, do it instead in `startup()`. Any other settings except notas should be configured here as well.

* The `run()` method gets called depending on the current scheduled rate (which can be changed using [notas](#notas)).

Fine-tuning this rate can vastly improve performance and/or its ability to handle larger volume of data. Reading from input pipes, writing to output pipes, and performing the core logic of your stage should be done here.

Shutting down your stage is another important task to do in your `run()` method. Once your processing has finished, you need to let Pronghorn know that work is done and that `run()` should not be executed again. You do this using the `requestShutdown()` method. Do **not** call `shutdown()`.

### Producers
**Producers** are the start of your graph and are responsible for providing data to the rest of the stages. You can have multiple producers on the same graph for different subsets of your graph.

An example of a producer would be the `FileBlobReadStage`. It opens a file and passes the data onto a pipe into the next stage for further processing, e.g. to parse a certain value from the file or to replicate the file onto other stages.

## Using your Stage
To use and test your stage, it needs to be added to the current GraphManager.

```java
public class CoolPronghornApp {

  final GraphManager gm = new GraphManager();

  public static void main(String[] args) {

     populateGraph(gm);

     //turn on awesome free telemetry
     gm.enableTelemetry(7777);

     StageScheduler.defaultScheduler(gm).startup();

  }

  private static void populateGraph(GraphManager gm) {

     //Create test pipes using the standard RawDataSchema:
     Pipe<RawDataSchema> pipeA = RawDataSchema.instance.newPipe(10, 10_000);
     Pipe<RawDataSchema> pipeB = RawDataSchema.instance.newPipe(20, 20_000);

     //Pronghorn convention:
     ExampleStage.newInstance(gm, pipeA, pipeB);

     //If you chose the standard instantiation:
     //new ExampleStage(gm, pipeA, pipeB);

  }

}
```

## Notas
**Notas** are Pronghorns alternative to Java annotations. We chose this approach because it avoids reflection while still providing readability. Notas allow you to "tag" your stage to modify their behavior or appearance. Please note that notas can have unexpected side effects and should be applied carefully.

**Notas are always defined in the constructor of your stage.** They should not be defined in ```startup()``` or any other place in your class.

Some common nota definitions are listed here:

<table class="data-table">
    <tr>
        <th class="border-bottom">Name</th>
        <th class="border-bottom">Description</th>
        <th class="border-bottom">Example</th>
    </tr>
    <tr>
        <td>SCHEDULE_RATE</td>
        <td>Specify the delay between calls regardless of how long call takes in nanoseconds.</td>
        <td><code>
GraphManager.addDefaultNota(gm, GraphManager.SCHEDULE_RATE, 500);
</code></td>
    </tr>
    <tr>
        <td>MONITOR</td>
        <td>Used to tag internal stages. Do not use.</td>
        <td></td>
    </tr>
    <tr>
        <td class="border-bottom">PRODUCER</td>
        <td class="border-bottom">Tag your stage as a producer. Usually, producing stages are automatically detected, but use this nota if the producer is expecting a response from another stage.</td>
        <td class="border-bottom"><code>GraphManager.addNota(gm, GraphManager.PRODUCER, GraphManager.PRODUCER,  this);</code></td>
    </tr>
    <tr>
        <td class="border-bottom">STAGE_NAME</td>
        <td class="border-bottom">Give a custom name to a stage.</td>
        <td class="border-bottom"><code>GraphManager.addNota(graphManager, GraphManager.STAGE_NAME, "CustomStageName", this);</code></td>
    </tr>
    <tr>
        <td class="border-bottom">SLA_LATENCY</td>
        <td class="border-bottom">Define a custom Service Level Agreement in milliseconds. If your stage takes longer than this, a warning will appear.</td>
        <td class="border-bottom"><code>GraphManager.addNota(graphManager, GraphManager.SLA_LATENCY, 100_000_000, this);</code></td>
    </tr>
    <tr>
        <td class="border-bottom">LOAD_BALANCER</td>
        <td class="border-bottom">Even splits traffic across outputs for your stage.</td>
        <td class="border-bottom"><code>GraphManager.addNota(graphManager, GraphManager.LOAD_BALANCER, GraphManager.LOAD_BALANCER, this);</code></td>
    </tr>
    <tr>
        <td class="border-bottom">LOAD_MERGE</td>
        <td class="border-bottom">Consume equal priority traffic from inputs for your stage.</td>
        <td class="border-bottom"><code>GraphManager.addNota(graphManager, GraphManager.LOAD_MERGE, GraphManager.LOAD_MERGE, this);</code></td>
    </tr>
    <tr>
        <td class="border-bottom">HEAVY_COMPUTE</td>
        <td class="border-bottom">Tag your stage as "Heavy compute". The scheduler will <b>avoid</b> putting any stages tagged with this onto the same thread.</td>
        <td class="border-bottom"><code>GraphManager.addNota(graphManager, GraphManager.HEAVY_COMPUTE, GraphManager.HEAVY_COMPUTE, this);</code></td>
    </tr>
    <tr>
        <td class="border-bottom">TRIGGER</td>
        <td class="border-bottom">Limit rate or flow and triggers other stages.</td>
        <td class="border-bottom"></td>
    </tr>
    <tr>
        <td class="border-bottom">ROUTER_HUB</td>
        <td class="border-bottom">Tag your stage as a potential bottleneck for traffic.</td>
        <td class="border-bottom"></td>
    </tr>
    <tr>
        <td class="border-bottom">ISOLATE</td>
        <td class="border-bottom">Isolate your stage from its neighbors and put it on its own thread.</td>
        <td class="border-bottom"><code>GraphManager.addNota(graphManager, GraphManager.ISOLATE, GraphManager.ISOLATE, this);</code></td>
    </tr>
    <tr>
        <td class="border-bottom">DOT_RANK_NAME</td>
        <td class="border-bottom">Stages with the same name as defined here will be put next to each other on the graph.</td>
        <td class="border-bottom"><code>GraphManager.addNota(gm, GraphManager.DOT_RANK_NAME, "SocketReader", socketReaderStage);</code></td>
    </tr>
    <tr>
        <td class="border-bottom">DOT_BACKGROUND</td>
        <td class="border-bottom">Define a custom stage background color for telemetry.</td>
        <td class="border-bottom"><code>GraphManager.addNota(graphManager, GraphManager.DOT_BACKGROUND, "lavenderblush", this);</code></td>
    </tr>
    <tr>
        <td class="border-bottom">UNSCHEDULED</td>
        <td class="border-bottom">Will <b>not</b> execute <code>run()</code> on this stage, only <code>startup()</code> and <code>shutdown()</code>.</td>
        <td class="border-bottom"><code>GraphManager.addNota(graphManager, GraphManager.UNSCHEDULED, GraphManager.UNSCHEDULED, this);</code></td>
    </tr>
    <tr>
        <td class="border-bottom">THREAD_GROUP</td>
        <td class="border-bottom">Used to tag internal stages and track behavior. Do not use.</td>
        <td class="border-bottom"></td>
    </tr>
</table>

### Example
A stage constructor with a custom background color in the telemetry can look like this:
```java
public BMPDumperStage(GraphManager graphManager, Pipe<JPGSchema> input, boolean verbose, boolean time) {
	super(graphManager, input, NONE);
	this.input = input;

	// Use GraphManager to add a background nota to the current stage. "this" refers to the current stage.
	GraphManager.addNota(graphManager, GraphManager.DOT_BACKGROUND, "red", this);
}
```