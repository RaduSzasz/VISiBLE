package com.visible.symbolic.jpf;

import com.visible.symbolic.Direction;
import com.visible.symbolic.State;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.jvm.bytecode.*;
import gov.nasa.jpf.jvm.bytecode.IFEQ;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.symbc.bytecode.*;
import gov.nasa.jpf.symbc.bytecode.IFGE;
import gov.nasa.jpf.symbc.bytecode.IFGT;
import gov.nasa.jpf.symbc.bytecode.IFLE;
import gov.nasa.jpf.symbc.bytecode.IFLT;
import gov.nasa.jpf.symbc.bytecode.IFNE;
import gov.nasa.jpf.symbc.bytecode.IF_ICMPEQ;
import gov.nasa.jpf.symbc.bytecode.IF_ICMPGE;
import gov.nasa.jpf.symbc.bytecode.IF_ICMPGT;
import gov.nasa.jpf.symbc.bytecode.IF_ICMPLE;
import gov.nasa.jpf.symbc.bytecode.IF_ICMPLT;
import gov.nasa.jpf.symbc.bytecode.IF_ICMPNE;
import gov.nasa.jpf.symbc.numeric.Comparator;
import gov.nasa.jpf.symbc.numeric.IntegerExpression;
import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.vm.*;

import java.util.*;

public class VisualiserListener extends PropertyListenerAdapter {

    private boolean shouldMoveForward;
    private ThreadInfo threadInfo;
    private State prev;
    private Map<Integer, State> stateById;
    private boolean searchHasFinished;
    private List<String> choicesTrace;
    private Direction direction;
    private State currentState;

    private static final Map<Class, Comparator> singleBranchComparators = singleBranchComparatorsBuilder();
    private final Map<Class, Comparator> doubleBranchComparators = doubleBranchComparatorsBuilder();
    private static final Map<Comparator, Comparator> comparatorsComplement = comparatorsComplementBuilder();

    boolean moveForward(Direction direction) {
        this.shouldMoveForward = true;
        threadInfo.setRunning();
        this.direction = direction;
        return searchHasFinished;
    }

    VisualiserListener(Config config, JPF jpf) {
        prev = null;
        stateById = new HashMap<>();
        this.shouldMoveForward = false;
        this.searchHasFinished = false;
        this.choicesTrace = initializeChoicesTrace();
        this.currentState = null;
    }

    public void stateAdvanced(Search search) {
        if (search.isIgnoredState()) {
            System.out.println("[advanced] ignored state");
            return;
        }

        boolean isNew = search.isNewState();
        State s;
        if (isNew) {
            s = createNewState(search);
            stateById.put(s.getId(), s);
        } else {
            s = stateById.get(search.getStateId());
        }

//        System.out.println("[advanced]\n" + (s == null ? "null" : s));
        prev = s;

        while (!this.shouldMoveForward) {
            ThreadInfo threadInfo = search.getVM().getCurrentThread();
            this.threadInfo = threadInfo;
            threadInfo.setSleeping();
        }
        this.shouldMoveForward = false;
        this.currentState = s;
    }

    private State createNewState(Search search) {
        PathCondition pc = null;
        ChoiceGenerator<?> cg = search.getVM().getLastChoiceGeneratorOfType(PCChoiceGenerator.class);
        if (cg != null) {
            pc = ((PCChoiceGenerator) cg).getCurrentPC();
        }

        State s = new State(search.getStateId(), prev);
        if (prev != null) {
            prev.children.add(s);
        }
        return s;
    }

    @Override
    public void stateProcessed(Search search) {
        System.out.println("Finished with State " + search.getStateId());
    }

    @Override
    public void stateRestored(Search search) {
        State s = stateById.get(search.getStateId());
        System.out.println("[restored]");
        prev = s;
    }

    @Override
    public void stateBacktracked(Search search) {
        State s = stateById.get(search.getStateId());
        System.out.println("[backtracked]");
        prev = s;
    }
    @Override
    public void searchFinished(Search search) {
        System.out.println("[finished]");
        this.searchHasFinished = true;
    }

    private List<String> initializeChoicesTrace() {
        List<String> newTrace = new LinkedList<String>();
        newTrace.add("TRUE");
        BytecodeUtils.clearSymVarCounter();
        return newTrace;
    }

    @Override
    public void choiceGeneratorAdvanced(VM vm, ChoiceGenerator<?> currentCG) {
        ChoiceGenerator<?> cg = vm.getChoiceGenerator();

        if (cg instanceof PCChoiceGenerator) {
            if (cg.getTotalNumberOfChoices() > 1) {
                Instruction instruction = vm.getInstruction();
                ThreadInfo threadInfo = vm.getCurrentThread();
                if (instruction instanceof IfInstruction) {
                    Boolean nextStep = null;
                    nextStep = computeIFBranchPC(instruction, threadInfo, cg);

                    if (nextStep == null) {
                        if (this.choicesTrace.size() > 1) {
                            this.choicesTrace.remove(this.choicesTrace.size() - 1);
                        }
                        vm.ignoreState();
                    } else {
                        if (nextStep) {
                            cg.select(0);
                        } else {
                            cg.select(1);
                        }
                    }
                }
            }
        }
    }

    private Boolean computeIFBranchPC(Instruction instruction, ThreadInfo threadInfo, ChoiceGenerator<?> currentChoiceGenerator) {
        StackFrame sf = threadInfo.getTopFrame();
        PathCondition pathConditionIFBranch = null;
        PathCondition pathConditionELSEBranch = null;
        PathCondition previousPathCondition = null;

        ChoiceGenerator<?> previousChoiceGenerator = currentChoiceGenerator.getPreviousChoiceGenerator();
        while (!((previousChoiceGenerator == null) || (previousChoiceGenerator instanceof PCChoiceGenerator))) {
            previousChoiceGenerator = previousChoiceGenerator.getPreviousChoiceGenerator();
        }
        previousPathCondition = (previousChoiceGenerator == null) ? new PathCondition() : ((PCChoiceGenerator) previousChoiceGenerator).getCurrentPC();

        pathConditionIFBranch = previousPathCondition.make_copy();
        pathConditionELSEBranch = previousPathCondition.make_copy();

        List<String> choicesTraceIF = new LinkedList<>(this.choicesTrace);
        List<String> choicesTraceELSE = new LinkedList<>(this.choicesTrace);

        if (singleBranchComparators.containsKey(instruction.getClass())) {
            // Single operand
            IntegerExpression sym_v = (IntegerExpression) sf.getOperandAttr();
            Comparator comparator = singleBranchComparators.get(instruction.getClass());
            pathConditionIFBranch._addDet(comparator, sym_v, 0);
            pathConditionELSEBranch._addDet(comparatorsComplement.get(comparator), sym_v, 0);

            addClause(choicesTraceIF, comparator, sym_v, 0);
            addClause(choicesTraceELSE, comparatorsComplement.get(comparator), sym_v, 0);

        } else if (doubleBranchComparators.containsKey(instruction.getClass())) {
            int v2 = threadInfo.getModifiableTopFrame().peek();
            int v1 = threadInfo.getModifiableTopFrame().peek(1);
            IntegerExpression sym_v1 = (IntegerExpression) sf.getOperandAttr(1);
            IntegerExpression sym_v2 = (IntegerExpression) sf.getOperandAttr(0);
            Comparator comparator = doubleBranchComparators.get(instruction.getClass());
            if (sym_v1 != null) {
                if (sym_v2 != null) { // both are symbolic values
                    pathConditionIFBranch._addDet(comparator, sym_v1, sym_v2);
                    pathConditionELSEBranch._addDet(comparatorsComplement.get(comparator), sym_v1, sym_v2);

                    addClause(choicesTraceIF, comparator, sym_v1, sym_v2);
                    addClause(choicesTraceELSE, comparatorsComplement.get(comparator), sym_v1, sym_v2);
                } else {
                    pathConditionIFBranch._addDet(comparator, sym_v1, v2);
                    pathConditionELSEBranch._addDet(comparatorsComplement.get(comparator), sym_v1, v2);

                    addClause(choicesTraceIF, comparator, sym_v1, v2);
                    addClause(choicesTraceELSE, comparatorsComplement.get(comparator), sym_v1, v2);
                }
            } else {
                pathConditionIFBranch._addDet(comparator, v1, sym_v2);
                pathConditionELSEBranch._addDet(comparatorsComplement.get(comparator), v1, sym_v2);

                addClause(choicesTraceIF, comparator, v1, sym_v2);
                addClause(choicesTraceELSE, comparatorsComplement.get(comparator), v1, sym_v2);
            }
        }

        // The code above returns the PCs swapped around for some reason...
        String ifPC = choicesTraceELSE.get(choicesTraceELSE.size() - 1);
        this.currentState.setIfPC(ifPC);

        String elsePC = choicesTraceIF.get(choicesTraceIF.size() - 1);
        this.currentState.setElsePC(elsePC);

        boolean switchCondition = this.direction == Direction.LEFT;

        if (switchCondition) {
            this.choicesTrace = choicesTraceELSE;
        } else {
            this.choicesTrace = choicesTraceIF;
        }
        return switchCondition;

    }

    private String cleanConstraint(String constraint) {
        String clean = constraint.replaceAll("\\s+", "");
        clean = clean.replaceAll("CONST_(\\d+)", "$1");
        clean = clean.replaceAll("CONST_-(\\d+)", "-$1");
        return clean;
    }

    private void addClause(List<String> choicesTrace, Comparator comparator, int v1, IntegerExpression sym_v2) {
        PathCondition emptyPC = new PathCondition();
        emptyPC._addDet(comparator, v1, sym_v2);
        String representation = cleanConstraint(emptyPC.header.toString());
        choicesTrace.add(representation);
    }

    private void addClause(List<String> choicesTrace, Comparator comparator, IntegerExpression sym_v1, IntegerExpression sym_v2) {
        PathCondition emptyPC = new PathCondition();
        emptyPC._addDet(comparator, sym_v1, sym_v2);
        String representation = cleanConstraint(emptyPC.header.toString());
        choicesTrace.add(representation);
    }

    private void addClause(List<String> choicesTrace, Comparator comparator, IntegerExpression sym_v, int i) {
        PathCondition emptyPC = new PathCondition();
        emptyPC._addDet(comparator, sym_v, i);
        String representation = cleanConstraint(emptyPC.header.toString());
        choicesTrace.add(representation);
    }

    @SuppressWarnings("rawtypes")
    private static Map<Class, Comparator> singleBranchComparatorsBuilder() {
        Map<Class, Comparator> map = new HashMap<>();
        map.put(IFEQ.class, Comparator.EQ);
        map.put(IFGE.class, Comparator.GE);
        map.put(IFGT.class, Comparator.GT);
        map.put(IFLE.class, Comparator.LE);
        map.put(IFLT.class, Comparator.LT);
        map.put(IFNE.class, Comparator.NE);
        return map;
    }
    @SuppressWarnings("rawtypes")
    private static Map<Class, Comparator> doubleBranchComparatorsBuilder() {
        Map<Class, Comparator> map = new HashMap<>();
        map.put(IF_ICMPEQ.class, Comparator.EQ);
        map.put(IF_ICMPGE.class, Comparator.GE);
        map.put(IF_ICMPGT.class, Comparator.GT);
        map.put(IF_ICMPLE.class, Comparator.LE);
        map.put(IF_ICMPLT.class, Comparator.LT);
        map.put(IF_ICMPNE.class, Comparator.NE);
        return map;
    }

    @SuppressWarnings("rawtypes")
    private static Map<Comparator, Comparator> comparatorsComplementBuilder() {
        Map<Comparator, Comparator> map = new HashMap<>();
        map.put(Comparator.EQ, Comparator.NE);
        map.put(Comparator.GE, Comparator.LT);
        map.put(Comparator.GT, Comparator.LE);
        map.put(Comparator.LE, Comparator.GT);
        map.put(Comparator.LT, Comparator.GE);
        map.put(Comparator.NE, Comparator.EQ);
        return map;
    }

    State getCurrentState() {
        return currentState;
    }
}