package aima.core.api.agent;

/**
 * Describes an Action that can or has been taken by an Agent via one of its
 * Actuators.
 *
 * @author Ciaran O'Reilly
 */
public interface Action {

    /**
     *
     * @return a uniquely identifying name for the Action.
     */
    String name();

    /**
     * A 'No Operation' action.<br>
     * <br>
     * <b>NOTE:</b> AIMA4e - NoOp, or no operation, is the name of an assembly language
     * instruction that does nothing.
     */
    Action NoOp = new Action() {
        @Override
        public String name() { return "NoOp"; }
        @Override
        public String toString() { return name(); }
    };

    static Action newNamedAction(final String name) {
        return new Action() {
            @Override
            public String name() { return name; }
            @Override
            public boolean equals(Object obj) {
                if (obj != null && obj instanceof Action) {
                    return this.name().equals(((Action)obj).name());
                }
                return super.equals(obj);
            }
            @Override
            public int hashCode() {
                return name().hashCode();
            }
        };
    }
}
