package aima.core.environment.vacuum;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.search.framework.ActionsFunction;

/**
 * Specifies the actions available to the agent at state s
 * 
 * @author Andrew Brown
 */
public class VacuumWorldActions implements ActionsFunction {

	private static final Set<Action> _actions;
	static {
		Set<Action> actions = new HashSet<Action>();
		actions.add(VacuumEnvironment.ACTION_SUCK);
		actions.add(VacuumEnvironment.ACTION_MOVE_LEFT);
		actions.add(VacuumEnvironment.ACTION_MOVE_RIGHT);
		// Ensure cannot be modified.
		_actions = Collections.unmodifiableSet(actions);
	}

	/**
	 * Returns possible actions given this state
	 * 
	 * @param s
	 * @return possible actions given this state.
	 */
	@Override
	public Set<Action> actions(Object s) {
		return _actions;
	}
}
