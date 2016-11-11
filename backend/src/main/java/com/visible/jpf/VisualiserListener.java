package com.visible.jpf;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.vm.*;

public class VisualiserListener extends PropertyListenerAdapter {

	private String mainFile;
	public Logger logger;

	public VisualiserListener(Config config, JPF jpf) {

	}

	public VisualiserListener(String targetName, Logger logger) {
		this.mainFile = targetName;
		this.logger = logger;
	}

	@Override
	public void stateAdvanced(Search search) {
		super.stateAdvanced(search);
		ChoiceGenerator cg = search.getVM().getChoiceGenerator();
		if (cg instanceof PCChoiceGenerator) {
			PCChoiceGenerator pcg = (PCChoiceGenerator) cg;
			PathCondition pc = pcg.getCurrentPC();
			System.out.println("Current PC: " + pc + " simplifies to " + pc.simplify
							());
		}
	}

	@Override
	public void stateBacktracked(Search search) {
		super.stateBacktracked(search);
	}

	@Override
	public void stateProcessed(Search search) {
		super.stateProcessed(search);
	}

	@Override
	public void searchFinished(Search search) {
		super.searchFinished(search);
	}

	@Override
	public void choiceGeneratorAdvanced(VM vm, ChoiceGenerator<?> currentCG) {
		super.choiceGeneratorAdvanced(vm, currentCG);
	}

	@Override
	public void executeInstruction(VM vm, ThreadInfo currentThread, Instruction instructionToExecute) {
		super.executeInstruction(vm, currentThread, instructionToExecute);
	}

	@Override
	public void methodEntered(VM vm, ThreadInfo currentThread, MethodInfo enteredMethod) {
		super.methodEntered(vm, currentThread, enteredMethod);
	}

	@Override
	public void methodExited(VM vm, ThreadInfo currentThread, MethodInfo exitedMethod) {
		super.methodExited(vm, currentThread, exitedMethod);
	}
}