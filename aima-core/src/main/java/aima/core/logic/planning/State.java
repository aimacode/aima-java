package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page
 * 367.<br>
 * <br>
 * <p>
 * Each state is represented as a conjunction of fluents that are ground, functionless atoms.
 * For example, Poor ∧ Unknown might represent the state of a hapless agent, and a state
 * in a package delivery problem might be At(Truck1 , Melbourne) ∧ At(Truck2 , Sydney).
 *
 * @author samagra
 * @author Ruediger Lunde
 */
public class State {
    List<Literal> fluents;
    int hashCode = 0;

    public State(List<Literal> fluents) {
        this.fluents = fluents;
        fluents.sort(Comparator.comparing(Literal::toString));
    }

    public State(String fluents) {
        this(Utils.parse(fluents));
    }

    /**
     * Returns the state obtained by the application of an applicable action to
     * the current state.
     * <p>
     * The result of executing action a in state s is defined as a state s which is represented
     * by the set of fluents formed by starting with s, removing the fluents that appear as negative
     * literals in the action’s effects (what we call the delete list or DEL(a)), and adding the fluents
     * that are positive literals in the action’s effects (what we call the add list or ADD(a)):
     * RESULT(s, a) = (s − DEL(a)) ∪ ADD(a).
     *
     * @param action The applicable action.
     * @return The new state.
     */
    public State result(ActionSchema action) {
        if (isApplicable(action)) {
            List<Literal> result = new ArrayList<>(fluents);
            for (Literal literal : action.getEffect()) {
                if (literal.isNegativeLiteral())
                    result.remove(literal.getComplementaryLiteral());
                else if (!result.contains(literal))
                    result.add(literal);
            }
            result.sort(Comparator.comparing(Literal::toString));
            return new State(result);
        }
        return this;
    }

    /**
     * Returns the state obtained by the application of a list of applicable actions to
     * the current state. This method does not change the original state and in fact returns
     * a new state representing the changed state.
     */
    public State result(List<ActionSchema> actions) {
        State state = this;
        for (ActionSchema action : actions)
            state = state.result(action);
        return state;
    }

    /**
     * Checks if the action is applicable in a state.
     * <p>
     * We say that action a is applicable in state s if the preconditions are satisfied by s.
     *
     * @param action an action
     * @return a boolean stating if the action is applicable.
     */
    public boolean isApplicable(ActionSchema action) {
        for (Literal literal : action.getPrecondition()) {
            if (literal.isPositiveLiteral()
                    ? !fluents.contains(literal)
                    : fluents.contains(literal.getComplementaryLiteral()))
                return false;
        }
        return true;
    }

    public List<Literal> getFluents() {
        return fluents;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass())
            return false;
        return fluents.equals(((State) obj).getFluents());
    }

    @Override
    public int hashCode() {
        hashCode = 17;
        for (Literal fluent : getFluents()) {
            hashCode = 37 * hashCode + fluent.hashCode();
        }
        return hashCode;
    }
}
