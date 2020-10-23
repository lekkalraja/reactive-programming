package com.reactive.basics;

import java.util.ArrayList;
import java.util.List;

public class ObservableDesignPattern {

    public static void main(String[] args) {
        BookObservable bookObservable = new BookObservable("Java", "Tech", "James", 90.9, "SoldOut");

        BookObservers achilleas = new BookObservers("Achilleas");
        BookObservers hector = new BookObservers("Hector");
        BookObservers helen = new BookObservers("Helen");

        bookObservable.subscribeObserver(achilleas);
        bookObservable.subscribeObserver(hector);
        bookObservable.subscribeObserver(helen);
        bookObservable.setInStock("Available");
    }


    public record BookObservers(String name) implements Observer {
        @Override
        public void update(Observable observable) {
            System.out.printf("Hey %s ! %s is available \n", name, observable);
        }
    }

    public record BookObservable(String name, String type, String author, double price, String inStock)
            implements Observable {

        private static List<Observer> observers = new ArrayList<>();

        public void setInStock(String stock) {
            BookObservable book = new BookObservable(name, type, author, price, stock);
            System.out.printf("Book [%s] Available! Notify Observers \n", book);
            this.notifyObserver(book);
        }

        @Override
        public void subscribeObserver(Observer observer) {
            observers.add(observer);
        }

        @Override
        public void unsubscribeObserver(Observer observer) {
            observers.remove(observer);
        }

        @Override
        public void notifyObserver(Observable observable) {
            observers.forEach( observer -> observer.update(observable));
        }
    }

    interface Observable {
        void subscribeObserver(Observer observer);
        void unsubscribeObserver(Observer observer);
        void notifyObserver(Observable observable);
    }

    interface Observer {
        void update(Observable observable);
    }
}