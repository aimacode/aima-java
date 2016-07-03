package aima.test.unit.agent.support;

/**
 * @author Ciaran O'Reilly
 */
public class TestState {
    public final String position;
    public final boolean dirty;

    public TestState(String position, boolean dirty) {
        this.position     = position;
        this.dirty        = dirty;
    }
}
