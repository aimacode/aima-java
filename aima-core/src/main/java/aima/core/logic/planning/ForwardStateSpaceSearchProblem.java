package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.search.framework.problem.GeneralProblem;
import aima.core.search.framework.problem.StepCostFunction;

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
    protected PlanningProblem pProblem;
    protected List<ActionSchema> propositionalisedActions;

    public ForwardStateSpaceSearchProblem(PlanningProblem pProblem) {
        this(pProblem, pProblem.getPropositionalisedActions(), (s, a, sp) -> 1);
    }

    public ForwardStateSpaceSearchProblem(PlanningProblem pProblem, List<ActionSchema> propositionalisedActions,
                                          StepCostFunction<List<Literal>, ActionSchema> stepCostFn) {
        this.pProblem = pProblem;
        this.propositionalisedActions = propositionalisedActions;

        initialState = pProblem.getInitialState().getFluents();
        actionsFn = this::actions;
        resultFn = this::result;
        goalTest = this::goalTest;
        this.stepCostFn = stepCostFn;
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
        }
        return result;
    }

    List<Literal> result(List<Literal> state, ActionSchema action) {
        List<Literal> result = new ArrayList<>(state);
        for (Literal literal : action.getEffect()) {
            if (literal.isNegativeLiteral())
                result.remove(literal.getComplementaryLiteral());
            else if (!result.contains(literal))
                result.add(literal);
        }
        result.sort(Comparator.comparing(Literal::toString));
        return result;
    }

    boolean goalTest(List<Literal> state) {
        for (Literal literal : pProblem.getGoal())
            if (literal.isPositiveLiteral()
                    ? !state.contains(literal)
                    : state.contains(literal.getComplementaryLiteral()))
                return false;
        return true;
    }
}
