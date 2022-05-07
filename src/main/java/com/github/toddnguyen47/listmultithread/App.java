package com.github.toddnguyen47.listmultithread;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {

  private static final Logger LOGGER = LogManager.getLogger(ListMultithread.class);

  private static final int NUM_THREADS = 4;

  public static void main(final String[] args) {
    final Worker<String> worker = new SampleWorkerImpl();
    final ListMultithread<String> lm = new ListMultithread<>(worker, NUM_THREADS);

    final String inputString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    final List<String> inputList = Arrays.asList(inputString.split(" "));

    try {
      lm.runMultithread(inputList);
    } catch (InterruptedException | ExecutionException e) {
      LOGGER.error(e.getStackTrace());
    }

    LOGGER.info("---");
    LOGGER.info("All Done!");
  }
}
