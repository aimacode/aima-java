package aima.probability.reasoning;

import java.util.Arrays;
import java.util.List;

public class HMMFactory {
	
	private static final String PUSH = "push";
	private static final String DO_NOTHING = "do_nothing";
	private static final String CLOSED = "closed";
	private static final String OPEN = "open";
	private static final String SEE_CLOSED = "see_closed";
	private static final String SEE_OPEN = "see_open";

	public static HiddenMarkovModel createRobotHMM(){
		
		//Example adopted from Sebastian Thrun's "Probabilistic Robotics" Chapter 2
		//A robot faces a door. The state of the door (open or closed) constitutes the "hidden state" 
		//The robot's sensor detects a "closed" or "open" state. 
		//(Perception may be out of synch with reality because sensors are probabilistic). 
		//The robot can either "do nothing" or "push" the door. These are the possible actions. 
		
		List<String> states = Arrays.asList(new String[] {OPEN, CLOSED });
		List<String> actions = Arrays.asList(new String[] {DO_NOTHING, PUSH });
		List<String> perceptions = Arrays.asList(new String[] {SEE_OPEN, SEE_CLOSED });
		
		HiddenMarkovModel hmm = new HiddenMarkovModel(states,perceptions,actions);
		
		//hmm.setTransitionModelValue(start_state, action, end_state, probability);
		//given a start state and an action the probability of the end state is probability
		hmm.setTransitionModelValue(OPEN, DO_NOTHING, OPEN, 1.0);
		hmm.setTransitionModelValue(OPEN, DO_NOTHING, CLOSED, 0.0);
		hmm.setTransitionModelValue(CLOSED, DO_NOTHING, CLOSED, 1.0);
		hmm.setTransitionModelValue(CLOSED, DO_NOTHING, OPEN, 0.0);
		
		
		hmm.setTransitionModelValue(OPEN, PUSH, OPEN, 1.0);
		hmm.setTransitionModelValue(OPEN, PUSH, CLOSED, 0.0);
		hmm.setTransitionModelValue(CLOSED, PUSH, CLOSED, 0.2);
		hmm.setTransitionModelValue(CLOSED, PUSH, OPEN, 0.8);
		
		//hmm.setSensorModelValue(state,perception,p); given a state  the probability of  a perception is p
		hmm.setSensorModelValue(OPEN,SEE_CLOSED,0.4);
		hmm.setSensorModelValue(OPEN,SEE_OPEN, 0.6);
		hmm.setSensorModelValue(CLOSED,SEE_OPEN, 0.2);
		hmm.setSensorModelValue(CLOSED,SEE_CLOSED, 0.8);
		return hmm;
	}

}
