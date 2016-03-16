package aima.core.search.framework;

import aima.core.agent.Action;
import aima.core.util.CancelableThread;
import aima.core.util.datastructure.Queue;

import java.util.Collections;
import java.util.List;

/**
 * Created by Lobster on 16.03.16.
 */

public class AngelicSearch extends NodeExpander{

    private Queue<Node> frontier = null;

    public List<Action> search(Problem problem, Queue<Node> frontier) {
        if (problem.getInitialState()==null) failure();

        Node root = new Node(problem.getInitialState());
        if (SearchUtils.isGoalState(problem, root)) {
            return SearchUtils.actionsFromNodes(root.getPathFromRoot());
        }

        this.frontier = frontier;
        Node initialPlan;

        if (frontier.isEmpty()){
            initialPlan = root;
        } else {
            initialPlan = popNodeFromFrontier();
        }

        if (!(frontier.isEmpty()) && !CancelableThread.currIsCanceled()) {
            Node nodeToExpand = initialPlan;

            List<Node> childNodes = expandNode(nodeToExpand, problem);
            Node bestChildNode = minCostWay(childNodes);
            if (SearchUtils.isGoalState(problem, bestChildNode)){
                return SearchUtils.actionsFromNodes(bestChildNode.getPathFromRoot());
            }

            frontier.insert(bestChildNode);
            return search(problem, frontier);

        }

        return failure();
    }

    public Node popNodeFromFrontier() {
        return frontier.pop();
    }

    public Node minCostWay(List<Node> childNodes){
        boolean firstNodeflag = false;
        Node childNode = null;
        for (Node n: childNodes){
            if (!firstNodeflag) {
                childNode = n;
                firstNodeflag=true;
            } else {
                if(n.getPathCost()<childNode.getPathCost()){
                    childNode = n;
                }
            }
        }

        return childNode;
    }

    private List<Action> failure() {
        return Collections.emptyList();
    }
}
