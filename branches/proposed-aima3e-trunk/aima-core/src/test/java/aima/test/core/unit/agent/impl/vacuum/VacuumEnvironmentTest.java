package aima.test.core.unit.agent.impl.vacuum;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.agent.impl.vacuum.ModelBasedReflexVacuumAgent;
import aima.core.agent.impl.vacuum.VacuumEnvironment;

/**
 * @author Ravi Mohan
 * 
 */
public class VacuumEnvironmentTest {
	VacuumEnvironment tve, tve2, tve3, tve4;

	ModelBasedReflexVacuumAgent a;

	@Before
	public void setUp() {
		tve = new VacuumEnvironment(
				VacuumEnvironment.LocationState.Dirty,
				VacuumEnvironment.LocationState.Dirty);
		tve2 = new VacuumEnvironment(
				VacuumEnvironment.LocationState.Clean,
				VacuumEnvironment.LocationState.Clean);
		tve3 = new VacuumEnvironment(
				VacuumEnvironment.LocationState.Clean,
				VacuumEnvironment.LocationState.Dirty);
		tve4 = new VacuumEnvironment(
				VacuumEnvironment.LocationState.Dirty,
				VacuumEnvironment.LocationState.Clean);
		a = new ModelBasedReflexVacuumAgent();
	}

	@Test
	public void testTVEConstruction() {
		Assert.assertEquals(VacuumEnvironment.LocationState.Dirty, tve.getLocationState(VacuumEnvironment.Location.A));
		Assert.assertEquals(VacuumEnvironment.LocationState.Dirty, tve.getLocationState(VacuumEnvironment.Location.B));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean, tve2.getLocationState(VacuumEnvironment.Location.A));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean, tve2.getLocationState(VacuumEnvironment.Location.B));
	}

	@Test
	public void testAgentAdd() {
		tve.addAgent(a, VacuumEnvironment.Location.A);
		Assert.assertEquals(VacuumEnvironment.Location.A, tve.getAgentLocation(a));
		Assert.assertEquals(1, tve.getAgents().size());
	}
}
