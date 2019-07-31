package aima.test.core.unit.environment.vacuum;

import aima.core.agent.impl.SimpleActionTracker;
import aima.core.environment.vacuum.ModelBasedReflexVacuumAgent;
import aima.core.environment.vacuum.VacuumEnvironment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * 
 */
public class ModelBasedReflexVacuumAgentTest {
	private ModelBasedReflexVacuumAgent agent;
	private SimpleActionTracker actionTracker;

	@Before
	public void setUp() {
		agent = new ModelBasedReflexVacuumAgent();
		actionTracker = new SimpleActionTracker();
	}

	@Test
	public void testCleanClean() {
		VacuumEnvironment tve = new VacuumEnvironment(
				VacuumEnvironment.LocationState.Clean,
				VacuumEnvironment.LocationState.Clean);
		tve.addAgent(agent, VacuumEnvironment.LOCATION_A);

		tve.addEnvironmentListener(actionTracker);

		tve.stepUntilDone();

		Assert.assertEquals("Action[name=Right]",
				actionTracker.getActions());
	}

	@Test
	public void testCleanDirty() {
		VacuumEnvironment tve = new VacuumEnvironment(
				VacuumEnvironment.LocationState.Clean,
				VacuumEnvironment.LocationState.Dirty);
		tve.addAgent(agent, VacuumEnvironment.LOCATION_A);

		tve.addEnvironmentListener(actionTracker);

		tve.stepUntilDone();

		Assert.assertEquals(
				"Action[name=Right], Action[name=Suck]",
				actionTracker.getActions());
	}

	@Test
	public void testDirtyClean() {
		VacuumEnvironment tve = new VacuumEnvironment(
				VacuumEnvironment.LocationState.Dirty,
				VacuumEnvironment.LocationState.Clean);
		tve.addAgent(agent, VacuumEnvironment.LOCATION_A);

		tve.addEnvironmentListener(actionTracker);

		tve.stepUntilDone();

		Assert.assertEquals(
				"Action[name=Suck], Action[name=Right]",
				actionTracker.getActions());
	}

	@Test
	public void testDirtyDirty() {
		VacuumEnvironment tve = new VacuumEnvironment(
				VacuumEnvironment.LocationState.Dirty,
				VacuumEnvironment.LocationState.Dirty);
		tve.addAgent(agent, VacuumEnvironment.LOCATION_A);

		tve.addEnvironmentListener(actionTracker);

		tve.stepUntilDone();

		Assert.assertEquals(
				"Action[name=Suck], Action[name=Right], Action[name=Suck]",
				actionTracker.getActions());
	}

	@Test
	public void testAgentActionNumber1() {
		VacuumEnvironment tve = new VacuumEnvironment(
				VacuumEnvironment.LocationState.Dirty,
				VacuumEnvironment.LocationState.Dirty);
		tve.addAgent(agent, VacuumEnvironment.LOCATION_A);

		Assert.assertEquals(VacuumEnvironment.LOCATION_A,
				tve.getAgentLocation(agent));
		Assert.assertEquals(1, tve.getAgents().size());
		tve.step(); // cleans location A
		Assert.assertEquals(VacuumEnvironment.LOCATION_A,
				tve.getAgentLocation(agent));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_A));
		tve.step(); // moves to lcation B
		Assert.assertEquals(VacuumEnvironment.LOCATION_B,
				tve.getAgentLocation(agent));
		Assert.assertEquals(VacuumEnvironment.LocationState.Dirty,
				tve.getLocationState(VacuumEnvironment.LOCATION_B));
		tve.step(); // cleans location B
		Assert.assertEquals(VacuumEnvironment.LOCATION_B,
				tve.getAgentLocation(agent));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_B));
		tve.step(); // NOOP
		Assert.assertEquals(VacuumEnvironment.LOCATION_B,
				tve.getAgentLocation(agent));
		Assert.assertEquals(19, tve.getPerformanceMeasure(agent), 0.001);
	}

	@Test
	public void testAgentActionNumber2() {
		VacuumEnvironment tve = new VacuumEnvironment(
				VacuumEnvironment.LocationState.Dirty,
				VacuumEnvironment.LocationState.Dirty);
		tve.addAgent(agent, VacuumEnvironment.LOCATION_B);

		Assert.assertEquals(VacuumEnvironment.LOCATION_B,
				tve.getAgentLocation(agent));
		Assert.assertEquals(1, tve.getAgents().size());
		tve.step(); // cleans location B
		Assert.assertEquals(VacuumEnvironment.LOCATION_B,
				tve.getAgentLocation(agent));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_B));
		tve.step(); // moves to lcation A
		Assert.assertEquals(VacuumEnvironment.LOCATION_A,
				tve.getAgentLocation(agent));
		Assert.assertEquals(VacuumEnvironment.LocationState.Dirty,
				tve.getLocationState(VacuumEnvironment.LOCATION_A));
		tve.step(); // cleans location A
		Assert.assertEquals(VacuumEnvironment.LOCATION_A,
				tve.getAgentLocation(agent));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_A));
		tve.step(); // NOOP
		Assert.assertEquals(VacuumEnvironment.LOCATION_A,
				tve.getAgentLocation(agent));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_A));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_B));
		Assert.assertEquals(19, tve.getPerformanceMeasure(agent), 0.001);
	}

	@Test
	public void testAgentActionNumber3() {
		VacuumEnvironment tve = new VacuumEnvironment(
				VacuumEnvironment.LocationState.Clean,
				VacuumEnvironment.LocationState.Clean);
		tve.addAgent(agent, VacuumEnvironment.LOCATION_A);

		Assert.assertEquals(VacuumEnvironment.LOCATION_A,
				tve.getAgentLocation(agent));
		Assert.assertEquals(1, tve.getAgents().size());
		tve.step(); // moves to location B
		Assert.assertEquals(VacuumEnvironment.LOCATION_B,
				tve.getAgentLocation(agent));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_B));
		tve.step(); // NOOP
		Assert.assertEquals(VacuumEnvironment.LOCATION_B,
				tve.getAgentLocation(agent));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_A));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_B));
		Assert.assertEquals(-1, tve.getPerformanceMeasure(agent), 0.001);
	}

	@Test
	public void testAgentActionNumber4() {
		VacuumEnvironment tve = new VacuumEnvironment(
				VacuumEnvironment.LocationState.Clean,
				VacuumEnvironment.LocationState.Clean);
		tve.addAgent(agent, VacuumEnvironment.LOCATION_B);

		Assert.assertEquals(VacuumEnvironment.LOCATION_B,
				tve.getAgentLocation(agent));
		Assert.assertEquals(1, tve.getAgents().size());
		tve.step(); // moves to location A
		Assert.assertEquals(VacuumEnvironment.LOCATION_A,
				tve.getAgentLocation(agent));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_A));
		tve.step(); // NOOP
		Assert.assertEquals(VacuumEnvironment.LOCATION_A,
				tve.getAgentLocation(agent));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_A));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_B));
		Assert.assertEquals(-1, tve.getPerformanceMeasure(agent), 0.001);
	}

	@Test
	public void testAgentActionNumber5() {
		VacuumEnvironment tve = new VacuumEnvironment(
				VacuumEnvironment.LocationState.Clean,
				VacuumEnvironment.LocationState.Dirty);
		tve.addAgent(agent, VacuumEnvironment.LOCATION_A);

		Assert.assertEquals(VacuumEnvironment.LOCATION_A,
				tve.getAgentLocation(agent));
		Assert.assertEquals(1, tve.getAgents().size());
		tve.step(); // moves to B
		Assert.assertEquals(VacuumEnvironment.LOCATION_B,
				tve.getAgentLocation(agent));
		Assert.assertEquals(VacuumEnvironment.LocationState.Dirty,
				tve.getLocationState(VacuumEnvironment.LOCATION_B));
		tve.step(); // cleans location B
		Assert.assertEquals(VacuumEnvironment.LOCATION_B,
				tve.getAgentLocation(agent));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_B));
		tve.step(); // NOOP
		Assert.assertEquals(VacuumEnvironment.LOCATION_B,
				tve.getAgentLocation(agent));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_A));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_B));
		Assert.assertEquals(9, tve.getPerformanceMeasure(agent), 0.001);
	}

	@Test
	public void testAgentActionNumber6() {
		VacuumEnvironment tve = new VacuumEnvironment(
				VacuumEnvironment.LocationState.Clean,
				VacuumEnvironment.LocationState.Dirty);
		tve.addAgent(agent, VacuumEnvironment.LOCATION_B);

		Assert.assertEquals(VacuumEnvironment.LOCATION_B,
				tve.getAgentLocation(agent));
		Assert.assertEquals(1, tve.getAgents().size());
		tve.step(); // cleans B
		Assert.assertEquals(VacuumEnvironment.LOCATION_B,
				tve.getAgentLocation(agent));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_B));
		tve.step(); // moves to A
		Assert.assertEquals(VacuumEnvironment.LOCATION_A,
				tve.getAgentLocation(agent));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_A));
		tve.step(); // NOOP
		Assert.assertEquals(VacuumEnvironment.LOCATION_A,
				tve.getAgentLocation(agent));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_A));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_B));
		Assert.assertEquals(9, tve.getPerformanceMeasure(agent), 0.001);
	}

	@Test
	public void testAgentActionNumber7() {
		VacuumEnvironment tve = new VacuumEnvironment(
				VacuumEnvironment.LocationState.Dirty,
				VacuumEnvironment.LocationState.Clean);
		tve.addAgent(agent, VacuumEnvironment.LOCATION_A);

		Assert.assertEquals(VacuumEnvironment.LOCATION_A,
				tve.getAgentLocation(agent));
		Assert.assertEquals(1, tve.getAgents().size());
		tve.step(); // cleans A
		Assert.assertEquals(VacuumEnvironment.LOCATION_A,
				tve.getAgentLocation(agent));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_A));
		tve.step(); // moves to B
		Assert.assertEquals(VacuumEnvironment.LOCATION_B,
				tve.getAgentLocation(agent));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_B));
		tve.step(); // NOOP
		Assert.assertEquals(VacuumEnvironment.LOCATION_B,
				tve.getAgentLocation(agent));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_A));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_B));
		Assert.assertEquals(9, tve.getPerformanceMeasure(agent), 0.001);
	}

	@Test
	public void testAgentActionNumber8() {
		VacuumEnvironment tve = new VacuumEnvironment(
				VacuumEnvironment.LocationState.Dirty,
				VacuumEnvironment.LocationState.Clean);
		tve.addAgent(agent, VacuumEnvironment.LOCATION_B);

		Assert.assertEquals(VacuumEnvironment.LOCATION_B,
				tve.getAgentLocation(agent));
		Assert.assertEquals(1, tve.getAgents().size());
		tve.step(); // moves to A
		Assert.assertEquals(VacuumEnvironment.LOCATION_A,
				tve.getAgentLocation(agent));
		Assert.assertEquals(VacuumEnvironment.LocationState.Dirty,
				tve.getLocationState(VacuumEnvironment.LOCATION_A));
		tve.step(); // cleans A
		Assert.assertEquals(VacuumEnvironment.LOCATION_A,
				tve.getAgentLocation(agent));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_A));
		tve.step(); // NOOP
		Assert.assertEquals(VacuumEnvironment.LOCATION_A,
				tve.getAgentLocation(agent));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_A));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean,
				tve.getLocationState(VacuumEnvironment.LOCATION_B));
		Assert.assertEquals(9, tve.getPerformanceMeasure(agent), 0.001);
	}
}