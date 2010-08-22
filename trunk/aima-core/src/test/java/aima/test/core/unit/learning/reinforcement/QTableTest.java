package aima.test.core.unit.learning.reinforcement;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.learning.reinforcement.QTable;

/**
 * @author Ravi Mohan
 * 
 */
public class QTableTest {

	private QTable<String, String> qt;

	@Before
	public void setUp() {
		// simple qlearning example from
		// http://people.revoledu.com/kardi/tutorial/ReinforcementLearning/index.
		// html
		// List<String> states = Arrays.asList(new String[] { "A", "B", "C",
		// "D",
		// "E", "F" });
		List<String> actions = Arrays.asList(new String[] { "toA", "toB",
				"toC", "toD", "toE", "toF" });
		qt = new QTable<String, String>(actions);
	}

	@Test
	public void testInitialSetup() {
		Assert.assertEquals(0.0, qt.getQValue("A", "toB"), 0.001);
		Assert.assertEquals(0.0, qt.getQValue("F", "toF"), 0.001);
	}

	@Test
	public void testQUpDate() {
		Assert.assertEquals(0.0, qt.getQValue("B", "toF"), 0.001);

		qt.upDateQ("B", "toF", "B", 1.0, 100, 0.8);
		Assert.assertEquals(100.0, qt.getQValue("B", "toF"), 0.001);

		qt.upDateQ("D", "toB", "B", 1.0, 0, 0.8);
		Assert.assertEquals(80.0, qt.getQValue("D", "toB"), 0.001);

		qt.upDateQ("A", "toB", "B", 1.0, 0, 0.8);
		Assert.assertEquals(80.0, qt.getQValue("A", "toB"), 0.001);

		qt.upDateQ("A", "toD", "D", 1.0, 0, 0.8);
		Assert.assertEquals(64.0, qt.getQValue("A", "toD"), 0.001);
	}
}
