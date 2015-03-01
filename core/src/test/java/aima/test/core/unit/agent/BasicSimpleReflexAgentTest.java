package aima.test.core.unit.agent;

import aima.core.agent.BasicSimpleReflexAgent;
import aima.core.api.agent.Action;
import aima.core.api.agent.Rule;
import aima.core.api.agent.SimpleReflexAgent;
import aima.test.core.unit.agent.support.TestPercept;
import aima.test.core.unit.agent.support.TestState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * @author Ciaran O'Reilly
 */
public class BasicSimpleReflexAgentTest {

    private SimpleReflexAgent<TestPercept, TestState> agent;

    @Before
    public void setUp() {
        agent = new BasicSimpleReflexAgent<>(
                percept -> new TestState(percept.location, percept.floorIsDirty),
                Arrays.asList(
                    new Rule<TestState>() {
                        public Predicate<TestState> condition() {return state -> state.dirty;}
                        public Action action() {return Action.newNamedAction("Sweep");}
                    },
                    new Rule<TestState>() {
                        public Predicate<TestState> condition() {return state -> !state.dirty;}
                        public Action action() {return Action.newNamedAction("Drink Tea");}
                    }
                )
        );
    }

    @Test
    public void testSweep() {
        Assert.assertEquals("Sweep", agent.perceive(new TestPercept("A", true)).name());
    }

    @Test
    public void tetDrinkTea() {
        Assert.assertEquals("Drink Tea", agent.perceive(new TestPercept("A", false)).name());
    }
}
