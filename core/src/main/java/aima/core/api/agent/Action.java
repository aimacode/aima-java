package aima.core.api.agent;

/**
 * Describes an Action that can or has been taken by an Agent via one of its
 * Actuators.
 *
 * @author Ciaran O'Reilly
 */
public interface Action {
    /**
     * Indicates whether or not this Action is a 'No Operation'.<br>
     * Note: AIMA4e - NoOp, or no operation, is the name of an assembly language
     * instruction that does nothing.
     *
     * @return true if this is a NoOp Action.
     */
    default boolean isNoOp() {
        return false;
    }
}
