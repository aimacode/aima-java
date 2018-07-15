package aima.core.search.basic.uninformedsearch;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.basic.SearchUtils;
import aima.core.search.basic.support.BasicNodeFactory;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class BreadthFirstSearch<A,S> implements GenericSearchInterface<A,S> {
    NodeFactory<A,S> nodeFactory = new BasicNodeFactory();

    @Override
    public Node<A,S> search(Problem<A,S> problem) {
        if (problem.isGoalState(problem.initialState())){
            return nodeFactory.newRootNode(problem.initialState());
        }
        Queue<Node> frontier = new LinkedList<>();
        ((LinkedList<Node>) frontier).add(nodeFactory.newRootNode(problem.initialState()));
        HashSet<S> reached = new HashSet<>();
        Node<A,S> solution = null;
        while (!frontier.isEmpty()){
            Node<A,S> parent = frontier.remove();
            for (Node<A,S> child: SearchUtils.successors(problem,parent)){
                S s = child.state();
                if (problem.isGoalState(s)){
                    return child;
                }
                if (!reached.contains(s)){
                    reached.add(s);
                    frontier.add(child);
                }
            }
        }
        return solution;
    }
}
