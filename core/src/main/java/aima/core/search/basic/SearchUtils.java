package aima.core.search.basic;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.basic.support.BasicNodeFactory;

import java.util.ArrayList;
import java.util.List;

public class SearchUtils {
    public static <A,S> List<Node<A,S>> successors(Problem<A,S> problem, Node<A,S> parent ){
        S s = parent.state();
        List<Node<A,S>> nodes = new ArrayList<>();

        NodeFactory<A,S> nodeFactory = new BasicNodeFactory<>();
        for (A action :
                problem.actions(s)) {
            S sPrime = problem.result(s,action);
            double cost = parent.pathCost() + problem.stepCost(s,action,sPrime);
            Node<A,S> node = nodeFactory.newChildNode(problem,parent,action);
            nodes.add(node);
        }
        return nodes;
    }

    public static int depth(Node node){
        Node temp = node;
        int count = 0;
        while(temp!=null){
            count ++;
            temp = temp.parent();
        }
        return count;
    }
}
