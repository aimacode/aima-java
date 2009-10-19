package aima.test.core.unit.agent.impl.vaccum;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.agent.impl.vaccum.ReflexVaccumAgent;
import aima.core.agent.impl.vaccum.VaccumEnvironment;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ReflexVaccumAgentTest {
	private ReflexVaccumAgent agent;

	private StringBuilder envChanges;

	@Before
	public void setUp() {
		agent = new ReflexVaccumAgent();
		envChanges = new StringBuilder();
	}

	@Test
	public void testCleanClean() {		
		VaccumEnvironment tve = new VaccumEnvironment(
				VaccumEnvironment.LocationState.Clean,
				VaccumEnvironment.LocationState.Clean);
		tve.addAgent(agent, VaccumEnvironment.Location.A);

		tve.addEnvironmentView(new EnvironmentViewActionTracker(envChanges));

		tve.step(8);

		Assert.assertEquals("Action[name==Right]Action[name==Left]Action[name==Right]Action[name==Left]Action[name==Right]Action[name==Left]Action[name==Right]Action[name==Left]", envChanges
				.toString());
	}

	@Test
	public void testCleanDirty() {		
		VaccumEnvironment tve = new VaccumEnvironment(
				VaccumEnvironment.LocationState.Clean,
				VaccumEnvironment.LocationState.Dirty);
		tve.addAgent(agent, VaccumEnvironment.Location.A);

		tve.addEnvironmentView(new EnvironmentViewActionTracker(envChanges));

		tve.step(8);

		Assert.assertEquals("Action[name==Right]Action[name==Suck]Action[name==Left]Action[name==Right]Action[name==Left]Action[name==Right]Action[name==Left]Action[name==Right]", envChanges
				.toString());
	}

	@Test
	public void testDirtyClean() {
		VaccumEnvironment tve = new VaccumEnvironment(
				VaccumEnvironment.LocationState.Dirty,
				VaccumEnvironment.LocationState.Clean);
		tve.addAgent(agent, VaccumEnvironment.Location.A);

		tve.addEnvironmentView(new EnvironmentViewActionTracker(envChanges));

		tve.step(8);

		Assert.assertEquals("Action[name==Suck]Action[name==Right]Action[name==Left]Action[name==Right]Action[name==Left]Action[name==Right]Action[name==Left]Action[name==Right]", envChanges
				.toString());
	}

	@Test
	public void testDirtyDirty() {	
		VaccumEnvironment tve = new VaccumEnvironment(
				VaccumEnvironment.LocationState.Dirty,
				VaccumEnvironment.LocationState.Dirty);
		tve.addAgent(agent, VaccumEnvironment.Location.A);

		tve.addEnvironmentView(new EnvironmentViewActionTracker(envChanges));

		tve.step(8);

		Assert.assertEquals("Action[name==Suck]Action[name==Right]Action[name==Suck]Action[name==Left]Action[name==Right]Action[name==Left]Action[name==Right]Action[name==Left]", envChanges
				.toString());
	}
}
