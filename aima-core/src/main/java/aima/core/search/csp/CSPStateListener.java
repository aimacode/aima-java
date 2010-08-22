package aima.core.search.csp;

/**
 * Interface which allows interested clients to register at a solution strategy
 * and follow their progress step by step.
 * 
 * @author Ruediger Lunde
 */
public interface CSPStateListener {
	void stateChanged(CSP csp);

	void stateChanged(Assignment assignment, CSP csp);
}
