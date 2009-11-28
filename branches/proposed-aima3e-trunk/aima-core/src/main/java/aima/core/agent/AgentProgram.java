package aima.core.agent;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): pg 35.<br>
 * An agent's behavior is described by the 'agent function' that maps any given percept
 * sequence to an action. Internally, the agent function for an artificial agent will be
 * implemented by an agent program.
 */

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public interface AgentProgram {
	/**
	 * The Agent's program, which maps any given percept sequences to an action.
	 * 
	 * @param percept
	 *            The current percept of a sequence perceived by the Agent.
	 * @return the Action to be taken in response to the currently perceived
	 *         percept.
	 */
	Action execute(Percept percept);
}
