package com.visible.jpf;

import java.util.ArrayList;
import java.util.List;

public class TreeInfo {

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

}
