---
title: "Bloom Filter"
permalink: /docs/bloom-filter/
toc: true
---
A Bloom Filter (located in <code>Pronghorn</code>) is a space-efficient probabilistic data structure used in Pronghorn
for fast data lookup.

Pronghorn uses [MurmurHash](https://en.wikipedia.org/wiki/MurmurHash) for its hash-based lookups.

## Usage
```java
BloomFilter filter = new BloomFilter(n, p);
BloomFilter filter = new BloomFilter(template); // alternative
BloomFilter filter = new BloomFilter(a, b, intersection); // alternative
```
* `n` is the number of items in the filter
* `p` is the probability of false positives, float between 0 and 1 or a number indicating 1-in-p
* `template` is a previous BloomFilter that you can re-use
* `a` and `b` are previous bloom filters for intersection checking
* `intersection` determines if there is an intersection

## Example
```java
String[] messages = new String[] { "Moe", "Larry", "Curley" };
BloomFilter filter = new BloomFilter(1000, .00000001);

//build up the filter with the known values.
int i = messages.length;
while (--i>=0) {
    filter.addValue(messages[i]);
}

//check if it contains
if(filter.mayContain(messages[0])) {
    //do something
}
```