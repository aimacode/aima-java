package aima.core.agent.impl.vaccum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aima.core.agent.Action;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.aprog.TableDrivenAgentProgram;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 2.3, page 34.
 * 
 * Figure 2.3 Partial tabulation of a simple agent function for the vacuum-cleaner world
 * shown in Figure 2.2.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class TableDrivenVaccumAgent extends AbstractAgent {

	public TableDrivenVaccumAgent() {
		super(new TableDrivenAgentProgram(getPerceptSequenceActions()));
	}

	//
	// PRIVATE METHODS
	//
	private static Map<List<Percept>, Action> getPerceptSequenceActions() {
		Map<List<Percept>, Action> perceptSequenceActions = new HashMap<List<Percept>, Action>();

		// NOTE: While this particular table could be setup simply
		// using a few loops, the intent is to show how quickly a table
		// based approach grows and becomes unusable.
		List<Percept> ps;
		//
		// Level 1: 4 states
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);

		//
		// Level 2: 4x4 states
		// 1
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		// 2
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		// 3
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		// 4
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);

		//
		// Level 3: 4x4x4 states
		// 1-1
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		// 1-2
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		// 1-3
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		// 1-4
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		// 2-1
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		// 2-2
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		// 2-3
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		// 2-4
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		// 3-1
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		// 3-2
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		// 3-3
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		// 3-4
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		// 4-1
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		// 4-2
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		// 4-3
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		// 4-4
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.A,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty), new VaccumEnvPercept(
				VaccumEnvironment.Location.B,
				VaccumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VaccumEnvironment.ACTION_SUCK);

		//
		// Level 4: 4x4x4x4 states
		// ...

		return perceptSequenceActions;
	}

	private static List<Percept> createPerceptSequence(Percept... percepts) {
		List<Percept> perceptSequence = new ArrayList<Percept>();

		for (Percept p : percepts) {
			perceptSequence.add(p);
		}

		return perceptSequence;
	}
}
