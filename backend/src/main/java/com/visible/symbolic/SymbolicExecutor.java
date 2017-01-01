package com.visible.symbolic;

import com.visible.symbolic.state.State;

import java.util.concurrent.Callable;

public interface SymbolicExecutor extends Callable<State> {
    State stepLeft();
    State stepRight();
}
