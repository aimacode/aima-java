package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.search.framework.problem.GeneralProblem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 380.<br>
 * <p>
 * In regression search we start at the goal and apply the actions backward until we find a
 * sequence of steps that reaches the initial state. It is called relevant-states search because we
 * only consider actions that are relevant to the goal (or current state).
 *
 * @author Ruediger Lunde
 */
public class BackwardStateSpaceSearchProblem extends GeneralProblem<List<Literal>, ActionSchema> {
    protected PlanningProblem pProblem;
    protected List<ActionSchema> propositionalisedActions;

    public BackwardStateSpaceSearchProblem(PlanningProblem pProblem) {
        this.pProblem = pProblem;
        propositionalisedActions = pProblem.getPropositionalisedActions();

        initialState = pProblem.getGoal();
        actionsFn = this::actions;
        resultFn = this::result;
        goalTest = this::goalTest;
        stepCostFn = (s, a, sp) -> 1;
    }

    List<ActionSchema> actions(List<Literal> state) {
        List<ActionSchema> result = new ArrayList<>();
        for (ActionSchema action : propositionalisedActions) {
            boolean relevant = false;
            for (Literal literal : action.getEffect()) {
                if (state.contains(literal)) {
                    relevant = true; // effect unifies with one of the goal literals
                } else if (state.contains(literal.getComplementaryLiteral())) {
                    relevant = false; // effect contradicts state specification
                    break;
                }
            }
            if (relevant) {
                for (Literal literal : action.getPrecondition()) {
                    Literal cLit = literal.getComplementaryLiteral();
                    if (state.contains(cLit) && !action.getEffect().contains(cLit)) {
                        relevant = false; // unchanged precondition contradicts state specification
                        break;
                    }
                }
            }
            if (relevant)
                result.add(action);
        }
        return result;
    }

    List<Literal> result(List<Literal> state, ActionSchema action) {
        List<Literal> result = new ArrayList<>(state);
        result.removeAll(action.getEffect());
        for (Literal literal : action.getPrecondition()) {
            if (!result.contains(literal))
                result.add(literal);
        }
        result.sort(Comparator.comparing(Literal::toString));
        return result;
    }

    boolean goalTest(List<Literal> state) {
        List<Literal> initialState = pProblem.getInitialState().getFluents();
        for (Literal literal : state) {
            if (literal.isPositiveLiteral()
                    ? !initialState.contains(literal)
                    : initialState.contains(literal.getComplementaryLiteral()))
                return false;
        }
        return true;
    }
}
