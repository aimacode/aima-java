package aima.test.core.unit.logic.planning;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.planning.ActionSchema;
import aima.core.logic.planning.State;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author samagra
 */
public class StateTest {
    private Literal testFluentOne, testFluentTwo, testFluentThree, testFluentFour;
    private State testState;
    private ActionSchema flyActionOne, flyActionTwo;

    @Before
    public void setup() {
        testFluentOne = new Literal(new Predicate("At",
                Arrays.asList(new Constant("P1"), new Constant("SFO"))));
        testFluentTwo = new Literal(new Predicate("Cargo",
                Collections.singletonList(new Constant("C1"))));
        testFluentThree = new Literal(new Predicate("Airport",
                Collections.singletonList(new Constant("JFK"))));
        testFluentFour = new Literal(new Predicate("At",
                Arrays.asList(new Constant("P1"), new Constant("JFK"))));
        testState = new State("At(C1,SFO)^At(C2,JFK)^At(P1,SFO)^At(P2,JFK)" +
                "^Cargo(C1)^Cargo(C2)^Plane(P1)^Plane(P2)^Airport(JFK)^Airport(SFO)");
        flyActionOne = new ActionSchema("Fly", null,
                "At(P1,SFO)^Plane(P1)^Airport(SFO)^Airport(JFK)",
                "~At(P1,SFO)^At(P1,JFK)");
        flyActionTwo = new ActionSchema("Fly", null,
                "At(P1,JFK)^Plane(P1)^Airport(SFO)^Airport(JFK)",
                "~At(P1,JFK)^At(P1,SFO)");
    }

    @Test
    public void constructorTest() {
        Assert.assertTrue(testState.getFluents().contains(testFluentOne));
        Assert.assertTrue(testState.getFluents().contains(testFluentTwo));
        Assert.assertTrue(testState.getFluents().contains(testFluentThree));
        Assert.assertFalse(testState.getFluents().contains(testFluentFour));
    }

    @Test
    public void isApplicableTest() {
        Assert.assertTrue(testState.isApplicable(flyActionOne));
        Assert.assertFalse(testState.isApplicable(flyActionTwo));
    }

    @Test
    public void resultTest() {
        State initState = new State("At(C1,SFO)^At(C2,JFK)^At(P1,SFO)^At(P2,JFK)" +
                "^Cargo(C1)^Cargo(C2)^Plane(P1)^Plane(P2)^Airport(JFK)^Airport(SFO)");
        State finalState = new State("At(C1,SFO)^At(C2,JFK)^At(P1,JFK)^At(P2,JFK)" +
                "^Cargo(C1)^Cargo(C2)^Plane(P1)^Plane(P2)^Airport(JFK)^Airport(SFO)");
        State newState = testState.result(flyActionTwo);
        Assert.assertNotEquals(finalState, newState);
        Assert.assertEquals(initState, newState);
        newState = testState.result(flyActionOne);
        Assert.assertEquals(finalState, newState);
        Assert.assertNotEquals(initState, newState);
        newState = testState.result(flyActionTwo);
        Assert.assertEquals(initState, newState);
    }
}
