package com.reactive.rxjava;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrencyAndParallelization {

    public static void main(String[] args) throws InterruptedException {
        /**
         * Observer's on this Observable will run on same thread i.e. main thread
         */
        var sequentialObservable = Observable.create(emitter -> {
            emitter.onNext("Hello");
            emitter.onNext("RxJava");
            emitter.onComplete();
        });

        sequentialObservable.subscribe(item -> System.out.printf("Sequential Observer 1 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        sequentialObservable.subscribe(item -> System.out.printf("Sequential Observer 2 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(1000);
        System.out.println("===========================================================================================");

        /**
         * Observer's on this Observable will get a separate thread for each observer and will execute parallel
         */
        var parallelObservable = Observable.create(emitter ->
            new Thread(() -> {
                emitter.onNext("Hello");
                emitter.onNext("RxJava");
                emitter.onComplete();
            }).start()
        );

        parallelObservable.subscribe(item -> System.out.printf("Parallel Observer 1 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        parallelObservable.subscribe(item -> System.out.printf("Parallel Observer 2 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(1000);
        System.out.println("===========================================================================================");

        var accountObservable = Observable.just("Apply Account", "Validate Details", "Create Account", "Withdraw Amount", "Check Authorization", "Dispense Amount");


        /**
         * flatMap() -> To Achieve Concurrency
         */

        accountObservable
                .flatMap(item -> Observable.fromArray(item.split(" "))
                        .subscribeOn(Schedulers.computation())
                        .map(word -> String.format("FlatMap Observable Item : %s on Thread : %s", word, Thread.currentThread().getName())))
                .subscribe(System.out::println);
        Thread.sleep(1000);
        System.out.println("===========================================================================================");


        // RxComputationThreadPool
        Observable<String> computationTasks = accountObservable.subscribeOn(Schedulers.computation());

        System.out.printf("Available Core Processors : %s \n", Runtime.getRuntime().availableProcessors());
        computationTasks.subscribe(item -> System.out.printf("Computational Observer 1 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        computationTasks.subscribe(item -> System.out.printf("Computational Observer 2 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        computationTasks.subscribe(item -> System.out.printf("Computational Observer 3 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        computationTasks.subscribe(item -> System.out.printf("Computational Observer 4 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        computationTasks.subscribe(item -> System.out.printf("Computational Observer 5 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        computationTasks.subscribe(item -> System.out.printf("Computational Observer 6 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        computationTasks.subscribe(item -> System.out.printf("Computational Observer 7 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        computationTasks.subscribe(item -> System.out.printf("Computational Observer 8 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        computationTasks.subscribe(item -> System.out.printf("Computational Observer 9 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(1000);
        System.out.println("===========================================================================================");

        // RxCachedThreadScheduler
        Observable<String> ioTasks = accountObservable.subscribeOn(Schedulers.io());

        ioTasks.subscribe(item -> System.out.printf("IO Observer 1 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        ioTasks.subscribe(item -> System.out.printf("IO Observer 2 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        ioTasks.subscribe(item -> System.out.printf("IO Observer 3 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        ioTasks.subscribe(item -> System.out.printf("IO Observer 4 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        ioTasks.subscribe(item -> System.out.printf("IO Observer 5 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(1000);
        System.out.println("===========================================================================================");

        // RxSingleScheduler
        Observable<String> singleThreadObservable = accountObservable.subscribeOn(Schedulers.single());

        singleThreadObservable.subscribe(item -> System.out.printf("SingleThread Observer 1 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        singleThreadObservable.subscribe(item -> System.out.printf("SingleThread Observer 2 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        singleThreadObservable.subscribe(item -> System.out.printf("SingleThread Observer 3 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(1000);
        System.out.println("===========================================================================================");

        // RxNewThreadScheduler
        Observable<String> newThreadObservable = accountObservable.subscribeOn(Schedulers.newThread());

        newThreadObservable.subscribe(item -> System.out.printf("New Thread Observer 1 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        newThreadObservable.subscribe(item -> System.out.printf("New Thread Observer 2 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        newThreadObservable.subscribe(item -> System.out.printf("New Thread Observer 3 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        newThreadObservable.subscribe(item -> System.out.printf("New Thread Observer 4 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(1000);
        System.out.println("===========================================================================================");

        // pool-2-thread-n
        ExecutorService executor = Executors.newCachedThreadPool();
        Scheduler scheduler = Schedulers.from(executor);
        Observable<String> executorServiceObservable = accountObservable.subscribeOn(scheduler)
                .doOnComplete(() -> executor.shutdown()); // Shutdown Executor after completion of tasks because Executors threads are daemon threads
                //.doFinally(() -> executor.shutdown());

        executorServiceObservable.subscribe(item -> System.out.printf("Executor Service Scheduler Observer 1 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        executorServiceObservable.subscribe(item -> System.out.printf("Executor Service Scheduler Observer 2 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        executorServiceObservable.subscribe(item -> System.out.printf("Executor Service Scheduler Observer 3 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(1000);
        System.out.println("===========================================================================================");

        //ObserveOn
        Observable<String> ioObserveOn = accountObservable.observeOn(Schedulers.io());

        ioObserveOn.subscribe(item -> System.out.printf("IO(ObserveOn) Observer 1 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        ioObserveOn.subscribe(item -> System.out.printf("IO(ObserveOn) Observer 2 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        ioObserveOn.subscribe(item -> System.out.printf("IO(ObserveOn) Observer 3 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        ioObserveOn.subscribe(item -> System.out.printf("IO(ObserveOn) Observer 4 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        ioObserveOn.subscribe(item -> System.out.printf("IO(ObserveOn) Observer 5 Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(1000);
        System.out.println("===========================================================================================");

        /**
         * Chain on SubscribeOn schedulers
         *  : Even if we have multiple Schedulers at each stage, only the scheduler which is near to observable will take place
         *    i.e. First scheduler from top and all downstream work on same scheduler
         *
         *    OUTPUT:
         *     (ChainOfSubscribers) Computation Subscriber Item : Apply Account on Thread : RxCachedThreadScheduler-2
         *     (ChainOfSubscribers) IO Subscriber Item : 13 on Thread : RxCachedThreadScheduler-2
         *     (ChainOfSubscribers) New Thread Observer  Item : 1300 on Thread : RxCachedThreadScheduler-2
         *
         *     ALL STAGES WILL EXECUTE ON SAME SCHEDULER SO EXECUTION WILL BE SEQUENTIAL
         */
        accountObservable
                .subscribeOn(Schedulers.io()) // ALWAYS IT WILL TAKE EFFECT AND DOWNSTREAM'S ALSO RUN ON SAME SCHEDULER
                .map(item -> {
                    System.out.printf("(ChainOfSubscribers) Computation Subscriber Item : %s on Thread : %s\n", item, Thread.currentThread().getName());
                    return item.length();
                })
                .subscribeOn(Schedulers.newThread())
                .map(item -> {
                    System.out.printf("(ChainOfSubscribers) IO Subscriber Item : %s on Thread : %s\n", item, Thread.currentThread().getName());
                    return item * 100;
                })
                .subscribeOn(Schedulers.computation())
                .subscribe(item -> System.out.printf("(ChainOfSubscribers) New Thread Observer  Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(1000);
        System.out.println("===========================================================================================");

        /**
         * Chain on observeOn schedulers
         *  : if we have multiple Schedulers at each stage, then it will work on same scheduler on that stage if we don't specify then downstream
         *  also work on the immediate top most scheduler
         *
         *  OUTPUT:
         *  (ChainOfObservers) Computation Subscriber Item : Apply Account on Thread : RxCachedThreadScheduler-3
         *  (ChainOfObservers) IO Subscriber Item : 13 on Thread : RxNewThreadScheduler-6
         *  (ChainOfObservers) New Thread Observer  Item : 1400 on Thread : RxComputationThreadPool-3
         *
         *  EACH STAGE WILL EXECUTE ON DIFFERENT SCHEDULER SO SEQUENCE OF EXECUTION WILL NOT HAPPEN.
         */
        accountObservable
                .observeOn(Schedulers.io()) // ALWAYS IT WILL TAKE EFFECT AND DOWNSTREAM'S ALSO RUN ON SAME SCHEDULER
                .map(item -> {
                    System.out.printf("(ChainOfObservers) Computation Subscriber Item : %s on Thread : %s\n", item, Thread.currentThread().getName());
                    return item.length();
                })
                .observeOn(Schedulers.newThread())
                .map(item -> {
                    System.out.printf("(ChainOfObservers) IO Subscriber Item : %s on Thread : %s\n", item, Thread.currentThread().getName());
                    return item * 100;
                })
                .observeOn(Schedulers.computation())
                .subscribe(item -> System.out.printf("(ChainOfObservers) New Thread Observer  Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(1000);
        System.out.println("===========================================================================================");

        /**
         * Combination of observableOn & subscribeOn
         *
         *  OUTPUT:
         *  (ChainOfObservers) Computation Subscriber Item : Apply Account on Thread : RxCachedThreadScheduler-3
         *  (ChainOfObservers) IO Subscriber Item : 13 on Thread : RxNewThreadScheduler-6
         *  (ChainOfObservers) New Thread Observer  Item : 1400 on Thread : RxComputationThreadPool-3
         */
        accountObservable
                .subscribeOn(Schedulers.io()) // ALWAYS IT WILL TAKE EFFECT AND DOWNSTREAM'S ALSO RUN ON SAME SCHEDULER
                .map(item -> {
                    System.out.printf("(ChainOfObservers) Computation Subscriber Item : %s on Thread : %s\n", item, Thread.currentThread().getName());
                    return item.length();
                })
                .subscribeOn(Schedulers.newThread())
                .map(item -> {
                    System.out.printf("(ChainOfObservers) IO Subscriber Item : %s on Thread : %s\n", item, Thread.currentThread().getName());
                    return item * 100;
                })
                .observeOn(Schedulers.computation())
                .subscribe(item -> System.out.printf("(ChainOfObservers) New Thread Observer  Item : %s on Thread : %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(1000);
        System.out.println("===========================================================================================");

    }
}