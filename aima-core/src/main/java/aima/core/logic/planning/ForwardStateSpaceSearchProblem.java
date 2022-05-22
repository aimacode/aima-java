package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.search.framework.problem.GeneralProblem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 379.<br>
 * <p>
 * Forward (progression) search [explores] the space of states, starting in the initial state and using the
 * problem’s actions to search forward for a member of the set of goal states.
 *
 * @author Ruediger Lunde
 */
public class ForwardStateSpaceSearchProblem extends GeneralProblem<List<Literal>, ActionSchema> {
    PlanningProblem pProblem;
    List<ActionSchema> propositionalisedActions;

    public ForwardStateSpaceSearchProblem(PlanningProblem pProblem) {
        this.pProblem = pProblem;
        propositionalisedActions = pProblem.getPropositionalisedActions();

        initialState = pProblem.getInitialState().getFluents();
        actionsFn = this::actions;
        resultFn = this::result;
        goalTest = s -> s.containsAll(pProblem.getGoalState().getFluents());
        stepCostFn = (s, a, sp) -> 1;
    }

    List<ActionSchema> actions(List<Literal> state) {
        List<ActionSchema> result = new ArrayList<>();
        for (ActionSchema action : propositionalisedActions) {
            boolean applicable = true;
            for (Literal literal : action.getPrecondition()) {
                if (literal.isPositiveLiteral()
                        ? !state.contains(literal)
                        : state.contains(literal.getComplementaryLiteral())) {
                    applicable = false;
                    break;
                }
            }
            if (applicable)
                result.add(action);

            if (state.containsAll(action.getPrecondition())) {
                result.add(action);
            }
        }
        return result;
    }

    List<Literal> result(List<Literal> state, ActionSchema action) {
        List<Literal> result = new ArrayList<>(state);
        for (Literal literal : action.getEffect()) {
            if (literal.isPositiveLiteral()) {
                if (!result.contains(literal))
                    result.add(literal);
            } else {
                result.remove(literal.getComplementaryLiteral());
            }
        }
        result.sort(Comparator.comparing(Literal::toString));
        return result;
    }
}
