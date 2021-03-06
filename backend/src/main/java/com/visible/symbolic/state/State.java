package com.visible.symbolic.state;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.HashMap;
import java.util.Map;

public final class State {

    public static final String ERR_UNKNOWN = "An unknown error occurred.";
    public static final String ERR_ARG_MISMATCH = "Mismatch in number of arguments";
    public static final String ERR_EXEC_NOT_INIT = "Symbolic Executor has not been initialised yet";
    public static final String ERR_RESTART_FAIL = "Restart could not be completed";
    public static final String ERR_NO_MAIN_CLASS = "No entry-point specified in Manifest file";
    public static final String ERR_MISSING_FILE = "No file has been uploaded";
    public static final String ERR_JPF_INTERNAL = "Internal error in JPF";

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
        if (elsePC != null ? !elsePC.equals(state.elsePC) : state.elsePC != null) return false;
        if (!type.equals(state.type)) return false;
        if (errorMsg != null ? !errorMsg.equals(state.errorMsg) : state.errorMsg != null) return false;
        return concreteValues != null ? concreteValues.entrySet().containsAll(state.concreteValues.entrySet()) && state.concreteValues.entrySet().containsAll(concreteValues.entrySet()) : state.concreteValues == null;
    }

    @Override
    public int hashCode() {
        int result = parent != null ? parent.hashCode() : 0;
        result = 31 * result + (ifPC != null ? ifPC.hashCode() : 0);
        result = 31 * result + (elsePC != null ? elsePC.hashCode() : 0);
        result = 31 * result + type.hashCode();
        result = 31 * result + (errorMsg != null ? errorMsg.hashCode() : 0);
        result = 31 * result + id;
        result = 31 * result + (concreteValues != null ? concreteValues.hashCode() : 0);
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

    public State setConcreteValues(Map<String, Integer> concreteValues) {
        this.concreteValues = concreteValues;
        return this;
    }
}