package aima.core.search.csp;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for CSP solver implementations. Solving a CSP means finding an
 * assignment, which is consistent and complete with respect to a CSP. This
 * abstract class provides the central interface method and additionally an
 * implementation of an observer mechanism.
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
			listener.stateChanged(csp.copyDomains());
	}

	protected void fireStateChanged(Assignment assignment, CSP csp) {
		for (CSPStateListener listener : listeners)
			listener.stateChanged(assignment.copy(), csp.copyDomains());
	}

	public abstract Assignment solve(CSP csp);
}
