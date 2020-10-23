package com.reactive.basics;

import java.util.Objects;

/**
 * As the Application grows, the call-backs also will grow as a result will get into callbacks-hell.
 */
public class CallBackDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.printf("[%s] Main Method Started \n", Thread.currentThread().getName());
        Runnable runnable = () -> new CallBackDemo().runAsync(() -> System.out.printf("[%s] CallBack \n", Thread.currentThread().getName()));
        new Thread(runnable).start();
        System.out.printf("[%s] Main Method Ended \n", Thread.currentThread().getName());
        Thread.sleep(2000);
    }

    public void runAsync(CallBack callBack)  {
        Objects.requireNonNull(callBack);
        try {
            System.out.printf("[%s] Async Method Running \n", Thread.currentThread().getName());
            Thread.sleep(1000);
            callBack.call();
        } catch (InterruptedException e) {
            callBack.call();
        }
    }

    interface CallBack {
        void call();
    }
}