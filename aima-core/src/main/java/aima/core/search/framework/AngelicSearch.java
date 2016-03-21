package aima.core.search.framework;

import aima.core.agent.Action;
import aima.core.util.CancelableThread;
import aima.core.util.datastructure.Queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Lobster
 */

public class AngelicSearch extends NodeExpander{

    private Queue<Node> frontier = null;

    /**
     * Returns a list of actions to the goal if the goal was found, a list
     * containing a single NoOp Action if already at the goal, or an empty list
     * if the goal could not be found.
     *
     * @param problem
     *            the search problem
     * @param frontier
     *            the collection of nodes that are waiting to be expanded
     *
     * @return a list of actions to the goal if the goal was found, a list
     *         containing a single NoOp Action if already at the goal, or an
     *         empty list if the goal could not be found.
     */
    public List<Action> search(Problem problem, Queue<Node> frontier) {
        //check that initial state is not null
        if (problem.getInitialState()==null) return failure();

        //now we can get root
        Node root = new Node(problem.getInitialState());
        Node initialPlan;

        this.frontier = frontier;

        //if this is first call of search method we have to push root in the frontier and initialize initial plan
        if (frontier.isEmpty()){
            clearInstrumentation();
            frontier.insert(root);
            initialPlan = root;
        } else {
            initialPlan = popNodeFromFrontier();
        }

        //check that initial plan is goal
        if (SearchUtils.isGoalState(problem, initialPlan)) {
            return SearchUtils.actionsFromNodes(root.getPathFromRoot());
        }

        if (!(frontier.isEmpty()) && !CancelableThread.currIsCanceled()) {
            Node nodeToExpand = initialPlan;

            //find nodes which can be reached from current node
            List<Node> childNodes = expandNode(nodeToExpand, problem);
            //reduce list of nodes, delete nodes which are contained in the childNodes
            reduceChildNodes(initialPlan, childNodes);
            //find the closest node which can be reached
            Node bestChildNode = minCostWay(childNodes);
            //check that goal is reached
            if (SearchUtils.isGoalState(problem, bestChildNode)){
                return SearchUtils.actionsFromNodes(bestChildNode.getPathFromRoot());
            }

            //if goal is not reached, push new node into frontier
            frontier.insert(bestChildNode);
            //continue process of search
            return search(problem, frontier);
        }

        return failure();
    }

    /**
     * Removes and returns the node at the head of the frontier.
     *
     * @return the node at the head of the frontier.
     */
    public Node popNodeFromFrontier() {
        return frontier.pop();
    }

    /**
     * Finds and return tha node which has lowest cost.
     *
     * @return the node which has lowest cost.
     */
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

    /**
     * Checks which nodes are contained in the current node path and deletes them
     * to avoid loop into search method.
     *
     * @return void.
     */
    public void reduceChildNodes(Node initialPlan, List<Node> childNodes){
        List<Object> allStates = new ArrayList<>();
        for (Node n: initialPlan.getPathFromRoot()){
            allStates.add(n.getState());
        }

        Iterator<Node> iterator = childNodes.iterator();
        Node n;
        while (iterator.hasNext()){
            n = iterator.next();
            if (allStates.contains(n.getState())){
                iterator.remove();
            }
        }
    }

    private List<Action> failure() {
        return Collections.emptyList();
    }
}
