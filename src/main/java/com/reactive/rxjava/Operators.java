package com.reactive.rxjava;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Predicate;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Operators {

    public static void main(String[] args) throws InterruptedException {

        var employees = List.of(
                new Employee(1, "Achilleas", "M", 4999),
                new Employee(2, "Hector", "M", 6899),
                new Employee(3, "Helen", "F", 6589),
                new Employee(4, "Draupadi", "F", 9879),
                new Employee(5, "Karna", "M", 6530),
                new Employee(6, "Ganga", "M", 5839),
                new Employee(7, "Gandhari", "M", 5493)
        );

        /**
         * Suppressing Operators
         */
        var employeeObservable = Observable.fromIterable(employees);

        employeeObservable
                .filter(employee -> employee.gender == "F")
                .subscribe(System.out::println);

        System.out.println("=====================================================");
        employeeObservable
                .take(3)
                .subscribe(System.out::println);

        System.out.println("=====================================================");
        employeeObservable
                .skip(3)
                .subscribe(System.out::println);

        System.out.println("=====================================================");
        employeeObservable
                .elementAt(4)
                .subscribe(System.out::println);

        employeeObservable
                .elementAt(9, new Employee(9, "Arjun", "M", 8932))
                .subscribe(System.out::println);
        System.out.println("=====================================================");

        Observable
                .just(1,2,2,1)
                .distinct()
                .subscribe(System.out::println);

        /**
         * Transforming Operators
         */

        employeeObservable
                .map(Employee::name)
                .toList()
                .subscribe(System.out::println);

        System.out.println("=====================================================");

        // cast() => takes class name as argument
        employeeObservable
                .map(Employee::id)
                .cast(Integer.class)
                .toList()
                .subscribe(System.out::println);

        System.out.println("=====================================================");

        // delay the emission with specified amount of time
        employeeObservable
                .delay(5, TimeUnit.SECONDS)
                .subscribe(System.out::println);

        System.out.println("=====================================================");

        // delay subscription with specified amount of time
        employeeObservable
                .delaySubscription(5, TimeUnit.SECONDS)
                .subscribe(System.out::println);

        Thread.sleep(6000);

        System.out.println("=====================================================");

        //scan() : takes accumulator function and apply to the first item and feeds result to next
        employeeObservable
                .map(Employee::id)
                .scan(0, (left, right) -> left + right)
                .subscribe(System.out::println);

        System.out.println("=====================================================");

        employeeObservable
                .sorted(Comparator.comparing(o -> o.name))
                .subscribe(System.out::println);

        System.out.println("=====================================================");

        // repeat() : that repeats subscription upstream specified number of times

        employeeObservable
                .repeat(3)
                .distinct()
                .subscribe(System.out::println);

        /**
         * Reducing Operations
         */
        System.out.println("=====================================================");

        employeeObservable.count()
                .subscribe(count -> System.out.printf("Total number of employees : %s \n", count));

        System.out.println("=====================================================");

        employeeObservable
                .map(Employee::salary)
                .reduce((left, right) -> left + right)
                .subscribe(totalSalary -> System.out.printf("Total Salary : %s \n", totalSalary));

        System.out.println("=====================================================");

        employeeObservable
                .contains(new Employee(3, "Helen", "F", 6589))
                .subscribe(isPresent -> System.out.printf("is Helen Present : %s \n", isPresent));


        employeeObservable
                .contains(new Employee(3, "Raja", "F", 6589))
                .subscribe(isPresent -> System.out.printf("is Raja Present : %s \n", isPresent));

        System.out.println("=====================================================");

        Predicate<Employee> isAllGetting5000 = emp -> emp.salary > 5000;
        Predicate<Employee> isAllGetting4000 = emp -> emp.salary > 4000;

        employeeObservable
                .all(isAllGetting5000)
                .subscribe(isPresent -> System.out.printf("Does Every Employee getting more than 5000 : %s \n", isPresent));

        employeeObservable
                .all(isAllGetting4000)
                .subscribe(isPresent -> System.out.printf("Does Every Employee getting more than 4000 : %s \n", isPresent));

        System.out.println("=====================================================");

        Predicate<Employee> anyOneGetting9000 = emp -> emp.salary > 9000;
        employeeObservable
                .any(anyOneGetting9000)
                .subscribe(isPresent -> System.out.printf("Does Anyone getting more than 9000 : %s \n", isPresent));

        System.out.println("=====================================================");

        /**
         * Collection Operators
         */

        employeeObservable
                .map(Employee::name)
                .toList()
                .subscribe(System.out::println);

        System.out.println("=====================================================");

        employeeObservable
                .map(Employee::name)
                .toSortedList()
                .subscribe(System.out::println);

        System.out.println("=====================================================");

        employeeObservable
                .toMap(Employee::name)
                .subscribe(System.out::println);

        employeeObservable
                .toMap(Employee::name, Employee::salary)
                .subscribe(System.out::println);

        System.out.println("=====================================================");

        employeeObservable
                .collect(Collectors.groupingBy(Employee::gender))
                .subscribe(System.out::println);

        System.out.println("=====================================================");

        /**
         * Error Recovery Operators
         */

        // onErrorReturnItem() - Return some default element when exception occurs

        Observable<Employee> mayGetError = Observable.create(emitter -> {
            emitter.onNext(new Employee(1, "Achilleas", "M", 4999));
            emitter.onNext(new Employee(2, "Hector", "M", 6899));
            emitter.onNext(new Employee(3, "Helen", "F", 6589));
            emitter.onError(new Exception("Got Runtime Exception"));
            emitter.onNext(new Employee(4, "Draupadi", "F", 9879));
            emitter.onNext(new Employee(5, "Karna", "M", 6530));
        });

        mayGetError
                .onErrorReturnItem(new Employee(0, "Raja", "M", 10000))
                .subscribe(System.out::println);

        System.out.println("=====================================================");

        mayGetError
                .onErrorReturn(error -> new Employee(0, "Raja", "M", 10000))
                .subscribe(System.out::println);

        System.out.println("=====================================================");

        Observable<Employee> fallBack = Observable.create(emitter -> {
            emitter.onNext(new Employee(4, "Draupadi", "F", 9879));
            emitter.onNext(new Employee(5, "Karna", "M", 6530));
        });

        mayGetError
                .onErrorResumeWith(fallBack)
                .subscribe(System.out::println);

        System.out.println("=====================================================");

        mayGetError
                .onErrorResumeNext(error -> fallBack)
                .subscribe(System.out::println);

        System.out.println("=====================================================");

        // retry() -> re-subscribes to the Observable hoping that it will complete without any error (May go to infinite retries)
        mayGetError
                //.retry()
                .onErrorComplete()
                .subscribe(System.out::println);

        mayGetError
                .retry(1) // will re-try 1 time
                .onErrorComplete()
                .subscribe(System.out::println);

        /**
         * Action Operators
         */
        mayGetError
                .doOnSubscribe(disposable -> System.out.println("Subscribed"))
                .doOnNext(employee -> System.out.printf("Called onNext for : %s \n",employee.id))
                .doOnError(throwable -> System.out.printf("Error occurred for : %s \n",throwable.getMessage()))
                .doOnComplete(() -> System.out.println("Completed"))
                .onErrorComplete()
                .subscribe(System.out::println);
    }

    public record Employee(int id, String name, String gender, double salary){}
}