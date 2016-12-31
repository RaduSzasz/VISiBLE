package com.visible.symbolic;

import com.visible.symbolic.state.State;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public interface SymbolicExecutor extends Callable<CountDownLatch> {
    State stepLeft();
    State stepRight();
}
