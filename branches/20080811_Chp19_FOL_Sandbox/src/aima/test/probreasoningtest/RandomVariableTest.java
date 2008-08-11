package aima.test.probreasoningtest;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import aima.probability.RandomVariable;
import aima.probability.reasoning.HmmConstants;

/**
 * @author Ravi Mohan
 * 
 */
public class RandomVariableTest extends TestCase {

	private RandomVariable aDistribution;

	@Override
	public void setUp() {
		List<String> states = Arrays.asList(new String[] {
				HmmConstants.DOOR_OPEN, HmmConstants.DOOR_CLOSED });
		aDistribution = new RandomVariable("HiddenState", states);
	}

	public void testSettingValuesOnInvalidStateThrowsException() {
		try {
			aDistribution.setProbabilityOf("invalid", 0.5);
			fail("exception not thrown on invalid state being set");
		} catch (RuntimeException e) {

		}
	}

	public void testGettingValuesOnInvalidStateThrowsException() {
		try {
			double d = aDistribution.getProbabilityOf("invalid");
			fail("exception not thrown on trying to get probability value of invalid state ");
		} catch (RuntimeException e) {

		}
	}
}
