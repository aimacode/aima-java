package aima.test.core.experiment.logic.propositional.algorithms;

import org.junit.Test;

import aima.core.logic.propositional.Model;
import aima.core.logic.propositional.inference.WalkSAT;
import aima.core.logic.propositional.kb.KnowledgeBase;

/**
 * @author Ravi Mohan
 * 
 */
public class WalkSATExperiment {

	// NOT REALLY A JUNIT TESTCASE BUT written as one to allow easy execution
	@Test
	public void testWalkSat() {
		WalkSAT walkSAT = new WalkSAT();
		Model m = walkSAT.findModelFor("A & B", 1000, 0.5);
		if (m == null) {
			System.out.println("failure");
		} else {
			m.print();
		}
	}

	@Test
	public void testWalkSat2() {
		WalkSAT walkSAT = new WalkSAT();
		Model m = walkSAT.findModelFor("A & ~B", 1000, 0.5);
		if (m == null) {
			System.out.println("failure");
		} else {
			m.print();
		}
	}

	@Test
	public void testAIMAExample() {
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell("P => Q");
		kb.tell("L & M => P");
		kb.tell("B & L => M");
		kb.tell("A & P => L");
		kb.tell("A & B => L");
		kb.tell("A");
		kb.tell("B");
		WalkSAT walkSAT = new WalkSAT();
		Model m = walkSAT.findModelFor(kb.asSentence().toString(), 1000, 0.5);
		if (m == null) {
			System.out.println("failure");
		} else {
			m.print();
		}
	}
}
