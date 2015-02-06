package aima.core.api.agent;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 *
 * Figure ?? Agents interact with environments through sensors and actuators.
 *
 * @author Ciaran O'Reilly
 */
public interface Agent {
    /**
     * Call the Agent's program, which maps any given percept sequences to an
     * action.
     *
     * @param percept
     *            The current percept of a sequence perceived by the Agent.
     * @return the Action to be taken in response to the currently perceived
     *         percept.
     */
    Action interact(Percept percept);
}
