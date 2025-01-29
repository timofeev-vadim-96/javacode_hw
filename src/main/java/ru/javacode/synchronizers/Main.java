package ru.javacode.synchronizers;

import ru.javacode.synchronizers.executor.ComplexTaskExecutor;

/**
 * Синхронизация потоков с использованием CyclicBarrier и ExecutorService
 * <p>
 * В этой задаче мы будем использовать CyclicBarrier и ExecutorService для синхронизации нескольких потоков, выполняющих
 * сложную задачу, и затем ожидающих, пока все потоки завершат выполнение, чтобы объединить результаты.
 * <p>
 * Создайте класс ComplexTask, представляющий сложную задачу, которую несколько потоков будут выполнять.
 * В каждой задаче реализуйте метод execute(), который выполняет часть сложной задачи.
 * <p>
 * Создайте класс ComplexTaskExecutor, в котором будет использоваться CyclicBarrier и ExecutorService для синхронизации
 * выполнения задач. Реализуйте метод executeTasks(int numberOfTasks), который создает пул потоков и назначает каждому
 * потоку экземпляр сложной задачи для выполнения. Затем используйте CyclicBarrier для ожидания завершения всех потоков
 * и объединения результатов их работы. В методе main создайте экземпляр ComplexTaskExecutor и вызовите метод
 * executeTasks с несколькими задачами для выполнения.
 */
public class Main {
    public static void main(String[] args) {
        ComplexTaskExecutor taskExecutor = new ComplexTaskExecutor(5); // Количество задач для выполнения

        Runnable testRunnable = () -> {
            System.out.println(Thread.currentThread().getName() + " started the test.");

            // Выполнение задач
            taskExecutor.executeTasks(5);

            System.out.println(Thread.currentThread().getName() + " completed the test.");
        };

        Thread thread1 = new Thread(testRunnable, "TestThread-1");
        Thread thread2 = new Thread(testRunnable, "TestThread-2");

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
