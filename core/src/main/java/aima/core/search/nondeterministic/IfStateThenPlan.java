package aima.core.search.nondeterministic;

/**
 * Represents an if-state-then-plan statement for use with AND-OR search;
 * explanation given on page 135 of AIMA3e.
 * 
 */
public class IfStateThenPlan<A> {

	private Object state;
	private Plan<A> plan;

	/**
	 * Constructor
	 * 
	 * @param state
	 * @param plan
	 */
	public IfStateThenPlan(Object state, Plan<A> plan) {
		this.state = state;
		this.plan = plan;
	}

	/**
	 * Uses this if-state-then-plan return a result based on the given state
	 * 
	 * @param state
	 * @return the plan if the given state matches, null otherwise.
	 */
	public Plan<A> ifStateMatches(Object state) {
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
		StringBuilder s = new StringBuilder();
		s.append("if ");
		s.append(this.state);
		s.append(" then ");
		s.append(this.plan);
		return s.toString();
	}
}
