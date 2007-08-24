package aima.test.learningtest;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import aima.learning.reinforcement.QTable;

/**
 * @author Ravi Mohan
 * 
 */

public class QTableTest extends TestCase {

	private QTable<String, String> qt;

	@Override
	public void setUp() {
		// simple qlearning example from
		// http://people.revoledu.com/kardi/tutorial/ReinforcementLearning/index.html
		List<String> states = Arrays.asList(new String[] { "A", "B", "C", "D",
				"E", "F" });
		List<String> actions = Arrays.asList(new String[] { "toA", "toB",
				"toC", "toD", "toE", "toF" });
		qt = new QTable<String, String>(actions);
	}

	public void testInitialSetup() {
		assertEquals(0.0, qt.getQValue("A", "toB"));
		assertEquals(0.0, qt.getQValue("F", "toF"));

	}

	public void testQUpDate() {
		assertEquals(0.0, qt.getQValue("B", "toF"));

		qt.upDateQ("B", "toF", "B", 1.0, 100, 0.8);
		assertEquals(100.0, qt.getQValue("B", "toF"));

		qt.upDateQ("D", "toB", "B", 1.0, 0, 0.8);
		assertEquals(80.0, qt.getQValue("D", "toB"));

		qt.upDateQ("A", "toB", "B", 1.0, 0, 0.8);
		assertEquals(80.0, qt.getQValue("A", "toB"));

		qt.upDateQ("A", "toD", "D", 1.0, 0, 0.8);
		assertEquals(64.0, qt.getQValue("A", "toD"));
	}

}
