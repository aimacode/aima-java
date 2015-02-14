package aima.core.search;

import aima.core.api.agent.Action;
import aima.core.api.search.Node;
import aima.core.api.search.Problem;

/**
 * @author Ciaran O'Reilly
 */
public class BasicNode<S> implements Node<S> {
    private S state;
    private Node<S> parent;
    private Action action;
    private double pathCost;

    public static <S> Node<S> rootNode(S state) {
       return new BasicNode(state);
    }

    public static <S> Node<S> childNode(Problem<S> problem, Node<S> parent, Action action) {
        S sPrime = problem.result(parent.state(), action);
        return new BasicNode(sPrime, parent, action, parent.pathCost()+problem.stepCost(parent.state(), action, sPrime));
    }

    @Override
    public S state() {
        return state;
    }

    @Override
    public Node<S> parent() {
        return parent;
    }

    @Override
    public Action action() {
        return action;
    }

    @Override
    public double pathCost() {
        return pathCost;
    }

    private BasicNode(S state) {
       this(state, null, Action.NoOp, 0.0);
    }

    private BasicNode(S state, Node<S> parent, Action action, double pathCost) {
        this.state    = state;
        this.parent   = parent;
        this.action   = action;
        this.pathCost = pathCost;
    }
}
