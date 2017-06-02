package aima.core.search.nondeterministic;

/**
 * Represents an if-state-then-plan statement for use with AND-OR search;
 * explanation given on page 135 of AIMA3e.
 * 
 * @author Andrew Brown
 */
public class IfStateThenPlan {

	private Object state;
	private Plan plan;

	/**
	 * Constructor
	 */
	public IfStateThenPlan(Object state, Plan plan) {
		this.state = state;
		this.plan = plan;
	}

	/**
	 * Uses this if-state-then-plan return a result based on the given state
	 *
	 * @return the plan if the given state matches, null otherwise.
	 */
	public Plan ifStateMatches(Object state) {
		if (this.state.equals(state)) {
			return this.plan;
		} else {
			return null;
		}
	}

	/**
	 * Return string representation of this if-then-else
	 * 
	 * @return a string representation of this if-then-else.
	 */
	@Override
	public String toString() {
		return "if " + state + " then " + plan;
	}
}