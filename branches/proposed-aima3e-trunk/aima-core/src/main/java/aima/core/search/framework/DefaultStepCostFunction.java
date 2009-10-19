package aima.core.search.framework;

import aima.core.agent.Action;

/**
 * @author Ravi Mohan
 * 
 */
public class DefaultStepCostFunction implements StepCostFunction {

	//
	// START-StepCostFunction
	public Double calculateStepCost(Object fromState, Object toState,
			Action action) {

		return new Double(1);
	}

	// END-StepCostFunction
	//
}