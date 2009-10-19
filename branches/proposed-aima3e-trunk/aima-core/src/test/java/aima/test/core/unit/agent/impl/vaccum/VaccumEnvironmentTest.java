package aima.test.core.unit.agent.impl.vaccum;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.agent.impl.vaccum.ModelBasedReflexVaccumAgent;
import aima.core.agent.impl.vaccum.VaccumEnvironment;

/**
 * @author Ravi Mohan
 * 
 */
public class VaccumEnvironmentTest {
	VaccumEnvironment tve, tve2, tve3, tve4;

	ModelBasedReflexVaccumAgent a;

	@Before
	public void setUp() {
		tve = new VaccumEnvironment(
				VaccumEnvironment.LocationState.Dirty,
				VaccumEnvironment.LocationState.Dirty);
		tve2 = new VaccumEnvironment(
				VaccumEnvironment.LocationState.Clean,
				VaccumEnvironment.LocationState.Clean);
		tve3 = new VaccumEnvironment(
				VaccumEnvironment.LocationState.Clean,
				VaccumEnvironment.LocationState.Dirty);
		tve4 = new VaccumEnvironment(
				VaccumEnvironment.LocationState.Dirty,
				VaccumEnvironment.LocationState.Clean);
		a = new ModelBasedReflexVaccumAgent();
	}

	@Test
	public void testTVEConstruction() {
		Assert.assertEquals(VaccumEnvironment.LocationState.Dirty, tve.getLocationState(VaccumEnvironment.Location.A));
		Assert.assertEquals(VaccumEnvironment.LocationState.Dirty, tve.getLocationState(VaccumEnvironment.Location.B));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve2.getLocationState(VaccumEnvironment.Location.A));
		Assert.assertEquals(VaccumEnvironment.LocationState.Clean, tve2.getLocationState(VaccumEnvironment.Location.B));
	}

	@Test
	public void testAgentAdd() {
		tve.addAgent(a, VaccumEnvironment.Location.A);
		Assert.assertEquals(VaccumEnvironment.Location.A, tve.getAgentLocation(a));
		Assert.assertEquals(1, tve.getAgents().size());
	}
}
