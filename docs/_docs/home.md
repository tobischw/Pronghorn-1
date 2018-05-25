---
title: "Home"
permalink: /docs/home/
excerpt: "Intro page for the Pronghorn Documentation"
toc: true
---
Welcome to the Pronghorn documentation.

**This documentation is currently a work-in-progress. Check back later for more information regarding usage.**

To get started, please visit the [Quick Start guide](../quick-start-guide).

## Demo 
Below is recorded live footage of the telemetry of a Pronghorn project that encodes and decodes JPGs as demonstrated
[here](/Pronghorn/docs/jpg-raster).

<img style="width:250px;" src="/Pronghorn/assets/gifs/decoding-jpgs-1.gif" />
<img style="width:250px;" src="/Pronghorn/assets/gifs/encoding-jpgs-1.gif" />

For a real-world example of Pronghorn, please see [JPG-Raster](https://objectcomputing.github.io/Pronghorn/docs/jpg-raster/), a fast JPG encoder/decoder.

## What is Pronghorn?
Pronghorn is a pragmatic approach to an actor based framework. It is a staged event driven single machine embedded micro-framework written in Java, designed to be garbage-free, have a small memory footprint, and be as performant as possible.

## Who is Pronghorn for?
For a project built completely with Pronghorn, there is GreenLighting, an incredibly fast & lightweight microservices framework.

## What are the benefits of Pronghorn over similar frameworks/languages?
While Pronghorn is new to the industry, here are some frameworks and languages that have influenced Pronghorn and have similar goals:
* Akka
  * Unlike Akka, Pronghorn is completely garbage free.
* Play! Framework
* Erlang

## What is the expected usage?
Most projects using this framework will follow these steps:

1. **Define** your data flow graph.
2. **Define** the contracts between each stage.
3. **Test** first development by using generative testing as the graph is implemented.
