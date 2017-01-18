package com.visible.symbolic;

import com.visible.symbolic.state.State;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

public interface SymbolicExecutor {
    State stepLeft();
    State stepRight();
    State execute() throws ExecutionException, InterruptedException, MalformedURLException, URISyntaxException;
    State restart() throws ExecutionException, InterruptedException;
}
