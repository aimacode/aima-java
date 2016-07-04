package aima.core.environment.vacuum;

import aima.core.agent.basic.TableDrivenAgent;

import java.util.*;

import static aima.core.environment.vacuum.VacuumEnvironment.ACTION_LEFT;
import static aima.core.environment.vacuum.VacuumEnvironment.ACTION_RIGHT;
import static aima.core.environment.vacuum.VacuumEnvironment.ACTION_SUCK;
import static aima.core.environment.vacuum.VacuumEnvironment.LOCATION_A;
import static aima.core.environment.vacuum.VacuumEnvironment.LOCATION_B;
import static aima.core.environment.vacuum.VacuumEnvironment.Status.Clean;
import static aima.core.environment.vacuum.VacuumEnvironment.Status.Dirty;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <br>
 * Figure ?? Partial tabulation of a simple agent function for the
 * vacuum-cleaner world shown in Figure ??.
 *
 * @author Ciaran O'Reilly
 */
public class TableDrivenVacuumAgent extends TableDrivenAgent<String, VEPercept> {
	public TableDrivenVacuumAgent() {
		super(generatePartialTabulationOfActionFunction());
	}

	public static Map<List<VEPercept>, String> generatePartialTabulationOfActionFunction() {
		Map<List<VEPercept>, String> perceptSequenceToAction = new HashMap<>();

		// NOTE: While this particular table could be setup simply
		// using a few loops, the intent is to show how quickly a table
		// based approach grows and becomes unusable.

		//
		// Level 1: 4 states
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Clean)), ACTION_RIGHT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Dirty)), ACTION_SUCK);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Clean)), ACTION_LEFT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Dirty)), ACTION_SUCK);

		//
		// Level 2: 4x4 states
		// 1
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Clean), new VEPercept(LOCATION_A, Clean)),
				ACTION_RIGHT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Clean), new VEPercept(LOCATION_A, Dirty)),
				ACTION_SUCK);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Clean), new VEPercept(LOCATION_B, Clean)),
				ACTION_LEFT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Clean), new VEPercept(LOCATION_B, Dirty)),
				ACTION_SUCK);
		// 2
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Dirty), new VEPercept(LOCATION_A, Clean)),
				ACTION_RIGHT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Dirty), new VEPercept(LOCATION_A, Dirty)),
				ACTION_SUCK);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Dirty), new VEPercept(LOCATION_B, Clean)),
				ACTION_LEFT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Dirty), new VEPercept(LOCATION_B, Dirty)),
				ACTION_SUCK);
		// 3
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Clean), new VEPercept(LOCATION_A, Clean)),
				ACTION_RIGHT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Clean), new VEPercept(LOCATION_A, Dirty)),
				ACTION_SUCK);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Clean), new VEPercept(LOCATION_B, Clean)),
				ACTION_LEFT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Clean), new VEPercept(LOCATION_B, Dirty)),
				ACTION_SUCK);
		// 4
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Dirty), new VEPercept(LOCATION_A, Clean)),
				ACTION_RIGHT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Dirty), new VEPercept(LOCATION_A, Dirty)),
				ACTION_SUCK);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Dirty), new VEPercept(LOCATION_B, Clean)),
				ACTION_LEFT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Dirty), new VEPercept(LOCATION_B, Dirty)),
				ACTION_SUCK);

		//
		// Level 3: 4x4x4 states
		// 1-1
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Clean), new VEPercept(LOCATION_A, Clean),
				new VEPercept(LOCATION_A, Clean)), ACTION_RIGHT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Clean), new VEPercept(LOCATION_A, Clean),
				new VEPercept(LOCATION_A, Dirty)), ACTION_SUCK);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Clean), new VEPercept(LOCATION_A, Clean),
				new VEPercept(LOCATION_B, Clean)), ACTION_LEFT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Clean), new VEPercept(LOCATION_A, Clean),
				new VEPercept(LOCATION_B, Dirty)), ACTION_SUCK);
		// 1-2
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Clean), new VEPercept(LOCATION_A, Dirty),
				new VEPercept(LOCATION_A, Clean)), ACTION_RIGHT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Clean), new VEPercept(LOCATION_A, Dirty),
				new VEPercept(LOCATION_A, Dirty)), ACTION_SUCK);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Clean), new VEPercept(LOCATION_A, Dirty),
				new VEPercept(LOCATION_B, Clean)), ACTION_LEFT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Clean), new VEPercept(LOCATION_A, Dirty),
				new VEPercept(LOCATION_B, Dirty)), ACTION_SUCK);
		// 1-3
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Clean), new VEPercept(LOCATION_B, Clean),
				new VEPercept(LOCATION_A, Clean)), ACTION_RIGHT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Clean), new VEPercept(LOCATION_B, Clean),
				new VEPercept(LOCATION_A, Dirty)), ACTION_SUCK);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Clean), new VEPercept(LOCATION_B, Clean),
				new VEPercept(LOCATION_B, Clean)), ACTION_LEFT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Clean), new VEPercept(LOCATION_B, Clean),
				new VEPercept(LOCATION_B, Dirty)), ACTION_SUCK);
		// 1-4
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Clean), new VEPercept(LOCATION_B, Dirty),
				new VEPercept(LOCATION_A, Clean)), ACTION_RIGHT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Clean), new VEPercept(LOCATION_B, Dirty),
				new VEPercept(LOCATION_A, Dirty)), ACTION_SUCK);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Clean), new VEPercept(LOCATION_B, Dirty),
				new VEPercept(LOCATION_B, Clean)), ACTION_LEFT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Clean), new VEPercept(LOCATION_B, Dirty),
				new VEPercept(LOCATION_B, Dirty)), ACTION_SUCK);
		// 2-1
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Dirty), new VEPercept(LOCATION_A, Clean),
				new VEPercept(LOCATION_A, Clean)), ACTION_RIGHT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Dirty), new VEPercept(LOCATION_A, Clean),
				new VEPercept(LOCATION_A, Dirty)), ACTION_SUCK);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Dirty), new VEPercept(LOCATION_A, Clean),
				new VEPercept(LOCATION_B, Clean)), ACTION_LEFT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Dirty), new VEPercept(LOCATION_A, Clean),
				new VEPercept(LOCATION_B, Dirty)), ACTION_SUCK);
		// 2-2
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Dirty), new VEPercept(LOCATION_A, Dirty),
				new VEPercept(LOCATION_A, Clean)), ACTION_RIGHT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Dirty), new VEPercept(LOCATION_A, Dirty),
				new VEPercept(LOCATION_A, Dirty)), ACTION_SUCK);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Dirty), new VEPercept(LOCATION_A, Dirty),
				new VEPercept(LOCATION_B, Clean)), ACTION_LEFT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Dirty), new VEPercept(LOCATION_A, Dirty),
				new VEPercept(LOCATION_B, Dirty)), ACTION_SUCK);
		// 2-3
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Dirty), new VEPercept(LOCATION_B, Clean),
				new VEPercept(LOCATION_A, Clean)), ACTION_RIGHT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Dirty), new VEPercept(LOCATION_B, Clean),
				new VEPercept(LOCATION_A, Dirty)), ACTION_SUCK);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Dirty), new VEPercept(LOCATION_B, Clean),
				new VEPercept(LOCATION_B, Clean)), ACTION_LEFT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Dirty), new VEPercept(LOCATION_B, Clean),
				new VEPercept(LOCATION_B, Dirty)), ACTION_SUCK);
		// 2-4
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Dirty), new VEPercept(LOCATION_B, Dirty),
				new VEPercept(LOCATION_A, Clean)), ACTION_RIGHT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Dirty), new VEPercept(LOCATION_B, Dirty),
				new VEPercept(LOCATION_A, Dirty)), ACTION_SUCK);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Dirty), new VEPercept(LOCATION_B, Dirty),
				new VEPercept(LOCATION_B, Clean)), ACTION_LEFT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_A, Dirty), new VEPercept(LOCATION_B, Dirty),
				new VEPercept(LOCATION_B, Dirty)), ACTION_SUCK);
		// 3-1
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Clean), new VEPercept(LOCATION_A, Clean),
				new VEPercept(LOCATION_A, Clean)), ACTION_RIGHT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Clean), new VEPercept(LOCATION_A, Clean),
				new VEPercept(LOCATION_A, Dirty)), ACTION_SUCK);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Clean), new VEPercept(LOCATION_A, Clean),
				new VEPercept(LOCATION_B, Clean)), ACTION_LEFT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Clean), new VEPercept(LOCATION_A, Clean),
				new VEPercept(LOCATION_B, Dirty)), ACTION_SUCK);
		// 3-2
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Clean), new VEPercept(LOCATION_A, Dirty),
				new VEPercept(LOCATION_A, Clean)), ACTION_RIGHT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Clean), new VEPercept(LOCATION_A, Dirty),
				new VEPercept(LOCATION_A, Dirty)), ACTION_SUCK);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Clean), new VEPercept(LOCATION_A, Dirty),
				new VEPercept(LOCATION_B, Clean)), ACTION_LEFT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Clean), new VEPercept(LOCATION_A, Dirty),
				new VEPercept(LOCATION_B, Dirty)), ACTION_SUCK);
		// 3-3
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Clean), new VEPercept(LOCATION_B, Clean),
				new VEPercept(LOCATION_A, Clean)), ACTION_RIGHT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Clean), new VEPercept(LOCATION_B, Clean),
				new VEPercept(LOCATION_A, Dirty)), ACTION_SUCK);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Clean), new VEPercept(LOCATION_B, Clean),
				new VEPercept(LOCATION_B, Clean)), ACTION_LEFT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Clean), new VEPercept(LOCATION_B, Clean),
				new VEPercept(LOCATION_B, Dirty)), ACTION_SUCK);
		// 3-4
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Clean), new VEPercept(LOCATION_B, Dirty),
				new VEPercept(LOCATION_A, Clean)), ACTION_RIGHT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Clean), new VEPercept(LOCATION_B, Dirty),
				new VEPercept(LOCATION_A, Dirty)), ACTION_SUCK);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Clean), new VEPercept(LOCATION_B, Dirty),
				new VEPercept(LOCATION_B, Clean)), ACTION_LEFT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Clean), new VEPercept(LOCATION_B, Dirty),
				new VEPercept(LOCATION_B, Dirty)), ACTION_SUCK);
		// 4-1
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Dirty), new VEPercept(LOCATION_A, Clean),
				new VEPercept(LOCATION_A, Clean)), ACTION_RIGHT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Dirty), new VEPercept(LOCATION_A, Clean),
				new VEPercept(LOCATION_A, Dirty)), ACTION_SUCK);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Dirty), new VEPercept(LOCATION_A, Clean),
				new VEPercept(LOCATION_B, Clean)), ACTION_LEFT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Dirty), new VEPercept(LOCATION_A, Clean),
				new VEPercept(LOCATION_B, Dirty)), ACTION_SUCK);
		// 4-2
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Dirty), new VEPercept(LOCATION_A, Dirty),
				new VEPercept(LOCATION_A, Clean)), ACTION_RIGHT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Dirty), new VEPercept(LOCATION_A, Dirty),
				new VEPercept(LOCATION_A, Dirty)), ACTION_SUCK);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Dirty), new VEPercept(LOCATION_A, Dirty),
				new VEPercept(LOCATION_B, Clean)), ACTION_LEFT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Dirty), new VEPercept(LOCATION_A, Dirty),
				new VEPercept(LOCATION_B, Dirty)), ACTION_SUCK);
		// 4-3
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Dirty), new VEPercept(LOCATION_B, Clean),
				new VEPercept(LOCATION_A, Clean)), ACTION_RIGHT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Dirty), new VEPercept(LOCATION_B, Clean),
				new VEPercept(LOCATION_A, Dirty)), ACTION_SUCK);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Dirty), new VEPercept(LOCATION_B, Clean),
				new VEPercept(LOCATION_B, Clean)), ACTION_LEFT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Dirty), new VEPercept(LOCATION_B, Clean),
				new VEPercept(LOCATION_B, Dirty)), ACTION_SUCK);
		// 4-4
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Dirty), new VEPercept(LOCATION_B, Dirty),
				new VEPercept(LOCATION_A, Clean)), ACTION_RIGHT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Dirty), new VEPercept(LOCATION_B, Dirty),
				new VEPercept(LOCATION_A, Dirty)), ACTION_SUCK);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Dirty), new VEPercept(LOCATION_B, Dirty),
				new VEPercept(LOCATION_B, Clean)), ACTION_LEFT);
		perceptSequenceToAction.put(Arrays.asList(new VEPercept(LOCATION_B, Dirty), new VEPercept(LOCATION_B, Dirty),
				new VEPercept(LOCATION_B, Dirty)), ACTION_SUCK);

		//
		// Level 4: 4x4x4x4 states
		// ...

		return perceptSequenceToAction;
	}
}
