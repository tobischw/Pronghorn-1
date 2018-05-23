---
title: "Stages"
permalink: /catalog/list-stages
toc: true
---
### ByteArrayEqualsStage
***
<table class="catalog-table">
    <tr>
        <th>Description</th>
        <td>Takes an array and an input pipe.<br/>
            If the bytes match the raw bytes, wasEqual willl be true.<br/>
            Used for testing <a href="list-schemas#rawdataschema"><code>RawDataSchema</code></a>.
        </td>
    </tr>
    <tr>
        <th>Input</th>
        <td>Pipe<<a href="list-schemas#rawdataschema"><code>RawDataSchema</code></a>></td>
    </tr>
    <tr>
        <th>Output</th>
        <td>NONE</td>
    </tr>
    <tr>
        <th>Project</th>
        <td>
            Pronghorn
        </td>
    </tr>
</table>

### ByteArrayProducerStage
***
<table class="catalog-table">
    <tr>
        <th>Description</th>
        <td>Takes an array of bytes and writes them to the output pipe once.
            Useful for testing <a href="list-schemas#rawdataschema"><code>RawDataSchema</code></a>.
        </td>
    </tr>
    <tr>
        <th>Input</th>
        <td>NONE</td>
    </tr>
    <tr>
        <th>Output</th>
        <td>Pipe<<a href="list-schemas#rawdataschema"><code>RawDataSchema</code></a>></td>
    </tr>
    <tr>
        <th>Project</th>
        <td>
            Pronghorn
        </td>
    </tr>
</table>

### ConsoleJSONDumpStage
***
<table class="catalog-table">
    <tr>
        <th>Description</th>
        <td>For some Schema&lt;<code>T</code>&gt; encode this data in JSON and write it to the target appendable.<br/>
            Can be set to assume that bytes are UTF8.<br/>
            The default output is <code>System.out</code>.
        </td>
    </tr>
    <tr>
        <th>Input</th>
        <td>
            Pipe<<code>Any</code>><br/>
        </td>
    </tr>
    <tr>
        <th>Output</th>
        <td>
            None<br/>
            Appendable (optional)
            Boolean <code>showBytesAsUTF</code> (optional)
        </td>
    </tr>
    <tr>
        <th>Project</th>
        <td>
            Pronghorn
        </td>
    </tr>
</table>

### ConsoleSummaryStage
***
<table class="catalog-table">
    <tr>
        <th>Description</th>
        <td>For some Schema&lt;<code>T</code>&gt; keeps running totals of each message type.
            Periodically reports the Number of each message type to target appendable.  Default target is <code>System.out</code>.
        </td>
    </tr>
    <tr>
        <th>Input</th>
        <td>
            Pipe<<code>Any</code>><br/>
        </td>
    </tr>
    <tr>
        <th>Output</th>
        <td>
            Appendable (optional)
        </td>
    </tr>
    <tr>
        <th>Project</th>
        <td>
            Pronghorn
        </td>
    </tr>
</table>
