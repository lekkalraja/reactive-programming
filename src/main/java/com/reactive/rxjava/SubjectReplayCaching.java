package com.reactive.rxjava;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.*;

import java.util.concurrent.TimeUnit;

public class SubjectReplayCaching {

    public static void main(String[] args) throws InterruptedException {
        Observable<Long> dataObservable = Observable.interval(1, TimeUnit.SECONDS);
        Disposable nObserver1 = dataObservable.subscribe(item -> System.out.printf("Normal Observable Observer 1 : %s \n", item));
        Thread.sleep(2000);
        Disposable nObserver2 = dataObservable.subscribe(item -> System.out.printf("Normal Observable Observer 2 : %s \n", item));
        Thread.sleep(2000);
        nObserver1.dispose(); nObserver2.dispose();
        System.out.println("============================================================================");

        Observable<Long> replayObservable = Observable.interval(1, TimeUnit.SECONDS).replay(2).autoConnect();
        Disposable rObserver1 = replayObservable.subscribe(item -> System.out.printf("Replay  Observable Observer 1 : %s \n", item));
        Thread.sleep(2000);
        Disposable rObserver2 = replayObservable.subscribe(item -> System.out.printf("Replay Observable Observer 2 : %s \n", item));
        Thread.sleep(2000);
        rObserver1.dispose(); rObserver2.dispose();
        System.out.println("============================================================================");

        Observable<Long> replayTimeWindowedObservable = Observable.interval(1, TimeUnit.SECONDS).replay(2, TimeUnit.SECONDS).autoConnect();
        Disposable rtwObserver1 = replayTimeWindowedObservable.subscribe(item -> System.out.printf("Replay Time Windowed Observable Observer 1 : %s \n", item));
        Thread.sleep(2000);
        Disposable rtwObserver2 = replayTimeWindowedObservable.subscribe(item -> System.out.printf("Replay Time Windowed Observable Observer 2 : %s \n", item));
        Thread.sleep(2000);
        rtwObserver1.dispose(); rtwObserver2.dispose();
        System.out.println("============================================================================");

        Observable<Long> cachedObservable = Observable.interval(1, TimeUnit.SECONDS).cache();
        Disposable cObserver1 = cachedObservable.subscribe(item -> System.out.printf("Cached Observable Observer 1 : %s \n", item));
        Thread.sleep(2000);
        Disposable cObserver2 = cachedObservable.subscribe(item -> System.out.printf("Cached Observable Observer 2 : %s \n", item));
        Thread.sleep(2000);
        cObserver1.dispose(); cObserver2.dispose();
        System.out.println("============================================================================");

        Observable<Long> cachedWithInitialCapObservable = Observable.interval(1, TimeUnit.SECONDS).cacheWithInitialCapacity(2);
        Disposable cWithInitialCapObserver1 = cachedWithInitialCapObservable.subscribe(item -> System.out.printf("Cached With Initial Capacity Observable Observer 1 : %s \n", item));
        Thread.sleep(2000);
        Disposable cWithInitialCapObserver2 = cachedWithInitialCapObservable.subscribe(item -> System.out.printf("Cached With Initial Capacity Observable Observer 2 : %s \n", item));
        Thread.sleep(2000);
        cWithInitialCapObserver1.dispose(); cWithInitialCapObserver2.dispose();
        System.out.println("============================================================================");

        Observable<Integer> evenSource = Observable.just(2, 4, 6, 8, 0);
        Observable<Integer> oddSource = Observable.just(1, 3, 5, 7, 9);

        Disposable eo = evenSource.subscribe(item -> System.out.printf(" Even Source Direct Observer : %s \n", item));
        Disposable oo = oddSource.subscribe(item -> System.out.printf(" Odd Source Direct Observer : %s \n", item));
        Thread.sleep(2000);
        eo.dispose(); oo.dispose();
        System.out.println("============================================================================");

        PublishSubject<Object> broker = PublishSubject.create();
        Disposable subject = broker.subscribe(item -> System.out.printf(" Subject : %s \n", item));
        evenSource.subscribe(broker);
        oddSource.subscribe(broker);
        Thread.sleep(2000);
        subject.dispose();
        System.out.println("============================================================================");

        PublishSubject<Object> brokerOnDifferentThreads = PublishSubject.create();
        Disposable subjectOnDifferentThreads = brokerOnDifferentThreads.subscribe(item -> System.out.printf(" Subject : %s on %s\n", item, Thread.currentThread().getName()));
        Observable.just(2, 4, 6, 8, 0).subscribeOn(Schedulers.computation()).subscribe(brokerOnDifferentThreads);
        Observable.just(1, 3, 5, 7, 9).subscribeOn(Schedulers.computation()).subscribe(brokerOnDifferentThreads);
        Thread.sleep(2000);
        subjectOnDifferentThreads.dispose();
        System.out.println("============================================================================");

        PublishSubject<Object> observableSubject = PublishSubject.create();
        Disposable observer = observableSubject.subscribe(item -> System.out.printf(" Observable Subject : %s on %s\n", item, Thread.currentThread().getName()));
        observableSubject.onNext("Hello");
        observableSubject.onNext("Rx Java!");
        observableSubject.onComplete();
        Thread.sleep(2000);
        observer.dispose();
        System.out.println("============================================================================");

        PublishSubject<Object> publishSubject = PublishSubject.create();

        publishSubject.subscribe(item -> System.out.printf(" Publish Subject Observer - 1: %s on %s\n", item, Thread.currentThread().getName()));
        publishSubject.onNext("Item 1");
        publishSubject.onNext("Item 2");
        publishSubject.onNext("Item 3");
        publishSubject.subscribe(item -> System.out.printf(" Publish Subject Observer - 2: %s on %s\n", item, Thread.currentThread().getName()));
        publishSubject.onNext("Item 4");
        publishSubject.onNext("Item 5");
        publishSubject.onComplete();
        Thread.sleep(2000);
        System.out.println("============================================================================");

        ReplaySubject<Object> replaySubject = ReplaySubject.create();

        replaySubject.subscribe(item -> System.out.printf(" Replay Subject Observer - 1: %s on %s\n", item, Thread.currentThread().getName()));
        replaySubject.onNext("Item 1");
        replaySubject.onNext("Item 2");
        replaySubject.onNext("Item 3");
        replaySubject.subscribe(item -> System.out.printf(" Replay Subject Observer - 2: %s on %s\n", item, Thread.currentThread().getName()));
        replaySubject.onNext("Item 4");
        replaySubject.onNext("Item 5");
        replaySubject.onComplete();
        Thread.sleep(2000);
        System.out.println("============================================================================");

        BehaviorSubject<Object> behaviorSubject = BehaviorSubject.create();

        behaviorSubject.subscribe(item -> System.out.printf(" Behavior Subject Observer - 1: %s on %s\n", item, Thread.currentThread().getName()));
        behaviorSubject.onNext("Item 1");
        behaviorSubject.onNext("Item 2");
        behaviorSubject.onNext("Item 3");
        behaviorSubject.subscribe(item -> System.out.printf(" Behavior Subject Observer - 2: %s on %s\n", item, Thread.currentThread().getName()));
        behaviorSubject.onNext("Item 4");
        behaviorSubject.onNext("Item 5");
        behaviorSubject.onComplete();
        Thread.sleep(2000);
        System.out.println("============================================================================");

        AsyncSubject<Object> asyncSubject = AsyncSubject.create();

        asyncSubject.subscribe(item -> System.out.printf(" Async Subject Observer - 1: %s on %s\n", item, Thread.currentThread().getName()));
        asyncSubject.onNext("Item 1");
        asyncSubject.onNext("Item 2");
        asyncSubject.onNext("Item 3");
        asyncSubject.subscribe(item -> System.out.printf(" Async Subject Observer - 2: %s on %s\n", item, Thread.currentThread().getName()));
        asyncSubject.onNext("Item 4");
        asyncSubject.onNext("Item 5");
        asyncSubject.onComplete();
        Thread.sleep(2000);
        System.out.println("============================================================================");

        UnicastSubject<Object> unicastSubject = UnicastSubject.create();

        unicastSubject.subscribe(item -> System.out.printf(" Unicast Subject Observer - 1: %s on %s\n", item, Thread.currentThread().getName()));
        unicastSubject.onNext("Item 1");
        unicastSubject.onNext("Item 2");
        unicastSubject.onNext("Item 3");
        //java.lang.IllegalStateException: Only a single observer allowed.
       // unicastSubject.subscribe(item -> System.out.printf(" Unicast Subject Observer - 2: %s on %s\n", item, Thread.currentThread().getName()));
        unicastSubject.onNext("Item 4");
        unicastSubject.onNext("Item 5");
        unicastSubject.onComplete();
        Thread.sleep(2000);
        System.out.println("============================================================================");
    }

}