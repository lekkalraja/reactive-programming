package com.reactive.basics;

import java.util.Objects;

public class PushDemo {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Main Method Started "+ Thread.currentThread().getName());

        Runnable runnable = () -> new PushDemo().runAsync(new CallBack<>() {
            @Override
            public void pushData(String data) {
                System.out.printf("[%s] Received : %s%n", Thread.currentThread().getName(), data);
            }

            @Override
            public void pushComplete() {
                System.out.printf("[%s] Received Completed Signal%n", Thread.currentThread().getName());
            }

            @Override
            public void pushException(Throwable throwable) {
                System.out.printf("[%s] Received Error: %s%n", Thread.currentThread().getName(), throwable.getMessage());
            }
        });

        new Thread(runnable).start();
        System.out.println("Main Method Ended "+ Thread.currentThread().getName());
        Thread.sleep(2000);
    }

    public void runAsync(CallBack<String> callBack) {
        Objects.requireNonNull(callBack);
        try {
            System.out.println("Async Method Running "+ Thread.currentThread().getName());
            Thread.sleep(1000);
            callBack.pushData("Data 1");
            callBack.pushData("Data 2");
            callBack.pushData("Data 3");
            callBack.pushComplete();
        } catch (InterruptedException e) {
            callBack.pushException(e);
        }
    }


    interface CallBack<T> {
        void pushData(T data);
        void pushComplete();
        void pushException(Throwable throwable);
    }
}
