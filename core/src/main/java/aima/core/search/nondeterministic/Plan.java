package aima.core.search.nondeterministic;

import java.util.LinkedList;
import java.util.StringJoiner;

/**
 * Represents a solution plan for an AND-OR search; according to page 135
 * AIMA3e, the plan must be "a subtree that (1) has a goal node at every leaf,
 * (2) specifies one Object at each of its OR nodes, and (3) includes every
 * outcome branch at each of its AND nodes." As demonstrated on page 136, this
 * subtree is implemented as a linked list where every OR node is an Object--
 * satisfying (2)--and every AND node is an if-state-then-plan-else
 * chain--satisfying (3).
 * 
 * @author Anurag Rai
 */
public class Plan<A> extends LinkedList<Object> {

	private static final long serialVersionUID = 1L;

	/**
	 * Each step is either an IfStateThenPlan, a Plan, or an Action.
	 */
	LinkedList<Object> steps = new LinkedList<Object>();

	/**
	 * Empty constructor
	 */
	public Plan() {
	}

	/**
	 * Construct a plan based on a sequence of steps (IfStateThenPlan or a
	 * Plan).
	 * 
	 * @param steps
	 */
	public Plan(Object... steps) {
		for (int i = 0; i < steps.length; i++) {
			add(steps[i]);
		}
	}

	/**
	 * Prepend an action to the plan and return itself.
	 * 
	 * @param action
	 *            the action to be prepended to this plan.
	 * @return this plan with action prepended to it.
	 */
	public Plan<A> prepend(A action) {
		this.offerFirst(action);
		return this;
	}

	/**
	 * Returns the string representation of this plan
	 * 
	 * @return a string representation of this plan.
	 */
	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(",", "[", "]");
		for (Object step : this) {
			sj.add((CharSequence) step);
		}
		return sj.toString();
	}
}

