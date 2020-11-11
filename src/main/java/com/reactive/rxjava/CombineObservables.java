package com.reactive.rxjava;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observables.GroupedObservable;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CombineObservables {

    public static void main(String[] args) throws InterruptedException {
        var source1 = Observable.interval(1, TimeUnit.SECONDS).map(item -> String.format("Source 1 : %s", item)).take(5);
        var source2 = Observable.interval(100, TimeUnit.MILLISECONDS).map(item -> String.format("Source 2 : %s", item)).take(20);

        /**
         * Merging sources :
         *   merge() & mergeWith() both are same i.e. combine items emitted by multiple sources so that they appear as a single
         */
        Disposable mergeSub1 = Observable.merge(source1, source2).subscribe(item -> System.out.printf("merge()  :: %s\n", item));
        Thread.sleep(6000);
        mergeSub1.dispose();
        Disposable mergeSub2 = source1.mergeWith(source2).subscribe(item -> System.out.printf("mergeWith()  :: %s\n", item));
        Thread.sleep(6000);
        mergeSub2.dispose();
        System.out.println("=====================================================================================================");

        /**
         * Concatenating Sources:
         *   concat() & concatWith() both are same i.e. Returns an Observable that emits the items emitted by multiple sources,
         *   one after the other, without interleaving them.
         */

        Disposable concatSub1 = Observable.concat(source1, source2).subscribe(item -> System.out.printf("concat()  :: %s\n", item));
        Thread.sleep(6000);
        concatSub1.dispose();
        Disposable concatSub2 = source1.concatWith(source2).subscribe(item -> System.out.printf("concatWith()  :: %s\n", item));
        Thread.sleep(6000);
        concatSub2.dispose();

        System.out.println("=====================================================================================================");

        /**
         * flatMap:
         *   Returns an Observable that emits items based on applying a function that you supply to each item emitted
         *   by the current Observable, where that function returns an ObservableSource, and then merging those returned
         *   ObservableSource's and emitting the results of this merger.
         */
        Disposable flatMapObserver = source1
                .flatMap(item -> Observable.just(item).repeat(3))
                .subscribe(item -> System.out.printf("flatMap()  :: %s\n", item));
        Thread.sleep(6000);
        flatMapObserver.dispose();

        System.out.println("=====================================================================================================");

        /**
         * concatMap:
         *  Returns a new Observable that emits items resulting from applying a function that you supply to each item
         *  emitted by the current Observable, where that function returns an ObservableSource, and then emitting the items
         *  that result from concatenating those returned ObservableSource's.
         */
        Disposable concatMapObserver = source1
                .concatMap(item -> Observable.just(item).repeat(3))
                .subscribe(item -> System.out.printf("concatMap()  :: %s\n", item));
        Thread.sleep(6000);
        concatMapObserver.dispose();

        System.out.println("=====================================================================================================");

        /**
         * amb()
         *  Mirrors the one ObservableSource in an array of several ObservableSource's that first either emits an item or sends
         *  a termination notification.
         */

        Disposable ambObserver = Observable.ambArray(source1, source2)
                .subscribe(item -> System.out.printf("amb()  :: %s\n", item));
        Thread.sleep(6000);
        ambObserver.dispose();

        System.out.println("=====================================================================================================");

        /**
         * zip():
         *  Returns an Observable that emits items that are the result of applying a specified function to pairs of
         *  values, one each from the current Observable and another specified ObservableSource.
         */

        Disposable zipSubscribe = source1.zipWith(source2, (src1, src2) -> String.format("%s --> %s", src1, src2))
                .subscribe(item -> System.out.printf("zip()  :: %s\n", item));
        Thread.sleep(6000);
        zipSubscribe.dispose();

        System.out.println("=====================================================================================================");

        /**
         * combineLatest():
         *  Combines two source ObservableSource's by emitting an item that aggregates the latest values of each of the
         *  ObservableSource's each time an item is received from either of the ObservableSource's, where this
         *  aggregation is defined by a specified function.
         */

        Disposable combineLastSubscribe = Observable.combineLatest(source1, source2, (src1, src2) -> String.format("%s --> %s", src1, src2))
                .subscribe(item -> System.out.printf("combineLatest()  :: %s\n", item));
        Thread.sleep(6000);
        combineLastSubscribe.dispose();

        System.out.println("=====================================================================================================");

        var employees = List.of(
                new Employee(1, "Achilleas", "M", 4999),
                new Employee(2, "Hector", "M", 6899),
                new Employee(3, "Helen", "F", 6589),
                new Employee(4, "Draupadi", "F", 9879),
                new Employee(6, "Ganga", "M", 5839),
                new Employee(5, "Karna", "M", 6530),
                new Employee(7, "Gandhari", "M", 5493)
        );

        Observable<GroupedObservable<String, Employee>> groupedObservableObservable = Observable.fromIterable(employees)
                .groupBy(Employee::gender);

        groupedObservableObservable
                .subscribe(groupedObservable -> {
                    System.out.print(groupedObservable.getKey());
                    groupedObservable.subscribe(employee -> System.out.printf("  %s", employee));
                });

        System.out.println("=====================================================================================================");

        groupedObservableObservable
                .flatMapSingle(groupedObservable -> groupedObservable.toMap(key -> groupedObservable.getKey(), emp -> emp.name))
                .subscribe(item -> System.out.printf("groupBy()  :: %s\n", item));

        Thread.sleep(1000);
    }

    public record Employee(int id, String name, String gender, double salary){}

}
