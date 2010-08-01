package aima.gui.applications.search.csp;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractEnvironment;
import aima.core.search.csp.Assignment;
import aima.core.search.csp.CSP;

/**
 * Simple environment which maintains a CSP and an assignment. The state
 * is modified by executing {@link StateChangeAction}s.
 * @author Ruediger Lunde
 */
public class CSPEnvironment extends AbstractEnvironment {
	CSP csp;
	Assignment assignment;

	public void init(CSP csp) {
		this.csp = csp;
		assignment = null;
	}
	
	public CSP getCSP() {
		return csp;
	}

	public Assignment getAssignment() {
		return assignment;
	}

	/** Executes the provided action and returns null. */
	@Override
	public EnvironmentState executeAction(Agent agent, Action action) {
		if (action instanceof StateChangeAction) {
			StateChangeAction a = (StateChangeAction) action;
			if (a.updateCSP())
				csp = a.getCSP();
			if (a.updateAssignment())
				assignment = a.getAssignment();
			if (agent == null)
				updateEnvironmentViewsAgentActed(agent, action, null);
		}
		return null;
	}

	/** Returns null. */
	@Override
	public EnvironmentState getCurrentState() {
		return null;
	}

	/** Returns null. */
	@Override
	public Percept getPerceptSeenBy(Agent anAgent) {
		return null;
	}

	/** Action to modify the CSP environment state. */
	public static class StateChangeAction implements Action {
		private CSP csp;
		private Assignment assignment;
		
		/** Update the domains of the CSP. */
		public StateChangeAction(CSP csp) {
			this.csp = csp;
		}

		/** Update the current assignment. */
		public StateChangeAction(Assignment assignment, CSP csp) {
			this.csp = csp;
			this.assignment = assignment;
		}
		
		public boolean updateCSP() {
			return csp != null;
		}

		public CSP getCSP() {
			return csp;
		}

		public boolean updateAssignment() {
			return assignment != null;
		}

		public Assignment getAssignment() {
			return assignment;
		}
		
		@Override
		public boolean isNoOp() {
			return false;
		}
		
		public String toString() {
			return "State Change "
			+ (updateAssignment() ? assignment : "(Domain Reduction)");
		}
	}
}
