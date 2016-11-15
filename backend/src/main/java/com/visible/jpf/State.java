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
    stringBuilder.append("\n");
    stringBuilder.append("Parent Id " + parent.id) ;
    stringBuilder.append("\nChildren ");
    children.stream().map(s -> s.getId());
    stringBuilder.append("\n");
    stringBuilder.append("PC " + pc);

    return stringBuilder.toString();
  }

}