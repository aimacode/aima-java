package aima.core.search.csp;

import java.util.ArrayList;
import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Ed.): Figure 6.1, Page 204.
 * The principal states and territories of Australia. Coloring this map can be
 * viewed as a constraint satisfaction problem (CSP). The goal is to assign
 * colors to each region so that no neighboring regions have the same color.
 * 
 * @author Ruediger Lunde
 */
public class MapCSP extends CSP {
	public static final Variable NSW = new Variable("NSW");
	public static final Variable NT = new Variable("NT");
	public static final Variable Q = new Variable("Q");
	public static final Variable SA = new Variable("SA");
	public static final Variable T = new Variable("T");
	public static final Variable V = new Variable("V");
	public static final Variable WA = new Variable("WA");
	public static final String RED = "RED";
	public static final String GREEN = "GREEN";
	public static final String BLUE = "BLUE";

	private static List<Variable> collectVariables() {
		List<Variable> variables = new ArrayList<Variable>();
		variables.add(NSW);
		variables.add(WA);
		variables.add(NT);
		variables.add(Q);
		variables.add(SA);
		variables.add(V);
		variables.add(T);
		return variables;
	}

	public MapCSP() {
		super(collectVariables());

		Domain colors = new Domain(new Object[] { RED, GREEN, BLUE });

		for (Variable var : getVariables())
			setDomain(var, colors);

		addConstraint(new NotEqualConstraint(WA, NT));
		addConstraint(new NotEqualConstraint(WA, SA));
		addConstraint(new NotEqualConstraint(NT, SA));
		addConstraint(new NotEqualConstraint(NT, Q));
		addConstraint(new NotEqualConstraint(SA, Q));
		addConstraint(new NotEqualConstraint(SA, NSW));
		addConstraint(new NotEqualConstraint(SA, V));
		addConstraint(new NotEqualConstraint(Q, NSW));
		addConstraint(new NotEqualConstraint(NSW, V));
	}
}