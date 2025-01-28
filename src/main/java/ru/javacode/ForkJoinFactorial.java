package ru.javacode;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Рассмотрим задачу вычисления факториала числа с использованием ForkJoinPool. Факториал числа n обозначается как n!
 * и вычисляется как произведение всех положительных целых чисел от 1 до n.
 *
 * Реализуйте класс FactorialTask, который расширяет RecursiveTask. Этот класс будет выполнять рекурсивное вычисление
 * факториала числа.
 * В конструкторе FactorialTask передайте число n, факториал которого нужно вычислить.
 * В методе compute() разбейте задачу на подзадачи и используйте fork() для их асинхронного выполнения.
 * Используйте join() для получения результатов подзадач и комбинирования их для получения общего результата.
 * В основном методе создайте экземпляр FactorialTask с числом, для которого нужно вычислить факториал,
 * и запустите его в ForkJoinPool.
 * Выведите результат вычисления факториала.
 */
public class ForkJoinFactorial {
    public static void main(String[] args) {
        int n = 10; // Вычисление факториала для числа 10

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        FactorialTask factorialTask = new FactorialTask(n);

        long result = forkJoinPool.invoke(factorialTask);

        System.out.println("Факториал " + n + "! = " + result);
    }

    private static class FactorialTask extends RecursiveTask<Integer> {
        private int n;

        public FactorialTask(int n) {
            this.n = n;
        }

        @Override
        protected Integer compute() {
            if (n == 1) {
                return n;
            }

            FactorialTask task = new FactorialTask(n - 1);
            task.fork();
            return n * task.join();
        }
    }
}
