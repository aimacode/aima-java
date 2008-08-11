package aima.test.tvenvironmenttest;

import junit.framework.TestCase;
import aima.basic.vaccum.ModelBasedTVEVaccumAgent;
import aima.basic.vaccum.TrivialVaccumEnvironment;

/**
 * @author Ravi Mohan
 * 
 */
public class TrivialVaccumEnvironmentTest extends TestCase {
	TrivialVaccumEnvironment tve, tve2, tve3, tve4;

	ModelBasedTVEVaccumAgent a;

	public TrivialVaccumEnvironmentTest(String name) {
		super(name);
	}

	@Override
	public void setUp() {
		tve = new TrivialVaccumEnvironment("Dirty", "Dirty");
		tve2 = new TrivialVaccumEnvironment("Clean", "Clean");
		tve3 = new TrivialVaccumEnvironment("Clean", "Dirty");
		tve4 = new TrivialVaccumEnvironment("Dirty", "Clean");
		a = new ModelBasedTVEVaccumAgent();
	}

	public void testTVEConstruction() {
		assertEquals("Dirty", tve.getLocation1Status());
		assertEquals("Dirty", tve.getLocation2Status());
		assertEquals("Clean", tve2.getLocation1Status());
		assertEquals("Clean", tve2.getLocation2Status());
	}

	public void testAgentAdd() {
		tve.addAgent(a, "A");
		assertEquals("A", tve.getAgentLocation(a));
		assertEquals(1, tve.getAgents().size());

	}

}
