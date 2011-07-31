package aima.core.probability.example;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.DynamicBayesianNetwork;
import aima.core.probability.bayes.FiniteNode;
import aima.core.probability.bayes.impl.BayesNet;
import aima.core.probability.bayes.impl.DynamicBayesNet;
import aima.core.probability.bayes.impl.FullCPTNode;

/**
 * 
 * @author Ciaran O'Reilly
 *
 */
public class DynamicBayesNetExampleFactory {
	/**
	 * Return a Dynamic Bayesian Network of the Umbrella World Network.
	 * 
	 * @return a Dynamic Bayesian Network of the Umbrella World Network.
	 */
	public static DynamicBayesianNetwork getUmbrellaWorldNetwork() {
		FiniteNode prior_rain_tm1 = new FullCPTNode(ExampleRV.RAIN_tm1_RV,
				new double[] { 0.5, 0.5 });

		BayesNet priorNetwork = new BayesNet(prior_rain_tm1);

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

		Map<RandomVariable, RandomVariable> X_0_to_X_1 = new HashMap<RandomVariable, RandomVariable>();
		X_0_to_X_1.put(ExampleRV.RAIN_tm1_RV, ExampleRV.RAIN_t_RV);
		Set<RandomVariable> E_1 = new HashSet<RandomVariable>();
		E_1.add(ExampleRV.UMBREALLA_t_RV);

		return new DynamicBayesNet(priorNetwork, X_0_to_X_1, E_1, rain_tm1);
	}
}
