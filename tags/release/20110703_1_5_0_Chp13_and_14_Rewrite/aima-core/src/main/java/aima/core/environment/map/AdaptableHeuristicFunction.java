package aima.core.environment.map;

import aima.core.search.framework.HeuristicFunction;

/**
 * This class extends heuristic functions in two ways: It maintains a goal and a
 * map to estimate distance to goal for states in route planning problems, and
 * it provides a method to adapt to different goals.
 * 
 * @author Ruediger Lunde
 */
public abstract class AdaptableHeuristicFunction implements HeuristicFunction,
		Cloneable {
	/** The Current Goal. */
	protected Object goal;
	/** The map to be used for distance to goal estimates. */
	protected Map map;

	/**
	 * Modifies goal and map information and returns the modified heuristic
	 * function.
	 */
	public AdaptableHeuristicFunction adaptToGoal(Object goal, Map map) {
		this.goal = goal;
		this.map = map;
		return this;
	}

	// when subclassing: Don't forget to implement the most important method
	// public double h(Object state)
}
