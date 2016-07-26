package aima.test.unit.logic.propositional.inference;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.basic.propositional.inference.TTEntails;
import aima.core.logic.basic.propositional.kb.BasicKnowledgeBase;
import aima.core.logic.basic.propositional.kb.data.Model;
import aima.core.logic.basic.propositional.parsing.ast.Sentence;
import aima.extra.logic.propositional.parser.PLParserWrapper;
import aima.core.logic.basic.propositional.parsing.ast.PropositionSymbol;

/**
 * @author Ravi Mohan
 * @author Anurag Rai
 */
public class TTEntailsTest {
	
	TTEntails tte;
	BasicKnowledgeBase kb;

	@Before
	public void setUp() {
		tte = new TTEntails();
		kb = new BasicKnowledgeBase(new PLParserWrapper());
	}

	@Test
	public void testSimpleSentence1() {
		kb.tell("A & B");
		Assert.assertEquals(true, tte.ttEntails(kb, "A", new PLParserWrapper()));
	}

	@Test
	public void testSimpleSentence2() {
		kb.tell("A | B");
		Assert.assertEquals(false, tte.ttEntails(kb, "A", new PLParserWrapper()));
	}

	@Test
	public void testSimpleSentence3() {
		kb.tell("(A => B) & A");
		Assert.assertEquals(true, tte.ttEntails(kb, "B", new PLParserWrapper()));
	}

	@Test
	public void testSimpleSentence4() {
		kb.tell("(A => B) & B");
		Assert.assertEquals(false, tte.ttEntails(kb, "A", new PLParserWrapper()));
	}

	@Test
	public void testSimpleSentence5() {
		kb.tell("A");
		Assert.assertEquals(false, tte.ttEntails(kb, "~A", new PLParserWrapper()));
	}

	@Test
	public void testSUnkownSymbol() {
		kb.tell("(A => B) & B");
		Assert.assertEquals(false, tte.ttEntails(kb, "X", new PLParserWrapper()));
	}

	@Test
	public void testSimpleSentence6() {
		kb.tell("~A");
		Assert.assertEquals(false, tte.ttEntails(kb, "A", new PLParserWrapper()));
	}

	@Test
	public void testNewAIMAExample() {
		kb.tell("~P11");
		kb.tell("B11 <=> P12 | P21");
		kb.tell("B21 <=> P11 | P22 | P31");
		kb.tell("~B11");
		kb.tell("B21");

		Assert.assertEquals(true, tte.ttEntails(kb, "~P12", new PLParserWrapper()));
		Assert.assertEquals(false, tte.ttEntails(kb, "P22", new PLParserWrapper()));
	}

	@Test
	public void testTTEntailsSucceedsWithChadCarffsBugReport() {
		BasicKnowledgeBase kb = new BasicKnowledgeBase(new PLParserWrapper());
		kb.tell("B12 <=> P11 | P13 | P22 | P02");
		kb.tell("B21 <=> P20 | P22 | P31 | P11");
		kb.tell("B01 <=> P00 | P02 | P11");
		kb.tell("B10 <=> P11 | P20 | P00");
		kb.tell("~B21");
		kb.tell("~B12");
		kb.tell("B10");
		kb.tell("B01");

		Assert.assertTrue(tte.ttEntails(kb, "P00", new PLParserWrapper()));
		Assert.assertFalse(tte.ttEntails(kb, "~P00", new PLParserWrapper()));
	}

	@Test
	public void testDoesNotKnow() {
		BasicKnowledgeBase kb = new BasicKnowledgeBase(new PLParserWrapper());
		kb.tell("A");
		Assert.assertFalse(tte.ttEntails(kb, "B", new PLParserWrapper()));
		Assert.assertFalse(tte.ttEntails(kb, "~B", new PLParserWrapper()));
	}

	public void testTTEntailsSucceedsWithCStackOverFlowBugReport() {
		BasicKnowledgeBase kb = new BasicKnowledgeBase(new PLParserWrapper());

		Assert.assertTrue(tte.ttEntails(kb, "((A | (~ A)) & (A | B))", new PLParserWrapper()));
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