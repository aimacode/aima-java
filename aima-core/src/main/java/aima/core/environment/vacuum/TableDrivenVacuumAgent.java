package aima.core.environment.vacuum;

import java.util.*;

import aima.core.agent.Action;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.DynamicPercept;
import aima.core.agent.impl.aprog.TableDrivenAgentProgram;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 2.3, page 36.<br>
 * <br>
 * Figure 2.3 Partial tabulation of a simple agent function for the
 * vacuum-cleaner world shown in Figure 2.2.
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * 
 */
public class TableDrivenVacuumAgent extends AbstractAgent<Percept, Action> {

	public TableDrivenVacuumAgent() {
		super(new TableDrivenAgentProgram<>(getPerceptSequenceActions()));
	}

	//
	// PRIVATE METHODS
	//
	private static Map<List<Percept>, Action> getPerceptSequenceActions() {
		Map<List<Percept>, Action> perceptSequenceActions = new HashMap<>();

		// NOTE: While this particular table could be setup simply
		// using a few loops, the intent is to show how quickly a table
		// based approach grows and becomes unusable.
		List<Percept> ps;
		//
		// Level 1: 4 states
		ps = createPerceptSequence(createPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(createPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(createPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(createPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);

		//
		// Level 2: 4x4 states
		// 1
		ps = createPerceptSequence(
				createPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(
				createPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(
				createPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(
				createPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		// 2
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		// 3
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		// 4
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);

		//
		// Level 3: 4x4x4 states
		// 1-1
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		// 1-2
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		// 1-3
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		// 1-4
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		// 2-1
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		// 2-2
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		// 2-3
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		// 2-4
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		// 3-1
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		// 3-2
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		// 3-3
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		// 3-4
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		// 4-1
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		// 4-2
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		// 4-3
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		// 4-4
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_RIGHT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_A,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Clean));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_MOVE_LEFT);
		ps = createPerceptSequence(createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty), createPercept(
				VacuumEnvironment.LOCATION_B,
				VacuumEnvironment.LocationState.Dirty));
		perceptSequenceActions.put(ps, VacuumEnvironment.ACTION_SUCK);

		//
		// Level 4: 4x4x4x4 states
		// ...

		return perceptSequenceActions;
	}

	private static List<Percept> createPerceptSequence(Percept... percepts) {
		return new ArrayList<>(Arrays.asList(percepts));
	}
	
	private static DynamicPercept createPercept(String location, VacuumEnvironment.LocationState state) {
		DynamicPercept percept = new DynamicPercept();
		percept.setAttribute(AttNames.CURRENT_LOCATION, location);
		percept.setAttribute(AttNames.CURRENT_STATE, state);
		return percept;
	}
}
