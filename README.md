# reactive-programming
Reactive Programming with RxJava 3.x

# Reactive Manifesto
  `URL : https://www.reactivemanifesto.org/`
  
  * Today applications are deployed on everything from mobile devices to cloud-based clusters running thousands of multi-core processors.
  Users expect millisecond response times and 100% uptime. Data is measured in Petabytes.
  Today's demands are simply not met by yesterday’s software architectures.
  
  * we want systems that are Responsive, Resilient, Elastic and Message Driven. 
  We call these Reactive Systems.
  
    * `Responsive` : Responsive systems focus on providing rapid and consistent response times, establishing reliable upper bounds so they deliver a consistent quality of service
    * `Resilient`  : The system stays responsive in the face of failure.
    * `Elastic`    : The system stays responsive under varying workload.
    * `Message Driven` : Reactive Systems rely on asynchronous message-passing to establish a boundary between components that ensures loose coupling, isolation and location transparency.
    
    
# RxJava
RxJava is a Java VM implementation of Reactive Extensions: a library for composing asynchronous and event-based programs by using observable sequences.
It extends the `observer pattern` to support sequences of data/events and adds operators that allow you to compose sequences together declaratively while abstracting away concerns about things like low-level threading, synchronization, thread-safety and concurrent data structures.

* RxJava works on PUSH rather than Pull
* Different channels for different signals
* RxJava can compose Events by operators
* RxJava uses Schedulers (concurrent/parallel processing)
* BackPressure using Flowable