---
title: "Features"
permalink: /docs/features/
toc: true
---

**1. Broad compatibility**
* Java profile compact1
* No use of Unsafe, Java 9 module compatibility

**2. Simple concurrency model**
* Quickly write correct code with actors
* Easily leverage hundreds or more cores
* All pipes are defined as produced from one actor and consumed by one other

**3. Separation of design concerns**
* Business aware scheduling

   * Actors who do not have work are not scheduled
   * Schedulers can be custom designed or existing solutions applied
* Strong types generated externally
   * Types between actors are externally defined
   * New fields can be added and mapped to new business specific usages

**4. Multiple APIs**
* Embedded friendly use of wrapping arrays
* Integrate with a wide variety of existing interfaces
* Visitors for reading and writing
* Object proxies for reading and writing
* Zero copy direct access to input and output fields
* Replay of messages until they are released

**5. Simple debug and refactoring**
* Messages have full provenance and actor chain upon exception
* Test framework supports automated regression test construction for refactor
* Fuzz testing based on message pipes definitions
* Generative contract testing based on behavior definitions for stages

**6. Static memory allocations**
* No need to release memory and no GC
* Simplify memory usage analysis of the application
* Minimize runtime failures, including out of memory

**7. Copy preferred over lock usage**
* No stalled cores, block free, wait free, continuous progress
* Efficient power usage
* Leverages new fast memory subsystems
* Enables efficient NUMA usage

**8. Sequential memory usage**
* Leverages CPU pre-fetch and caches for fastest possible throughput
* Persistence and immediate start up for free with non-volatile memory
* All media is sequential, mechanical sympathy
* Maximum use of hardware bandwidth

**9. Software sketches**
* Extensive requirements gathering put into a graph
* Involve non-technical people in the early stages
* Refine the design before making any commitments, or beginning iterations

**10. Minimized deployed application**
* For embedded systems, only the needed applications and interfaces are deployed
* Configuration is done at compile time
* Ultra-small attack surface
* Scales well in docker and cloud deployments
* Targets absolute minimum resources consumed