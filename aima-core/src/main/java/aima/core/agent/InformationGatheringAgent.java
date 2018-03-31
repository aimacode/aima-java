package aima.core.agent;

import aima.core.agent.Action;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.RandomVariable;

import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 16.9, page 632.<br>
 * <p>
 * <pre>
 * function INFORMATION-GATHERING-AGENT(percept) returns an action
 *	 persistent: D, a decision network
 *
 *	 integrate percept into D
 *	 j ← the value that maximizes VPI(Ej) / Cost(Ej)
 *	 if VPI(Ej) > Cost(Ej)
 *	   return REQUEST(Ej)
 *	 else return the best action from D
 * </pre>
 * <p>
 * Figure 16.9 Design of a simple information-gathering agent.  
 * The agent works by repeatedly selecting the observation with the highest information value, 
 * until the cost of the next observation is greater than its expected benefit. 
 * 
 * @author Ritwik Sharma
 */
public abstract class InformationGatheringAgent extends AbstractAgent {
	// persistent: D, a decision network
	protected BayesianNetwork D;
	private BayesianNetwork j;
	Action action;
	Action real;
	 
	public InformationGatheringAgent(BayesianNetwork D) {
		this.D = D;
	}
	
	// function INFORMATION-GATHERING-AGENT(percept) returns an action
	public Action IGA(Percept percept) {
		/** get maximum value(best action) from D.
		 *  @para real
		 *	      the best action from D	
		 */	          
		real = D.getMaximumValue(percept); // the value that maximizes VPI (Ej) / Cost (Ej )

		List<RandomVariable> ej; // observable evidence variable
		
		for (RandomVariable Ej : D.getVariablesInTopologicalOrder()) {
			/**
			 * @para Cost(Ej)  
			 *				gives associated cost for each random variable Ej.
			 * @para VPI(Ej)   
			 *				gives value of perfect information for each random variable Ej.
			 */
			if (D.VPI(Ej) > D.Cost(Ej))
				action = REQUEST(Ej);
			  else
			  	action = real; 
		}	

		return action;

	}
	public abstract Action VPI();
	public abstract Action Cost();
	public abstract Action REQUEST();
	public abstract Percept getMaximumValue(Percept percept);

}
