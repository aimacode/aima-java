package aima.test.core.unit.environment.vacuum;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.environment.vacuum.ModelBasedReflexVacuumAgent;
import aima.core.environment.vacuum.VacuumEnvironment;

/**
 * @author Ravi Mohan
 * 
 */
public class VacuumEnvironmentTest {
	VacuumEnvironment tve, tve2, tve3, tve4;

	ModelBasedReflexVacuumAgent a;

	@Before
	public void setUp() {
		tve = new VacuumEnvironment(VacuumEnvironment.LocationState.Dirty,
				VacuumEnvironment.LocationState.Dirty);
		tve2 = new VacuumEnvironment(VacuumEnvironment.LocationState.Clean,
				VacuumEnvironment.LocationState.Clean);
		tve3 = new VacuumEnvironment(VacuumEnvironment.LocationState.Clean,
				VacuumEnvironment.LocationState.Dirty);
		tve4 = new VacuumEnvironment(VacuumEnvironment.LocationState.Dirty,
				VacuumEnvironment.LocationState.Clean);
		a = new ModelBasedReflexVacuumAgent();
	}

	@Test
	public void testTVEConstruction() {
		Assert.assertEquals(VacuumEnvironment.LocationState.Dirty, tve
				.getLocationState(VacuumEnvironment.LOCATION_A));
		Assert.assertEquals(VacuumEnvironment.LocationState.Dirty, tve
				.getLocationState(VacuumEnvironment.LOCATION_B));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean, tve2
				.getLocationState(VacuumEnvironment.LOCATION_A));
		Assert.assertEquals(VacuumEnvironment.LocationState.Clean, tve2
				.getLocationState(VacuumEnvironment.LOCATION_B));
	}

	@Test
	public void testAgentAdd() {
		tve.addAgent(a, VacuumEnvironment.LOCATION_A);
		Assert.assertEquals(VacuumEnvironment.LOCATION_A, tve
				.getAgentLocation(a));
		Assert.assertEquals(1, tve.getAgents().size());
	}
}
