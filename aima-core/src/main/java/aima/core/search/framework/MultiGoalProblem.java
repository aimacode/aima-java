package aima.core.search.framework;

/**
 * An specialization of the Problem class can implement this interface if there
 * is a desire to search for more than 1 goal in the search space. However, care
 * needs to be taken when doing this as it does not always make sense to
 * continue with a search once an initial goal is found, for example if using a
 * heuristic targeted at a single goal.
 * 
 * @author Ciaran O'Reilly
 */
public interface MultiGoalProblem {

	/**
	 * This method is only called if Problem.isGoalState() returns true.
	 * 
	 * @param n
	 *            the current Node in the search.
	 * 
	 * @return true if all of the Goals for this particular problem have been
	 *         reached.
	 */
	boolean isFinalGoalState(Node n);
}
