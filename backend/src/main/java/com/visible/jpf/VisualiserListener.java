package com.visible.jpf;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.jvm.bytecode.IfInstruction;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;

import java.util.HashMap;
import java.util.Map;

public class VisualiserListener extends PropertyListenerAdapter {

	private TreeInfo treeInfo;
	private boolean shouldMoveForward;
	private ThreadInfo threadInfo;
	private State prev;
	private Map<Integer, State> stateById;
	private boolean searchHasFinished;
	private int shouldIBacktrack = 0;

	public TreeInfo getTreeInfo() {
		return treeInfo;
	}

	public boolean moveForward() {
		this.shouldMoveForward = true;
		threadInfo.setRunning();
		return searchHasFinished;
	}

	public VisualiserListener(Config config, JPF jpf, TreeInfo treeInfo) {
		this(config, jpf);
		this.treeInfo = treeInfo;
		this.shouldMoveForward = false;
		this.searchHasFinished = false;
	}

	public VisualiserListener(Config conf, JPF jpf) {
		prev = new State(-1, null, null);
		stateById = new HashMap<>();
		this.treeInfo = new TreeInfo();
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
			stateById.put(s.id, s);
		} else {
			s = stateById.get(search.getStateId());
			
		}

		treeInfo.addState(s);
		System.out.println("[advanced]");
		System.out.println(s);
		prev = s;

		while (!shouldMoveForward) {
				ThreadInfo threadInfo = search.getVM().getCurrentThread();
				this.threadInfo = threadInfo;
				threadInfo.setSleeping();
		}

		shouldMoveForward = false;

		if (shouldIBacktrack >= 1) {
            System.out.println("BACKTRAAAAACKING!!!!!");
            search.getVM().backtrack();
        }
	}

	private State createNewState(Search search) {
		PathCondition pc = null;
		ChoiceGenerator<?> cg = search.getVM()
				.getLastChoiceGeneratorOfType(PCChoiceGenerator.class);
		if (cg != null) {
			pc = ((PCChoiceGenerator) cg).getCurrentPC();
		}

		State s = new State(search.getStateId(), prev, pc);
		prev.children.add(s);
		return s;
	}

	@Override
	public void stateProcessed(Search search) {
		System.out.println("Finished with State " + search.getStateId());
	}

	@Override
	public void stateBacktracked(Search search) {
		State s = stateById.get(search.getStateId());
		System.out.println("[backtracked]");
		prev = s;
	}

	@Override
	public void stateRestored(Search search) {
		State s = stateById.get(search.getStateId());
		System.out.println("[backtracked]");
		prev = s;
	}

	@Override
	public void searchFinished(Search search) {
		System.out.println("[finished]");
		this.searchHasFinished = true;
	}
	boolean nextStep = true;

	@Override
	public void choiceGeneratorAdvanced(VM vm, ChoiceGenerator<?> currentCG) {
		ChoiceGenerator<?> cg = vm.getChoiceGenerator();
		Search search = vm.getSearch();
		int currentState = search.getStateId();
		System.out.println("CGs equal?" + (cg == currentCG));
		if (cg instanceof PCChoiceGenerator) {

			if (cg.getTotalNumberOfChoices() > 1) {

			    shouldIBacktrack++;
				Instruction instruction = vm.getInstruction();

				ThreadInfo threadInfo = vm.getCurrentThread();

				if (instruction instanceof IfInstruction) {

					if (nextStep) {
						System.out.println("currentState = " + currentState + "LEFT");
						((PCChoiceGenerator) cg).select(0);
					} else {
						System.out.println("currentState = " + currentState + "RIGHT");
						((PCChoiceGenerator) cg).select(1);
					}
				}

			}
		}
		nextStep = !nextStep;
	}
}
