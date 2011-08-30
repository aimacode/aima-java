package aima.test.core.unit.probability.temporal.generic;

import org.junit.Before;
import org.junit.Test;

import aima.core.probability.example.GenericTemporalModelFactory;
import aima.core.probability.temporal.generic.ForwardBackward;
import aima.test.core.unit.probability.temporal.CommonForwardBackwardTest;

/**
 * 
 * @author Ciaran O'Reilly
 *
 */
public class ForwardBackwardTest extends CommonForwardBackwardTest {

	//
	private ForwardBackward uw = null;

	@Before
	public void setUp() {
		uw = new ForwardBackward(
				GenericTemporalModelFactory.getUmbrellaWorldTransitionModel(),
				GenericTemporalModelFactory.getUmbrellaWorld_Xt_to_Xtm1_Map(),
				GenericTemporalModelFactory.getUmbrellaWorldSensorModel());
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
}
