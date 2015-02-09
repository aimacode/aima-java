package aima.test.core.unit.agent.support;

import aima.core.api.agent.Action;

/**
 * @author Ciaran O'Reilly
 */
public class TestAction implements Action {
    public final String name;
    public TestAction(String name) {this.name = name;}
    public String toString() {return name;}
}
