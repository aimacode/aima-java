package aima.core.search.planning;

import java.util.List;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

import aima.core.search.framework.Problem_Extend;
import aima.core.agent.Action;
import aima.core.agent.State;
import aima.core.agent.impl.aprog.simplerule.*;
import aima.core.search.framework.*;

/**
 * <h2> GRAPHPLAN.java
 * Note- This is partial implementation andcomplete implementation will be added soon
 * </h2> 
 * 
 * Artificial Intelligence A Modern Approach (3rd Edition): page 383 Figure 10.9.<br>
 * <br>
 * 
 * <pre>
 * <code>
 *function GRAPHPLAN(Problem_Extend) returns solution or failure
 *graph <- INITIAL-PLANNING-GRAPH(Problem_Extend)
 *goals <- CONJUNCTS(Problem_Extend.GOAL)
 *nogoods <- an empty hash table
 *for tl = 0 to Inf do
 *if goals all non-mutex in St of graph then
 *solution <- EXTRACT-SOLUTION(graph, goals, NUMLEVELS(graph), nogoods)
 *if solution != failure then return solution
 *if graph and nogoods have both leveled off then return failure
 *graph <- EXPAND-GRAPH(graph, Problem_Extend)
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
	    * function GRAPHPLAN( Problem_Extend) returns
	    * solution or failure
	    * 
	    * @param p
	    *       Problem_Extend whose solution is to be found
	    * @return
	    * 
	    */
	public Hashtable<State,Action> graphplan(Problem_Extend p)
	{
		Hashtable<State,Iterator<Action>> graph = new Hashtable<State,Iterator<Action>>();
		//graph <- INITIAL-PLANNING-GRAPH(Problem_Extend)
		graph = INITIALPLANNINGGRAPH(p); 
	    List<Action> goals = new ArrayList<Action>();
       //goals <- CONJUNCTS(Problem_Extend.GOAL)
		goals=CONJUNCTS(p);
	    Hashtable<Integer,String> nogoods=new Hashtable<Integer,String>();  //an empty hash table
		for(int tl=0;tl<1/0.;tl++) //1/0. = Infinity
		{
			if(isNonMutex(graph,p)) //if goals all non-mutex in St of graph
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
	/**
	 * used to initialize the planning graph 
	 * to a one-level (So) graph representing the initial state
	 * @param pb
	 *      Takes Problem_Extend
	 * @return
	 *       initial state So
	 */
	public Hashtable<State, Iterator<Action>> INITIALPLANNINGGRAPH(Problem_Extend pb)
	{
		return pb.getInitialState();
	}
	/**
	 * Used to extract Goal Conditions form Problem_Extend
	 * @param pb
	 *      Takes Problem_Extend
	 * @return
	 *      Goals in Problem_Extend pb
	 */
	public List<Action>  CONJUNCTS(Problem_Extend pb)
	{
		return pb.Goal;
	}
	/**
	 * Expands graph to add actions whose preconditions 
	 * are satisfied by So
	 * 
	 * @param graph
	 *     accepts graph with states
	 * @param pb
	 *     Takes Problem_Extend 
	 * 
	 * @return
	 *     Expanded graph
	 * 
	 */
	public Hashtable<State, Iterator<Action>> EXPANDGRAPH(Hashtable<State, Iterator<Action>> graph,Problem_Extend pb)
	{
		//yet to be implemented completely
			Set<Iterator<State>> a=pb.action_Conditions.keySet(); //State
			Enumeration<Action> e=pb.action_Conditions.elements(); //Actions
			while(e.hasMoreElements())
			{
				for(int i=0;;i++)
				{
					for (State key : graph.keySet())
					{
						
					}
						
				}
			}
		}
	}
	/**
	 * Once all goal states are in Si it is called to find solution
	 * 
	 * @param graph
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
	public Hashtable<State,Action> EXTRACTSOLUTION(Hashtable<State, Set<Action>> graph,List<Action> goal,int i,Hashtable<Integer,String> nogood)
	{
	//yet to be implemented	
	}
	/**
	 * to check if all states in goals are non-mutex
	 * will return true if goal state is already in 
	 * graph , false otherwise
	 * 
	 * @return
	 * true if all the literals from the goal are present
     * in S2, and none of them is mutex with any other
     * false otherwise
	 */
	public boolean isNonMutex(Hashtable<State,Action> graph,Problem_Extend pb)
	{
		
		/**Enumeration e = graph.keys();
		for (State key : graph.keySet())
		{
			if(pb.isGoalState(key));
			return true;
		}
		return false;
		**/
		return pb.isGoalState(graph);
	}
	/**
	 * check if graph and nogoods have both leveled off
	 * 
	 * @return
	 * true if graph and nogoods have both leveled off
	 * false otherwise
	 * 
	 */
	public boolean isLeveledOff()
	{
		//yet to be implemented
	}
	/**
	 * used to find numlevel of action g
	 * 
	 * @param g
	 *   action whose numlevel is required
	 * @return
	 *  numlevel of action g
	 */
	public int NUMLEVELS(List<Action> g)
	{
		//yet to be implemented
	}
}
