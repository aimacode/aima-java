package aima.test.core.unit.agent.impl.vacuum;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.agent.impl.vacuum.TableDrivenVaccumAgent;
import aima.core.agent.impl.vacuum.VaccumEnvironment;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class TableDrivenVaccumAgentTest {
	private TableDrivenVaccumAgent agent;

	private StringBuilder envChanges;

	@Before
	public void setUp() {
		agent = new TableDrivenVaccumAgent();
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

		Assert.assertEquals("Action[name==Right]Action[name==Left]Action[name==Right]Action[name==NoOp]", envChanges.toString());
	}

	@Test
	public void testCleanDirty() {
		VaccumEnvironment tve = new VaccumEnvironment(
				VaccumEnvironment.LocationState.Clean,
				VaccumEnvironment.LocationState.Dirty);
		tve.addAgent(agent, VaccumEnvironment.Location.A);

		tve.addEnvironmentView(new EnvironmentViewActionTracker(envChanges));

		tve.stepUntilDone();

		Assert.assertEquals("Action[name==Right]Action[name==Suck]Action[name==Left]Action[name==NoOp]", envChanges.toString());
	}

	@Test
	public void testDirtyClean() {
		VaccumEnvironment tve = new VaccumEnvironment(
				VaccumEnvironment.LocationState.Dirty,
				VaccumEnvironment.LocationState.Clean);
		tve.addAgent(agent, VaccumEnvironment.Location.A);

		tve.addEnvironmentView(new EnvironmentViewActionTracker(envChanges));

		tve.stepUntilDone();

		Assert.assertEquals("Action[name==Suck]Action[name==Right]Action[name==Left]Action[name==NoOp]", envChanges.toString());
	}

	@Test
	public void testDirtyDirty() {
		VaccumEnvironment tve = new VaccumEnvironment(
				VaccumEnvironment.LocationState.Dirty,
				VaccumEnvironment.LocationState.Dirty);
		tve.addAgent(agent, VaccumEnvironment.Location.A);

		tve.addEnvironmentView(new EnvironmentViewActionTracker(envChanges));

		tve.stepUntilDone();

		Assert.assertEquals("Action[name==Suck]Action[name==Right]Action[name==Suck]Action[name==NoOp]", envChanges.toString());
	}
}
