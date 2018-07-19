package aima.core.search.basic;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.basic.support.BasicNodeFactory;

import java.util.ArrayList;
import java.util.Collections;
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

    public static <A,S> List<A> generateActions(Node<A,S> solution){
        Node<A,S> parent = solution;
        List<A> actions = new ArrayList<>();
        while(parent.parent() != null){
            actions.add(parent.action());
            parent = parent.parent();
        }
        Collections.reverse(actions);
        return actions;
    }
}
