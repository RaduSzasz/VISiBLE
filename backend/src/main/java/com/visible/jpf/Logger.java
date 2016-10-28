package com.visible.jpf;

public class Logger {

    private String currentMethod;

    public int getCurrentDepth() {
        return currentDepth;
    }

    private int currentDepth;

    public String getCurrentMethod() {
        return currentMethod;
    }

    public Logger() {
    }

    public void log(String currentMethod) {
        this.currentMethod = currentMethod;
    }

    public void log(int currentDepth) {
        this.currentDepth = currentDepth;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("JPF Terminated");
        sb.append("\nFinal Method: ");
        sb.append(currentMethod);
        sb.append("\nFinal Depth: ");
        sb.append(currentDepth);
        return sb.toString();
    }

}
