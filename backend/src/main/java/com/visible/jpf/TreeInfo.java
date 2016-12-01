package com.visible.jpf;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TreeInfo {

    private static final String DELIM = ",";

    private List<State> states;

    public TreeInfo() {
        this.states = new ArrayList<>();
    }

    public void addState(State current, State left, State right) {
        states.add(current);
        states.add(left);
        states.add(right);
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

    public static <T> String stringWithDelim(List<T> list, String delim) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(list.stream()
                      .map(Object::toString)
                      .collect(Collectors.joining(delim)));
        sb.append("]");
        return sb.toString();
    }

}