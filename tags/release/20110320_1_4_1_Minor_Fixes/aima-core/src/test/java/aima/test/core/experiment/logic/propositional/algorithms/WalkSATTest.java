package aima.test.core.experiment.logic.propositional.algorithms;

import org.junit.Test;

import aima.core.logic.propositional.algorithms.KnowledgeBase;
import aima.core.logic.propositional.algorithms.Model;
import aima.core.logic.propositional.algorithms.WalkSAT;

/**
 * @author Ravi Mohan
 * 
 */
public class WalkSATTest {

	// NOT REALLY A JUNIT TESTCASE BUT written as one to allow easy execution
	@Test
	public void testWalkSat() {
		WalkSAT walkSAT = new WalkSAT();
		Model m = walkSAT.findModelFor("( A AND B )", 1000, 0.5);
		if (m == null) {
			System.out.println("failure");
		} else {
			m.print();
		}
	}

	@Test
	public void testWalkSat2() {
		WalkSAT walkSAT = new WalkSAT();
		Model m = walkSAT.findModelFor("( A AND (NOT B) )", 1000, 0.5);
		if (m == null) {
			System.out.println("failure");
		} else {
			m.print();
		}
	}

	@Test
	public void testAIMAExample() {
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell(" (P => Q)");
		kb.tell("((L AND M) => P)");
		kb.tell("((B AND L) => M)");
		kb.tell("( (A AND P) => L)");
		kb.tell("((A AND B) => L)");
		kb.tell("(A)");
		kb.tell("(B)");
		WalkSAT walkSAT = new WalkSAT();
		Model m = walkSAT.findModelFor(kb.asSentence().toString(), 1000, 0.5);
		if (m == null) {
			System.out.println("failure");
		} else {
			m.print();
		}
	}
}
