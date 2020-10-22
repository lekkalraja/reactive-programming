package com.reactive;

/**
 * As the Application grows, the call-backs also will grow as a result will get into callbacks-hell.
 */
public class CallBackDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Main Method Started "+ Thread.currentThread().getName());
        Runnable runnable = () -> new CallBackDemo().runAsync(() -> System.out.println("CallBack "+ Thread.currentThread().getName()));
        new Thread(runnable).start();
        System.out.println("Main Method Ended "+ Thread.currentThread().getName());
        Thread.sleep(2000);
    }

    public void runAsync(CallBack callBack)  {
        try {
            System.out.println("Async Method Running "+ Thread.currentThread().getName());
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
