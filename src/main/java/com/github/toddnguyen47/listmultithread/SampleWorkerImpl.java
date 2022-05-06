package com.github.toddnguyen47.listmultithread;

import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SampleWorkerImpl<T> implements Worker<T> {
    private final Logger LOGGER = LogManager.getLogger(SampleWorkerImpl.class);
    private final ReentrantLock LOCK = new ReentrantLock();

    private int currentProgress = 0; // NOPMD

    /**
     * TODO: Edit what to do per list
     *
     * @param inputList
     */
    public void run(final List<T> inputList) {
        final int listSize = inputList.size();
        LOGGER.info(String.format("List size: %d", listSize));

        for (int i = 0; i < listSize; i++) {
            sleepAndLog();
            // Locking our lock. This should be the last statement ran.
            lockAndIncrementProgress(listSize);
        }
    }

    private void sleepAndLog() {
        final Random random = new Random();
        try {
            final double extraSeconds = random.nextDouble();
            final int sleepMillis = 1000 + ((int) (extraSeconds * 1000));
            LOGGER.info(String.format("Sleep for %s milliseconds", sleepMillis));
            Thread.sleep(sleepMillis);
        } catch (final InterruptedException e) {
            LOGGER.error("Exception on sleep!");
        }
        LOGGER.info("Sleep done!");
    }

    private void lockAndIncrementProgress(final int listSize) {
        LOCK.lock();
        try {
            // CRITICAL SECTION
            // Try to minimize code here!
            currentProgress += 1;
            LOGGER.info(getProgressString(currentProgress, listSize));
        } finally {
            LOCK.unlock();
        }
    }

    private String getProgressString(final int current, final int maxProgress) {
        // GUARD
        if (maxProgress <= 0) {
            return "MAX PROGRESS IS ZERO";
        }

        final double percentage = (current / (double) maxProgress) * 100.0;
        return String.format("PROCESSING: %d/%d, %.2f%%", current, maxProgress, percentage);
    }
}
