package com.visible.symbolic;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public interface SymbolicExecutor extends Runnable {
    State stepLeft();
    State stepRight();
}
