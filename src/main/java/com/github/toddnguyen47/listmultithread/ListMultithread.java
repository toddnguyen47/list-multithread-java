package com.github.toddnguyen47.listmultithread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ListMultithread<T> {

  private final Worker<T> worker;
  private final int numThreads;

  public ListMultithread(Worker<T> worker, final int numThreads) {
    this.worker = worker;
    this.numThreads = numThreads;
  }

  public void runMultithread(final List<T> inputList) throws InterruptedException, ExecutionException {
    final ExecutorService exec = Executors.newFixedThreadPool(numThreads);
    final List<Future<?>> futures = new ArrayList<>();

    for (final T elem : inputList) {
      final Future<?> future = exec.submit(() -> {
        worker.run(elem);
      });
      futures.add(future);
    }

    for (final Future<?> future : futures) {
      future.get();
    }

    // SHUT DOWN exec. THIS MUST BE after the `get()` call!
    exec.shutdown();
  }

  /**
   * <div> Run this service in multithreaded fashion. </div> <div> Ref:
   * https://stackoverflow.com/a/30655678/6323360 </div>
   *
   * @deprecated
   * @param inputList
   * @param client
   * @throws InterruptedException
   * @throws ExecutionException
   */
  public void runMultithreadOld(final List<T> inputList) throws InterruptedException, ExecutionException {

    final int listSize = inputList.size();
    final ExecutorService exec = Executors.newFixedThreadPool(numThreads);
    // Ex: With 14 items, we want [4, 4, 3, 3] items instead of [3, 3, 3, 5] items
    final int minItemsPerThread = listSize / numThreads;
    final int maxItemsPerThread = minItemsPerThread + 1;
    final int threadIndicesWithMaxItems = listSize - (minItemsPerThread * numThreads);
    final List<Future<?>> futures = new ArrayList<>();
    int startIndex = 0;
    for (int i = 0; i < numThreads; i++) {
      final int itemCount = i < threadIndicesWithMaxItems ? maxItemsPerThread : minItemsPerThread;
      final int endIndex = startIndex + itemCount;
      final List<T> subList = Collections.synchronizedList(inputList.subList(startIndex, endIndex));
      futures.add(exec.submit(() -> {
        // worker.run(subList);
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
