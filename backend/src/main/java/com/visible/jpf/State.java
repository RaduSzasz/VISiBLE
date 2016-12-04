package com.visible.jpf;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class State {

  private static final String DELIM = ",";
  private State parent;
  List<State> children;
  private String pc;
  int id;

  public void setPc(String pc) {
    this.pc = pc;
  }

  public int getId() {
    return id;
  }

  public State getParent() {
    return parent;
  }

  public List<State> getChildren() {
    return children;
  }

  public String getPc() {
    return pc;
  }

  State(int id, State parent, String pc) {
    this.id = id;
    this.children = new LinkedList<>();
    this.parent = parent;
    this.pc = pc;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    int id = getId();
    sb.append("{");
    sb.append("\"id\":" + (id == -1 ? "root" : id) + DELIM);
    int parentId = getParent() == null ? -1 : getParent().getId();
    sb.append("\"parent_\":" + parentId + DELIM);
    sb.append("\"children\": " + TreeInfo.stringWithDelim(children.stream().map(State::getId).collect(Collectors.toList()), DELIM) + DELIM);
    String pathCondition = (pc.equals("true")) ? pc : pc.substring(pc.indexOf('_') - 1, pc.length());
    sb.append("\"pc\" : \"" + pathCondition + "\"");
    sb.append("}\n");
    return sb.toString();
  }
}