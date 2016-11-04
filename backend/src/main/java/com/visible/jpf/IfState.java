package com.visible.jpf;

public class IfState {

    private String condition;
    private boolean conditionValue;

    public String getCondition() {
        return condition;
    }

    public boolean getConditionValue() {
        return conditionValue;
    }

    public IfState(String condition, boolean conditionValue) {
        this.condition = condition;
        this.conditionValue = conditionValue;
    }

    @Override
    public String toString() {
        return "Condition " + condition + ") evaluated to " + conditionValue;
    }

}
