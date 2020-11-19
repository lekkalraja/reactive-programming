package com.reactive.rxjava;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class BufferingThrottlingAndSwitching {

    public static void main(String[] args) throws InterruptedException {

        Disposable bufferedDisposable = Observable.interval(500, TimeUnit.MILLISECONDS)
                .buffer(5)
                .subscribe(item -> System.out.printf("Buffered Observer - List{5} - : %s on %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(7000);
        bufferedDisposable.dispose();
        System.out.println("===================================================================");

        Disposable bufferWithSupplierDisposable = Observable.interval(500, TimeUnit.MILLISECONDS)
                .buffer(5, HashSet::new)
                .subscribe(item -> System.out.printf("Buffer-With-Supplier[HashSet] Observer - Set{5} - : %s on %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(7000);
        bufferWithSupplierDisposable.dispose();
        System.out.println("===================================================================");

        Disposable bufferWithElementSkipDisposable = Observable.interval(500, TimeUnit.MILLISECONDS)
                .buffer(4, 7)
                .subscribe(item -> System.out.printf("Buffer-With-7th-Element-Skip Observer - : %s on %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(7000);
        bufferWithElementSkipDisposable.dispose();
        System.out.println("===================================================================");

        Disposable bufferWithTimeSpanDisposable = Observable.interval(500, TimeUnit.MILLISECONDS)
                .buffer(2, TimeUnit.SECONDS)
                .subscribe(item -> System.out.printf("Buffer-With-TimeSpan Observer - : %s on %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(7000);
        bufferWithTimeSpanDisposable.dispose();
        System.out.println("===================================================================");

        Disposable bufferWithTimeSpanWithCountDisposable = Observable.interval(500, TimeUnit.MILLISECONDS)
                .buffer(2, TimeUnit.SECONDS, 5)
                .subscribe(item -> System.out.printf("Buffer-With-TimeSpan-With-Count Observer - : %s on %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(7000);
        bufferWithTimeSpanWithCountDisposable.dispose();
        System.out.println("===================================================================");

        Disposable bufferWithObservable = Observable.interval(500, TimeUnit.MILLISECONDS)
                .buffer(Observable.interval(2, TimeUnit.SECONDS))
                .subscribe(item -> System.out.printf("Buffer-With-Observable Observer - : %s on %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(7000);
        bufferWithObservable.dispose();
        System.out.println("===================================================================");

        Observable<Observable<Long>> windowWithCountObservable = Observable.interval(500, TimeUnit.MILLISECONDS)
                .window(5);
        Disposable windowWithCountDisposable = windowWithCountObservable
                .flatMap(item -> item)
                .subscribe(item -> System.out.printf("Window Observer - : %s on %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(2000);
        windowWithCountDisposable.dispose();
        System.out.println("===================================================================");

        Disposable windowWithElementSkipDisposable = Observable.interval(500, TimeUnit.MILLISECONDS)
                .window(4, 7)
                .flatMap(item -> item)
                .subscribe(item -> System.out.printf("Window-With-7th-Element-Skip Observer - : %s on %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(2000);
        windowWithElementSkipDisposable.dispose();
        System.out.println("===================================================================");

        Disposable windowWithTimeSpanDisposable = Observable.interval(500, TimeUnit.MILLISECONDS)
                .window(2, TimeUnit.SECONDS)
                .flatMap(item -> item)
                .subscribe(item -> System.out.printf("Window-With-TimeSpan Observer - : %s on %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(2000);
        windowWithTimeSpanDisposable.dispose();
        System.out.println("===================================================================");

        Disposable windowWithTimeSpanWithCountDisposable = Observable.interval(500, TimeUnit.MILLISECONDS)
                .window(2, TimeUnit.SECONDS, 5)
                .flatMap(item -> item)
                .subscribe(item -> System.out.printf("Window-With-TimeSpan-With-Count Observer - : %s on %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(2000);
        windowWithTimeSpanWithCountDisposable.dispose();
        System.out.println("===================================================================");

        Disposable windowWithObservable = Observable.interval(500, TimeUnit.MILLISECONDS)
                .window(Observable.interval(2, TimeUnit.SECONDS))
                .flatMap(item -> item)
                .subscribe(item -> System.out.printf("Window-With-Observable Observer - : %s on %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(2000);
        windowWithObservable.dispose();
        System.out.println("===================================================================");

        Disposable throttleFirstWith2SecondsDisposable = Observable.interval(500, TimeUnit.MILLISECONDS)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(item -> System.out.printf("ThrottleFirst Observer With 2 Seconds - : %s on %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(6000);
        throttleFirstWith2SecondsDisposable.dispose();
        System.out.println("===================================================================");

        Disposable throttleFirstWithSkipDisposable = Observable.interval(500, TimeUnit.MILLISECONDS)
                .throttleFirst(1, TimeUnit.SECONDS, Schedulers.computation())
                .subscribe(item -> System.out.printf("ThrottleFirst Observer With 1 Second Skip Duration - : %s on %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(6000);
        throttleFirstWithSkipDisposable.dispose();
        System.out.println("===================================================================");

        Disposable throttleLastWith2SecondsDisposable = Observable.interval(500, TimeUnit.MILLISECONDS)
                .throttleLast(2, TimeUnit.SECONDS)
                .subscribe(item -> System.out.printf("ThrottleLast Observer With 2 Seconds - : %s on %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(6000);
        throttleLastWith2SecondsDisposable.dispose();
        System.out.println("===================================================================");

        Disposable throttleLastWithSkipDisposable = Observable.interval(500, TimeUnit.MILLISECONDS)
                .throttleLast(1, TimeUnit.SECONDS, Schedulers.computation())
                .subscribe(item -> System.out.printf("ThrottleLast Observer With 1 Second duration- : %s on %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(6000);
        throttleLastWithSkipDisposable.dispose();
        System.out.println("===================================================================");

        Disposable throttleLatestWithTimeoutDisposable = Observable.interval(500, TimeUnit.MILLISECONDS)
                .throttleLatest(2, TimeUnit.SECONDS)
                .subscribe(item -> System.out.printf("ThrottleLatest Observer With 2 Seconds timeout - : %s on %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(6000);
        throttleLatestWithTimeoutDisposable.dispose();
        System.out.println("===================================================================");

        Disposable throttleLatestWithFalseEmitLastDisposable = Observable.interval(500, TimeUnit.MILLISECONDS)
                .throttleLatest(1, TimeUnit.SECONDS, false)
                .subscribe(item -> System.out.printf("ThrottleLatest Observer With False Emit Last of 1 Second Timeout - : %s on %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(6000);
        throttleLatestWithFalseEmitLastDisposable.dispose();
        System.out.println("===================================================================");


        Disposable throttleWithTimeoutDisposable = Observable.interval(500, TimeUnit.MILLISECONDS)
                .throttleWithTimeout(2, TimeUnit.SECONDS)
                .subscribe(item -> System.out.printf("Throttle Observer With 2 Seconds Timeout - : %s on %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(6000);
        throttleWithTimeoutDisposable.dispose();
        System.out.println("===================================================================");

        Disposable switchMapDisposable = Observable.interval(2, TimeUnit.SECONDS)
                .switchMap(item -> Observable.range(50, 2).doOnDispose(() -> System.out.println("Range Observable Disposing")))
                .subscribe(item -> System.out.printf("SwitchMap Observable - : %s on %s\n", item, Thread.currentThread().getName()));
        Thread.sleep(4000);
        switchMapDisposable.dispose();
        System.out.println("===================================================================");

    }
}
