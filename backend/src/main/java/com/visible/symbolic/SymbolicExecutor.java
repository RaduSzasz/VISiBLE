package com.visible.symbolic;

import com.visible.symbolic.state.State;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public interface SymbolicExecutor extends Runnable {
    State stepLeft();
    State stepRight();
}
