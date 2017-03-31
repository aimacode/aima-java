package aima.core.search.basic.uninformed;

import aima.core.search.api.SearchController;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.BidirectionalActions;
import aima.core.search.api.Node;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsBidirectionallyFunction;
import aima.core.search.basic.support.BasicBidirectionalActions;
import aima.core.search.basic.support.BasicFrontierQueue;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicSearchController;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <br>
 *
 * <pre>
 * The strategy of this search implementation is inspired by the description of
 * the bidirectional search algorithm i.e. Problem is reversed and search is performed from both
 * frontiers. The logic used in traversal is breadth first search.
 * @author manthan
 */
public class BidirectionalSearch<A, S> implements SearchForActionsBidirectionallyFunction<A, S> {
    private BidirectionalActions<A> bidirectionalActions;
    private List<A> fromInitialStatePartList = new ArrayList<>();
    private List<A> fromGoalStatePartList = new ArrayList<>();
    private Node<A, S> previousMeetingOfTwoFrontiers;
    private Node<A, S> meetingOfTwoFrontiers;
    private Node<A, S> nextNodeToBeEvaluated;
    private boolean fromFront;
    @Override

    public BidirectionalActions<A> apply(Problem<A, S> originalProblem, Problem<A, S> reverseProblem) {
        Node<A, S> node = newRootNode(originalProblem.initialState(), 0); //node -> starting point 
        //if starting node is the goal node then return bidirectionalAction 
        //which is the solution of the problem (total cost -> zero)
        //First terminating condition
        if (originalProblem.isGoalState(node.state())) {
            this.previousMeetingOfTwoFrontiers = node;
            this.nextNodeToBeEvaluated = null;
            bidirectionalActions = new BasicBidirectionalActions<>(fromInitialStatePart(), fromGoalStatePart());
            return bidirectionalActions;
        }
        
        Node<A, S> revNode = newRootNode(reverseProblem.initialState(), 0);//revNode -> goal node
        //front -> priority queue which has node to be explored when traversing from starting node
        Queue<Node<A, S>> front = newFIFOQueue(node);
        //back -> priority queue which has node to be explored when traversing from goal node
        Queue<Node<A, S>> back = newFIFOQueue(revNode);
        //exploredFront -> map which has explored nodes marked when traversing from starting node 
        Map<S, Node<A, S>> exploredFront = newExploredMap(node.state());
        //exploredBack -> map which has explored nodes marked when traversing from goal node
        Map<S, Node<A, S>> exploredBack = newExploredMap(revNode.state());

        //while both the priority queue (i.e. front and back) are filled then that implies
        //there is a path from both the end hence we traverse from both the end till we meet in the middle
        //otherwise there is no solution
        
        //a loop is run till solution is found or 
        //failure is reported
        while (!front.isEmpty() && !back.isEmpty()) {
        	
            // Existence of path is checked from both ends of the problem.
        	
            if (isSolution(pathExistsBidirectional(front, exploredFront, exploredBack, originalProblem), true)) {
                bidirectionalActions = new BasicBidirectionalActions<>(fromInitialStatePart(), fromGoalStatePart());
                return bidirectionalActions;
            }
            if (isSolution(pathExistsBidirectional(back, exploredBack, exploredFront, reverseProblem), false)) {
                bidirectionalActions = new BasicBidirectionalActions<>(fromInitialStatePart(), fromGoalStatePart());
                return bidirectionalActions;
            }
        }
        //when anyone of the priority queue gets empty
        //return failure (i.e. no path found)
        this.previousMeetingOfTwoFrontiers = null;
        bidirectionalActions = new BasicBidirectionalActions<>(fromInitialStatePart(), fromGoalStatePart());
        return bidirectionalActions;
    }
    
    //checking whether there is a path from the given side
    private Node<A, S> pathExistsBidirectional(Queue<Node<A, S>> queue, Map<S, Node<A, S>> exploredFront, Map<S, Node<A, S>> exploredBack, Problem<A, S> problem) {
        //queue -> any one of the priority queue (i.e. front or back)
    	//exploredFront -> map which has explored nodes marked when traversing from starting node
    	//exploredBack -> map which has explored nodes marked when traversing from goal node
    	
    	//problem contains information about the 
    	//initial state, action that is to be performed, goal test, path cost and result function
    	//which gives the result of the action 
    	
    	//if queue is not empty then there is a node to be explored
    	if (!queue.isEmpty()) {
    	    //exploring the first node in the queue
    	    //next -> first node from queue
            Node<A, S> next = queue.remove();
            //performing the action on next
            //till its all child has not been explored
            for (A action : problem.actions(next.state())) {
            	//checking every child of next whether it can be used in the path or not
                Node<A, S> child = newChildNode(problem, next, action);
                //if child node is in exploredBack map
                if (exploredBack.containsKey(child.state())) {
                    //child node is found in exploredBack
                    //meeting point of two frontiers
                    this.previousMeetingOfTwoFrontiers = next;
                    this.nextNodeToBeEvaluated = exploredBack.get(child.state());
                    return child;
                }
                //if child node is not contained in any one of the map and queue
            	//then perform following action
                if (!(exploredFront.containsKey(child.state()) || queue.contains(child.state()))) {
                	
                    //check if it is a goal state
                    if (problem.isGoalState(child.state())) {
                    	//setting meeting point -> next node
                        this.previousMeetingOfTwoFrontiers = next;
                        this.nextNodeToBeEvaluated = exploredBack.get(child.state());
                        return child;
                    }
                    
                    //every child is added into exploredFront who is not in it already
                    //this is to overcome the self loop
                    exploredFront.put(child.state(), next);
                    //child node is added in queue so that 
                    //action can be performed on its every child 
                    queue.add(child);
                }
            }
        }
    	//if queue is empty return null (i.e. failure)
        this.previousMeetingOfTwoFrontiers = null;
        return null;
    }

    private boolean isSolution(Node<A, S> solutionNode, boolean fromFront) {
    	//solutionNode is a node which is checked whether it is a meeting of two frontiers or not
    	//if solutionNode is not null 
        if (solutionNode != null) {
            //then it is the meeting of the two frontiers
            this.meetingOfTwoFrontiers = solutionNode;
            this.fromFront = fromFront;
            return true;
        }
        //else it is not a meeting point
        return false;
    }
    
    //Supporting Code
    protected NodeFactory<A, S> nodeFactory = new BasicNodeFactory<>();
    protected SearchController<A, S> searchController = new BasicSearchController<>();

    public Node<A, S> newRootNode(S initialState, double pathCost) {
        return nodeFactory.newRootNode(initialState, pathCost);
    }

    public Node<A, S> newChildNode(Problem<A, S> problem, Node<A, S> node, A action) {
        return nodeFactory.newChildNode(problem, node, action);
    }

    private Queue<Node<A, S>> newFIFOQueue(Node<A, S> initialNode) {
        Queue<Node<A, S>> frontier = new BasicFrontierQueue<>();
        frontier.add(initialNode);
        return frontier;
    }

    private Map<S, Node<A, S>> newExploredMap(S state) {
        Map<S, Node<A, S>> exploredMap = new HashMap<>();
        exploredMap.put(state, null);
        return exploredMap;
    }

    public List<A> solution(Node<A, S> node) {
        return searchController.solution(node);
    }

    public List<A> failure() {
        return searchController.failure();
    }

    public BidirectionalSearch() {

    }

    public Node<A, S> getMeetingOfTwoFrontiers() {
        return this.meetingOfTwoFrontiers;
    }

    private List<A> fromInitialStatePart() {
        if (this.previousMeetingOfTwoFrontiers == null || this.nextNodeToBeEvaluated == null)
            return failure();
        if (this.fromFront) {
            fromInitialStatePartList = searchController.solution(this.previousMeetingOfTwoFrontiers);
            fromInitialStatePartList.add(this.meetingOfTwoFrontiers.action());
            return fromInitialStatePartList;
        } else {
            fromInitialStatePartList = searchController.solution(this.nextNodeToBeEvaluated);
            return fromInitialStatePartList;
        }
    }

    private List<A> fromGoalStatePart() {
        if (this.previousMeetingOfTwoFrontiers == null || this.nextNodeToBeEvaluated == null)
            return failure();
        if (!this.fromFront) {
            fromGoalStatePartList = searchController.solution(this.previousMeetingOfTwoFrontiers);
            fromGoalStatePartList.add(this.meetingOfTwoFrontiers.action());
            return fromGoalStatePartList;
        } else {
            fromGoalStatePartList = searchController.solution(this.nextNodeToBeEvaluated);
            return fromGoalStatePartList;
        }
    }
}
