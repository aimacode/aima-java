package aima.core.search.planning;

import java.util.List;
import java.util.Hashtable;
import java.util.ArrayList;

import aima.core.search.framework.Problem;
import aima.core.agent.Action;

/**
 * <h2> GRAPHPLAN.java
 * </h2> 
 * 
 * Artificial Intelligence A Modern Approach (3rd Edition): page 383 Figure 10.9.<br>
 * <br>
 * 
 * <pre>
 * <code>
 *function GRAPHPLAN(problem) returns solution or failure
 *graph <- INITIAL-PLANNING-GRAPH(problem)
 *goals <- CONJUNCTS(problem.GOAL)
 *nogoods <- an empty hash table
 *for tl = 0 to ∞ do
 *if goals all non-mutex in St of graph then
 *solution <- EXTRACT-SOLUTION(graph, goals, NUMLEVELS(graph), nogoods)
 *if solution 6= failure then return solution
 *if graph and nogoods have both leveled off then return failure
 *graph <- EXPAND-GRAPH(graph, problem)
 * </code>
 * </pre>
 * Figure 10.9 The GRAPHPLAN algorithm. GRAPHPLAN calls EXPAND-GRAPH to add a
 * level until either a solution is found by EXTRACT- SOLUTION, or no solution is possible.
 * @author Subham Mishra
 */


   public abstract class GRAPHPLAN 
    {
	   protected Hashtable<State,Action> solution=new Hashtable<State,Action>(); 
	   /**
	    * function GRAPHPLAN( problem) returns
	    * solution or failure
	    * 
	    * @param p
	    *       Problem whose solution is to be found
	    * @return
	    * 
	    */
	public Hashtable<State,Action> graphplan(Problem p)
	{
		List<Action> graph = new ArrayList<Action>();
		//graph <- INITIAL-PLANNING-GRAPH(problem)
		graph = INITIALPLANNINGGRAPH(p); 
	    List<Action> goals = new ArrayList<Action>();
       //goals <- CONJUNCTS(problem.GOAL)
		goals=CONJUNCTS(p);
	    Hashtable<Integer,String> nogoods=new Hashtable<Integer,String>();  //an empty hash table
		for(int tl=0;tl<1/0.;tl++) //1/0. = Infinity
		{
			if(isNonMutex()) //if goals all non-mutex in St of graph
			{
				solution=EXTRACTSOLUTION(graph,goals,0,nogoods);
				if(solution!=null); //null represents failure
				 return solution;
			}
			if(isLeveledOff()) //if graph and nogoods have both leveled off then return
				return null; //returns failure
			graph=EXPANDGRAPH(graph,p);
		}
		
		return null; //returns failure
	}
	//
	//SUPPORTING CODE
	//
	interface State {
	
}
	/**
	 * used to initialize the planning graph 
	 * to a one-level (So) graph representing the initial state
	 * @param pb
	 *      Takes problem
	 * @return
	 *       initial state So
	 */
	public abstract List<Action> INITIALPLANNINGGRAPH(Problem pb);
	/**
	 * Used to extract Goal Conditions form Problem
	 * @param pb
	 *      Takes Problem
	 * @return
	 *      Goals in Problem pb
	 */
	public abstract List<Action>  CONJUNCTS(Problem pb);
	/**
	 * Expands graph to add actions whose preconditions 
	 * are satisfied by So
	 * 
	 * @param g
	 *     accepts graph with states
	 * @param pb
	 *     Takes Problem 
	 * 
	 * @return
	 *     Expanded graph
	 * 
	 */
	public abstract List<Action> EXPANDGRAPH(List<Action> g,Problem pb);
	/**
	 * Once all goal states are in Si it is called to find solution
	 * 
	 * @param g
	 *      graph
	 * @param goal
	 *   list of goal state
	 * 
	 * @param i
	 *    numlevel of action
	 * @param nogood
	 *    hash table
	 *     
	 * @return
	 *  if solution exists it is returned else
	 *   null , indicating failure is returned
	 */
	public abstract Hashtable<State,Action> EXTRACTSOLUTION(List<Action> g,List<Action> goal,int i,Hashtable<Integer,String> nogood);
	/**
	 * to check if all states in goals are non-mutex
	 * 
	 * @return
	 * true if all the literals from the goal are present
     * in S2, and none of them is mutex with any other
     * false otherwise
	 */
	public abstract boolean isNonMutex();
	/**
	 * check if graph and nogoods have both leveled off
	 * 
	 * @return
	 * true if graph and nogoods have both leveled off
	 * false otherwise
	 * 
	 */
	public abstract boolean isLeveledOff();
	/**
	 * used to find numlevel of action g
	 * 
	 * @param g
	 *   action whose numlevel is required
	 * @return
	 *  numlevel of action g
	 */
	public abstract int NUMLEVELS(List<Action> g);
}
