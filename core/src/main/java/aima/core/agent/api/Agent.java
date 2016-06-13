package aima.core.agent.api;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <p>
 * Figure ?? Agents interact with environments through sensors and actuators.
 * </p>
 *
 * @param <A>
 *            the type of actions the agent can take.
 * @param <P>
 *            the specific type of perceptual information the agent can perceive
 *            through its sensors.
 * @author Ciaran O'Reilly
 */
public interface Agent<A, P> {
	/**
	 * Call the Agent's program, which maps any given percept sequences to an
	 * action.
	 *
	 * @param percept
	 *            The current percept of a sequence perceived by the Agent
	 *            within its environment.
	 * @return the Action to be taken in response to the currently perceived
	 *         percept.
	 */
	A perceive(P percept);
}
