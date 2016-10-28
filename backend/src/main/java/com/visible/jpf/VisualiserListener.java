import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.vm.*;

public class VisualiserListener extends PropertyListenerAdapter {

	@Override
	public void stateAdvanced(Search search) {
			super.stateAdvanced(search);
		ChoiceGenerator choiceGenerator = search.getVM().getChoiceGenerator();
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
		System.out.println(methodName);
	}

}
