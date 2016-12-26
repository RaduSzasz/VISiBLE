package com.visible.symbolic;

import com.visible.symbolic.State;

public interface SymbolicExecutor extends Runnable {
    State stepLeft();
    State stepRight();
}
