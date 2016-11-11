package com.visible.jpf;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.vm.ChoiceGenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class VisualiserListener extends PropertyListenerAdapter {

	private TreeInfo treeInfo;

	public VisualiserListener(Config config, JPF jpf, TreeInfo treeInfo) {
		this(config, jpf);
		this.treeInfo = treeInfo;
	}

	private State root;
	private State prev;
	private Map<Integer, State> stateById;

	public VisualiserListener(Config conf, JPF jpf) {
		root = prev = new State(-1, null, null);
		stateById = new HashMap<>();
		this.treeInfo = new TreeInfo();
	}

	public void stateAdvanced(Search search) {
		if (search.isIgnoredState()) {
			System.out.println("[advance] ignored state - returning!");
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

		System.out.println("[advance] new: " + s);
		prev = s;
		treeInfo.log(s);
		System.out.println("press enter to continue: ");
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		System.out.println("Finished with state " + search.getStateId());
	}

	@Override
	public void stateBacktracked(Search search) {
		State s = stateById.get(search.getStateId());
		System.out.println("[backtrack] s:" + s);
		prev = s;
	}

	@Override
	public void stateRestored(Search search) {
		State s = stateById.get(search.getStateId());
		System.out.println("[backtrack] s:" + s);
		prev = s;
	}

	@Override
	public void searchFinished(Search search) {
		for (Entry<Integer, State> e : stateById.entrySet()) {
			System.out.println(e.getKey() + "\t-> " + e.getValue());
		}
	}
}
