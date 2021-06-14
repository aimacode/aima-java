package aima.gui.demo.logic;

import aima.core.logic.propositional.inference.PLResolution;
import aima.core.logic.propositional.kb.KnowledgeBase;
import aima.core.logic.propositional.parsing.PLParser;

/**
 * @author Ravi Mohan
 * 
 */
public class PlResolutionDemo {
	private static PLResolution plr = new PLResolution();

	public static void main(String[] args) {
		KnowledgeBase kb = new KnowledgeBase();
		String fact = "(B11 => ~P11) & B11)";
		kb.tell(fact);
		System.out.println("\nPlResolutionDemo\n");
		System.out.println("adding " + fact + "to knowldegebase");
		displayResolutionResults(kb, "~B11");
	}

	private static void displayResolutionResults(KnowledgeBase kb, String query) {
		PLParser parser = new PLParser();
		System.out.println("Running plResolution of query " + query
				+ " on knowledgeBase  gives " + plr.isEntailed(kb, parser.parse(query)));
	}
}
