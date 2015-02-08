package aima.test.core.unit.agent;

import aima.core.agent.BasicSimpleReflexAgent;
import aima.core.api.agent.Action;
import aima.core.api.agent.Percept;
import aima.core.api.agent.Rule;
import aima.core.api.agent.SimpleReflexAgent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * @author Ciaran O'Reilly
 */
public class BasicSimpleReflexAgentTest {
    class TestAction implements Action {
        final String name;
        TestAction(String name) {this.name = name;}
        public String toString() {return name;}
    }
    class TestState {
        final boolean dirty;
        TestState(boolean dirty) {this.dirty = dirty;}
    }
    class TestPercept implements Percept {
        final boolean floorIsDirty;
        TestPercept(boolean floorIsDirty) {this.floorIsDirty = floorIsDirty;}
    }

    private SimpleReflexAgent<TestPercept, TestState> agent;

    @Before
    public void setUp() {
        agent = new BasicSimpleReflexAgent<>(
                percept -> new TestState(percept.floorIsDirty),
                Arrays.asList(
                    new Rule<TestState>() {
                        public Predicate<TestState> condition() {return state -> state.dirty;}
                        public Action action() {return new TestAction("Sweep");}
                    },
                    new Rule<TestState>() {
                        public Predicate<TestState> condition() {return state -> !state.dirty;}
                        public Action action() {return new TestAction("Drink Tea");}
                    }
                )
        );
    }

    @Test
    public void testSweep() {
        Assert.assertEquals("Sweep", agent.perceive(new TestPercept(true)).toString());
    }

    @Test
    public void tetDrinkTea() {
        Assert.assertEquals("Drink Tea", agent.perceive(new TestPercept(false)).toString());
    }
}
