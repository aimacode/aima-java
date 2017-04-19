package aima.core.logic.fol.inference.graphplan;

import aima.core.agent.Action;
import aima.core.logic.fol.kb.data.CNF;

/**
 * Class that represents an action in a PDDL problem.
 * @author Matt Grenander
 */
public class PDDLAction implements Action {
    private String name;
    private CNF precond;
    private CNF effect;

    public PDDLAction(String name, CNF precond, CNF effect) {
        this.name = name;
        this.precond = precond;
        this.effect = effect;
    }

    //If the action is equal to the preconditions then this action is a NoOp
    public boolean isNoOp() {
        return precond.equals(effect);
    }

    public String getName() {
        return name;
    }

    public String toString() { return name; }

    public CNF getPrecond() {
        return precond;
    }

    public CNF getEffect() {
        return effect;
    }

    public boolean equals(PDDLAction o) {
        return this.name.equals(o.name) && this.precond.equals(o.precond) && this.effect.equals(o.effect);
    }
}
