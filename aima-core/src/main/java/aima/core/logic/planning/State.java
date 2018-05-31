package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.planning.angelicsearch.AngelicHLA;

import java.util.ArrayList;
import java.util.HashSet;
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

    public boolean isApplicable(AngelicHLA angelicHLA) {
        return this.getFluents().containsAll(angelicHLA.getPrecondition());
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

    public HashSet<State> optimisticReach(AngelicHLA angelicAction) {
        HashSet<State> result = new HashSet<>();
        State thisStateCopy = new State(new ArrayList<>(this.getFluents()));
        if (!this.isApplicable(angelicAction)) {
            result.add(new State(new ArrayList<>(this.getFluents())));
            return result;
        } else {
            for (Literal fluent :
                    angelicAction.getEffectsNegativeLiterals()) {
                Literal tempFluent = new Literal(fluent.getAtomicSentence());
                thisStateCopy.fluents.remove(tempFluent);
                tempFluent = new Literal(fluent.getAtomicSentence(), true);
                if (!thisStateCopy.fluents.contains(tempFluent))
                    thisStateCopy.fluents.add(tempFluent);
            }
            for (Literal fluent :
                    angelicAction.getEffectsPositiveLiterals()) {
                Literal tempFluent = new Literal(fluent.getAtomicSentence(), true);
                tempFluent = new Literal(tempFluent.getAtomicSentence());
                if (!thisStateCopy.fluents.contains(tempFluent)) {
                    thisStateCopy.fluents.add(tempFluent);
                    tempFluent = new Literal(tempFluent.getAtomicSentence(), true);
                    thisStateCopy.fluents.remove(tempFluent);
                }
            }
            result.add(new State(new ArrayList<>(thisStateCopy.getFluents())));
            for (Literal literal :
                    angelicAction.getEffectsMaybePositiveLiterals()) {
                List<State> listToAdd = new ArrayList<>();
                for (State state :
                        result) {
                    State tempCopyState = new State(new ArrayList<>(state.getFluents()));
                    if (!tempCopyState.getFluents().contains(literal)) {
                        Literal tempFluent = new Literal(literal.getAtomicSentence(), true);
                        tempCopyState.fluents.remove(tempFluent);
                        tempCopyState.getFluents().add(literal);
                        listToAdd.add(new State(new ArrayList<>(tempCopyState.getFluents())));
                    }
                }
                result.addAll(listToAdd);
            }
            for (Literal literal :
                    angelicAction.getEffectsMaybeNegativeLiterals()) {
                List<State> listToAdd = new ArrayList<>();
                for (State state :
                        result) {
                    State tempCopyState = new State(new ArrayList<>(state.getFluents()));
                    Literal tempFluent = new Literal(literal.getAtomicSentence(), true);
                    if (!tempCopyState.getFluents().contains(tempFluent)) {
                        tempFluent = new Literal(tempFluent.getAtomicSentence());
                        tempCopyState.getFluents().remove(tempFluent);
                        tempFluent = new Literal(tempFluent.getAtomicSentence(), true);
                        tempCopyState.fluents.add(tempFluent);
                        listToAdd.add(new State(new ArrayList<>(tempCopyState.getFluents())));
                    }
                }
                result.addAll(listToAdd);
            }
        }
        return result;
    }

    public HashSet<State> pessimisticReach(AngelicHLA angelicAction) {
        HashSet<State> result = new HashSet<>();
        State thisStateCopy = new State(new ArrayList<>(this.getFluents()));
        if (!this.isApplicable(angelicAction)) {
            result.add(new State(new ArrayList<>(this.getFluents())));
            return result;
        } else {
            for (Literal fluent :
                    angelicAction.getEffectsNegativeLiterals()) {
                Literal tempFluent = new Literal(fluent.getAtomicSentence());
                thisStateCopy.fluents.remove(tempFluent);
                tempFluent = new Literal(fluent.getAtomicSentence(), true);
                if (!thisStateCopy.fluents.contains(tempFluent))
                    thisStateCopy.fluents.add(tempFluent);
            }
            for (Literal fluent :
                    angelicAction.getEffectsPositiveLiterals()) {
                Literal tempFluent = new Literal(fluent.getAtomicSentence(), true);
                tempFluent = new Literal(tempFluent.getAtomicSentence());
                if (!thisStateCopy.fluents.contains(tempFluent)) {
                    thisStateCopy.fluents.add(tempFluent);
                    tempFluent = new Literal(tempFluent.getAtomicSentence(), true);
                    thisStateCopy.fluents.remove(tempFluent);
                }
            }
            result.add(new State(new ArrayList<>(thisStateCopy.getFluents())));
            return result;
        }
    }

    public HashSet<State> optimisticReach(List<Object> plan) {
        HashSet<State> result = new HashSet<>();
        result.add(new State(this.getFluents()));
        if (plan.isEmpty()) {
            return result;
        }
        for (Object action :
                plan) {
            HashSet<State> currResult = new HashSet<>();
            if (action instanceof ActionSchema) {
                for (State state :
                        result) {
                        currResult.add(state.result((ActionSchema) action));
                }
            } else if (action instanceof AngelicHLA) {
                for (State state :
                        result) {
                        currResult.addAll(state.optimisticReach((AngelicHLA) action));
                }
            }
            result = currResult;
        }
        return result;
    }

    public HashSet<State> pessimisticReach(List<Object> plan){
        HashSet<State> result = new HashSet<>();
        result.add(new State(this.getFluents()));
        if (plan.isEmpty()) {
            return result;
        }
        for (Object action :
                plan) {
            HashSet<State> currResult = new HashSet<>();
            if (action instanceof ActionSchema) {
                for (State state :
                        result) {
                        currResult.add(state.result((ActionSchema) action));
                }
            } else if (action instanceof AngelicHLA) {
                for (State state :
                        result) {
                        currResult.addAll(state.pessimisticReach((AngelicHLA) action));
                }
            }
            result = currResult;
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("State:");
        for (Literal literal :
                this.getFluents()) {
            result.append("\n").append(literal.toString());
        }
        return result.toString();
    }
}
