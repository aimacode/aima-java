package aima.test.core.unit.logic.propositional.inference;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.propositional.inference.TTEntails;
import aima.core.logic.propositional.kb.KnowledgeBase;
import aima.core.logic.propositional.kb.data.Model;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;

/**
 * @author Ravi Mohan
 * 
 */
public class TTEntailsTest {
	TTEntails tte;

	KnowledgeBase kb;

	@Before
	public void setUp() {
		tte = new TTEntails();
		kb = new KnowledgeBase();
	}

	@Test
	public void testSimpleSentence1() {
		kb.tell("A & B");
		Assert.assertEquals(true, kb.askWithTTEntails("A"));
	}

	@Test
	public void testSimpleSentence2() {
		kb.tell("A | B");
		Assert.assertEquals(false, kb.askWithTTEntails("A"));
	}

	@Test
	public void testSimpleSentence3() {
		kb.tell("(A => B) & A");
		Assert.assertEquals(true, kb.askWithTTEntails("B"));
	}

	@Test
	public void testSimpleSentence4() {
		kb.tell("(A => B) & B");
		Assert.assertEquals(false, kb.askWithTTEntails("A"));
	}

	@Test
	public void testSimpleSentence5() {
		kb.tell("A");
		Assert.assertEquals(false, kb.askWithTTEntails("~A"));
	}

	@Test
	public void testSUnkownSymbol() {
		kb.tell("(A => B) & B");
		Assert.assertEquals(false, kb.askWithTTEntails("X"));
	}

	@Test
	public void testSimpleSentence6() {
		kb.tell("~A");
		Assert.assertEquals(false, kb.askWithTTEntails("A"));
	}

	@Test
	public void testNewAIMAExample() {
		kb.tell("~P11");
		kb.tell("B11 <=> P12 | P21");
		kb.tell("B21 <=> P11 | P22 | P31");
		kb.tell("~B11");
		kb.tell("B21");

		Assert.assertEquals(true, kb.askWithTTEntails("~P12"));
		Assert.assertEquals(false, kb.askWithTTEntails("P22"));
	}

	@Test
	public void testTTEntailsSucceedsWithChadCarffsBugReport() {
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell("B12 <=> P11 | P13 | P22 | P02");
		kb.tell("B21 <=> P20 | P22 | P31 | P11");
		kb.tell("B01 <=> P00 | P02 | P11");
		kb.tell("B10 <=> P11 | P20 | P00");
		kb.tell("~B21");
		kb.tell("~B12");
		kb.tell("B10");
		kb.tell("B01");

		Assert.assertTrue(kb.askWithTTEntails("P00"));
		Assert.assertFalse(kb.askWithTTEntails("~P00"));
	}

	@Test
	public void testDoesNotKnow() {
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell("A");
		Assert.assertFalse(kb.askWithTTEntails("B"));
		Assert.assertFalse(kb.askWithTTEntails("~B"));
	}

	public void testTTEntailsSucceedsWithCStackOverFlowBugReport() {
		KnowledgeBase kb = new KnowledgeBase();

		Assert.assertTrue(kb.askWithTTEntails("((A | (~ A)) & (A | B))"));
	}

	@Test
	public void testModelEvaluation() {
		kb.tell("~P11");
		kb.tell("B11 <=> P12 | P21");
		kb.tell("B21 <=> P11 | P22 | P31");
		kb.tell("~B11");
		kb.tell("B21");

		Model model = new Model();
		model = model.union(new PropositionSymbol("B11"), false);
		model = model.union(new PropositionSymbol("B21"), true);
		model = model.union(new PropositionSymbol("P11"), false);
		model = model.union(new PropositionSymbol("P12"), false);
		model = model.union(new PropositionSymbol("P21"), false);
		model = model.union(new PropositionSymbol("P22"), false);
		model = model.union(new PropositionSymbol("P31"), true);

		Sentence kbs = kb.asSentence();
		Assert.assertEquals(true, model.isTrue(kbs));
	}
}