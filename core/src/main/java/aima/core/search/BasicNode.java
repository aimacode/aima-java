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


    public static <S> Node<S> rootNode(S state, double pathCost) {
       return new BasicNode<>(state, pathCost);
    }

    public static <S> Node<S> childNode(Problem<S> problem, Node<S> parent, Action action) {
        S sPrime = problem.result(parent.state(), action);
        return new BasicNode<>(sPrime, parent, action, parent.pathCost()+problem.stepCost(parent.state(), action, sPrime));
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

    private BasicNode(S state, double pathCost) {
        this(state, null, Action.NoOp, pathCost);
    }

    private BasicNode(S state, Node<S> parent, Action action, double pathCost) {
        this.state    = state;
        this.parent   = parent;
        this.action   = action;
        this.pathCost = pathCost;
    }
}
