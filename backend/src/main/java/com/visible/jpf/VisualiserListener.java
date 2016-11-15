package com.visible.jpf;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.ThreadInfo;

import java.util.HashMap;
import java.util.Map;

public class VisualiserListener extends PropertyListenerAdapter {

	private TreeInfo treeInfo;

	private boolean shouldMoveForward;

	private ThreadInfo threadInfo;

	private State prev;

	private Map<Integer, State> stateById;

	public void moveForward() {
		this.shouldMoveForward = true;
		threadInfo.setRunning();
	}

	public TreeInfo getTreeInfo() {
		return treeInfo;
	}


	public VisualiserListener(Config config, JPF jpf, TreeInfo treeInfo) {
		this(config, jpf);
		this.treeInfo = treeInfo;
		this.shouldMoveForward = false;
	}

	public VisualiserListener(Config conf, JPF jpf) {
		prev = new State(-1, null, null);
		stateById = new HashMap<>();
		this.treeInfo = new TreeInfo();
	}

	public void stateAdvanced(Search search) {
		if (search.isIgnoredState()) {
			System.out.println("[advanced] ignored state - returning!");
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

		treeInfo.setCurrentState(s);
		System.out.println("[advanced]");
		prev = s;
		System.out.println("Tree Information: " + treeInfo.toString());

		// Communication with Frontend
		while (!shouldMoveForward) {
				ThreadInfo threadInfo = search.getVM().getCurrentThread();
				this.threadInfo = threadInfo;
				threadInfo.setSleeping();
		}

		shouldMoveForward = false;
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
//		for (Entry<Integer, State> e : stateById.entrySet()) {
//			System.out.println(e.getKey() + "\t-> " + e.getValue());
//		}
	}

}
