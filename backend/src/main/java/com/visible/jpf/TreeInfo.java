package com.visible.jpf;

import java.util.ArrayList;
import java.util.List;

public class TreeInfo {

    State current;
    List<State> visited = new ArrayList<>();

    public TreeInfo() {
        this.current = new State(-1, null, null);
    }

    public synchronized void setCurrentState(State current) {
        this.current = current;
        visited.add(current);
    }

    @Override
    public synchronized String toString() {
        StringBuilder sb = new StringBuilder();
//        sb.append(current);
        if (visited.isEmpty()) {
            sb.append("empty");
        } else {
            visited.forEach(sb::append);
        }

        return sb.toString();
    }
}
