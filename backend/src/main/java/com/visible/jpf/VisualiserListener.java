package com.visible.jpf;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.jvm.bytecode.IfInstruction;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.search.heuristic.HeuristicSearch;
import gov.nasa.jpf.search.heuristic.HeuristicState;
import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisualiserListener extends PropertyListenerAdapter {

	private TreeInfo treeInfo;
	private boolean shouldMoveForward;
	private ThreadInfo threadInfo;
	private State prev;
	private Map<Integer, State> stateById;
	private boolean searchHasFinished;
	private Direction direction;
	private int count;
	private HeuristicState heuristicState;

	public VisualiserListener(Config config, JPF jpf, TreeInfo treeInfo) {
		this(config, jpf);
		this.treeInfo = treeInfo;
		this.shouldMoveForward = false;
		this.searchHasFinished = false;
		this.count = 0;
	}

	public TreeInfo getTreeInfo() {
		return treeInfo;
	}

	public boolean moveForward(Direction direction) {
		this.direction = direction;
		this.shouldMoveForward = true;
		threadInfo.setRunning();
		return searchHasFinished;
	}

	public VisualiserListener(Config conf, JPF jpf) {
		prev = new State(- 1, null, null);
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

		System.out.println("[advanced]\n" + s);
		prev = s;
	}

	private State createNewState(Search search) {
		PathCondition pc = null;
		ChoiceGenerator<?> cg = search.getVM().getLastChoiceGeneratorOfType(PCChoiceGenerator.class);
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

	@Override
	public void stateStored(Search search) {
		super.stateStored(search);
		HeuristicSearch heuristicSearch = (HeuristicSearch) search;
		List<HeuristicState> states = heuristicSearch.getChildStates();
		if (heuristicState == null && states != null) {
			heuristicState =  states.isEmpty() ? null : states.get(states.size() - 1);
			if (heuristicState != null) {
				System.out.println("Saved this state");
			}
		}
		count++;
		if (count > 1 && heuristicState != null) {
			System.out.println("Restoring!!!!!!!!");
			search.getVM().restoreState(heuristicState.getVMState());
		}

	}

	@Override
	public void choiceGeneratorAdvanced(VM vm, ChoiceGenerator<?>
					currentCG) {
		ChoiceGenerator<?> cg = vm.getChoiceGenerator();
		Search search = vm.getSearch();

		if (cg instanceof PCChoiceGenerator) {
			if (cg.getTotalNumberOfChoices() > 1) {
				Instruction instruction = vm.getInstruction();
				if (instruction instanceof IfInstruction) {
					while (! shouldMoveForward) {
						ThreadInfo threadInfo = search.getVM().getCurrentThread();
						this.threadInfo = threadInfo;
						threadInfo.setSleeping();
					}
					shouldMoveForward = false;

					if (direction == Direction.LEFT) {
						cg.select(0);
					} else {
						cg.select(1);
					}
				}
			}
		}

	}
}