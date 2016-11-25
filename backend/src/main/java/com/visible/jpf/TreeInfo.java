package com.visible.jpf;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TreeInfo {

    private static final String DELIM = ",";

    private List<State> statesToSend;

    public TreeInfo() {
        this.statesToSend = new ArrayList<>();
    }

    public synchronized void addState(State current) {
        statesToSend.add(current);
    }

    @Override
    public synchronized String toString() {
        StringBuilder sb = new StringBuilder();
        if (statesToSend.isEmpty()) {
            sb.append("empty");
        } else {
            statesToSend.forEach(sb::append);
        }

        return sb.toString();
    }

    public String toJSON() {
        return stringWithDelim(statesToSend, DELIM);
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
