package aima.core.search.basic.uninformedsearch;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.basic.SearchUtils;
import aima.core.search.basic.support.BasicNodeFactory;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class UniformCostSearch<A,S> implements GenericSearchInterface<A,S> {

    PriorityQueue<Node<A,S>> frontier = new PriorityQueue<>(new Comparator<Node<A, S>>() {
        @Override
        public int compare(Node<A, S> o1, Node<A, S> o2) {
            return (int)(o1.pathCost()-o2.pathCost());
        }
    });

    NodeFactory<A,S> nodeFactory = new BasicNodeFactory<>();

    @Override
    public Node<A, S> search(Problem<A, S> problem) {
        if (problem.isGoalState(problem.initialState())){
            return nodeFactory.newRootNode(problem.initialState());
        }
        frontier.clear();
        frontier.add(nodeFactory.newRootNode(problem.initialState()));
        HashMap<S, Node<A, S>> reached = new HashMap<>();
        Node<A,S> solution = null;
        while (!frontier.isEmpty() &&
                (solution==null || frontier.peek().pathCost()<solution.pathCost())){
            Node<A,S> parent = frontier.poll();
            for (Node<A, S> child :
                    SearchUtils.successors(problem, parent)) {
                S s = child.state();
                if (!reached.containsKey(s) ||
                        child.pathCost()<reached.get(s).pathCost()){
                    reached.put(s,child);
                    frontier.add(child);
                    if (problem.isGoalState(s) && (solution==null || child.pathCost() <solution.pathCost())){
                        solution = child;
                    }
                }
            }
        }
        return solution;
    }
}
