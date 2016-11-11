package com.visible.jpf;

public class TreeInfo {

    State current;

    public TreeInfo() {
        current = new State(-1, null, null);
    }

    public void log(State state) {
        this.current = state;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.toString();
    }
}
