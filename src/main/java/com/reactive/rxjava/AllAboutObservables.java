package com.reactive.rxjava;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.observables.ConnectableObservable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AllAboutObservables {

    public static void main(String[] args) throws InterruptedException {

        /**
         * Different ways to create Observable
         */

        Observable.create(emitter -> {
            emitter.onNext(10);
            emitter.onNext(12);
            emitter.onComplete();
        })
        .subscribe(System.out::println);

        // COLD OBSERVABLE
        Observable<Long> interval = Observable.interval(1, TimeUnit.SECONDS);
        Disposable dis1 = interval.subscribe(item -> System.out.printf("Cold Observer - 1 : %s\n", item));
        //Thread.sleep(5000);
        Disposable dis2 = interval.subscribe(item -> System.out.printf("Cold Observer - 2 : %s\n", item));
        //Thread.sleep(10000);
        dis1.dispose(); dis2.dispose();

        // HOT OBSERVABLE
        ConnectableObservable<Long> connectableObservable = Observable.interval(1, TimeUnit.SECONDS).publish();
        connectableObservable.connect();
        connectableObservable.subscribe(item -> System.out.printf("Hot Observer - 1 : %s\n", item));
        //Thread.sleep(5000);
        connectableObservable.subscribe(item -> System.out.printf("Hot Observer - 2 : %s\n", item));
        //Thread.sleep(10000);


        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(item -> System.out.printf("Interval Observer - : %s\n", item));

        Observable.just(10, 20).subscribe(item -> System.out.printf("Just Observer - : %s\n", item));

        /**
         * Lot of Observable.from** overloaded available
         */
        Observable.fromIterable(Arrays.asList(10, 20)).subscribe(item -> System.out.printf("From Iterable Observer - : %s\n", item));
        Observable.fromAction(() -> System.out.printf("From Action Observer - : %s\n", 400)).subscribe();


        Observable.range(10, 3).subscribe(item -> System.out.printf("Range Observer - : %s\n", item));


        Observable.empty().subscribe(item -> System.out.println("Empty Observer - Never Calls"),
                throwable -> System.out.println("Empty Observer - Never calls"),
                () -> System.out.println("Empty Observer - Immediately calls onComplete")
        );


        Observable.never().subscribe(item -> System.out.println("Never Observer - Never Calls (onNext)"),
                throwable -> System.out.println("Never Observer - Never calls (onError)"),
                () -> System.out.println("Never Observer - Never Calls (onComplete)")
        );


        Observable.error(new Exception("Throw")).subscribe(item -> System.out.println("Error Observer - Never Calls (onNext)"),
                throwable -> System.out.printf("Error Observer - %s\n",throwable.getMessage()),
                () -> System.out.println("Error Observer - Never Class (onComplete)")
        );

        List<String> names = new ArrayList<>(List.of("Achilleas", "Hector"));
        Observable<String> source = Observable.defer(() -> Observable.fromIterable(names));
        source.subscribe(item -> System.out.printf("Defer Observer 1- : %s\n", item));
        names.add("Helen");
        source.subscribe(item -> System.out.printf("Defer Observer 2- : %s\n", item));

        Single<String> singleObserver = source.first("Default");
        Single<String> one_item = Single.just("One Item");
        Consumer<String> onSuccess = item -> System.out.printf("Single Observer 1- : %s \n", item);
        Consumer<Throwable> onError = error -> System.out.printf("Single Observer 1- : %s \n", error.getMessage());
        one_item.subscribe(onSuccess, onError);

        Maybe<String> mayBe = source.firstElement();
        Consumer<String> mayBeSuccess = item -> System.out.printf("MayBe Observer 1- : %s \n", item);
        Consumer<Throwable> mayBeFailure = error -> System.out.printf("MayBe Observer 1- : %s \n", error.getMessage());
        Action mayBeComplete = () -> System.out.println("MayBe Observer 1 - onComplete");
        mayBe.subscribe(mayBeSuccess, mayBeFailure, mayBeComplete);

        Completable.complete().subscribe(() -> System.out.println("Completable Observer 1 - onComplete"));
        Thread.sleep(5000);
    }
}