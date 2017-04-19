package aima.core.logic.fol.inference.graphplan;

/**
 * Node for planning graph that represents an action.
 * @author Matt Grenander
 */
public class PlanGraphAction implements GraphPlanNode {
    private int level;
    private PDDLAction action;

    PlanGraphAction(int level, PDDLAction action) {
        this.level = level;
        this.action = action;
    }

    public int getLevel() {
        return level;
    }

    public PDDLAction getAction() {
        return action;
    }

    public boolean equals(PlanGraphAction o) {
        return this.level == o.level && this.action == o.action;
    }

    @Override
    public String toString() {
        return "Level=" + level + ", Action=" + action.getName() + ". ";
    }
}
