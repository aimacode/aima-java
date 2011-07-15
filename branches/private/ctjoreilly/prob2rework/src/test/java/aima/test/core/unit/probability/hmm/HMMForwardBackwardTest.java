package aima.test.core.unit.probability.hmm;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import aima.core.probability.example.ExampleRV;
import aima.core.probability.hmm.HMMForwardBackward;
import aima.core.util.math.Matrix;
import aima.test.core.unit.probability.temporal.CommonForwardBackwardTest;

public class HMMForwardBackwardTest extends CommonForwardBackwardTest {

	//
	private HMMForwardBackward uw = null;

	@Before
	public void setUp() {
		uw = new HMMForwardBackward(ExampleRV.RAIN_t_RV,
				getUmbrellaWorldTransitionModel(),
				getUmbrellaWorldSensorModel());
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
	private static Matrix getUmbrellaWorldTransitionModel() {
		return new Matrix(new double[][] { { 0.7, 0.3 }, { 0.3, 0.7 } });
	}

	private static Map<Object, Matrix> getUmbrellaWorldSensorModel() {
		Map<Object, Matrix> sensorModel = new HashMap<Object, Matrix>();
		sensorModel.put(Boolean.TRUE, new Matrix(new double[][] { { 0.9, 0.0 },
				{ 0.0, 0.2 } }));
		sensorModel.put(Boolean.FALSE, new Matrix(new double[][] {
				{ 0.1, 0.0 }, { 0.0, 0.8 } }));
		return sensorModel;
	}
}
