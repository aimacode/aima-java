package aima.test.core.unit.environment.vacuum;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.agent.impl.SimpleActionTracker;
import aima.core.environment.vacuum.SimpleReflexVacuumAgent;
import aima.core.environment.vacuum.VacuumEnvironment;

/**
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * 
 */
public class SimpleReflexVacuumAgentTest {
	private SimpleReflexVacuumAgent agent;
	private SimpleActionTracker actionTracker;

	@Before
	public void setUp() {
		agent = new SimpleReflexVacuumAgent();
		actionTracker = new SimpleActionTracker();
	}

	@Test
	public void testCleanClean() {
		VacuumEnvironment tve = new VacuumEnvironment(
				VacuumEnvironment.LocationState.Clean,
				VacuumEnvironment.LocationState.Clean);
		tve.addAgent(agent, VacuumEnvironment.LOCATION_A);

		tve.addEnvironmentListener(actionTracker);

		tve.step(8);

		Assert.assertEquals(
				"Action[name=Right], Action[name=Left], Action[name=Right], Action[name=Left], Action[name=Right], Action[name=Left], Action[name=Right], Action[name=Left]",
				actionTracker.getActions());
	}

	@Test
	public void testCleanDirty() {
		VacuumEnvironment tve = new VacuumEnvironment(
				VacuumEnvironment.LocationState.Clean,
				VacuumEnvironment.LocationState.Dirty);
		tve.addAgent(agent, VacuumEnvironment.LOCATION_A);

		tve.addEnvironmentListener(actionTracker);

		tve.step(8);

		Assert.assertEquals(
				"Action[name=Right], Action[name=Suck], Action[name=Left], Action[name=Right], Action[name=Left], Action[name=Right], Action[name=Left], Action[name=Right]",
				actionTracker.getActions());
	}

	@Test
	public void testDirtyClean() {
		VacuumEnvironment tve = new VacuumEnvironment(
				VacuumEnvironment.LocationState.Dirty,
				VacuumEnvironment.LocationState.Clean);
		tve.addAgent(agent, VacuumEnvironment.LOCATION_A);

		tve.addEnvironmentListener(actionTracker);

		tve.step(8);

		Assert.assertEquals(
				"Action[name=Suck], Action[name=Right], Action[name=Left], Action[name=Right], Action[name=Left], Action[name=Right], Action[name=Left], Action[name=Right]",
				actionTracker.getActions());
	}

	@Test
	public void testDirtyDirty() {
		VacuumEnvironment tve = new VacuumEnvironment(
				VacuumEnvironment.LocationState.Dirty,
				VacuumEnvironment.LocationState.Dirty);
		tve.addAgent(agent, VacuumEnvironment.LOCATION_A);

		tve.addEnvironmentListener(actionTracker);

		tve.step(8);

		Assert.assertEquals(
				"Action[name=Suck], Action[name=Right], Action[name=Suck], Action[name=Left], Action[name=Right], Action[name=Left], Action[name=Right], Action[name=Left]",
				actionTracker.getActions());
	}
}
