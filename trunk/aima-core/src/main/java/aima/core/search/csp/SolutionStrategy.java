package aima.core.search.csp;

import java.util.ArrayList;
import java.util.List;

/**
 * General interface for CSP solvers. Solving a CSP means finding an assignment,
 * which is consistent and complete with respect to a CSP.
 * 
 * @author Ruediger Lunde
 */
public abstract class SolutionStrategy {
	List<CSPStateListener> listeners = new ArrayList<CSPStateListener>();
	
	public void addCSPStateListener(CSPStateListener listener) {
		listeners.add(listener);
	}
	
	public void removeCSPStateListener(CSPStateListener listener) {
		listeners.remove(listener);
	}
	
	protected void fireStateChanged(CSP csp) {
		for (CSPStateListener listener : listeners)
			listener.stateChanged(csp.copyForPropagation());
	}
	
	protected void fireStateChanged(Assignment assignment) {
		for (CSPStateListener listener : listeners)
			listener.stateChanged(assignment.copy());
	}
	
	public abstract Assignment solve(CSP csp);
}
