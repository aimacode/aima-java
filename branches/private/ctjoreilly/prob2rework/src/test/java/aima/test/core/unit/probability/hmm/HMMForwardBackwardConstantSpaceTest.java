package aima.test.core.unit.probability.hmm;

import org.junit.Before;
import org.junit.Test;

import aima.core.probability.example.ExampleRV;
import aima.core.probability.example.HMMExampleFactory;
import aima.core.probability.hmm.HMMForwardBackwardConstantSpace;
import aima.test.core.unit.probability.temporal.CommonForwardBackwardTest;

public class HMMForwardBackwardConstantSpaceTest extends CommonForwardBackwardTest {

	//
	private HMMForwardBackwardConstantSpace uw = null;

	@Before
	public void setUp() {
		uw = new HMMForwardBackwardConstantSpace(ExampleRV.RAIN_t_RV,
				HMMExampleFactory.getUmbrellaWorldTransitionModel(),
				HMMExampleFactory.getUmbrellaWorldSensorModel());
	}

	@Test
	public void testForwardBackward_UmbrellaWorld() {
		super.testForwardBackward_UmbrellaWorld(uw);
	}
}
