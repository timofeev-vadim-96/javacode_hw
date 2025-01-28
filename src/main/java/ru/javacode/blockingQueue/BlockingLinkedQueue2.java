package ru.javacode.blockingQueue;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Предположим, у вас есть пул потоков, и вы хотите реализовать блокирующую очередь для передачи задач между потоками.
 * Создайте класс BlockingQueue, который будет обеспечивать безопасное добавление и извлечение элементов между
 * производителями и потребителями в контексте пула потоков.
 * <p>
 * Класс BlockingQueue должен содержать методы enqueue() для добавления элемента в очередь и dequeue() для извлечения
 * элемента. Если очередь пуста, dequeue() должен блокировать вызывающий поток до появления нового элемента.
 * <p>
 * Используйте механизмы wait() и notify() для координации между производителями и потребителями.
 * Реализуйте метод size(), который возвращает текущий размер очереди.
 */
public class BlockingLinkedQueue2<T> {
    private static final int DEFAULT_CAPACITY = 16;

    private Node<T> head;

    private Node<T> tail;

    private final int capacity;

    private final AtomicInteger size;

    private final ReentrantLock lock;

    private final Condition fullCondition;

    private final Condition emtpyCondition;

    public BlockingLinkedQueue2(int capacity) {
        this.capacity = capacity;
        size = new AtomicInteger(0);
        lock = new ReentrantLock();
        fullCondition = lock.newCondition();
        emtpyCondition = lock.newCondition();
    }

    public BlockingLinkedQueue2() {
        this(DEFAULT_CAPACITY);
    }

    public void enqueue(T value) throws InterruptedException {
        try {
            lock.lock();
            while (size.get() == capacity) {
                fullCondition.await();
            }
            if (head == null) {
                head = new Node<>(value);
                tail = head;
            } else {
                tail.next = new Node<>(value);
                tail = tail.next;
            }
            size.incrementAndGet();
            emtpyCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public T dequeue() throws InterruptedException {
        try {
            lock.lock();
            while (size.get() == 0) {
                emtpyCondition.await();
            }

            T value = head.value;
            head = head.next;

            size.decrementAndGet();

            fullCondition.signalAll();
            return value;
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        return size.get();
    }

    private static class Node<T> {
        private final T value;

        private Node<T> next;

        public Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }

        public Node(T value) {
            this.value = value;
        }
    }
}
