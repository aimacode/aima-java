package aima.core.logic.fol.inference.graphplan;

import aima.core.logic.fol.kb.data.CNF;

import java.util.List;

/**
 * Describes a PDDL problem in classical planning
 * @author Matt Grenander
 */
public class PDDL {
    private List<PDDLAction> actionSchema;
    private CNF init;
    private CNF goal;

    public PDDL(List<PDDLAction> actions, CNF init, CNF goal) {
        this.actionSchema = actions;
        this.init = init;
        this.goal = goal;
    }

    public CNF getInit() {
        return init;
    }

    public CNF getGoal() {
        return goal;
    }

    public List<PDDLAction> getActions() {
        return actionSchema;
    }
}
