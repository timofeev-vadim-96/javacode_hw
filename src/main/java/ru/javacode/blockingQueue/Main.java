package ru.javacode.blockingQueue;

public class Main {
    public static void main(String[] args) {
        BlockingLinkedQueue<Integer> queue = new BlockingLinkedQueue<>(16);

        System.out.println("начальный размер очереди = " + queue.size()); //размер = 0

        Thread thread1 = new Thread(() -> {

                queue.dequeue();

                for (int i = 1; i <= 16; i++) {
                    queue.enqueue(i);
                }

                queue.enqueue(17);

                System.out.println(queue.size());

        });

        Thread thread2 = new Thread(() -> {
            try {
                queue.enqueue(0);

                Thread.sleep(1000);

                queue.dequeue();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        thread1.start();
        thread2.start();
    }
}
