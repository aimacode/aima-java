package aima.test.core.unit.logic.planning;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.planning.ActionSchema;
import aima.core.logic.planning.State;
import aima.core.logic.planning.Utils;
import aima.core.logic.planning.angelicsearch.AngelicHLA;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

/**
 * @author samagra
 */
public class StateTest {
    private Literal testFluentOne, testFluentTwo, testFluentThree, testFluentFour;
    private State stateOne, stateTwo, testState;
    private ActionSchema flyActionOne, flyActionTwo;
    private AngelicHLA h1, h2;

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

        h1 = new AngelicHLA("h1", null, "~A", "A^~-B");
        h2 = new AngelicHLA("h2", null, "~B", "~+A^~+-C");
        stateOne = new State("~A");
        stateTwo = new State("~B");
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
        Assert.assertTrue(stateOne.isApplicable(h1));
        Assert.assertFalse(stateOne.isApplicable(h2));
        Assert.assertTrue(stateTwo.isApplicable(h2));
        Assert.assertFalse(stateTwo.isApplicable(h1));
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

    @Test
    public void optimisticReachTest() {
        // States obtained after applying h1 to stateOne
        State stateOneResultOne = new State("A^~B");
        State stateOneResultTwo = new State("A");
        Assert.assertTrue(stateOne.optimisticReach(h1).containsAll(Arrays.asList(stateOneResultTwo, stateOneResultOne)));
        Assert.assertEquals(2, stateOne.optimisticReach(h1).size());
        // States obtained after applying h2 to stateTwo
        Assert.assertEquals(6, stateTwo.optimisticReach(h2).size());
        State[] statesResultTwo = {
                stateTwo,
                new State("~B^~C"),
                new State("~B^A^C"),
                new State("~B^A"),
                new State("~B^C"),
                new State("~B^A^~C")
        };
        Assert.assertTrue(stateTwo.optimisticReach(h2).containsAll(Arrays.asList(statesResultTwo)));
    }

    @Test
    public void pessimisticReachTest() {
        Assert.assertEquals(1, stateOne.pessimisticReach(h1).size());
        Assert.assertTrue(stateOne.pessimisticReach(h1).
                contains(new State("A")));
        Assert.assertEquals(1, stateTwo.pessimisticReach(h2).size());
        Assert.assertTrue(stateTwo.pessimisticReach(h2).
                contains(new State("~B")));
    }

    @Test
    public void optimisticReachListTest(){
        HashSet<State> resultingStates = stateOne.optimisticReach(Arrays.asList(h1, h2));
        Assert.assertEquals(4,resultingStates.size());
        Assert.assertTrue(resultingStates.contains(new State("A")));
        Assert.assertTrue(resultingStates.contains(new State("A^~B")));
        Assert.assertTrue(resultingStates.contains(new State("A^~B^C")));
        Assert.assertTrue(resultingStates.contains(new State("A^~B^~C")));
    }

    @Test
    public void pessimisticReachTestList(){
        HashSet<State> resultStates = stateOne.pessimisticReach(Arrays.asList(h1,h2));
        Assert.assertEquals(1,resultStates.size());
        Assert.assertTrue(resultStates.contains(new State("A")));
    }
}
