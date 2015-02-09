package aima.test.core.unit.agent.support;

import java.util.HashSet;
import java.util.Set;

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
