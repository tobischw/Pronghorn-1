---
title: "Appendables"
permalink: /docs/appendables/
toc: true
---
In order to allow for mostly garbage-free behavior, standard Java string concatenation using the "+" operator is not ideal. Pronghorn has its own Appendables class that allows for garbage-free, single pass creation of text.

The API follows a fluent pattern where every method returns the same Appendable which was passed in.
It is located in <code>PronghornPipes</code>.

### Examples

#### Append Array
Append an array to a StringBuilder
```java
byte[] a = new byte[1000];
Random r = new Random(101);
r.nextBytes(a);
StringBuilder str = new StringBuilder();
Appendables.appendArray(str, a);
```

#### Append Value
Append value(s) to an existing StringBuilder
```java
StringBuilder str = new StringBuilder();
Appendables.appendValue(str, "Label: ", 5, " -Suffix");
```

#### Append Fixed Decimal Digits
Append fixed decimals to a StringBuilder
```java
StringBuilder str = new StringBuilder();
Appendables.appendFixedDecimalDigits(str, -42,10 );
```

#### Append Base64-encoded String
Creates a Base64-encoded String and appends it
```java
String value = "Man is distinguished, not only by his reason, but by this singular passion from "
	    	+"other animals, which is a lust of the mind, that by a perseverance of delight "
	    	+"in the continued and indefatigable generation of knowledge, exceeds the short "
	    	+"vehemence of any carnal pleasure.";

byte[] b = value.getBytes();

StringBuilder str = new StringBuilder();
Appendables.appendBase64(str, b, 0, b.length, Integer.MAX_VALUE);
```

#### Append Base64-decoded String
Decodes a Base64 string and writes it to target
```java
String value = "TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlz"
               +"IHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2Yg"
               +"dGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGlu"
               +"dWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRo"
               +"ZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4=";

byte[] encBytes = value.getBytes();
byte[] target = new byte[value.length()];

Appendables.decodeBase64(encBytes, 0, encBytes.length, Integer.MAX_VALUE, target, 0, Integer.MAX_VALUE);
```

#### Append Epoch Time
Append an epoch time to a string
```java
StringBuilder str = new StringBuilder();
Appendables.appendEpochTime(str, 10000); //10 seconds
```

#### Append Hex Value
Convert an integer to its hex value and append that to the StringBuilder
```java
StringBuilder str = new StringBuilder();
int value = 400;

Appendables.appendHexDigits(str, value);
```

#### Append Hex Fixed Value
Convert an integer to its fixed hex value and append that to the StringBuilder
```java
StringBuilder str = new StringBuilder();
int value = 400;
int bits = 8;

Appendables.appendFixedHexDigits(str, value, bits);
```

#### Append Hex Array
Add hex array to the StringBuilder
```java
int[] a = new int[1000];
Random rand = new Random(101);

byte [] b = new byte[1000];
rand.nextBytes(b);

for(int i = 0;i<a.length;i++){
  a[i] = rand.nextInt(0x10) + 0x10;  // Generates a random number between 0x10 and 0x20
}

StringBuilder str = new StringBuilder();
Appendables.appendHexArray(str, 'L', a, 0, 0xFF, 'R', a.length);
```