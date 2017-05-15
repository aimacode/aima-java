package aima.core.search.csp;

/**
 * Interface which allows interested clients to register at a solution strategy
 * and follow their progress step by step.
 * 
 * @author Ruediger Lunde
 */
public interface CspListener<VAR extends Variable, VAL> {
	/** Informs about changed assignments. */
	void stateChanged(Assignment<VAR, VAL> assignment, CSP<VAR, VAL> csp);

	/** Informs about changed domains (inferences). */
	void stateChanged(CSP<VAR, VAL> csp);
}
