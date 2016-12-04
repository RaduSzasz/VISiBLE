package com.visible.jpf;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TreeInfo {

    private static final String DELIM = ",";

    private List<State> states;
    private State currentState;
    private String ifPC;
    private String elsePC;

    TreeInfo() {
        this.states = new ArrayList<>();
        this.currentState = new State(0, null, "true");
        this.ifPC = this.elsePC = null;
    }

    @Override
    public synchronized String toString() {
        StringBuilder sb = new StringBuilder();
        if (states.isEmpty()) {
            sb.append("empty");
        } else {
            states.forEach(sb::append);
        }

        return sb.toString();
    }

    public String toJSON() {
        return stringWithDelim(states, DELIM);
    }

    static <T> String stringWithDelim(List<T> list, String delim) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(list.stream()
                      .map(Object::toString)
                      .collect(Collectors.joining(delim)));
        sb.append("]");
        return sb.toString();
    }

    public String getElsePC() {
        return elsePC;
    }

    public String getIfPC() {
        return ifPC;
    }

    public void setIfPC(String ifPC) {
        this.ifPC = ifPC;
    }

    public void setElsePC(String elsePC) {
        this.elsePC = elsePC;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }
}