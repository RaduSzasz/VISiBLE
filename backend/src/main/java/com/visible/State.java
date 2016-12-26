package com.visible;

import java.util.LinkedList;
import java.util.List;

public class State {

    private static final String DELIM = ",";
    private State parent;
    public List<State> children;
    private String ifPC;
    private String elsePC;
    int id;

    public void setIfPC(String ifPC) {
        this.ifPC = ifPC;
    }

    public void setElsePC(String elsePC) {
        this.elsePC = elsePC;
    }

    public int getId() {
        return id;
    }

    public State(int id, State parent) {
        this.id = id;
        this.children = new LinkedList<>();
        this.parent = parent;
        this.ifPC = null;
        this.elsePC = null;
    }

//  static <T> String stringWithDelim(List<T> list, String delim) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("[");
//        sb.append(list.stream()
//                      .map(Object::toString)
//                      .collect(Collectors.joining(delim)));
//        sb.append("]");
//        return sb.toString();
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"id\":" + (id == -1 ? "root" : id) + DELIM);
        int parentId = parent == null ? -1 : parent.getId();
        sb.append("\"parent_\":" + parentId + DELIM);
//    sb.append("\"children\": " + stringWithDelim(children.stream().map(State::getId).collect(Collectors.toList()), DELIM) + DELIM);
        sb.append("\"IfPC\":" + ifPC + DELIM);
        sb.append("\"ElsePC\":" + elsePC);
        sb.append("}\n");
        return sb.toString();
    }

    public String toJSON() {
        return toString();
    }
}