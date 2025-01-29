package ru.javacode.synchronizers.task;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class ComplexTask implements Callable<Integer> {
    private final CyclicBarrier cyclicBarrier;

    private final AtomicInteger counter;


    public ComplexTask(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
        counter = new AtomicInteger(1);
    }

    @Override
    public Integer call() throws Exception {
        return execute();
    }

    private int execute() throws Exception {
        Random random = new Random();

        Thread.sleep(random.nextInt(0, 5));
        int result = counter.getAndIncrement();
        cyclicBarrier.await();

        return result;
    }
}
