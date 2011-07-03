package aima.test.core.unit.probability.hmm;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import aima.core.probability.hmm.HmmConstants;
import aima.core.probability.hmm.VarDistribution;

/**
 * @author Ravi Mohan
 * 
 */
public class VarDistributionTest {

	private VarDistribution aDistribution;

	@Before
	public void setUp() {
		List<String> states = Arrays.asList(new String[] {
				HmmConstants.DOOR_OPEN, HmmConstants.DOOR_CLOSED });
		aDistribution = new VarDistribution("HiddenState", states);
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
