package com.visible.jpf;

import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.vm.*;

public class VisualiserListener extends PropertyListenerAdapter {

	private String mainFile;
	public Logger logger;

	public VisualiserListener(String mainFile, Logger logger) {
		this.mainFile = mainFile.substring(0, mainFile.lastIndexOf("."));
		this.logger = logger;
	}

	@Override
	public void stateAdvanced(Search search) {
		super.stateAdvanced(search);
		logger.log(search.getDepth());
	}

	@Override
	public void stateBacktracked(Search search) {
		super.stateBacktracked(search);
	}

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
		String methodName = instructionToExecute.getMethodInfo().getName();
		String className = instructionToExecute.getMethodInfo().getClassName();

		if (!methodName.equals(logger.getCurrentMethod()) && className.equals(mainFile)) {
			logger.log(methodName);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < logger.getCurrentDepth(); i++) {
				sb.append(" ");
			}
			sb.append(methodName);
			System.out.println(sb.toString());
		}
	}

}
