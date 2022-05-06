package com.github.toddnguyen47.listmultithread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ListMultithread<T> {

    private final List<Worker<T>> workers;
    private final int numThreads;

    public ListMultithread(final List<Worker<T>> workers, final int numThreads) {
        assert workers.size() == numThreads : "List of workers must be the same as `numThreads`!";
        this.workers = workers;
        this.numThreads = numThreads;
    }

    /**
     * <div> Run this service in multithreaded fashion. </div> <div> Ref:
     * https://stackoverflow.com/a/30655678/6323360 </div>
     *
     * @param inputList
     * @param client
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void runMultithread(final List<T> inputList) throws InterruptedException, ExecutionException {

        final int listSize = inputList.size();
        final ExecutorService exec = Executors.newFixedThreadPool(numThreads);
        // Ex: With 14 items, we want [4, 4, 3, 3] items instead of [3, 3, 3, 5] items
        final int minItemsPerThread = listSize / numThreads;
        final int maxItemsPerThread = minItemsPerThread + 1;
        final int threadIndicesWithMaxItems = listSize - (minItemsPerThread * numThreads);
        final List<Future<?>> futures = new ArrayList<>(listSize);
        int startIndex = 0;
        for (int i = 0; i < numThreads; i++) {
            final int itemCount = i < threadIndicesWithMaxItems ? maxItemsPerThread : minItemsPerThread;
            final int endIndex = startIndex + itemCount;
            final List<T> subList = Collections.synchronizedList(inputList.subList(startIndex, endIndex));
            final Worker<T> worker = workers.get(i);
            futures.add(exec.submit(() -> {
                worker.run(subList);
            }));
            startIndex = endIndex;
        }

        for (final Future<?> future : futures) {
            future.get();
        }

        // SHUT DOWN exec. THIS MUST BE after the `get()` call!
        exec.shutdown();
    }
}
