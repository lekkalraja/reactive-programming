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

## Cold Observable vs Hot Observable

*  `Cold observables` are sequences that only emits item upon subscription. Each `observer` will have its own set of items emitted to them and depending on how the observable was created, will have different instances of emitted items.

```
        Observable<Long> interval = Observable.interval(1, TimeUnit.SECONDS);
        interval.subscribe(item -> System.out.printf("Observer - 1 : %s\n", item));
        Thread.sleep(5000);
        interval.subscribe(item -> System.out.printf("Observer - 2 : %s\n", item));
        Thread.sleep(10000);
        
        Observer - 1 : 1
        Observer - 1 : 2
        Observer - 1 : 3
        Observer - 1 : 4
        Observer - 1 : 5
        Observer - 2 : 0
        Observer - 1 : 6
        Observer - 2 : 1
        Observer - 1 : 7
        Observer - 2 : 2
        Observer - 1 : 8
        Observer - 2 : 3
        Observer - 1 : 9
        Observer - 2 : 4
```
* Regardless of whether the same observable instance is used, both observers started from 0. This is a `cold observable`. 
This happens because a different observable source is being created for each observer

* `Hot observables` emit items regardless of whether there are observers. 
In a hot observable, there is a single source of emission and depending on when observers subscribe, 
they may miss some of those emissions.

* A `ConnectableObservable` is a single observable source for different observers. 
The main difference aside from being a single observable source is that calling subscribe on a ConnectableObserver will not trigger emission, but connect.

```
        ConnectableObservable<Long> connectableObservable = Observable.interval(1, TimeUnit.SECONDS).publish();
        connectableObservable.connect();
        connectableObservable.subscribe(item -> System.out.printf("Observer - 3 : %s\n", item));
        Thread.sleep(5000);
        connectableObservable.subscribe(item -> System.out.printf("Observer - 4 : %s\n", item));
        Thread.sleep(10000);
        Observer - 3 : 0
        Observer - 3 : 1
        Observer - 3 : 2
        Observer - 3 : 3
        Observer - 3 : 4
        Observer - 3 : 5
        Observer - 4 : 5
        Observer - 3 : 6
        Observer - 4 : 6
        Observer - 3 : 7
        Observer - 4 : 7
        Observer - 3 : 8
        Observer - 4 : 8
        Observer - 3 : 9
        Observer - 4 : 9
```

## Observable Variants
    * Single => It always either emits one value or an error notification
    * Maybe => 0 or 1 Emission
    * Completable => No Emission
    
## Types of Operators
    * Suppressing Operators : suppress the emissions which fails to meet some specific criteria
    * Transforming Operators : Transforms the emissions, type of returned observable may not be the same
    * Reducing Operators : Take a series of emission and reduce them into single emission. Works with finite Observables
    * Collection Operators : Combine all the emission from upstream to some collection. Reduce emissions to a single collection.
    * Error-recovery Operators : Used to handle the errors and to recover from them
    * Action Operators : Used to do debugging in the observable chain
    
## Concurrency And Parallelization
 * The Observable Contract : `The emissions must be passed sequentially and one at a time`
 
 * To run Observer's parallel use `Schedulers`
 
## Schedulers
    * Computation Scheduler : `Schedulers.computation()` => `Number Of Threads = Number of Available Cores (Runtime.getRunTime().availableProcessors())`
    * IO Scheduler          : `Schedulers.io()`          => `Number Of Threads > Number of Available Cores`
    * NewThread Scheduler   : `Schedulers.newThread()`   => `Create 1 Thread per Observer and then destroy the thread when done!`
    * Single Scheduler      : `Schedulers.single()`      => `Create Only 1 Thread, so to run all the tasks sequentially on that thread`
    * Trampoline Scheduler  : `Schedulers.trampoline()`  => `Queue's work and execute them in a FIFO manner on one of the participating threads`
    * From ExecutorService  : `Schedulers.from(executorService)`
#### subscribeOn
  * Asynchronously subscribes Observer's to the current Observable on the specified Scheduler.
  
#### observeOn
  * Returns an Observable to perform the current Observable's emissions and notifications on a specified Scheduler, 
  asynchronously with an unbounded buffer with Flowable#bufferSize() "island size".
  
  
  
  