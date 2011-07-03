package aima.gui.demo.logic;

import aima.core.logic.propositional.algorithms.KnowledgeBase;
import aima.core.logic.propositional.algorithms.Model;
import aima.core.logic.propositional.algorithms.WalkSAT;

/**
 * @author Ravi Mohan
 * 
 */
public class WalkSatDemo {
	public static void main(String[] args) {
		System.out.println("\nWalkSatDemo\n");
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell(" (P => Q)");
		kb.tell("((L AND M) => P)");
		kb.tell("((B AND L) => M)");
		kb.tell("( (A AND P) => L)");
		kb.tell("((A AND B) => L)");
		kb.tell("(A)");
		kb.tell("(B)");

		System.out.println("Example from  page 220 of AIMA 2nd Edition");
		System.out.println("KnowledgeBsse consists of sentences");
		System.out.println(" (P => Q)");
		System.out.println("((L AND M) => P)");
		System.out.println("((B AND L) => M)");
		System.out.println("( (A AND P) => L)");
		System.out.println("((A AND B) => L)");
		System.out.println("(A)");
		System.out.println("(B)");

		WalkSAT walkSAT = new WalkSAT();
		Model m = walkSAT.findModelFor(kb.asSentence().toString(), 1000, 0.5);
		if (m == null) {
			System.out.println("failure");
		} else {
			m.print();
		}
	}

}