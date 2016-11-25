package com.visible.jpf;

import gov.nasa.jpf.symbc.numeric.PathCondition;

import java.util.ArrayList;
import java.util.List;

public class TreeInfo {

    public List<State> getStatesToSend() {
        return statesToSend;
    }

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
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (State s: statesToSend) {
            sb.append("{");
            sb.append("id :" + s.getId() + ",\n");
            sb.append("parent_ :" + s.getParent().getId() + ",\n");
            s.getChildren().forEach(state -> sb.append(state.getId()));
            PathCondition pc = s.getPc();
            sb.append("pc :" + ((pc == null) ? "null" : pc.stringPC()));
            sb.append("}\n");
        }
        sb.append("]");
        return sb.toString();
    }
}
