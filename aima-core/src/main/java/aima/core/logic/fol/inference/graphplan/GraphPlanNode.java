package aima.core.logic.fol.inference.graphplan;

/**
 * A node in a planning graph. Can either be an action node or a state node.
 * @author Matt Grenander
 */
public interface GraphPlanNode {
    int getLevel();
}
