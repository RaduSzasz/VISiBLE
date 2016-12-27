package com.visible.symbolic;

import java.util.LinkedList;
import java.util.List;

public class State {

    private State parent;
    public List<State> children;
    private String ifPC;
    private String elsePC;
    private int id;

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
        this.children = new LinkedList<State>();
        this.parent = parent;
        this.ifPC = null;
        this.elsePC = null;
    }

}