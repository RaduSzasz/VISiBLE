package com.visible.jpf;

import java.util.ArrayList;
import java.util.List;

public class TreeInfo {

    List<State> visited;
    State current;

    public TreeInfo() {
        this.current = new State(-1, null, null);
        this.visited = new ArrayList<>();
    }

    public void log(State current) {
        visited.add(current);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        visited.forEach(sb::append);
        return sb.toString();
    }
}
