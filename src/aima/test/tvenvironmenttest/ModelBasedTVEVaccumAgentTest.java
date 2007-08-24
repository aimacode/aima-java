/*
 * Created by IntelliJ IDEA.
 * User: rrmohan
 * Date: Jan 20, 2003
 * Time: 9:57:10 AM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package aima.test.tvenvironmenttest;

import junit.framework.TestCase;
import aima.basic.vaccum.ModelBasedTVEVaccumAgent;
import aima.basic.vaccum.TrivialVaccumEnvironment;

/**
 * @author Ravi Mohan
 * 
 */

public class ModelBasedTVEVaccumAgentTest extends TestCase {
	TrivialVaccumEnvironment tve, tve2, tve3, tve4;

	ModelBasedTVEVaccumAgent a;

	public ModelBasedTVEVaccumAgentTest(String name) {
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

	public void testAgentActionNumber1() {
		tve.addAgent(a, "A");
		assertEquals("A", tve.getAgentLocation(a));
		assertEquals(1, tve.getAgents().size());
		tve.step(); // cleans location A
		assertEquals("A", tve.getAgentLocation(a));
		assertEquals("Clean", tve.getLocation1Status());
		tve.step(); // moves to lcation B
		assertEquals("B", tve.getAgentLocation(a));
		assertEquals("Dirty", tve.getLocation2Status());
		tve.step(); // cleans location B
		assertEquals("B", tve.getAgentLocation(a));
		assertEquals("Clean", tve.getLocation2Status());
		tve.step(); // NOOP
		assertEquals("B", tve.getAgentLocation(a));
		assertEquals(19, tve.getAgentperformance(a));

	}

	public void testAgentActionNumber2() {
		tve.addAgent(a, "B");
		assertEquals("B", tve.getAgentLocation(a));
		assertEquals(1, tve.getAgents().size());
		tve.step(); // cleans location B
		assertEquals("B", tve.getAgentLocation(a));
		assertEquals("Clean", tve.getLocation2Status());
		tve.step(); // moves to lcation A
		assertEquals("A", tve.getAgentLocation(a));
		assertEquals("Dirty", tve.getLocation1Status());
		tve.step(); // cleans location A
		assertEquals("A", tve.getAgentLocation(a));
		assertEquals("Clean", tve.getLocation1Status());
		tve.step(); // NOOP
		assertEquals("A", tve.getAgentLocation(a));
		assertEquals("Clean", tve.getLocation1Status());
		assertEquals("Clean", tve.getLocation2Status());
		assertEquals(19, tve.getAgentperformance(a));

	}

	public void testAgentActionNumber3() {
		tve2.addAgent(a, "A");
		assertEquals("A", tve2.getAgentLocation(a));
		assertEquals(1, tve2.getAgents().size());
		tve2.step(); // moves to location B
		assertEquals("B", tve2.getAgentLocation(a));
		assertEquals("Clean", tve2.getLocation2Status());
		tve2.step(); // NOOP
		assertEquals("B", tve2.getAgentLocation(a));
		assertEquals("Clean", tve2.getLocation1Status());
		assertEquals("Clean", tve2.getLocation2Status());
		assertEquals(-1, tve.getAgentperformance(a));

	}

	public void testAgentActionNumber4() {
		tve2.addAgent(a, "B");
		assertEquals("B", tve2.getAgentLocation(a));
		assertEquals(1, tve2.getAgents().size());
		tve2.step(); // moves to location A
		assertEquals("A", tve2.getAgentLocation(a));
		assertEquals("Clean", tve2.getLocation1Status());
		tve2.step(); // NOOP
		assertEquals("A", tve2.getAgentLocation(a));
		assertEquals("Clean", tve2.getLocation1Status());
		assertEquals("Clean", tve2.getLocation2Status());
		assertEquals(-1, tve.getAgentperformance(a));
	}

	public void testAgentActionNumber5() {
		tve3.addAgent(a, "A");
		assertEquals("A", tve3.getAgentLocation(a));
		assertEquals(1, tve3.getAgents().size());
		tve3.step(); // moves to B
		assertEquals("B", tve3.getAgentLocation(a));
		assertEquals("Dirty", tve3.getLocation2Status());
		tve3.step(); // cleans location B
		assertEquals("B", tve3.getAgentLocation(a));
		assertEquals("Clean", tve3.getLocation2Status());
		tve3.step(); // NOOP
		assertEquals("B", tve3.getAgentLocation(a));
		assertEquals("Clean", tve3.getLocation1Status());
		assertEquals("Clean", tve3.getLocation2Status());
		assertEquals(9, tve.getAgentperformance(a));

	}

	public void testAgentActionNumber6() {
		tve3.addAgent(a, "B");
		assertEquals("B", tve3.getAgentLocation(a));
		assertEquals(1, tve3.getAgents().size());
		tve3.step(); // cleans B
		assertEquals("B", tve3.getAgentLocation(a));
		assertEquals("Clean", tve3.getLocation2Status());
		tve3.step(); // moves to A
		assertEquals("A", tve3.getAgentLocation(a));
		assertEquals("Clean", tve3.getLocation1Status());
		tve3.step(); // NOOP
		assertEquals("A", tve3.getAgentLocation(a));
		assertEquals("Clean", tve3.getLocation1Status());
		assertEquals("Clean", tve3.getLocation2Status());
		assertEquals(9, tve.getAgentperformance(a));

	}

	public void testAgentActionNumber7() {
		tve4.addAgent(a, "A");
		assertEquals("A", tve4.getAgentLocation(a));
		assertEquals(1, tve4.getAgents().size());
		tve4.step(); // cleans A
		assertEquals("A", tve4.getAgentLocation(a));
		assertEquals("Clean", tve4.getLocation1Status());
		tve4.step(); // moves to B
		assertEquals("B", tve4.getAgentLocation(a));
		assertEquals("Clean", tve4.getLocation2Status());
		tve4.step(); // NOOP
		assertEquals("B", tve4.getAgentLocation(a));
		assertEquals("Clean", tve4.getLocation1Status());
		assertEquals("Clean", tve4.getLocation2Status());
		assertEquals(9, tve.getAgentperformance(a));

	}

	public void testAgentActionNumber8() {
		tve4.addAgent(a, "B");
		assertEquals("B", tve4.getAgentLocation(a));
		assertEquals(1, tve4.getAgents().size());
		tve4.step(); // moves to A
		assertEquals("A", tve4.getAgentLocation(a));
		assertEquals("Dirty", tve4.getLocation1Status());
		tve4.step(); // cleans A
		assertEquals("A", tve4.getAgentLocation(a));
		assertEquals("Clean", tve4.getLocation1Status());
		tve4.step(); // NOOP
		assertEquals("A", tve4.getAgentLocation(a));
		assertEquals("Clean", tve4.getLocation1Status());
		assertEquals("Clean", tve4.getLocation2Status());
		assertEquals(9, tve.getAgentperformance(a));

	}

}
