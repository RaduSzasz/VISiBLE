package com.visible.symbolic.state;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashMap;
import java.util.Map;

public final class State {

    private static final String ERROR = "error";
    @JsonSerialize(using = ParentSerializer.class)
    private State parent;

    @JsonSerialize(using = ConditionSerializer.class)
    private String ifPC;

    @JsonSerialize(using = ConditionSerializer.class)
    private String elsePC;

    private String type;
    @JsonInclude(JsonInclude.Include.NON_EMPTY) private String errorMsg;
    private int id;

    @JsonSerialize(keyUsing = VarNameSerializer.class)
    private Map<String, Integer> concreteValues;

    public State setIfPC(String ifPC) {
        this.ifPC = ifPC;
        return this;
    }

    public State setElsePC(String elsePC) {
        this.elsePC = elsePC;
        return this;
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
        this.parent = parent;
        this.ifPC = null;
        this.elsePC = null;
        this.concreteValues = new HashMap<>();
    }

    public static State createErrorState(String errorMsg) {
        State s = new State(-1, null);
        s.setError(errorMsg);
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        if (id != state.id) return false;
        if (parent != null ? !parent.equals(state.parent) : state.parent != null) return false;
        if (ifPC != null ? !ifPC.equals(state.ifPC) : state.ifPC != null) return false;
        return elsePC != null ? elsePC.equals(state.elsePC) : state.elsePC == null;

    }

    @Override
    public int hashCode() {
        int result = parent != null ? parent.hashCode() : 0;
        result = 31 * result + (ifPC != null ? ifPC.hashCode() : 0);
        result = 31 * result + (elsePC != null ? elsePC.hashCode() : 0);
        result = 31 * result + id;
        return result;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public String getType() {
        return type;
    }

    public State setType(String type) {
        this.type = type;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public State setError(String errorMsg) {
        this.type = ERROR;
        this.errorMsg = errorMsg;
        return this;
    }

    public Map<String, Integer> getConcreteValues() {
        return concreteValues;
    }

    public void setConcreteValues(Map<String, Integer> concreteValues) {
        this.concreteValues = concreteValues;
    }
}