package aima.core.environment.vacuum;

import aima.core.agent.Action;
import aima.core.agent.impl.SimpleAgent;
import aima.core.agent.impl.DynamicPercept;
import aima.core.agent.impl.aprog.TableDrivenAgentProgram;
import aima.core.environment.vacuum.VacuumEnvironment.LocationState;

import java.util.*;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 2.3, page 36.<br>
 * <br>
 * Figure 2.3 Partial tabulation of a simple agent function for the
 * vacuum-cleaner world shown in Figure 2.2.
 *
 * @author Ruediger Lunde
 * @author Ciaran O'Reilly
 */
public class TableDrivenVacuumAgent extends SimpleAgent<DynamicPercept, Action> {

	public TableDrivenVacuumAgent() {
		super(new TableDrivenAgentProgram<>(createPerceptsToActionMap()));
	}

	private static Map<List<DynamicPercept>, Action> createPerceptsToActionMap() {
		Map<List<DynamicPercept>, Action> result = new HashMap<>();

		// NOTE: While this particular result could be setup simply
		// using a few loops, the intent is to show how quickly a result
		// based approach grows and becomes unusable.
		//
		// Level 1: 4 states
		result.put(Collections.singletonList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_RIGHT);
		result.put(Collections.singletonList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		result.put(Collections.singletonList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_LEFT);
		result.put(Collections.singletonList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);

		//
		// Level 2: 4x4 states
		// 1
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_RIGHT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_LEFT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		// 2
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_RIGHT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_LEFT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		// 3
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_RIGHT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_LEFT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		// 4
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_RIGHT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_LEFT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);

		//
		// Level 3: 4x4x4 states
		// 1-1
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_RIGHT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_LEFT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		// 1-2
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_RIGHT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_LEFT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		// 1-3
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_RIGHT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_LEFT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		// 1-4
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_RIGHT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_LEFT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		// 2-1
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_RIGHT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_LEFT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		// 2-2
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_RIGHT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_LEFT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		// 2-3
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_RIGHT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_LEFT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		// 2-4
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_RIGHT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_LEFT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		// 3-1
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_RIGHT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_LEFT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		// 3-2
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_RIGHT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_LEFT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		// 3-3
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_RIGHT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_LEFT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		// 3-4
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_RIGHT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_LEFT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		// 4-1
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_RIGHT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_LEFT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		// 4-2
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_RIGHT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_LEFT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		// 4-3
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_RIGHT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_LEFT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		// 4-4
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_RIGHT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_A, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Clean)),
				VacuumEnvironment.ACTION_MOVE_LEFT);
		result.put(Arrays.asList(
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty),
				createPercept(VacuumEnvironment.LOCATION_B, LocationState.Dirty)),
				VacuumEnvironment.ACTION_SUCK);

		//
		// Level 4: 4x4x4x4 states
		// ...

		return result;
	}

	private static DynamicPercept createPercept(String location, LocationState state) {
		DynamicPercept percept = new DynamicPercept();
		percept.setAttribute(AttNames.CURRENT_LOCATION, location);
		percept.setAttribute(AttNames.CURRENT_STATE, state);
		return percept;
	}
}
