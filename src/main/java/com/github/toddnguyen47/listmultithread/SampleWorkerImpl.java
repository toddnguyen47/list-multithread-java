package com.github.toddnguyen47.listmultithread;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SampleWorkerImpl implements Worker<String> {
  private final Logger LOGGER = LogManager.getLogger(SampleWorkerImpl.class);

  /**
   * TODO: Edit what to do per elem
   *
   * @param inputList
   */
  public void run(final String elem) {
    LOGGER.info("Doing work on " + elem);
    sleepAndLog();
  }

  private void sleepAndLog() {
    final Random random = new Random();
    try {
      final double extraSeconds = random.nextDouble();
      final int sleepMillis = 200 + ((int) (extraSeconds * 1000));
      LOGGER.info(String.format("Sleep for %s milliseconds", sleepMillis));
      Thread.sleep(sleepMillis);
    } catch (final InterruptedException e) {
      LOGGER.error("Exception on sleep!");
    }
    LOGGER.info("Sleep done!");
  }
}
