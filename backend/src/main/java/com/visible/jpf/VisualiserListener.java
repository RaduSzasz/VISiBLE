package com.visible.jpf;

import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.jvm.bytecode.*;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.vm.*;

public class VisualiserListener extends PropertyListenerAdapter {

	private String mainFile;
	public Logger logger;

	public VisualiserListener(String fileName, Logger logger) {
		this.mainFile = fileName.substring(0, fileName.lastIndexOf("."));
		this.logger = logger;
	}

	@Override
	public void stateAdvanced(Search search) {
		super.stateAdvanced(search);
		if (search.getVM().getInstruction() instanceof IfInstruction) {
			System.out.println(search.getDepth());
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

		Instruction instruction = instructionToExecute;
		if (!instruction.getMethodInfo().getClassName().equals(mainFile)) {
			return;
		}

		if (instruction instanceof IfInstruction) {
			IfInstruction ifInstr = (IfInstruction) instruction;
			String sourceCode = instruction.getSourceLine();
			String condition = sourceCode.substring(sourceCode.indexOf('('), sourceCode.indexOf(')'));
			boolean conditionValue = ifInstr.getConditionValue();
			logger.log(condition, conditionValue);
		} else if (instruction instanceof GOTO) {
			GOTO gotoInstr = (GOTO) instruction;
			System.out.println("Do I back jump???? " + gotoInstr.isBackJump());
		}
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