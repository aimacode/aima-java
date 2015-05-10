package aima.core.search;

import aima.core.api.agent.Action;
import aima.core.api.search.Node;
import aima.core.api.search.Problem;

/**
 * Basic implementation of the Node interface.
 *
 * @author Ciaran O'Reilly
 */
public class BasicNode<S> implements Node<S> {
    private S state;
    private Node<S> parent;
    private Action action;
    private double pathCost;


    public static <S> Node<S> rootNode(S state) {
        return rootNode(state, 0);
    }

    public static <S> Node<S> rootNode(S state, double pathCost) {
       return new BasicNode<>(state, pathCost);
    }

    /**
     * Take a parent node and an action and return the resulting child node.
     * @param problem
     *        the problem for which the child node is to be instantiated, contains
     *        the function RESULT(s, a) that returns the state that results from doing
     *        action a in state s.
     * @param parent
     *        the parent node of the child.
     * @param action
     *        the action taken in the parent node, which results in the child node.
     * @param <S>
     *        the type of the state that node contains.
     * @return the resulting child node.
     */
    public static <S> Node<S> childNode(Problem<S> problem, Node<S> parent, Action action) {
        S sPrime = problem.result(parent.state(), action);
        return new BasicNode<>(sPrime, parent, action, parent.pathCost()+problem.stepCost(parent.state(), action, sPrime));
    }

    public static int depth(Node n) {
        int level = 0;
        while ((n = n.parent()) != null) {
            level++;
        }
        return level;
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
