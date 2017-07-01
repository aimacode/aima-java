package aima.core.environment.connectfour;

/**
 * Helper class for action ordering.
 * @author Ruediger Lunde
 */
public class ActionValuePair<A> implements Comparable<ActionValuePair<A>> {
	private A action;
	private double value;
	
	public static <A> ActionValuePair<A> createFor(A action, double utility) {
		return new ActionValuePair<A>(action, utility);
	}
	
	public ActionValuePair(A action, double utility) {
		this.action = action;
		this.value = utility;
	}
	
	public A getAction() {
		return action;
	}

	public double getValue() {
		return value;
	}

	@Override
	public int compareTo(ActionValuePair<A> pair) {
		if (value < pair.value)
			return 1;
		else if (value > pair.value)
			return -1;
		else
			return 0;
	}
}
