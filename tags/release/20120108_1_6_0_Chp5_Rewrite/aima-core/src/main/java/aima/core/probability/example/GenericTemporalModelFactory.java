package aima.core.probability.example;

import java.util.HashMap;
import java.util.Map;

import aima.core.probability.FiniteProbabilityModel;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.FiniteNode;
import aima.core.probability.bayes.impl.BayesNet;
import aima.core.probability.bayes.impl.FullCPTNode;
import aima.core.probability.bayes.model.FiniteBayesModel;

/**
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class GenericTemporalModelFactory {

	public static FiniteProbabilityModel getUmbrellaWorldTransitionModel() {
		return getUmbrellaWorldModel();
	}

	public static FiniteProbabilityModel getUmbrellaWorldSensorModel() {
		return getUmbrellaWorldModel();
	}

	public static FiniteProbabilityModel getUmbrellaWorldModel() {
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

	public static Map<RandomVariable, RandomVariable> getUmbrellaWorld_Xt_to_Xtm1_Map() {
		Map<RandomVariable, RandomVariable> tToTm1StateVarMap = new HashMap<RandomVariable, RandomVariable>();
		tToTm1StateVarMap.put(ExampleRV.RAIN_t_RV, ExampleRV.RAIN_tm1_RV);

		return tToTm1StateVarMap;
	}
}
