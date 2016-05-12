package aima.core.search.basic.support;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;

/**
 * Basic implementation of the NodeFactory interface.
 *
 * @author Ciaran O'Reilly
 */
public class BasicNodeFactory<A, S> implements NodeFactory<A, S>  {
	@Override
    public Node<A, S> newRootNode(S state) {
        return newRootNode(state, 0);
    }

	@Override
    public Node<A, S> newRootNode(S state, double pathCost) {
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
	@Override
    public Node<A, S> newChildNode(Problem<A, S> problem, Node<A, S> parent, A action) {
        S sPrime = problem.result(parent.state(), action);
        return new BasicNode<>(sPrime, parent, action, parent.pathCost()+problem.stepCost(parent.state(), action, sPrime));
    }
}
