package com.visible.jpf;

import gov.nasa.jpf.symbc.numeric.PathCondition;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
    return "State [id=" + id + " parent=" + parent.id +
            ", children=" + children.stream().map(s -> s.id).collect(Collectors.toList()) + ", pc="
            + pc + "]";
  }

}