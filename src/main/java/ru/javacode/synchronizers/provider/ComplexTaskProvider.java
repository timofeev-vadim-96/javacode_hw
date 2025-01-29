package ru.javacode.synchronizers.provider;

import java.util.Collection;
import java.util.concurrent.Callable;

public interface ComplexTaskProvider {
    Collection<Callable<Integer>> getComplexTasks(int quantity);
}
