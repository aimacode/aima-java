package aima.core.search.basic.uninformedsearch;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.basic.SearchUtils;
import aima.core.search.basic.support.BasicNodeFactory;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class DepthLimitedSearch<A,S> {
    NodeFactory<A,S> nodeFactory = new BasicNodeFactory<>();

    public Node<A,S> search(Problem<A,S> problem, int l){
        Stack<Node<A,S>> frontier = new Stack<>();
        frontier.push(nodeFactory.newRootNode(problem.initialState()));
        Node<A,S> solution = null;
        while(!frontier.isEmpty()){
            Node<A,S> parent = frontier.pop();
            if(SearchUtils.depth(parent)>l){
                solution = null;
            }
            else {
                for (Node<A, S> child :
                        SearchUtils.successors(problem, parent)) {
                    if (problem.isGoalState(child.state())){
                        return child;
                    }
                    frontier.push(child);
                }
            }
        }
        return solution;
    }
}
