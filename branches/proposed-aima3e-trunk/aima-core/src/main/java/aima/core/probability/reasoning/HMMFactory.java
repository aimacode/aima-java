package aima.core.probability.reasoning;

import java.util.Arrays;
import java.util.List;

import aima.core.probability.RandomVariable;

/**
 * @author Ravi Mohan
 * 
 */
public class HMMFactory {

	public static HiddenMarkovModel createRobotHMM() {

		// Example adopted from Sebastian Thrun's "Probabilistic Robotics"
		// Chapter 2
		// A robot faces a door. The state of the door (open or closed)
		// constitutes the "hidden state"
		// The robot's sensor detects a "closed" or "open" state.
		// (Perception may be out of synch with reality because sensors are
		// probabilistic).
		// The robot can either "do nothing" or "push" the door. These are the
		// possible actions.

		List<String> states = Arrays.asList(new String[] {
				HmmConstants.DOOR_OPEN, HmmConstants.DOOR_CLOSED });
		List<String> actions = Arrays.asList(new String[] {
				HmmConstants.DO_NOTHING, HmmConstants.PUSH_DOOR });
		List<String> perceptions = Arrays.asList(new String[] {
				HmmConstants.SEE_DOOR_OPEN, HmmConstants.SEE_DOOR_CLOSED });

		RandomVariable prior = new RandomVariable(states);
		TransitionModel tm = new TransitionModel(states, actions);
		// tm.setTransitionProbability(start_state, action, end_state,
		// probability)
		// given a start state and an action the probability of the end state is
		// probability
		tm.setTransitionProbability(HmmConstants.DOOR_OPEN,
				HmmConstants.DO_NOTHING, HmmConstants.DOOR_OPEN, 1.0);
		tm.setTransitionProbability(HmmConstants.DOOR_OPEN,
				HmmConstants.DO_NOTHING, HmmConstants.DOOR_OPEN, 1.0);
		tm.setTransitionProbability(HmmConstants.DOOR_OPEN,
				HmmConstants.DO_NOTHING, HmmConstants.DOOR_CLOSED, 0.0);
		tm.setTransitionProbability(HmmConstants.DOOR_CLOSED,
				HmmConstants.DO_NOTHING, HmmConstants.DOOR_CLOSED, 1.0);
		tm.setTransitionProbability(HmmConstants.DOOR_CLOSED,
				HmmConstants.DO_NOTHING, HmmConstants.DOOR_OPEN, 0.0);

		tm.setTransitionProbability(HmmConstants.DOOR_OPEN,
				HmmConstants.PUSH_DOOR, HmmConstants.DOOR_OPEN, 1.0);
		tm.setTransitionProbability(HmmConstants.DOOR_OPEN,
				HmmConstants.PUSH_DOOR, HmmConstants.DOOR_CLOSED, 0.0);
		tm.setTransitionProbability(HmmConstants.DOOR_CLOSED,
				HmmConstants.PUSH_DOOR, HmmConstants.DOOR_CLOSED, 0.2);
		tm.setTransitionProbability(HmmConstants.DOOR_CLOSED,
				HmmConstants.PUSH_DOOR, HmmConstants.DOOR_OPEN, 0.8);

		SensorModel sm = new SensorModel(states, perceptions);
		// sm.setSensingProbaility(state,perception,p); given a state the
		// probability of a perception is p
		sm.setSensingProbability(HmmConstants.DOOR_OPEN,
				HmmConstants.SEE_DOOR_CLOSED, 0.4);
		sm.setSensingProbability(HmmConstants.DOOR_OPEN,
				HmmConstants.SEE_DOOR_OPEN, 0.6);
		sm.setSensingProbability(HmmConstants.DOOR_CLOSED,
				HmmConstants.SEE_DOOR_OPEN, 0.2);
		sm.setSensingProbability(HmmConstants.DOOR_CLOSED,
				HmmConstants.SEE_DOOR_CLOSED, 0.8);

		HiddenMarkovModel hmm = new HiddenMarkovModel(prior, tm, sm);
		return hmm;
	}

	public static HiddenMarkovModel createRainmanHMM() {
		List<String> states = Arrays.asList(new String[] {
				HmmConstants.RAINING, HmmConstants.NOT_RAINING });
		// no actions because the observer has no way of changing the hidden
		// state and i spassive
		List<String> perceptions = Arrays.asList(new String[] {
				HmmConstants.SEE_UMBRELLA, HmmConstants.SEE_NO_UMBRELLA });

		RandomVariable prior = new RandomVariable(states);

		TransitionModel tm = new TransitionModel(states);
		// tm.setTransitionModelValue(start_state, action, end_state,
		// probability);
		// given a start state and an action the probability of the end state is
		// probability
		tm.setTransitionProbability(HmmConstants.RAINING, HmmConstants.RAINING,
				0.7);
		tm.setTransitionProbability(HmmConstants.RAINING,
				HmmConstants.NOT_RAINING, 0.3);
		tm.setTransitionProbability(HmmConstants.NOT_RAINING,
				HmmConstants.RAINING, 0.3);
		tm.setTransitionProbability(HmmConstants.NOT_RAINING,
				HmmConstants.NOT_RAINING, 0.7);

		SensorModel sm = new SensorModel(states, perceptions);
		// sm.setSensingProbaility(state,perception,p); given a state the
		// probability of a perception is p
		sm.setSensingProbability(HmmConstants.RAINING,
				HmmConstants.SEE_UMBRELLA, 0.9);
		sm.setSensingProbability(HmmConstants.RAINING,
				HmmConstants.SEE_NO_UMBRELLA, 0.1);
		sm.setSensingProbability(HmmConstants.NOT_RAINING,
				HmmConstants.SEE_UMBRELLA, 0.2);
		sm.setSensingProbability(HmmConstants.NOT_RAINING,
				HmmConstants.SEE_NO_UMBRELLA, 0.8);

		HiddenMarkovModel hmm = new HiddenMarkovModel(prior, tm, sm);

		// hmm.setSensorModelValue(state,perception,p); given a state the
		// probability of a perception is p

		return hmm;
	}
}
