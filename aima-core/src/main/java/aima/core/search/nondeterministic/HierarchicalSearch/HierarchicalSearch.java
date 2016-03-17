package aima.core.search.nondeterministic.HierarchicalSearch;

/**
 * 
 * @author Subham Mishra
 * @version 1.0
 * @since 15th Mar 2016
 * <h1>
 * Artificial Intelligence A Modern Approach (3rd Edition): pg 409 ,Fig 11.5
 * </h1>
 *  <pre>
 *function HIERARCHICAL-SEARCH(problem, hierarchy) returns a solution, or failure
 *frontier <- a FIFO queue with [Act] as the only element
 *loop do
 *if EMPTY?( frontier) then return failure
 *plan <- POP( frontier) //chooses the shallowest plan in frontier 
 *hla <- the first HLA in plan, or null if none
 *prefix ,suffix <-the action subsequences before and after hla in plan
 *outcome <- RESULT(problem.INITIAL-STATE, prefix )
 *if hla is null then //so plan is primitive and outcome is its result 
 *if outcome satisfies problem.GOAL then return plan
 *else for each sequence in REFINEMENTS(hla, outcome, hierarchy) do
 *frontier <- INSERT(APPEND(prefix , sequence, suffix), frontier)
 * </pre>
 */
public class HierarchicalSearch {
	 public static int front=-1;
	  public static String[] frontier=new String[100];
	  public static int i=0,j=0,k=0,l=2,m=1; //hierarchy
	  public static String[] hla=new String[100]; //to  store next hla
	  public static String[] storePrefixSuffix=new String[100];
	  public static String[] storeOutcome=new String[100];
	  public static String returnPlan=new String("Home"); //to store plan
	  /**
	   * 
	   * @param p: Gets Problem from caller
	   * @param size: Gets size of prefix and suffix
	   * @return failure if problem if empty else return path for solving problem
	   */
	public static String Hierarchical_Search(Problem p,int size)
	{
		String plan=pop(frontier);  //chooses the shallowest plan in frontier
		String hla;
		String outcome;
		do{
		if(frontier[0].equalsIgnoreCase("")) 
		{
		  return "Failure";
		}
		else
		{
			hla=hla1(plan);//choose hla's
			String prefix=action(hla,"before"); //get action before hla
			String suffix=action(hla,"after"); //get actions after hla
			outcome=Result(p.initialState,prefix);
			//if hla is null then /* so plan is primitive and outcome is its result */
			if(hla.equalsIgnoreCase("")) 
			{
				//if outcome is goal then we have successfully reached solution return plan
			if(outcome.equalsIgnoreCase(p.goal))
				return returnPlan.toString();
			}
			else if(l!=size+2)
			{
				addtoPlan("Prefix");
				l=l+2; //incremented to get all even index in storePrefixSuffix[]
				addtoPlan("Suffix");
				m=m+2; //incremented to get all odd index in storePrefixSuffix[]
			}
		}
		}while(true); //continue till either we return plan or failure
	}  
	/**
	 * 
	 * @param frn : used to remove elements from frontier as FIFO Queue
	 * @return deleted element from frontier
	 */
	public static String pop(String[] frn)
	{ 
		int tempFront=front;
		if(front!=-1)
		{
			front--;
			return frontier[tempFront];
		}
		else 
			return "";
	}
	/**
	 * 
	 * @param s:Receives plan
	 * @return next hla in plan
	 */
	static String hla1(String s)
	{
			return hla[j++]; //return HLA for plan
	}
	/**
	 * 
	 * @param p:Receives hla for which action is to be returned
	 * @param l:receives whether it is before of after hla (to match pseudo code)
	 * @return action before , after hla alternatively
	 */
	static String action(String p,String l)
	{
		return storePrefixSuffix[i++]; //no action after it; //returns action
	}
	/**
	 * 
	 * @param s:receives initial state
	 * @param str: receives hla
	 * @return outcome of initial state to hla
	 */
	public static String Result(String s,String str)
	{
		return storeOutcome[k++];
	}
	/**
	 * 
	 * @param s:add hla's to plan
	 */
	public static void addtoPlan(String s)
	{
		if(s.equalsIgnoreCase("Prefix"))
		returnPlan=returnPlan+" -> "+storePrefixSuffix[l];
		else
			returnPlan=returnPlan+" -> "+storePrefixSuffix[m];
	}
}

