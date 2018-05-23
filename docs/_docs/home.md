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
[here](/docs/jpg-raster).

![Decoding JPGs GIF](Pronghorn/assets/gifs/decoding-jpgs-1.gif "Decoding JPGs")
![Encoding JPGs GIF](Pronghorn/assets/gifs/encoding-jpgs-1.gif "Encoding JPGs")

## Overview
1. [What is Pronghorn?](#what-is-pronghorn)
2. [Who is Pronghorn for?](#who-is-pronghorn-for)
3. [What are the benefits of Pronghorn over similar frameworks?](#what-are-the-benefits-of-pronghorn-over-similar-frameworks)
4. [What is the expected usage?](#what-is-the-expected-usage)

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
