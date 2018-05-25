---
title: "Web Cookbook"
permalink: /docs/web-cookbook/
toc: true
---
## What is it?
The web cookbook demonstrates how to perform REST calls, create proxy requests, utilize blocking calls against databases (H2 in this project), and
serve files from disk and the resource folder

## What do I need?
* Java 8
* Maven

## How to use it
Download the source code on the GitHub page ([here](https://github.com/oci-pronghorn/WebCookbook/blob/master/src/main/java/com/ociweb/WebCookbook.java)).
Extract the source and place it somewhere with write and read access on your system.

Run the following command in the directory with your source:
```
mvn install
```

This will install all the required dependencies.
You can then run the cookbook. Use the "files" or "-f" arguments to specify a folder where your web files are located.
By default, it uses the www folder in your HOME environment folder.