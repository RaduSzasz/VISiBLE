package com.visible.jpf;

import gov.nasa.jpf.symbc.numeric.PathCondition;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class State {

  private static final String DELIM = ",";
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
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    sb.append("\"id\":" + getId() + DELIM);
    sb.append("\"parent_\":" + getParent().getId() + DELIM);
    sb.append("\"children\": " + TreeInfo.stringWithDelim(children.stream().map(State::getId).collect(Collectors.toList()), DELIM) + DELIM);
    PathCondition pc = getPc();
    String pathCondition = "";
    if (pc == null) {
        pathCondition = "true";
    } else {
        pathCondition = pc.stringPC();
        pathCondition = pathCondition.substring(pathCondition.indexOf('_') - 1, pathCondition.length());
    }
    sb.append("\"pc\" : \"" + pathCondition + "\"");
    sb.append("}\n");
    return sb.toString();
  }



}