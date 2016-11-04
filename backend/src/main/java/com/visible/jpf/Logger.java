package com.visible.jpf;

import java.util.ArrayList;
import java.util.List;

public class Logger {

    List<IfState> conditions;

    public Logger() {
        this.conditions = new ArrayList<>();
    }

    public void log(String condition, boolean conditionValue) {
        IfState ifState = new IfState(condition, conditionValue);
        conditions.add(ifState);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        conditions.forEach(sb::append);
        sb.append("JPF Terminated");
        return sb.toString();
    }
}