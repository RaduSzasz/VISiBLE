package com.visible.symbolic;

import com.visible.symbolic.state.State;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public interface SymbolicExecutor extends Callable<State> {
    State stepLeft();
    State stepRight();
    State execute() throws ExecutionException, InterruptedException;
    State restart() throws ExecutionException, InterruptedException;
}
