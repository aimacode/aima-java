package aima.core.search.map;

import aima.core.search.framework.HeuristicFunction;

/**
 * This class extends heuristic functions in two ways: It maintains a goal and a
 * map to estimate distance to goal for states in route planning problems, and
 * it provides a method to adapt to different goals.
 * 
 * @author R. Lunde
 */
public abstract class AdaptableHeuristicFunction implements HeuristicFunction,
		Cloneable {
	/** The Current Goal. */
	protected Object goal;
	/** The map to be used for distance to goal estimates. */
	protected Map map;

	/**
	 * Creates a clone and stores goal and map in the corresponding attributes.
	 * This method MUST be called before using the heuristic!
	 */
	public AdaptableHeuristicFunction getAdaptation(Object goal, Map map) {
		AdaptableHeuristicFunction result = null;
		try {
			result = (AdaptableHeuristicFunction) this.clone();
			result.goal = goal;
			result.map = map;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return result;
	}

	// when subclassing: Don't forget to implement the most important method
	// public double h(Object state)
}
