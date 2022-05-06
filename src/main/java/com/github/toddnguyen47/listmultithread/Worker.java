package com.github.toddnguyen47.listmultithread;

import java.util.List;

public interface Worker<T> {

    /**
     * What the work needs to be done per list.
     *
     * @param inputList
     */
    void run(final List<T> inputList);
}
