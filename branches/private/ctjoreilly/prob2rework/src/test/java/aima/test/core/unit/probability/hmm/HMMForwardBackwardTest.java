package aima.test.core.unit.probability.hmm;

import org.junit.Before;
import org.junit.Test;

import aima.core.probability.FiniteProbabilityModel;
import aima.core.probability.hmm.HMMForwardBackward;
import aima.test.core.unit.probability.temporal.CommonForwardBackwardTest;

public class HMMForwardBackwardTest extends CommonForwardBackwardTest {

	//
	private HMMForwardBackward uw = null;

	@Before
	public void setUp() {
		
	}
	
	@Test
	public void testForwardStep_UmbrellaWorld() {
		super.testForwardStep_UmbrellaWorld(uw);
	}
	
	@Test
	public void testBackwardStep_UmbrellaWorld() {
		super.testBackwardStep_UmbrellaWorld(uw);
	}
	
	@Test
	public void testForwardBackward_UmbrellaWorld() {
		super.testForwardBackward_UmbrellaWorld(uw);
	}
	
	//
	// PRIVATE METHODS
	//
	private static FiniteProbabilityModel getUmbrellaWorldTransitionModel() {
		return null; // TODO
	}

	private static FiniteProbabilityModel getUmbrellaWorldSensorModel() {
		return null; // TODO
	}
}
