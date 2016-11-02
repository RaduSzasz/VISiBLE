package com.visible.jpf;

import gov.nasa.jpf.vm.SystemState;

import java.util.HashSet;
import java.util.Set;

public class Logger {

    private Set<String> methods;
    private Set<SystemState> states;

    public Logger() {
        this.states = new HashSet<>();
        this.methods = new HashSet<>();
    }

    public void log(String currentMethod) {
        methods.add(currentMethod);
    }

    public void log(SystemState state) {
        states.add(state);
    }

    @Override
    public String toString() {
        // Print methods called, states visited
        return "JPF Terminated";
    }
}