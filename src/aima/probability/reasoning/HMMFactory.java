package aima.probability.reasoning;

import java.util.Arrays;
import java.util.List;

public class HMMFactory {
	


	public static HiddenMarkovModel createRobotHMM(){
		
		//Example adopted from Sebastian Thrun's "Probabilistic Robotics" Chapter 2
		//A robot faces a door. The state of the door (open or closed) constitutes the "hidden state" 
		//The robot's sensor detects a "closed" or "open" state. 
		//(Perception may be out of synch with reality because sensors are probabilistic). 
		//The robot can either "do nothing" or "push" the door. These are the possible actions. 
		
		List<String> states = Arrays.asList(new String[] {HmmConstants.OPEN, HmmConstants.CLOSED });
		List<String> actions = Arrays.asList(new String[] {HmmConstants.DO_NOTHING, HmmConstants.PUSH });
		List<String> perceptions = Arrays.asList(new String[] {HmmConstants.SEE_OPEN, HmmConstants.SEE_CLOSED });
		
		HiddenMarkovModel hmm = new HiddenMarkovModel(states,perceptions,actions);
		
		//hmm.setTransitionModelValue(start_state, action, end_state, probability);
		//given a start state and an action the probability of the end state is probability
		hmm.setTransitionModelValue(HmmConstants.OPEN, HmmConstants.DO_NOTHING, HmmConstants.OPEN, 1.0);
		hmm.setTransitionModelValue(HmmConstants.OPEN, HmmConstants.DO_NOTHING, HmmConstants.CLOSED, 0.0);
		hmm.setTransitionModelValue(HmmConstants.CLOSED, HmmConstants.DO_NOTHING, HmmConstants.CLOSED, 1.0);
		hmm.setTransitionModelValue(HmmConstants.CLOSED, HmmConstants.DO_NOTHING, HmmConstants.OPEN, 0.0);
		
		
		hmm.setTransitionModelValue(HmmConstants.OPEN, HmmConstants.PUSH, HmmConstants.OPEN, 1.0);
		hmm.setTransitionModelValue(HmmConstants.OPEN, HmmConstants.PUSH, HmmConstants.CLOSED, 0.0);
		hmm.setTransitionModelValue(HmmConstants.CLOSED, HmmConstants.PUSH, HmmConstants.CLOSED, 0.2);
		hmm.setTransitionModelValue(HmmConstants.CLOSED, HmmConstants.PUSH, HmmConstants.OPEN, 0.8);
		
		//hmm.setSensorModelValue(state,perception,p); given a state  the probability of  a perception is p
		hmm.setSensorModelValue(HmmConstants.OPEN,HmmConstants.SEE_CLOSED,0.4);
		hmm.setSensorModelValue(HmmConstants.OPEN,HmmConstants.SEE_OPEN, 0.6);
		hmm.setSensorModelValue(HmmConstants.CLOSED,HmmConstants.SEE_OPEN, 0.2);
		hmm.setSensorModelValue(HmmConstants.CLOSED,HmmConstants.SEE_CLOSED, 0.8);
		return hmm;
	}
	
	public static HiddenMarkovModel createUmbrellaHMM(){
		List<String> states = Arrays.asList(new String[] {HmmConstants.RAINING, HmmConstants.NOT_RAINING });
		//no actions  because the observer has no way of changing the hidden state and i spassive
		List<String> perceptions = Arrays.asList(new String[] {HmmConstants.UMBRELLA, HmmConstants.NO_UMBRELLA});
		
		HiddenMarkovModel hmm = new HiddenMarkovModel(states,perceptions);
		return hmm;
	}

}
