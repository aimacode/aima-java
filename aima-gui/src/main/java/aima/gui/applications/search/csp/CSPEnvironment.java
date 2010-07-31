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
			if (a.changeCSP)
				csp = a.getCSP();
			if (a.changeAssignment)
				assignment = a.getAssignment();
			if (agent == null && a.getAssignment() != null)
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
		private boolean changeCSP;
		private boolean changeAssignment;
		private CSP csp;
		private Assignment assignment;

		public StateChangeAction(CSP csp) {
			changeCSP = true;
			this.csp = csp;
		}

		public StateChangeAction(Assignment assignment) {
			changeAssignment = true;
			this.assignment = assignment;
		}

		public StateChangeAction(CSP csp, Assignment assignment) {
			changeCSP = true;
			changeAssignment = true;
			this.csp = csp;
			this.assignment = assignment;
		}

		public boolean changeCSP() {
			return changeCSP;
		}

		public CSP getCSP() {
			return csp;
		}

		public boolean changeAssignment() {
			return changeAssignment;
		}

		public Assignment getAssignment() {
			return assignment;
		}

		@Override
		public boolean isNoOp() {
			return false;
		}
		
		public String toString() {
			return "StateChangeAction " + assignment;
		}
	}
}
