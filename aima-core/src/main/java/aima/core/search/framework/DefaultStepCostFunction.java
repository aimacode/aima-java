package aima.core.search.framework;

import aima.core.agent.Action;
import aima.core.search.framework.problem.StepCostFunction;

/**
 * Returns one for every action.
 * 
 * @author Ravi Mohan
 */
public class DefaultStepCostFunction implements StepCostFunction {

	public double c(Object stateFrom, Action action, Object stateTo) {
		return 1;
	}
}