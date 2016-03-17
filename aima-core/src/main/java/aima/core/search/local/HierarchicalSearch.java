package aima.core.search.local;

import java.util.Collections;
import java.util.List;


import aima.core.agent.Action;

import aima.core.search.framework.Node;
import aima.core.search.framework.NodeExpander;
import aima.core.search.framework.Problem;
import aima.core.util.CancelableThread;
import aima.core.util.datastructure.FIFOQueue;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 11.5, page
 * 409.<br>
 * <br>
 *
 * <pre>
 * 
 * function HIERARCHICAL_SEARCH(problem,hierarchy) returns a solution or failure
 *                    
 *   frontier <- a FIFO queue with[Act] as the only element
 *   loop do
 *   	if EMPTY?(frontier)then return failure
 *   	plan<-POP(frontier) //chooses the shallowest plan in frontier
 *   	hla<-the first HLA in plan,or null if none
 *   	prefix,suffix <-action before and after hla in plan
 *   	outcome <- RESULT(problem.INITIAL-STATE,prefix)
 *   	if hla is null then //so plan is primitive
 *   		if outcome statisfies.problem.GOAL then return plan
 *   	else for each sequence in REFINEMENTS(hla,outcome,hierarchy)do
 *   		frontier <-INSERT(APPEND(prefix,sequence,suffix),frontier)
 *   
 *    
 * </pre>
 * 
 * Figure 11.5 A breadth-first implementation of hierarchical forward planning search.
 * The initial plan supplied to the algorithm is [Act]. The REFINEMENTS function 
 * returns a set of action sequences, one for each refinement of the HLA whose preconditions
 * are satisfied by the specified state, outcome.
 * 
 * @author Shubhankar Mohapatra
 *
 */
public class HierarchicalSearch extends NodeExpander {
	public enum SearchOutcome {
		FAILURE, SOLUTION_FOUND
	}
	
	private SearchOutcome outcome = SearchOutcome.FAILURE;
	/**
	 * Searches for a solution or failure in the problem and hierarchy.
	 * Uses refinement of the HLA to output a set of actions which has
	 * the same precondition as the outcome of the goal.
	 * @param problem
	 * 		Problem to the search
	 * @param frontier
	 * 		Hierarchy to the search
	 * @return
	 * 		A list of actions containing a solution or null if failure
	 * 
	 */
	public List<Action> search(Problem problem,FIFOQueue<Node> frontier) throws Exception {
		
		clearInstrumentation();
		outcome=SearchOutcome.FAILURE;
		
		Node root = new Node(problem.getInitialState());
		
		frontier.insert(root); //frontier <- a FIFO queue with[Act] as the only element
		//loop do
		while (!CancelableThread.currIsCanceled()) {
			if(frontier.isEmpty()) //if EMPTY?(frontier)then return failure
			{
				outcome=SearchOutcome.FAILURE;
				return failure();			
			}
				
			Node plan=frontier.pop();
			Action hla=plan.getAction();
			Action prefix=plan.getParent().getAction();
			Action suffix=plan.getAction();
			
			Object outcome=problem.getResultFunction().result(problem.getInitialState(), prefix);//if outcome statisfies.problem.GOAL then return plan
			if(hla.isNoOp())
			{
				
				if(problem.isGoalState(outcome))
				{
					outcome=SearchOutcome.SOLUTION_FOUND;
					return success(plan);
					
				}
				
				
			}
			else
			{
			List<Action> x= REFINEMENTS(plan,outcome,frontier);
			x=APPEND(prefix,x,suffix);
			for(Action sequence:x)
			{
				Node n=new Node(plan.getState(),plan.getParent(),sequence,plan.getPathCost());
				frontier.push(n);
			}
			}
		}
			
		return null;
			
		}

	/**
	 *  The REFINEMENTS function 
	 * returns a set of action sequences, one for each refinement of the HLA 
	 * whose preconditions are satisfied by the specified state, outcome.
	 *
	 * @param HLA
	 *         the node 
	 * @param outcome2
	 * 		   the state which has to be satisfies
	 * @param frontier
	 * 		   the FIFOQueue which contains all the nodes        
	 * 
	 * @return a list of actions which after implementing the HLA has the precondition
	 * 			outcome
	 *        
	 */
	public List<Action> REFINEMENTS(Node hla, Object outcome2,FIFOQueue<Node> frontier) {
		 

			
	List<Node> x=hla.getPathFromRoot();
	List<Action> r=Collections.emptyList();
	for(Node a:x)
	{
		if(a.getState()==outcome2)
		{
			r.add(a.getAction());
		}
	}
	
	return r;
	
	}
	//get outcome of search
	public SearchOutcome getOutcome() {
		return outcome;
	}
	
	//
	//Private functions
	private List<Action> failure() {
		return Collections.emptyList();
	}
	
	//returns an Action list with only one action of p
	private List<Action> success(Node p)
	{
		List<Action> k=Collections.emptyList();
		k.add(p.getAction()); 
		return k;
	}
	//appends the prefix,suffix and action list
	private List<Action> APPEND(Action prefix, List<Action> x, Action suffix) {
		x.add(0,prefix);
		x.add(suffix);
		
		return x;
	}

	
}

