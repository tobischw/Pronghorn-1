---
title: "Trie Parser"
permalink: /docs/trie-parser/
toc: true
---
Almost all of Pronghorn's string manipulation utilizes a [bitwise trie](https://en.wikipedia.org/wiki/Trie).
It is called the <code>TrieParser</code> and is used in route matching, JSON parsing, HTTP header parsing, and much more.

Utilize the TrieParser for fast parsing and matching in your Pronghorn projects.

## Example
```java
// Define a tag. This number is an ID used to associate with certain matches. Make sure this is unique.
final int tagIndex = 120;

// Create a new TrieParser
TrieParser map = new TrieParser();

// Define your matches here
// Use %b for bytes and strings
map.setUTF8Value("<strong>%b</strong>", tagIndex);

// Create a new reader. Since we already know all of our content, make sure to set alwaysCompletePayloads to true
TrieParserReader reader = new TrieParserReader(true);

// Our example string. Since it has to be in bytes, use CharSequenceToUTF8Local to convert to a String.
CharSequenceToUTF8Local.get().convert("Wow, it is a beautiful day <strong>today</strong>!").parseSetup(reader);

// This will store the output
StringBuilder tag = new StringBuilder();

// Now do the actual parsing. Make sure we have content and check if we found our tagIndex
while (TrieParserReader.parseHasContent(reader)) {
	long val = TrieParserReader.parseNext(reader, map); // The ID the parser found

	// The parser found our index
	if(val == tagIndex) {
		// Capture it and output it to the StringBuilder
		TrieParserReader.capturedFieldBytesAsUTF8(reader, 0, tag);
	} else {
		// Skip over anything else
		TrieParserReader.parseSkipOne(reader);
	}
}

System.out.println("This is what's inside the strong tag: " + tag);
```

## Supported Types
<table>
    <tr>
        <td>Name</td>
        <td>Description</td>
        <td>Symbol</td>
    </tr>
    <tr>
        <td>Optional Signed Integer</td>
        <td>Optional signed int, if absent returns zero</td>
        <td><code>%o</code></td>
    </tr>
    <tr>
        <td>Signed Integer</td>
        <td>Signed Integer (may be hex if starts with 0x)</td>
        <td><code>%i</code></td>
    </tr>
    <tr>
        <td>Unsigned Integer</td>
        <td>Unsigned Integer (may be hex if starts with 0x)</td>
        <td><code>%u</code></td>
    </tr>
    <tr>
        <td>Signed Hex</td>
        <td>Signed Integer (may skip prefix 0x, assumed to be hex)</td>
        <td><code>%I</code></td>
    </tr>
    <tr>
        <td>Unsigned Hex</td>
        <td>Unsigned Integer (may skip prefix 0x, assumed to be hex)</td>
        <td><code>%U</code></td>
    </tr>
    <tr>
        <td>Decimal</td>
        <td>If found capture u and places else captures zero and 1 place</td>
        <td><code>%.</code></td>
    </tr>
    <tr>
        <td>Rational</td>
        <td>If found capture i else captures 1</td>
        <td><code>%/</code></td>
    </tr>
    <tr>
        <td>Bytes/String</td>
        <td>Captures bytes and Strings</td>
        <td><code>%b</code></td>
    </tr>
</table>