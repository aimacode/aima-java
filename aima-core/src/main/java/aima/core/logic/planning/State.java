package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;

import java.util.ArrayList;
import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page
 * 367.<br>
 * <br>
 * <p>
 * Each state is represented as a conjunction of fluents that are ground, functionless atoms.
 * For example, Poor ∧ Unknown might represent the state of a hapless agent, and a state
 * in a package delivery problem might be At(Truck 1 , Melbourne) ∧ At(Truck 2 , Sydney).
 *
 * @author samagra
 */
public class State {
    List<Literal> fluents;
    int hashCode = 0;

    public State(List<Literal> fluents) {
        this.fluents = fluents;
    }

    public State(String fluents) {
        this(Utils.parse(fluents));
    }

    /**
     * Returns the state obtained by the application of an applicable action to
     * the current state.
     * <p>
     * The result of executing action a in state s is defined as a state s  which is represented
     * by the set of fluents formed by starting with s, removing the fluents that appear as negative
     * literals in the action’s effects (what we call the delete list or D EL (a)), and adding the fluents
     * that are positive literals in the action’s effects (what we call the add list or A DD (a)):
     * RESULT (s, a) = (s − DEL (a)) ∪ ADD (a) .
     *
     * @param a The applicable action.
     * @return The new state.
     */
    public State result(ActionSchema a) {
        if (this.isApplicable(a)) {
            for (Literal fluent :
                    a.getEffectsNegativeLiterals()) {
                Literal tempFluent = new Literal(fluent.getAtomicSentence());
                this.fluents.remove(tempFluent);
            }
            this.fluents.addAll(a.getEffectsPositiveLiterals());
        }
        return this;
    }

    /**
     * Returns the state obtained by the application of a list of applicable actions to
     * the current state. This method does not change the original state and in fact returns
     * a new state representing the changed state.
     *
     * @param actions
     * @return
     */
    public State result(List<ActionSchema> actions) {
        State resultState = new State(new ArrayList<>(this.getFluents()));
        for (ActionSchema action :
                actions) {
            resultState = resultState.result(action);
        }
        return resultState;
    }


    /**
     * Checks if the action is applicable in a state.
     * <p>
     * We say that action a is applicable in state s if the preconditions are satisfied by s.
     *
     * @param a an action
     * @return a boolean stating if the action is applicable.
     */
    public boolean isApplicable(ActionSchema a) {
        return this.getFluents().containsAll(a.getPrecondition());
    }

    public List<Literal> getFluents() {
        return fluents;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof State))
            return false;
        return this.fluents.containsAll(((State) obj).getFluents())
                && ((State) obj).getFluents().containsAll(this.getFluents());
    }

    @Override
    public int hashCode() {
        hashCode = 17;
        for (Literal fluent :
                getFluents()) {
            hashCode = 37 * hashCode + fluent.hashCode();
        }
        return hashCode;
    }
}
