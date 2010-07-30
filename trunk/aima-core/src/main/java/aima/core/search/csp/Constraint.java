package aima.core.search.csp;

import java.util.List;

/**
 * A constraint specifies the allowable combinations of values for a set of
 * variables.
 * 
 * @author Ruediger Lunde
 */
public interface Constraint {
	/** Returns a tuple of variables that participate in the constraint. */ 
	List<Variable> getScope();
	/** Constrains the values that the variables can take on. */
	boolean isSatisfiedWith(Assignment assignment);
}