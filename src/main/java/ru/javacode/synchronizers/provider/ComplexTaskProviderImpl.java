package ru.javacode.synchronizers.provider;

import ru.javacode.synchronizers.task.ComplexTask;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ComplexTaskProviderImpl implements ComplexTaskProvider {
    private final CyclicBarrier cyclicBarrier;

    public ComplexTaskProviderImpl(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public Collection<Callable<Integer>> getComplexTasks(int quantity) {
        return IntStream.range(0, quantity)
                .mapToObj(n -> new ComplexTask(cyclicBarrier))
                .collect(Collectors.toList());
    }
}
