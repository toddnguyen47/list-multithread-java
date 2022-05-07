package com.github.toddnguyen47.listmultithread;

public interface Worker<T> {

  /**
   * What the work needs to be done per list.
   *
   * @param inputList
   */
  void run(final T elem);
}
