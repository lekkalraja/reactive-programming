package com.reactive.rxjava;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.internal.operators.observable.ObservableCreate;


public class ObservableAndObserver {

    public static void main(String[] args) {

        ObservableCreate<Integer> source = new ObservableCreate<Integer>(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Throwable {
                try {
                    emitter.onNext(10);
                    emitter.onNext(20);
                    emitter.onNext(30);
                    emitter.onNext(40);
                    emitter.onNext(50);
                    //throw new RuntimeException("ForFUn!!");
                    emitter.onNext(60); // No Impact
                    // throw new RuntimeException("ForFUn!!"); // io.reactivex.rxjava3.exceptions.UndeliverableException: The exception could not be delivered to the consumer because it has already canceled/disposed the flow or the exception has nowhere to go to begin with. Further reading: https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#error-handling | java.lang.RuntimeException: ForFUn!!
                } catch (Exception e) {
                    emitter.onError(e);
                }
                // emitter.onComplete(); // No Impact after throwing Error
            }
        });

        Observer<Integer> observer = new Observer<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("Subscribed!");
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                System.out.println("onNext-Inline : "+integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("onError-Inline : "+ e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete-Inline !");
            }
        };

        Consumer<Integer> onNext = item -> System.out.println("onNext-Lambda : "+ item);
        Consumer<Throwable> onError = throwable -> System.err.println("oneError-Lambda : "+ throwable.getMessage());
        Action onComplete = () -> System.out.println("onComplete-Lambda!");

        /**
         * Overloaded Subscribe methods
         */
        source.subscribe(observer); // onSubscribe will call initially
        Disposable disposable1 = source.subscribe(onNext, onError, onComplete); // No onSubscribe call
        Disposable disposable2 = source.subscribe(onNext); // only onNext call
        Disposable disposable3 = source.subscribe(onNext, onError); // No onSubscribe and onComplete
    }
}