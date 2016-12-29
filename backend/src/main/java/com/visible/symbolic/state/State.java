package com.visible.symbolic.state;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.LinkedList;
import java.util.List;

public class State {

    @JsonSerialize(using = ParentSerializer.class)
    private State parent;
    @JsonIgnore public List<State> children;
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

    public State getParent() {
        return parent;
    }

    public String getIfPC() {
        return ifPC;
    }

    public String getElsePC() {
        return elsePC;
    }

    public State(int id, State parent) {
        this.id = id;
        this.children = new LinkedList<State>();
        this.parent = parent;
        this.ifPC = null;
        this.elsePC = null;
    }

}