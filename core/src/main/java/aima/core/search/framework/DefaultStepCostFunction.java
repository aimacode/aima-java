package aima.core.search.framework;


/**
 * Returns one for every action.
 * 
 * @author Subham Mishra
 * @author Ravi Mohan
 */
public class DefaultStepCostFunction<A> implements StepCostFunction<A> {

	public double c(Object stateFrom, A action, Object stateTo) {
		return 1;
	}
}
