package aima.core.search;

import aima.core.api.search.Node;
import aima.core.api.search.Problem;

/**
 * Basic implementation of the Node interface.
 *
 * @author Ciaran O'Reilly
 */
public class BasicNode<A, S> implements Node<A, S> {
    private S state;
    private Node<A, S> parent;
    private A action;
    private double pathCost;


    public static <A, S> Node<A, S> rootNode(S state) {
        return rootNode(state, 0);
    }

    public static <A, S> Node<A, S> rootNode(S state, double pathCost) {
       return new BasicNode<>(state, pathCost);
    }

    /**
     * Take a parent node and an action and return the resulting child node.
     *
     * @param problem
     *        the problem for which the child node is to be instantiated, contains
     *        the function RESULT(s, a) that returns the state that results from doing
     *        action a in state s, as well as the step cost function for going from
     *        state s to s' using action a.
     * @param parent
     *        the parent node of the child.
     * @param action
     *        the action taken in the parent node, which results in the child node.
     * @param <A>
     *        the action type.        
     * @param <S>
     *        the type of the state that node contains.
     * @return the resulting child node.
     */
    public static <A, S> Node<A, S> childNode(Problem<A, S> problem, Node<A, S> parent, A action) {
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
    public Node<A, S> parent() {
        return parent;
    }

    @Override
    public A action() {
        return action;
    }

    @Override
    public double pathCost() {
        return pathCost;
    }

    private BasicNode(S state, double pathCost) {
        this(state, null, null, pathCost);
    }

    private BasicNode(S state, Node<A, S> parent, A action, double pathCost) {
        this.state    = state;
        this.parent   = parent;
        this.action   = action;
        this.pathCost = pathCost;
    }
}
