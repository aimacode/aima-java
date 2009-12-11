/*
 * Created on Dec 4, 2004
 *
 */
package aima.test.logictest.prop.algorithms;

import junit.framework.TestCase;
import aima.logic.propositional.algorithms.KnowledgeBase;
import aima.logic.propositional.algorithms.Model;
import aima.logic.propositional.algorithms.TTEntails;
import aima.logic.propositional.parsing.ast.Sentence;
import aima.logic.propositional.parsing.ast.Symbol;

/**
 * @author Ravi Mohan
 * 
 */

public class TTEntailsTest extends TestCase {
	TTEntails tte;

	KnowledgeBase kb;

	@Override
	public void setUp() {
		tte = new TTEntails();
		kb = new KnowledgeBase();
	}

	public void testSimpleSentence1() {
		kb.tell("(A AND B)");
		assertEquals(true, kb.askWithTTEntails("A"));
	}

	public void testSimpleSentence2() {
		kb.tell("(A OR B)");
		assertEquals(false, kb.askWithTTEntails("A"));
	}

	public void testSimpleSentence3() {
		kb.tell("((A => B) AND A)");
		assertEquals(true, kb.askWithTTEntails("B"));
	}

	public void testSimpleSentence4() {
		kb.tell("((A => B) AND B)");
		assertEquals(false, kb.askWithTTEntails("A"));
	}

	public void testSimpleSentence5() {
		kb.tell("A");
		assertEquals(false, kb.askWithTTEntails("NOT A"));
	}

	public void testSUnkownSymbol() {
		kb.tell("((A => B) AND B)");
		assertEquals(false, kb.askWithTTEntails("X"));
	}

	public void testSimpleSentence6() {
		kb.tell("NOT A");
		assertEquals(false, kb.askWithTTEntails("A"));
	}

	public void testNewAIMAExample() {
		kb.tell("(NOT P11)");
		kb.tell("(B11 <=> (P12 OR P21))");
		kb.tell("(B21 <=> ((P11 OR P22) OR P31))");
		kb.tell("(NOT B11)");
		kb.tell("(B21)");

		assertEquals(true, kb.askWithTTEntails("NOT P12"));
		assertEquals(false, kb.askWithTTEntails("P22"));
	}

	public void testTTEntailsSucceedsWithChadCarffsBugReport() {
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell("(B12 <=> (P11 OR (P13 OR (P22 OR P02))))");
		kb.tell("(B21 <=> (P20 OR (P22 OR (P31 OR P11))))");
		kb.tell("(B01 <=> (P00 OR (P02 OR P11)))");
		kb.tell("(B10 <=> (P11 OR (P20 OR P00)))");
		kb.tell("(NOT B21)");
		kb.tell("(NOT B12)");
		kb.tell("(B10)");
		kb.tell("(B01)");

		assertTrue(kb.askWithTTEntails("(P00)"));
		assertFalse(kb.askWithTTEntails("(NOT P00)"));

	}

	// public void testTTEntailsSucceedsWithCStackOverFlowBugReport() {
	// KnowledgeBase kb = new KnowledgeBase();
	//
	// assertTrue(kb.askWithTTEntails("((A OR (NOT A)) AND (A OR B))"));
	// }

	public void testModelEvaluation() {
		kb.tell("(NOT P11)");
		kb.tell("(B11 <=> (P12 OR P21))");
		kb.tell("(B21 <=> ((P11 OR P22) OR P31))");
		kb.tell("(NOT B11)");
		kb.tell("(B21)");

		Model model = new Model();
		model = model.extend(new Symbol("B11"), false);
		model = model.extend(new Symbol("B21"), true);
		model = model.extend(new Symbol("P11"), false);
		model = model.extend(new Symbol("P12"), false);
		model = model.extend(new Symbol("P21"), false);
		model = model.extend(new Symbol("P22"), false);
		model = model.extend(new Symbol("P31"), true);

		Sentence kbs = kb.asSentence();
		assertEquals(true, model.isTrue(kbs));
	}

}