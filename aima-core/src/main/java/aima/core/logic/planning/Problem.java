package aima.core.logic.planning;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Problem {
    State initialState;
    Set<ActionSchema> actionSchemas;
    State goalState;

    public Problem(State initialState, State goalState, Set<ActionSchema> actionSchemas) {
        this.initialState = initialState;
        this.actionSchemas = actionSchemas;
        this.goalState = goalState;
    }

    public Problem(State initialState, State goalState, ActionSchema... actions) {
        this(initialState, goalState, new HashSet<>(Arrays.asList(actions)));
    }

    public State getInitialState() {
        return initialState;
    }

    public Set<ActionSchema> getActionSchemas() {
        return actionSchemas;
    }

    public State getGoalState() {
        return goalState;
    }
}
