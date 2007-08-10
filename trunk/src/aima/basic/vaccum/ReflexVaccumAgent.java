package aima.basic.vaccum;

import aima.basic.Agent;
import aima.basic.AgentProgram;
import aima.basic.BasicEnvironmentView;
import aima.basic.Percept;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 2.8, page 46.
 * <code>
 * function REFLEX-VACUUM-AGENT([location, status]) returns an action
 *   
 *   if status = Dirty then return Suck
 *   else if location = A then return Right
 *   else if location = B then return Left
 * </code>
 * Figure 2.8 The agent program for a simple reflex agent in the two-state vacuum environment.
 * This program implements the action function tabulated in Figure 2.3.
 */

/**
 * @author Ciaran O'Reilly
 *
 */
public class ReflexVaccumAgent extends Agent {
	

	// public static void main(String[] args) {
	// TrivialVaccumEnvironment tve = new TrivialVaccumEnvironment();
	// ReflexVaccumAgent a = new ReflexVaccumAgent();
	// tve.addAgent(a, "A");
	// BasicEnvironmentView view = new BasicEnvironmentView();
	// tve.registerView(view);
	//		
	// System.out.println("Initial Environment=[A,
	// "+tve.getLocation1Status()+"], [B, "+tve.getLocation2Status()+"]");
	// tve.step(8);
	//	}

	public ReflexVaccumAgent() {
		super(new AgentProgram() {
			//function REFLEX-VACUUM-AGENT([location, status]) returns an action
			public String execute(Percept percept) {
				
				// if status = Dirty then return Suck
				if ("Dirty".equals(percept.getAttribute("status"))) {
					return "Suck";
				// else if location = A then return Right
				} else if ("A".equals(percept.getAttribute("location"))) {
					return "Right";
				// else if location = B then return Left
				} else if ("B".equals(percept.getAttribute("location"))) {
					return "Left";
				}
				
				return "NoOP"; // Note: This should not be returned if the environment is correct
			}
		});
	}
}
