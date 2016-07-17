package aima.core.search.api;

import java.util.function.Function;

/**
 * Description of a Search function that looks for a complete and consistent
 * assignment for a Constraint Satisfaction Problem (CSP).
 * 
 * @author Ciaran O'Reilly
 * 
 */
@FunctionalInterface
public interface SearchForAssignmentFunction extends Function<CSP, Assignment> {
	@Override
	Assignment apply(CSP csp);
}
