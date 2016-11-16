package com.visible.jpf;

import gov.nasa.jpf.symbc.numeric.PathCondition;

import java.util.LinkedList;
import java.util.List;

public class State {

  private State parent;
  List<State> children;
  private PathCondition pc;
  int id;

  public int getId() {
    return id;
  }

  public State getParent() {
    return parent;
  }

  public List<State> getChildren() {
    return children;
  }

  public PathCondition getPc() {
    return pc;
  }

  State(int id, State parent, PathCondition pc) {
    this.id = id;
    this.children = new LinkedList<>();
    this.parent = parent;
    this.pc = pc;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("State " + id);
    stringBuilder.append("<br>");
    stringBuilder.append("Parent " + parent.id) ;
    stringBuilder.append("<br>Children [");

    if (children.isEmpty()) {
      stringBuilder.append("empty");
    } else {
      for (State s : children) {
        stringBuilder.append(s.getId());
        stringBuilder.append(", ");
      }
    }

    stringBuilder.append("]<br>");
    stringBuilder.append("PC [" + pc + "]");
    stringBuilder.append("<br>");
    stringBuilder.append("<br>");

    return stringBuilder.toString();
  }

}