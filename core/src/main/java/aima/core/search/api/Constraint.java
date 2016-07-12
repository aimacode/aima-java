package aima.core.search.api;

import java.util.Iterator;
import java.util.List;

/**
 * A constraint consists of a pair <scope, rel>, where scope is a tuple of
 * variables that participate in the constraint and rel is a relation that
 * defines the values that those variables can take on.
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public interface Constraint {
	interface Relation {
		/**
		 * Test if a tuple of values satisfy the constraint.
		 * 
		 * @param values
		 *            the values corresponding to the scope to be tested.
		 * @return true if the values are satisfied by the constraint, false
		 *         otherwise.
		 */
		boolean isMember(Object[] values);

		/**
		 * @return an iterator that allows you to enumerate over the values that
		 *         the variables in the scope of the constraint can take on.
		 */
		Iterator<List<Object>> iterator(List<Domain> domainsOfScope);
	}

	/**
	 * 
	 * @return a tuple of variable names that participate in the constraint.
	 */
	List<String> getScope();

	/**
	 * 
	 * @return the relation that defines the values the the scope of the
	 *         constraint can take on.
	 */
	Relation getRelation();
	
	/**
	 * 
	 * @return true if a unary constraint, false otherwise.
	 */
	default boolean isUnary() {
		return getScope().size() == 1;
	}
	
	/**
	 * 
	 * @return true if a binary constraint, false otherwise.
	 */
	default boolean isBinary() {
		return getScope().size() == 2;
	}
}
