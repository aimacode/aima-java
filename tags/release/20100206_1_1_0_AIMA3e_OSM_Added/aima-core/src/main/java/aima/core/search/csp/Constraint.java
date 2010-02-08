package aima.core.search.csp;

/**
 * @author Ravi Mohan
 * 
 */
public interface Constraint {
	boolean isSatisfiedWith(Assignment assignment, String variable, Object value);
}