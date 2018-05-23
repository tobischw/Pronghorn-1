---
title: "Formatting Guide"
permalink: /catalog/format
toc: true
---
Below is an example of a schema:
### RawDataSchema
<table>
    <tr><th>Description</th><td>Used for raw data passing. Often used if no fitting schema is yet available.</td></tr>
    <tr><th>Class</th><td><code>RawDataSchema</code></td></tr>
</table>

***
Below is an example of a stage:
### ByteArrayEqualsStage
<table>
    <tr>
        <th>Description</th>
        <td>Takes an array and an input pipe.<br/>
            If the bytes match the raw bytes, wasEqual willl be true.<br/>
            For testing RawDataSchema
        </td>
    </tr>
    <tr>
        <th>Input</th>
        <td><a href="#rawdataschema"><code>RawDataSchema</code></a></td>
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
