package aima.gui.demo.logic;

import aima.core.logic.propositional.algorithms.KnowledgeBase;
import aima.core.logic.propositional.algorithms.PLResolution;

/**
 * @author Ravi Mohan
 * 
 */
public class PLResolutionDemo {
	private static PLResolution plr = new PLResolution();

	public static void main(String[] args) {
		KnowledgeBase kb = new KnowledgeBase();
		String fact = "((B11 =>  (NOT P11)) AND B11)";
		kb.tell(fact);
		System.out.println("\nPLResolutionDemo\n");
		System.out.println("adding " + fact + "to knowldegebase");
		displayResolutionResults(kb, "(NOT B11)");
	}

	private static void displayResolutionResults(KnowledgeBase kb, String query) {
		System.out.println("Running plResolution of query " + query
				+ " on knowledgeBase  gives " + plr.plResolution(kb, query));
	}
}
