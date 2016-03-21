package aima.test.core.unit.search.framework;


import aima.core.agent.Action;
import aima.core.search.framework.*;
import aima.core.util.datastructure.*;
import aima.core.util.datastructure.Queue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class AngelicSearchTest {

    Problem problem;
    AngelicSearch angelicSearch;
    GoalTest goalTest;
    ActionsFunction testActionFunction;
    ResultFunction resultFunction;
    StepCostFunction stepCostFunction;

    TestAction goToItaly;
    TestAction goToGermany;
    TestAction goToPoland;
    TestAction goToEngland;

    //There are 5 states (countries)
    static final String INITIAL_STATE_UKRAINE = "Ukraine";
    static final String GOAL_STATE_ENGLAND = "England";
    static final String INTERMEDIATE_STATE_ITALY = "Italy";
    static final String INTERMEDIATE_STATE_GERMANY = "Germany";
    static final String INTERMEDIATE_STATE_POLAND = "Poland";


    //Implementations Action
    private class TestAction implements Action{

        private String action;

        public TestAction(String action){
            this.action = action;
        }

        @Override
        public boolean isNoOp() {
            return false;
        }

    }

    //Implementations ActionFunction
    private class TestActionFunction implements ActionsFunction{

        //Announce set actions for each county
        Set<Action> actionsFromUkraine;
        Set<Action> actionsFromItaly;
        Set<Action> actionsFromGermany;
        Set<Action> actionsFromPoland;

        public TestActionFunction(Set<Action> actionsFromGermany, Set<Action> actionsFromItaly,
                                  Set<Action> actionsFromPoland, Set<Action> actionsFromUkraine){
            this.actionsFromGermany = actionsFromGermany;
            this.actionsFromItaly = actionsFromItaly;
            this.actionsFromPoland = actionsFromPoland;
            this.actionsFromUkraine = actionsFromUkraine;
        }

        @Override
        public Set<Action> actions(Object s) {
            if (s.equals(INITIAL_STATE_UKRAINE)) return actionsFromUkraine;
            if (s.equals(INTERMEDIATE_STATE_POLAND)) return actionsFromPoland;
            if (s.equals(INTERMEDIATE_STATE_GERMANY)) return actionsFromGermany;
            if (s.equals(INTERMEDIATE_STATE_ITALY)) return actionsFromItaly;
            else return null;
        }
    }

    //Implementations ResultFunction
    private class TestResultFunction implements ResultFunction{

        @Override
        public Object result(Object s, Action a) {
            if (s.equals(INITIAL_STATE_UKRAINE) && a.equals(goToGermany)) return INTERMEDIATE_STATE_GERMANY;
            if (s.equals(INITIAL_STATE_UKRAINE) && a.equals(goToItaly)) return INTERMEDIATE_STATE_ITALY;
            if (s.equals(INITIAL_STATE_UKRAINE) && a.equals(goToPoland)) return INTERMEDIATE_STATE_POLAND;

            if (s.equals(INTERMEDIATE_STATE_GERMANY) && a.equals(goToPoland)) return INTERMEDIATE_STATE_POLAND;
            if (s.equals(INTERMEDIATE_STATE_GERMANY) && a.equals(goToItaly)) return INTERMEDIATE_STATE_ITALY;
            if (s.equals(INTERMEDIATE_STATE_GERMANY) && a.equals(goToEngland)) return GOAL_STATE_ENGLAND;

            if (s.equals(INTERMEDIATE_STATE_ITALY) && a.equals(goToPoland)) return INTERMEDIATE_STATE_POLAND;
            if (s.equals(INTERMEDIATE_STATE_ITALY) && a.equals(goToGermany)) return INTERMEDIATE_STATE_GERMANY;
            if (s.equals(INTERMEDIATE_STATE_ITALY) && a.equals(goToEngland)) return GOAL_STATE_ENGLAND;

            if (s.equals(INTERMEDIATE_STATE_POLAND) && a.equals(goToGermany)) return INTERMEDIATE_STATE_GERMANY;
            if (s.equals(INTERMEDIATE_STATE_POLAND) && a.equals(goToItaly)) return INTERMEDIATE_STATE_ITALY;
            if (s.equals(INTERMEDIATE_STATE_POLAND) && a.equals(goToEngland)) return GOAL_STATE_ENGLAND;

            return null;
        }
    }

    //Implementations StepCostFunction
    private class TestStepCostFunction implements StepCostFunction{

        //set path cost for each action between 2 each states
        @Override
        public double c(Object s, Action a, Object sDelta) {
            if (s.equals(INITIAL_STATE_UKRAINE) && a.equals(goToGermany) && sDelta.equals(INTERMEDIATE_STATE_GERMANY)) return 5.0;
            if (s.equals(INITIAL_STATE_UKRAINE) && a.equals(goToItaly) && sDelta.equals(INTERMEDIATE_STATE_ITALY)) return 4.0;
            if (s.equals(INITIAL_STATE_UKRAINE) && a.equals(goToPoland) && sDelta.equals(INTERMEDIATE_STATE_POLAND) ) return 2.0;

            if (s.equals(INTERMEDIATE_STATE_GERMANY) && a.equals(goToPoland) && sDelta.equals(INTERMEDIATE_STATE_POLAND)) return 1.0;
            if (s.equals(INTERMEDIATE_STATE_GERMANY) && a.equals(goToItaly) && sDelta.equals(INTERMEDIATE_STATE_ITALY)) return 2.0;
            if (s.equals(INTERMEDIATE_STATE_GERMANY) && a.equals(goToEngland) && sDelta.equals(GOAL_STATE_ENGLAND)) return 1.0;

            if (s.equals(INTERMEDIATE_STATE_ITALY) && a.equals(goToPoland) && sDelta.equals(INTERMEDIATE_STATE_POLAND)) return 2.0;
            if (s.equals(INTERMEDIATE_STATE_ITALY) && a.equals(goToGermany) && sDelta.equals(INTERMEDIATE_STATE_GERMANY)) return 2.0;
            if (s.equals(INTERMEDIATE_STATE_ITALY) && a.equals(goToEngland) && sDelta.equals(GOAL_STATE_ENGLAND)) return 3.0;

            if (s.equals(INTERMEDIATE_STATE_POLAND) && a.equals(goToGermany) && sDelta.equals(INTERMEDIATE_STATE_GERMANY)) return 1.0;
            if (s.equals(INTERMEDIATE_STATE_POLAND) && a.equals(goToItaly) && sDelta.equals(INTERMEDIATE_STATE_ITALY)) return 3.0;
            if (s.equals(INTERMEDIATE_STATE_POLAND) && a.equals(goToEngland) && sDelta.equals(GOAL_STATE_ENGLAND)) return 5.0;

            return 0;
        }
    }

    @Before
    public void initialization(){
        angelicSearch = new AngelicSearch();

        //Implementations GoalTest
        goalTest = new GoalTest() {
            //initialization goal
            private Object goalState = GOAL_STATE_ENGLAND;

            @Override
            public boolean isGoalState(Object state) {
                return this.goalState.equals(state);
            }

            public Object getGoalState(){
                return goalState;
            }
        };

        //initialization of actions
        goToItaly = new TestAction("goToItaly");
        goToGermany = new TestAction("goToGermany");
        goToPoland = new TestAction("goToPoland");
        goToEngland = new TestAction("goToEngland");

        //initialization of available actions
        List<Object> states = new ArrayList<>();
        states.add(INITIAL_STATE_UKRAINE);
        states.add(INTERMEDIATE_STATE_GERMANY);
        states.add(INTERMEDIATE_STATE_ITALY);
        states.add(INTERMEDIATE_STATE_POLAND);
        states.add(GOAL_STATE_ENGLAND);

        Set<Action> actionsFromUkraine = new HashSet<>();
        actionsFromUkraine.add(goToItaly);
        actionsFromUkraine.add(goToGermany);
        actionsFromUkraine.add(goToPoland);

        Set<Action> actionsFromItaly = new HashSet<>();
        actionsFromItaly.add(goToEngland);
        actionsFromItaly.add(goToGermany);
        actionsFromItaly.add(goToPoland);

        Set<Action> actionsFromGermany = new HashSet<>();
        actionsFromGermany.add(goToEngland);
        actionsFromGermany.add(goToItaly);
        actionsFromGermany.add(goToPoland);

        Set<Action> actionsFromPoland = new HashSet<>();
        actionsFromPoland.add(goToEngland);
        actionsFromPoland.add(goToItaly);
        actionsFromPoland.add(goToGermany);

        testActionFunction = new TestActionFunction(actionsFromGermany,actionsFromItaly,
                                                                    actionsFromPoland,actionsFromUkraine);

        resultFunction = new TestResultFunction();
        stepCostFunction = new TestStepCostFunction();
    }

    @Test
    public void searchTest(){
        Problem problem = new Problem(INITIAL_STATE_UKRAINE, testActionFunction, resultFunction, goalTest, stepCostFunction);

        Queue<Node> frontier = new LIFOQueue<>();
        List<Action> goalActions = new ArrayList<>();
        goalActions.add(goToPoland);
        goalActions.add(goToGermany);
        goalActions.add(goToEngland);

        List<Action> resultActions = angelicSearch.search(problem,frontier);

        Assert.assertEquals(resultActions,goalActions);
    }

    @Test
    public void initialStateIsNullTest(){
        problem = new Problem(null, null, null, null);

        Assert.assertEquals(Collections.emptyList(), angelicSearch.search(problem,null));
    }

    @Test
    public void initialStateIsGoalState(){
        Queue<Node> frontier = new LIFOQueue<>();
        goalTest = new GoalTest() {

            private Object initialState = INITIAL_STATE_UKRAINE;

            @Override
            public boolean isGoalState(Object state) {
                return this.initialState.equals(state);
            }
        };

        problem = new Problem(INITIAL_STATE_UKRAINE,null,null, goalTest);
        List<Action> listOfActions = angelicSearch.search(problem, frontier);
        Assert.assertEquals(listOfActions.size(),1);
    }
}
