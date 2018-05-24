---
title: "Quick Start Guide"
permalink: /docs/quick-start-guide/
excerpt: "Get started with Pronghorn immediately"
toc: true
---
## With PronghornRanch
### What you need
* Java 8
* [Maven](https://maven.apache.org/)
* Git (or any other way to download from GitHub)

To simplify creation of a Pronghorn project, we created PronghornRanch, a Maven archetype which generates a working Pronghorn example.

***


First, `clone` the PronghornRanch repository onto your machine:

```
git clone https://github.com/oci-pronghorn/PronghornRanch.git
```

Next, `cd` into the PronghornRanch directory and allow Maven to recognize and install the archetype:

```
cd PronghornRanch
mvn install
```

`cd` into the directory in which you want your new project to reside in, and run the following command:

```
mvn archetype:generate -DarchetypeGroupId=com.ociweb -DarchetypeArtifactId=PronghornRanch -DarchetypeVersion=0.0.1-SNAPSHOT
```

You will then be prompted to enter the following properties:

**Property**|**Default**|**Description**
-----|-----|-----
groupID|None|Type in `com.ociweb`
artifactID|None|Type in the name of your project
version|1.0-SNAPSHOT|Ignore
package|com.ociweb|Ignore

Then, hit enter or type in "Y" to continue. Press any other key to restart the process.

The project will then be generated for you.
Before you can start editing and compiling your code, make sure to run the following:

```
cd YOUR_PROJECT_NAME
mvn install
```

If all goes well, your Pronghorn project is now ready. Open up your project folder in your favorite IDE and start coding.

## Working with the Sample Project
Your newly generated project now contains a basic Pronghorn example and its source code. The example takes in a file by argument, creates a new graph, populates this graph with pipes and stages required to read, duplicate, and output the file, enables telemetry, and starts the default scheduler.

This will get you started with everything you need to write your own Pronghorn project.

| File                                       | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| ------------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **src/main/java/YOUR_PROJECT_NAME.java**       | Main class that creates and populates the graph and starts the scheduler. You will want to focus on this file the most.                                                                                                                                                                                                                                                                                                                                                                                         |
| **src/main/java/SchemaOneSchema.java**         | Auto-generated example schema. Not currently related to actual demo project functionality, but useful to look at in the future.                                                                                                                                                                                                                                                                                                                                                                                 |
| **src/test/java/YOUR_PROJECT_NAMETest.java**   | Test for the main class. It creates a new instance of YOUR_PROJECT_NAME, checks the graph, and then  verifies the output.                                                                                                                                                                                                                                                                                                                                                                                       |
| **src/test/java/SchemaTest.java**              | Test that simply asserts schema. Every schema needs this assertion. It verifies that the FAST XML is formatted correctly, and if it isn't, outputs generated code to be placed in the corresponding Schema Java file (not test file).                                                                                                                                                                                                                                                                           |
| **src/test/resources/SchemaOne.xml**           | This is an example of a FAST XML Schema. Pronghorn uses this protocol to define and enforce the format of data being passed between stages and pipes. These XML files are used as a template for the test to assert and generate the actual Schema java file. The SchemaOne.xml demonstrates various different types and values that will be used to communicate data (such as string, uint, etc...). Please refer to the [Schema page](../schemas) on how to create your own schemas.     |

### Tests
The project also includes 2 files for testing. The SchemaTest verifies that the schema is valid. The YOUR_PROGRAM_NAMETest ensures that ChunkedStream appear in the output and that the input pipe volume matches the output pipe volume.

## Without PronghornRanch
You are not required to use PronghornRanch to start your Pronghorn project. You can start a brand new Maven project and simply add the following dependency:

```xml
<dependency>
	<groupId>com.ociweb</groupId>
	<artifactId>Pronghorn</artifactId>
	<version>0.0.10-SNAPSHOT</version>
</dependency>
```

Add the following repository if you are using <1.0 release:
```xml
<repository>
	<releases>
		<enabled>false</enabled>
	</releases>
	<snapshots>
		<enabled>true</enabled>
	</snapshots>
	<id>repository-pronghorn.forge.cloudbees.com</id>
	<name>Active Repo for PronghornPipes</name>
	<url>http://repository-pronghorn.forge.cloudbees.com/snapshot/</url>
	<layout>default</layout>
</repository>
```