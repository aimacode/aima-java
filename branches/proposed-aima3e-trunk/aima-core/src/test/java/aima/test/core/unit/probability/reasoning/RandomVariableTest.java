package aima.test.core.unit.probability.reasoning;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import aima.core.probability.RandomVariable;
import aima.core.probability.reasoning.HmmConstants;

/**
 * @author Ravi Mohan
 * 
 */
public class RandomVariableTest {

	private RandomVariable aDistribution;

	@Before
	public void setUp() {
		List<String> states = Arrays.asList(new String[] {
				HmmConstants.DOOR_OPEN, HmmConstants.DOOR_CLOSED });
		aDistribution = new RandomVariable("HiddenState", states);
	}

	@Test(expected = RuntimeException.class)
	public void testSettingValuesOnInvalidStateThrowsException() {
		aDistribution.setProbabilityOf("invalid", 0.5);
	}

	@Test(expected = RuntimeException.class)
	public void testGettingValuesOnInvalidStateThrowsException() {
		aDistribution.getProbabilityOf("invalid");
	}
}
