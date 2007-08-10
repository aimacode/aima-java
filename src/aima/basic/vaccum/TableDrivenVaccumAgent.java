package aima.basic.vaccum;

import java.util.Hashtable;
import java.util.Map;

import aima.basic.Agent;
import aima.basic.Percept;
import aima.basic.PerceptSequence;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 2.3, page 34.
 * 
 * Figure 2.3 Partial tabulation of a simple agent function for the vacuum-cleaner world
 * shown in Figure 2.2.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class TableDrivenVaccumAgent extends Agent {

	// This shold move to a test method what happens inside this method should
	// be verified in a TestCase method - RM
	// public static void main(String[] args) {
	// TrivialVaccumEnvironment tve = new TrivialVaccumEnvironment();
	// TableDrivenVaccumAgent a = new TableDrivenVaccumAgent();
	// tve.addAgent(a, "A");
	// BasicEnvironmentView view = new BasicEnvironmentView();
	// tve.registerView(view);
	//		
	// System.out.println("Initial Environment=[A,
	// "+tve.getLocation1Status()+"], [B, "+tve.getLocation2Status()+"]");
	// tve.stepUntilNoOp();
	// }

	public TableDrivenVaccumAgent() {
		super(new TableDrivenAgentProgram(getPerceptSequenceActions()));
	}

	//
	// PRIVATE METHODS
	//
	private static Map<PerceptSequence, String> getPerceptSequenceActions() {
		Map<PerceptSequence, String> perceptSequenceActions = new Hashtable<PerceptSequence, String>();

		PerceptSequence ps;
		//
		// Level 1: 4 states
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Clean"));
		perceptSequenceActions.put(ps, "Right");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Clean"));
		perceptSequenceActions.put(ps, "Left");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");

		//
		// Level 2: 4x4 states
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Clean"), new Percept(
						"location", "A", "status", "Clean"));
		perceptSequenceActions.put(ps, "Right");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Clean"), new Percept(
						"location", "A", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Clean"), new Percept(
						"location", "B", "status", "Clean"));
		perceptSequenceActions.put(ps, "Left");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Clean"), new Percept(
						"location", "B", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		//
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Dirty"), new Percept(
						"location", "A", "status", "Clean"));
		perceptSequenceActions.put(ps, "Right");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Dirty"), new Percept(
						"location", "A", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Dirty"), new Percept(
						"location", "B", "status", "Clean"));
		perceptSequenceActions.put(ps, "Left");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Dirty"), new Percept(
						"location", "B", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		//
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Clean"), new Percept(
						"location", "A", "status", "Clean"));
		perceptSequenceActions.put(ps, "Right");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Clean"), new Percept(
						"location", "A", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Clean"), new Percept(
						"location", "B", "status", "Clean"));
		perceptSequenceActions.put(ps, "Left");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Clean"), new Percept(
						"location", "B", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		//
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Dirty"), new Percept(
						"location", "A", "status", "Clean"));
		perceptSequenceActions.put(ps, "Right");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Dirty"), new Percept(
						"location", "A", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Dirty"), new Percept(
						"location", "B", "status", "Clean"));
		perceptSequenceActions.put(ps, "Left");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Dirty"), new Percept(
						"location", "B", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");

		//
		// Level 3: 4x4x4 states
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Clean"), new Percept(
						"location", "A", "status", "Clean"), new Percept(
						"location", "A", "status", "Clean"));
		perceptSequenceActions.put(ps, "Right");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Clean"), new Percept(
						"location", "A", "status", "Clean"), new Percept(
						"location", "A", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Clean"), new Percept(
						"location", "A", "status", "Clean"), new Percept(
						"location", "B", "status", "Clean"));
		perceptSequenceActions.put(ps, "Left");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Clean"), new Percept(
						"location", "A", "status", "Clean"), new Percept(
						"location", "B", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		//
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Clean"), new Percept(
						"location", "A", "status", "Dirty"), new Percept(
						"location", "A", "status", "Clean"));
		perceptSequenceActions.put(ps, "Right");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Clean"), new Percept(
						"location", "A", "status", "Dirty"), new Percept(
						"location", "A", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Clean"), new Percept(
						"location", "A", "status", "Dirty"), new Percept(
						"location", "B", "status", "Clean"));
		perceptSequenceActions.put(ps, "Left");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Clean"), new Percept(
						"location", "A", "status", "Dirty"), new Percept(
						"location", "B", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		//		
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Clean"), new Percept(
						"location", "B", "status", "Clean"), new Percept(
						"location", "A", "status", "Clean"));
		perceptSequenceActions.put(ps, "Right");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Clean"), new Percept(
						"location", "B", "status", "Clean"), new Percept(
						"location", "A", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Clean"), new Percept(
						"location", "B", "status", "Clean"), new Percept(
						"location", "B", "status", "Clean"));
		perceptSequenceActions.put(ps, "Left");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Clean"), new Percept(
						"location", "B", "status", "Clean"), new Percept(
						"location", "B", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		//	
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Clean"), new Percept(
						"location", "B", "status", "Dirty"), new Percept(
						"location", "A", "status", "Clean"));
		perceptSequenceActions.put(ps, "Right");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Clean"), new Percept(
						"location", "B", "status", "Dirty"), new Percept(
						"location", "A", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Clean"), new Percept(
						"location", "B", "status", "Dirty"), new Percept(
						"location", "B", "status", "Clean"));
		perceptSequenceActions.put(ps, "Left");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Clean"), new Percept(
						"location", "B", "status", "Dirty"), new Percept(
						"location", "B", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		//
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Dirty"), new Percept(
						"location", "A", "status", "Clean"), new Percept(
						"location", "A", "status", "Clean"));
		perceptSequenceActions.put(ps, "Right");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Dirty"), new Percept(
						"location", "A", "status", "Clean"), new Percept(
						"location", "A", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Dirty"), new Percept(
						"location", "A", "status", "Clean"), new Percept(
						"location", "B", "status", "Clean"));
		perceptSequenceActions.put(ps, "Left");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Dirty"), new Percept(
						"location", "A", "status", "Clean"), new Percept(
						"location", "B", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		//		
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Dirty"), new Percept(
						"location", "A", "status", "Dirty"), new Percept(
						"location", "A", "status", "Clean"));
		perceptSequenceActions.put(ps, "Right");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Dirty"), new Percept(
						"location", "A", "status", "Dirty"), new Percept(
						"location", "A", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Dirty"), new Percept(
						"location", "A", "status", "Dirty"), new Percept(
						"location", "B", "status", "Clean"));
		perceptSequenceActions.put(ps, "Left");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Dirty"), new Percept(
						"location", "A", "status", "Dirty"), new Percept(
						"location", "B", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		//	
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Dirty"), new Percept(
						"location", "B", "status", "Clean"), new Percept(
						"location", "A", "status", "Clean"));
		perceptSequenceActions.put(ps, "Right");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Dirty"), new Percept(
						"location", "B", "status", "Clean"), new Percept(
						"location", "A", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Dirty"), new Percept(
						"location", "B", "status", "Clean"), new Percept(
						"location", "B", "status", "Clean"));
		perceptSequenceActions.put(ps, "Left");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Dirty"), new Percept(
						"location", "B", "status", "Clean"), new Percept(
						"location", "B", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		//	
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Dirty"), new Percept(
						"location", "B", "status", "Dirty"), new Percept(
						"location", "A", "status", "Clean"));
		perceptSequenceActions.put(ps, "Right");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Dirty"), new Percept(
						"location", "B", "status", "Dirty"), new Percept(
						"location", "A", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Dirty"), new Percept(
						"location", "B", "status", "Dirty"), new Percept(
						"location", "B", "status", "Clean"));
		perceptSequenceActions.put(ps, "Left");
		ps = new PerceptSequence(
				new Percept("location", "A", "status", "Dirty"), new Percept(
						"location", "B", "status", "Dirty"), new Percept(
						"location", "B", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		//
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Clean"), new Percept(
						"location", "A", "status", "Clean"), new Percept(
						"location", "A", "status", "Clean"));
		perceptSequenceActions.put(ps, "Right");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Clean"), new Percept(
						"location", "A", "status", "Clean"), new Percept(
						"location", "A", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Clean"), new Percept(
						"location", "A", "status", "Clean"), new Percept(
						"location", "B", "status", "Clean"));
		perceptSequenceActions.put(ps, "Left");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Clean"), new Percept(
						"location", "A", "status", "Clean"), new Percept(
						"location", "B", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		//
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Clean"), new Percept(
						"location", "A", "status", "Dirty"), new Percept(
						"location", "A", "status", "Clean"));
		perceptSequenceActions.put(ps, "Right");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Clean"), new Percept(
						"location", "A", "status", "Dirty"), new Percept(
						"location", "A", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Clean"), new Percept(
						"location", "A", "status", "Dirty"), new Percept(
						"location", "B", "status", "Clean"));
		perceptSequenceActions.put(ps, "Left");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Clean"), new Percept(
						"location", "A", "status", "Dirty"), new Percept(
						"location", "B", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		//
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Clean"), new Percept(
						"location", "B", "status", "Clean"), new Percept(
						"location", "A", "status", "Clean"));
		perceptSequenceActions.put(ps, "Right");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Clean"), new Percept(
						"location", "B", "status", "Clean"), new Percept(
						"location", "A", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Clean"), new Percept(
						"location", "B", "status", "Clean"), new Percept(
						"location", "B", "status", "Clean"));
		perceptSequenceActions.put(ps, "Left");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Clean"), new Percept(
						"location", "B", "status", "Clean"), new Percept(
						"location", "B", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		//
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Clean"), new Percept(
						"location", "B", "status", "Dirty"), new Percept(
						"location", "A", "status", "Clean"));
		perceptSequenceActions.put(ps, "Right");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Clean"), new Percept(
						"location", "B", "status", "Dirty"), new Percept(
						"location", "A", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Clean"), new Percept(
						"location", "B", "status", "Dirty"), new Percept(
						"location", "B", "status", "Clean"));
		perceptSequenceActions.put(ps, "Left");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Clean"), new Percept(
						"location", "B", "status", "Dirty"), new Percept(
						"location", "B", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		//
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Dirty"), new Percept(
						"location", "A", "status", "Clean"), new Percept(
						"location", "A", "status", "Clean"));
		perceptSequenceActions.put(ps, "Right");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Dirty"), new Percept(
						"location", "A", "status", "Clean"), new Percept(
						"location", "A", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Dirty"), new Percept(
						"location", "A", "status", "Clean"), new Percept(
						"location", "B", "status", "Clean"));
		perceptSequenceActions.put(ps, "Left");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Dirty"), new Percept(
						"location", "A", "status", "Clean"), new Percept(
						"location", "B", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		//
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Dirty"), new Percept(
						"location", "A", "status", "Dirty"), new Percept(
						"location", "A", "status", "Clean"));
		perceptSequenceActions.put(ps, "Right");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Dirty"), new Percept(
						"location", "A", "status", "Dirty"), new Percept(
						"location", "A", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Dirty"), new Percept(
						"location", "A", "status", "Dirty"), new Percept(
						"location", "B", "status", "Clean"));
		perceptSequenceActions.put(ps, "Left");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Dirty"), new Percept(
						"location", "A", "status", "Dirty"), new Percept(
						"location", "B", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		//
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Dirty"), new Percept(
						"location", "B", "status", "Clean"), new Percept(
						"location", "A", "status", "Clean"));
		perceptSequenceActions.put(ps, "Right");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Dirty"), new Percept(
						"location", "B", "status", "Clean"), new Percept(
						"location", "A", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Dirty"), new Percept(
						"location", "B", "status", "Clean"), new Percept(
						"location", "B", "status", "Clean"));
		perceptSequenceActions.put(ps, "Left");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Dirty"), new Percept(
						"location", "B", "status", "Clean"), new Percept(
						"location", "B", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		//
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Dirty"), new Percept(
						"location", "B", "status", "Dirty"), new Percept(
						"location", "A", "status", "Clean"));
		perceptSequenceActions.put(ps, "Right");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Dirty"), new Percept(
						"location", "B", "status", "Dirty"), new Percept(
						"location", "A", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Dirty"), new Percept(
						"location", "B", "status", "Dirty"), new Percept(
						"location", "B", "status", "Clean"));
		perceptSequenceActions.put(ps, "Left");
		ps = new PerceptSequence(
				new Percept("location", "B", "status", "Dirty"), new Percept(
						"location", "B", "status", "Dirty"), new Percept(
						"location", "B", "status", "Dirty"));
		perceptSequenceActions.put(ps, "Suck");

		//
		// Level 4: 4x4x4x4 states
		// ...

		return perceptSequenceActions;
	}
}
