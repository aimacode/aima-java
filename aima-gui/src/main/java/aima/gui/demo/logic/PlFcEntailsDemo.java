package aima.gui.demo.logic;

import aima.core.logic.propositional.inference.PLFCEntails;
import aima.core.logic.propositional.kb.KnowledgeBase;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;

/**
 * @author Ravi Mohan
 * 
 */
public class PlFcEntailsDemo {
	private static PLFCEntails plfce = new PLFCEntails();

	public static void main(String[] args) {

		System.out.println("\nPlFcEntailsDemo\n");
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell("P => Q");
		kb.tell("L & M => P");
		kb.tell("B & L => M");
		kb.tell("A & P => L");
		kb.tell("A & B => L");
		kb.tell("A");
		kb.tell("B");

		System.out.println("Example from  page 220 of AIMA 2nd Edition");
		System.out.println("KnowledgeBsse consists of sentences");
		System.out.println("P => Q");
		System.out.println("L & M => P");
		System.out.println("B & L => M");
		System.out.println("A & P => L");
		System.out.println("A & B => L");
		System.out.println("A");
		System.out.println("B");

		displayPLFCEntailment(kb, "Q");
	}

	private static void displayPLFCEntailment(KnowledgeBase kb, String q) {
		System.out.println("Running PLFCEntailment on knowledge base"
				+ " with query " + q + " gives " + plfce.isEntailed(kb, new PropositionSymbol(q)));
	}
}
