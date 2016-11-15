package com.visible.jpf;

public class TreeInfo {

    State current;

    public TreeInfo() {
        this.current = new State(-1, null, null);
    }

    public void setCurrentState(State current) {
        this.current = current;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(current);
        return sb.toString();
    }
}
