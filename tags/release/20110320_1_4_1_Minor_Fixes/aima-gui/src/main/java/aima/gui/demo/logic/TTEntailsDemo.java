package aima.gui.demo.logic;

import aima.core.logic.propositional.algorithms.KnowledgeBase;

/**
 * @author Ravi Mohan
 * 
 */
public class TTEntailsDemo {
	public static void main(String[] args) {
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell("(B12 <=> (P11 OR (P13 OR (P22 OR P02))))");
		kb.tell("(B21 <=> (P20 OR (P22 OR (P31 OR P11))))");
		kb.tell("(B01 <=> (P00 OR (P02 OR P11)))");
		kb.tell("(B10 <=> (P11 OR (P20 OR P00)))");
		kb.tell("(NOT B21)");
		kb.tell("(NOT B12)");
		kb.tell("(B10)");
		kb.tell("(B01)");

		System.out.println("\nTTEntailsDemo\n");
		System.out.println("(B12 <=> (P11 OR (P13 OR (P22 OR P02))))");
		System.out.println("(B21 <=> (P20 OR (P22 OR (P31 OR P11))))");
		System.out.println("(B01 <=> (P00 OR (P02 OR P11)))");
		System.out.println("(B10 <=> (P11 OR (P20 OR P00)))");
		System.out.println("(NOT B21)");
		System.out.println("(NOT B12)");
		System.out.println("(B10)");
		System.out.println("(B01)");

		displayTTEntails(kb, "(P00)");
		displayTTEntails(kb, "(NOT P00)");

	}

	private static void displayTTEntails(KnowledgeBase kb, String s) {
		System.out.println(" ttentails (\"" + s + "\" ) returns "
				+ kb.askWithTTEntails(s));
	}
}