package aima.core.search.framework;

import aima.core.agent.Action;

/**
 * @author Ravi Mohan
 * 
 */
public interface StepCostFunction {
	Double calculateStepCost(Object fromState, Object toState, Action action);
}