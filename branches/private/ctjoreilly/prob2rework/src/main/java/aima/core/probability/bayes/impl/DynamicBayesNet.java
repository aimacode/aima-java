package aima.core.probability.bayes.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.DynamicBayesianNetwork;
import aima.core.probability.bayes.Node;
import aima.core.util.SetOps;

/**
 * Default implementation of the DynamicBayesianNetwork interface.
 * 
 * @author Ciaran O'Reilly
 */
public class DynamicBayesNet extends BayesNet implements DynamicBayesianNetwork {

	private Set<RandomVariable> X_0 = new LinkedHashSet<RandomVariable>();
	private Set<RandomVariable> X_1 = new LinkedHashSet<RandomVariable>();
	private Set<RandomVariable> E_1 = new LinkedHashSet<RandomVariable>();
	private Map<RandomVariable, RandomVariable> X_0_to_X_1 = new LinkedHashMap<RandomVariable, RandomVariable>();
	private Map<RandomVariable, RandomVariable> X_1_to_X_0 = new LinkedHashMap<RandomVariable, RandomVariable>();
	private BayesianNetwork priorNetwork = null;
	private List<RandomVariable> X_1_VariablesInTopologicalOrder = new ArrayList<RandomVariable>();

	public DynamicBayesNet(BayesianNetwork priorNetwork,
			Map<RandomVariable, RandomVariable> X_0_to_X_1,
			Set<RandomVariable> E_1, Node... rootNodes) {
		super(rootNodes);

		for (Map.Entry<RandomVariable, RandomVariable> x0_x1 : X_0_to_X_1
				.entrySet()) {
			RandomVariable x0 = x0_x1.getKey();
			RandomVariable x1 = x0_x1.getValue();
			this.X_0.add(x0);
			this.X_1.add(x1);
			this.X_0_to_X_1.put(x0, x1);
			this.X_1_to_X_0.put(x1, x0);
		}
		this.E_1.addAll(E_1);

		// Assert the X_0, X_1, and E_1 sets are of expected sizes
		Set<RandomVariable> combined = new LinkedHashSet<RandomVariable>();
		combined.addAll(X_0);
		combined.addAll(X_1);
		combined.addAll(E_1);
		if (SetOps.difference(varToNodeMap.keySet(), combined).size() != 0) {
			throw new IllegalArgumentException(
					"X_0, X_1, and E_1 do not map correctly to the Nodes describing this Dynamic Bayesian Network.");
		}
		this.priorNetwork = priorNetwork;

		X_1_VariablesInTopologicalOrder
				.addAll(getVariablesInTopologicalOrder());
		X_1_VariablesInTopologicalOrder.removeAll(X_0);
		X_1_VariablesInTopologicalOrder.removeAll(E_1);
	}

	//
	// START-DynamicBayesianNetwork
	@Override
	public BayesianNetwork getPriorNetwork() {
		return priorNetwork;
	}

	@Override
	public Set<RandomVariable> getX_0() {
		return X_0;
	}

	@Override
	public Set<RandomVariable> getX_1() {
		return X_1;
	}

	@Override
	public List<RandomVariable> getX_1_VariablesInTopologicalOrder() {
		return X_1_VariablesInTopologicalOrder;
	}

	@Override
	public Map<RandomVariable, RandomVariable> getX_0_to_X_1() {
		return X_0_to_X_1;
	}

	@Override
	public Map<RandomVariable, RandomVariable> getX_1_to_X_0() {
		return X_1_to_X_0;
	}

	@Override
	public Set<RandomVariable> getE_1() {
		return E_1;
	}

	// END-DynamicBayesianNetwork
	//

	//
	// PRIVATE METHODS
	//
}
