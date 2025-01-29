package ru.javacode.synchronizers.executor;

import ru.javacode.synchronizers.provider.ComplexTaskProvider;
import ru.javacode.synchronizers.provider.ComplexTaskProviderImpl;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Создайте класс ComplexTaskExecutor, в котором будет использоваться CyclicBarrier и ExecutorService для синхронизации
 * выполнения задач. Реализуйте метод executeTasks(int numberOfTasks), который создает пул потоков и назначает каждому
 * потоку экземпляр сложной задачи для выполнения. Затем используйте CyclicBarrier для ожидания завершения всех потоков
 * и объединения результатов их работы. В методе main создайте экземпляр ComplexTaskExecutor и вызовите метод
 * executeTasks с несколькими задачами для выполнения.
 */
public class ComplexTaskExecutor implements TaskExecutor {
    private final CyclicBarrier cyclicBarrier;

    private final ExecutorService executorService;

    private final ComplexTaskProvider taskProvider;

    public ComplexTaskExecutor(CyclicBarrier cyclicBarrier,
                               ExecutorService executorService,
                               ComplexTaskProvider taskProvider) {
        this.cyclicBarrier = cyclicBarrier;
        this.executorService = executorService;
        this.taskProvider = taskProvider;
    }

    public ComplexTaskExecutor(int poolSize) {
        executorService = Executors.newFixedThreadPool(poolSize);
        cyclicBarrier = new CyclicBarrier(poolSize);
        taskProvider = new ComplexTaskProviderImpl(cyclicBarrier);
    }

    @Override
    public void executeTasks(int numberOfTasks) {
        try {
            Collection<Callable<Integer>> tasks = taskProvider.getComplexTasks(numberOfTasks);
            List<Future<Integer>> futures = executorService.invokeAll(tasks);

            int result = 0;

            for (Future<Integer> future : futures) {
                try {
                    result += future.get();
                } catch (ExecutionException e) {
                    System.out.println("Ошибка выполнения задачи: " + e.getCause());
                }
            }

            //рекомендация по завершению работы ExecutorService от Oracle
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
            }

            System.out.println("Результат выполнения задач: " + result);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
