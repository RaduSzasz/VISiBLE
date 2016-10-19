package gov.nasa.jpf.symbc;

import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.ThreadChoiceGenerator;

public class VisualizerListener extends PropertyListenerAdapter {

    @Override
    public void stateAdvanced(Search search) {
        super.stateAdvanced(search);
   /*     System.out.println("STATE ADVANCED");
        ChoiceGenerator choiceGenerator = search.getVM().getChoiceGenerator();
        if(choiceGenerator instanceof PCChoiceGenerator){
            PCChoiceGenerator pcChoiceGenerator = (PCChoiceGenerator) choiceGenerator;
            System.out.println("Current PC: "+pcChoiceGenerator.getCurrentPC());
            System.out.println("PC choices: "+pcChoiceGenerator.getAllChoices());
        }else if(choiceGenerator instanceof ThreadChoiceGenerator){
            ThreadChoiceGenerator threadChoiceGenerator = (ThreadChoiceGenerator) choiceGenerator;
            System.out.println("Thread choices: "+threadChoiceGenerator.getAllChoices());
        }

        search.setIgnoredState(true); //stop going in this direction and backtrack
  */

    }

    @Override
    public void stateBacktracked(Search search) {
        super.stateBacktracked(search);
        //when backtracked
     //   System.out.println("STATE BACKTRACKED");
    }

    @Override
    public void searchFinished(Search search) {
        super.searchFinished(search);
       // System.out.println("SEARCH FINISHED");
    }

    @Override
    public void stateProcessed(Search search) {
        super.stateProcessed(search);
        //System.out.println("STATE PROCESSED");
    }
}
