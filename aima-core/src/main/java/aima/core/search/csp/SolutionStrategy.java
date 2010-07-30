package aima.core.search.csp;

/**
 * General interface for CSP solvers. Solving a CSP means finding an assignment,
 * which is consistent and complete with respect to a CSP.
 * 
 * @author Ruediger Lunde
 */
public interface SolutionStrategy {
	Assignment solve(CSP csp);
}
