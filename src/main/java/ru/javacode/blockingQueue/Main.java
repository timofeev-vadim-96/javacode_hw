package ru.javacode.blockingQueue;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        BlockingLinkedQueue<Integer> queue = new BlockingLinkedQueue<>();

        System.out.println(queue.size()); //размер = 0

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                //пытаюсь забрать объект из пустой очереди. Происходит блокировка до notify
                System.out.println(queue.dequeue());
                System.out.println(queue.size());
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                //кладется объект и происходит notify других заблоченных потоков
                queue.enqueue(100);
            }
        });

        thread1.start();
        thread2.start();
    }
}
