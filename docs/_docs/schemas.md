---
title: "Defining Schemas"
permalink: /docs/schemas/
toc: true
---
Pronghorn enforces data consistency for stage communication by using a fork of the XML based [FAST Protocol](https://www.fixtrading.org/standards/fast/) called PHAST. Unit tests (that every Schema requires) act as a contract between the Java Schema and the PHAST file to ensure correctness.

For a more technical overview of PHAST, please see the [specifications](https://github.com/objectcomputing/Pronghorn/blob/master/PhastSpecification.md).

## Adding your own Schema
Pronghorn allows you to generate schema code based on an XML file. This saves time and allows rapid prototyping. Use the following list as a general guideline to get started on adding your own schemas.

**1. Realize what the data looks like**

Concern yourself with data flow first. For example, draw out the data flow on a whiteboard - enumerate what type of messages go down the pipes and what fields they need.

**2. Define your XML schema as demonstrated in the Example Schema below**

We use PHAST, a derivative of FAST, to define our messages. Create a new XML file in the `test/resources` folder of your new PronghornRanch project, formatted as `SchemaNAME.xml`. Edit the Example Schema to get started, using grouping, descriptive field names, and appropriate types as enumerated in Step 1.

**Note:** There is a known issue regarding shorts in the parser (they do not get generated). For now, use integers instead.

***Example Schema***
```xml
<?xml version="1.0" encoding="UTF-8"?>
<templates xmlns="http://www.fixprotocol.org/ns/fast/td/1.1">
   <template name="SomeKindOfMessage" id="1">
     <uInt32 name="AnUnsignedInt" id="101"/>
     <uInt64 name="AnUnsignedLong" id="201"/>
     <byteVector name="AnArrayOfBytes" id="301"/>
   </template>
   <template name="SomeOtherMessage" id="2">
     <int32 name="ASignedInt" id="102"/>
     <int64 name="ASignedLong" id="202"/>
   </template>
</templates>
```

**3. Create a basic unit test**

You do not have to manually generate the schema java file. Once your test runs, it will fail but output the required Java file in the console that you then paste into your `SchemaNAME.java` located in `main/java/SchemaNAME.java`.
See the example below or [generate a PronghornRanch project](../quick-getting-started).


***Example Schema Test***
```java
package com.ociweb;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import com.ociweb.SchemaOneSchema;
import com.ociweb.pronghorn.pipe.util.build.FROMValidation;

public class SchemaTest {

    @Test
    public void messageClientNetResponseSchemaFROMTest() {

        assertTrue(FROMValidation.checkSchema("/SchemaOne.xml", SchemaOneSchema.class));

    }

}
```

**4. Use your Schema**

Your schema is now ready for use. You can now define pipes using your schema. See [Building Custom Stages](../stages/).

## Supported Types
**Note:** PHAST does not support all types specified in FAST 1.0.

|Name       |Tag                    |Notes    |
|-----------|-------------------------|---------|
|Byte Vector|<byteVector...|An array of bytes (useful for data)|
|String|<string...|Use attribute charset="utf-8" to support unicode instead of ASCII|
|Unsigned Integer|<uint32...||
|Unsigned Long|<uint64...||
|Unsigned Short|<uint16...|Currently unsupported|
|Signed Integer|<int32...||
|Signed Long|<int64...||
|Signed Short|<int16...|Currently unsupported|