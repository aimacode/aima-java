package aima.gui.demo.logic;

import aima.core.logic.propositional.kb.KnowledgeBase;

/**
 * @author Ravi Mohan
 * 
 */
public class TTEntailsDemo {
	public static void main(String[] args) {
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell("B12 <=> P11 | P13 | P22 | P02");
		kb.tell("B21 <=> P20 | P22 | P31 | P11");
		kb.tell("B01 <=> P00 | P02 | P11");
		kb.tell("B10 <=> P11 | P20 | P00");
		kb.tell("~B21");
		kb.tell("~B12");
		kb.tell("B10");
		kb.tell("B01");

		System.out.println("\nTTEntailsDemo\n");
		System.out.println(kb.toString());

		displayTTEntails(kb, "P00");
		displayTTEntails(kb, "~P00");
	}

	private static void displayTTEntails(KnowledgeBase kb, String s) {
		System.out.println(" ttentails (\"" + s + "\" ) returns "
				+ kb.askWithTTEntails(s));
	}
}