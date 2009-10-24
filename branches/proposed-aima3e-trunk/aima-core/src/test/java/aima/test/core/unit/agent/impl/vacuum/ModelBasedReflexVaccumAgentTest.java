package aima.test.core.unit.agent.impl.vacuum;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.agent.impl.vacuum.ModelBasedReflexVaccumAgent;
import aima.core.agent.impl.vacuum.VaccumEnvironment;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * 
 */
public class ModelBasedReflexVaccumAgentTest {
	private ModelBasedReflexVaccumAgent agent;

	private StringBuilder envChanges;

	@Before
	public void setUp() {
		agent = new ModelBasedReflexVaccumAgent();
		envChanges = new StringBuilder();
	}

	@Test
	public void testCleanClean() {
		VaccumEnvironment tve = new VaccumEnvironment(
				VaccumEnvironment.LocationState.Clean,
				VaccumEnvironment.LocationState.Clean);
		tve.addAgent(agent, VaccumEnvironment.Location.A);

		tve.addEnvironmentView(new EnvironmentViewActionTracker(envChanges));

		tve.stepUntilDone();

		Assert.assertEquals("Action[name==Right]Action[name==NoOp]", envChanges
				.toString());
	}

	@Test
	public void testCleanDirty() {
		VaccumEnvironment tve = new VaccumEnvironment(
				VaccumEnvironment.LocationState.Clean,
				VaccumEnvironment.LocationState.Dirty);
		tve.addAgent(agent, VaccumEnvironment.Location.A);

		tve.addEnvironmentView(new EnvironmentViewActionTracker(envChanges));

		tve.stepUntilDone();

		Assert.assertEquals(
				"Action[name==Right]Action[name==Suck]Action[name==NoOp]",
				envChanges.toString());
	}

	@Test
	public void testDirtyClean() {
		VaccumEnvironment tve = new VaccumEnvironment(
				VaccumEnvironment.LocationState.Dirty,
				VaccumEnvironment.LocationState.Clean);
		tve.addAgent(agent, VaccumEnvironment.Location.A);

		tve.addEnvironmentView(new EnvironmentViewActionTracker(envChanges));

		tve.stepUntilDone();

		Assert.assertEquals(
				"Action[name==Suck]Action[name==Right]Action[name==NoOp]",
				envChanges.toString());
	}

	@Test
	public void testDirtyDirty() {
		VaccumEnvironment tve = new VaccumEnvironment(
				VaccumEnvironment.LocationState.Dirty,
				VaccumEnvironment.LocationState.Dirty);
		tve.addAgent(agent, VaccumEnvironment.Location.A);

		tve.addEnvironmentView(new EnvironmentViewActionTracker(envChanges));

		tve.stepUntilDone();

		Assert
				.assertEquals(
						"Action[name==Suck]Action[name==Right]Action[name==Suck]Action[name==NoOp]",
						envChanges.toString());
	}

	@Test
	public void testAgentActionNumber1() {
		VaccumEnvironment tve = new VaccumEnvironment(
				VaccumEnvironment.LocationState.Dirty,
				VaccumEnvironment.LocationState.Dirty);
		tve.addAgent(agent, VaccumEnvironment.Location.A);

		Assert.assertEquals(VaccumEnvironment.Location.A, tve
				.getAgentLocation(agent));
		Assert.assertEquals(1, tve.getAgents().size());
		tve.step(); // cleans location A
		Assert.assertEquals(VaccumEnvironment.Location.A, tve
				.getAgentLocation(agent));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.A));
		tve.step(); // moves to lcation B
		Assert.assertEquals(VaccumEnvironment.Location.B, tve
				.getAgentLocation(agent));
		Assert.assertEquals(VaccumEnvironment.LocationState.Dirty, tve
				.getLocationState(VaccumEnvironment.Location.B));
		tve.step(); // cleans location B
		Assert.assertEquals(VaccumEnvironment.Location.B, tve
				.getAgentLocation(agent));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.B));
		tve.step(); // NOOP
		Assert.assertEquals(VaccumEnvironment.Location.B, tve
				.getAgentLocation(agent));
		Assert.assertEquals(19, tve.getPerformanceMeasure(agent));
	}

	@Test
	public void testAgentActionNumber2() {
		VaccumEnvironment tve = new VaccumEnvironment(
				VaccumEnvironment.LocationState.Dirty,
				VaccumEnvironment.LocationState.Dirty);
		tve.addAgent(agent, VaccumEnvironment.Location.B);

		Assert.assertEquals(VaccumEnvironment.Location.B, tve
				.getAgentLocation(agent));
		Assert.assertEquals(1, tve.getAgents().size());
		tve.step(); // cleans location B
		Assert.assertEquals(VaccumEnvironment.Location.B, tve
				.getAgentLocation(agent));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.B));
		tve.step(); // moves to lcation A
		Assert.assertEquals(VaccumEnvironment.Location.A, tve
				.getAgentLocation(agent));
		Assert.assertEquals(VaccumEnvironment.LocationState.Dirty, tve
				.getLocationState(VaccumEnvironment.Location.A));
		tve.step(); // cleans location A
		Assert.assertEquals(VaccumEnvironment.Location.A, tve
				.getAgentLocation(agent));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.A));
		tve.step(); // NOOP
		Assert.assertEquals(VaccumEnvironment.Location.A, tve
				.getAgentLocation(agent));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.A));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.B));
		Assert.assertEquals(19, tve.getPerformanceMeasure(agent));
	}

	@Test
	public void testAgentActionNumber3() {
		VaccumEnvironment tve = new VaccumEnvironment(
				VaccumEnvironment.LocationState.Clean,
				VaccumEnvironment.LocationState.Clean);
		tve.addAgent(agent, VaccumEnvironment.Location.A);

		Assert.assertEquals(VaccumEnvironment.Location.A, tve
				.getAgentLocation(agent));
		Assert.assertEquals(1, tve.getAgents().size());
		tve.step(); // moves to location B
		Assert.assertEquals(VaccumEnvironment.Location.B, tve
				.getAgentLocation(agent));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.B));
		tve.step(); // NOOP
		Assert.assertEquals(VaccumEnvironment.Location.B, tve
				.getAgentLocation(agent));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.A));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.B));
		Assert.assertEquals(-1, tve.getPerformanceMeasure(agent));
	}

	@Test
	public void testAgentActionNumber4() {
		VaccumEnvironment tve = new VaccumEnvironment(
				VaccumEnvironment.LocationState.Clean,
				VaccumEnvironment.LocationState.Clean);
		tve.addAgent(agent, VaccumEnvironment.Location.B);

		Assert.assertEquals(VaccumEnvironment.Location.B, tve
				.getAgentLocation(agent));
		Assert.assertEquals(1, tve.getAgents().size());
		tve.step(); // moves to location A
		Assert.assertEquals(VaccumEnvironment.Location.A, tve
				.getAgentLocation(agent));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.A));
		tve.step(); // NOOP
		Assert.assertEquals(VaccumEnvironment.Location.A, tve
				.getAgentLocation(agent));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.A));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.B));
		Assert.assertEquals(-1, tve.getPerformanceMeasure(agent));
	}

	@Test
	public void testAgentActionNumber5() {
		VaccumEnvironment tve = new VaccumEnvironment(
				VaccumEnvironment.LocationState.Clean,
				VaccumEnvironment.LocationState.Dirty);
		tve.addAgent(agent, VaccumEnvironment.Location.A);

		Assert.assertEquals(VaccumEnvironment.Location.A, tve
				.getAgentLocation(agent));
		Assert.assertEquals(1, tve.getAgents().size());
		tve.step(); // moves to B
		Assert.assertEquals(VaccumEnvironment.Location.B, tve
				.getAgentLocation(agent));
		Assert.assertEquals(VaccumEnvironment.LocationState.Dirty, tve
				.getLocationState(VaccumEnvironment.Location.B));
		tve.step(); // cleans location B
		Assert.assertEquals(VaccumEnvironment.Location.B, tve
				.getAgentLocation(agent));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.B));
		tve.step(); // NOOP
		Assert.assertEquals(VaccumEnvironment.Location.B, tve
				.getAgentLocation(agent));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.A));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.B));
		Assert.assertEquals(9, tve.getPerformanceMeasure(agent));
	}

	@Test
	public void testAgentActionNumber6() {
		VaccumEnvironment tve = new VaccumEnvironment(
				VaccumEnvironment.LocationState.Clean,
				VaccumEnvironment.LocationState.Dirty);
		tve.addAgent(agent, VaccumEnvironment.Location.B);

		Assert.assertEquals(VaccumEnvironment.Location.B, tve
				.getAgentLocation(agent));
		Assert.assertEquals(1, tve.getAgents().size());
		tve.step(); // cleans B
		Assert.assertEquals(VaccumEnvironment.Location.B, tve
				.getAgentLocation(agent));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.B));
		tve.step(); // moves to A
		Assert.assertEquals(VaccumEnvironment.Location.A, tve
				.getAgentLocation(agent));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.A));
		tve.step(); // NOOP
		Assert.assertEquals(VaccumEnvironment.Location.A, tve
				.getAgentLocation(agent));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.A));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.B));
		Assert.assertEquals(9, tve.getPerformanceMeasure(agent));
	}

	@Test
	public void testAgentActionNumber7() {
		VaccumEnvironment tve = new VaccumEnvironment(
				VaccumEnvironment.LocationState.Dirty,
				VaccumEnvironment.LocationState.Clean);
		tve.addAgent(agent, VaccumEnvironment.Location.A);

		Assert.assertEquals(VaccumEnvironment.Location.A, tve
				.getAgentLocation(agent));
		Assert.assertEquals(1, tve.getAgents().size());
		tve.step(); // cleans A
		Assert.assertEquals(VaccumEnvironment.Location.A, tve
				.getAgentLocation(agent));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.A));
		tve.step(); // moves to B
		Assert.assertEquals(VaccumEnvironment.Location.B, tve
				.getAgentLocation(agent));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.B));
		tve.step(); // NOOP
		Assert.assertEquals(VaccumEnvironment.Location.B, tve
				.getAgentLocation(agent));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.A));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.B));
		Assert.assertEquals(9, tve.getPerformanceMeasure(agent));
	}

	@Test
	public void testAgentActionNumber8() {
		VaccumEnvironment tve = new VaccumEnvironment(
				VaccumEnvironment.LocationState.Dirty,
				VaccumEnvironment.LocationState.Clean);
		tve.addAgent(agent, VaccumEnvironment.Location.B);

		Assert.assertEquals(VaccumEnvironment.Location.B, tve
				.getAgentLocation(agent));
		Assert.assertEquals(1, tve.getAgents().size());
		tve.step(); // moves to A
		Assert.assertEquals(VaccumEnvironment.Location.A, tve
				.getAgentLocation(agent));
		Assert.assertEquals(VaccumEnvironment.LocationState.Dirty, tve
				.getLocationState(VaccumEnvironment.Location.A));
		tve.step(); // cleans A
		Assert.assertEquals(VaccumEnvironment.Location.A, tve
				.getAgentLocation(agent));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.A));
		tve.step(); // NOOP
		Assert.assertEquals(VaccumEnvironment.Location.A, tve
				.getAgentLocation(agent));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.A));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve
				.getLocationState(VaccumEnvironment.Location.B));
		Assert.assertEquals(9, tve.getPerformanceMeasure(agent));
	}
}