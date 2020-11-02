package com.reactive.rxjava;

import io.reactivex.rxjava3.core.Observable;

public class HelloRxJava {

    public static void main(String[] args) {

        Observable<String> source = Observable.create(e -> {
            e.onNext("Hello");
            e.onNext("RxJava");
        });

        source.subscribe( item -> System.out.printf("Observer 1 %s \n", item));
        source.subscribe( item -> System.out.printf("Observer 2 %s \n", item));
    }
}