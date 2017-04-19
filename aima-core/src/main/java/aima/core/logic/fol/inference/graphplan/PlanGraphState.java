package aima.core.logic.fol.inference.graphplan;

import aima.core.logic.fol.kb.data.Literal;

/**
 * Node for planning graph that represents a state
 * @author Matt Grenander
 */
public class PlanGraphState implements GraphPlanNode {
    private int level;
    private Literal literal;

    PlanGraphState(int level, Literal literal) {
        this.level = level;
        this.literal = literal;
    }

    public int getLevel() {
        return level;
    }

    public Literal getLiteral() {
        return literal;
    }

    public boolean equals(PlanGraphState o) {
        if (o == null) {
            return false;
        }

        return (this.level == o.level && this.literal.equals(o.literal));
    }

    @Override
    public String toString() {
        return "Level=" + level + ", Literal=" + literal + ". ";
    }
}
