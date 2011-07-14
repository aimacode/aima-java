package aima.test.core.unit.probability.temporal.generic;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import aima.core.probability.FiniteProbabilityModel;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.FiniteNode;
import aima.core.probability.bayes.impl.BayesNet;
import aima.core.probability.bayes.impl.FullCPTNode;
import aima.core.probability.bayes.model.FiniteBayesModel;
import aima.core.probability.example.ExampleRV;
import aima.core.probability.temporal.generic.ForwardBackward;
import aima.test.core.unit.probability.temporal.CommonForwardBackwardTest;

public class ForwardBackwardTest extends CommonForwardBackwardTest {

	//
	private ForwardBackward uw = null;

	@Before
	public void setUp() {
		Map<RandomVariable, RandomVariable> tToTm1StateVarMap = new HashMap<RandomVariable, RandomVariable>();
		tToTm1StateVarMap.put(ExampleRV.RAIN_t_RV, ExampleRV.RAIN_tm1_RV);
		uw = new ForwardBackward(getUmbrellaWorldTransitionModel(),
				tToTm1StateVarMap, getUmbrellaWorldSensorModel());
	}
	
	@Test
	public void testForward_UmbrellaWorld() {
		super.testForward_UmbrellaWorld(uw);
	}
	
	@Test
	public void testBackward_UmbrellaWorld() {
		super.testBackward_UmbrellaWorld(uw);
	}
	
	@Test
	public void testForwardBackward_UmbrellaWorld() {
		super.testForwardBackward_UmbrellaWorld(uw);
	}
	
	//
	// PRIVATE METHODS
	//
	private static FiniteProbabilityModel getUmbrellaWorldTransitionModel() {
		return getUmbrellaWorldModel();
	}

	private static FiniteProbabilityModel getUmbrellaWorldSensorModel() {
		return getUmbrellaWorldModel();
	}

	private static FiniteProbabilityModel getUmbrellaWorldModel() {
		// Prior belief state
		FiniteNode rain_tm1 = new FullCPTNode(ExampleRV.RAIN_tm1_RV,
				new double[] { 0.5, 0.5 });
		// Transition Model
		FiniteNode rain_t = new FullCPTNode(ExampleRV.RAIN_t_RV, new double[] {
				// R_t-1 = true, R_t = true
				0.7,
				// R_t-1 = true, R_t = false
				0.3,
				// R_t-1 = false, R_t = true
				0.3,
				// R_t-1 = false, R_t = false
				0.7 }, rain_tm1);
		// Sensor Model
		@SuppressWarnings("unused")
		FiniteNode umbrealla_t = new FullCPTNode(ExampleRV.UMBREALLA_t_RV,
				new double[] {
						// R_t = true, U_t = true
						0.9,
						// R_t = true, U_t = false
						0.1,
						// R_t = false, U_t = true
						0.2,
						// R_t = false, U_t = false
						0.8 }, rain_t);

		return new FiniteBayesModel(new BayesNet(rain_tm1));
	}
}
