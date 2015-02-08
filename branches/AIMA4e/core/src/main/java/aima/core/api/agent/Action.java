package aima.core.api.agent;

/**
 * Describes an Action that can or has been taken by an Agent via one of its
 * Actuators.
 *
 * @author Ciaran O'Reilly
 */
public interface Action {
    /**
     * A 'No Operation' action.<br>
     * <br>
     * <b>NOTE:</b> AIMA4e - NoOp, or no operation, is the name of an assembly language
     * instruction that does nothing.
     */
    Action NoOp = new Action() {
        @Override
        public String toString() {
            return "NoOp";
        }
    };
}
