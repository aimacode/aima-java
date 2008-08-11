package aima.search.framework;

/**
 * @author Ravi Mohan
 * 
 */

public interface StepCostFunction {
	Double calculateStepCost(Object fromState, Object toState, String action);
}