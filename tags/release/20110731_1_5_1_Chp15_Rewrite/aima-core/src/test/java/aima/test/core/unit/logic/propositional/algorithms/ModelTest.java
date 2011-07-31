package aima.test.core.unit.logic.propositional.algorithms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.propositional.algorithms.Model;
import aima.core.logic.propositional.parsing.PEParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;

/**
 * @author Ravi Mohan
 * 
 */
public class ModelTest {
	private Model m;

	private PEParser parser;

	Sentence trueSentence, falseSentence, andSentence, orSentence,
			impliedSentence, biConditionalSentence;

	@Before
	public void setUp() {
		parser = new PEParser();
		trueSentence = (Sentence) parser.parse("true");
		falseSentence = (Sentence) parser.parse("false");
		andSentence = (Sentence) parser.parse("(P  AND  Q)");
		orSentence = (Sentence) parser.parse("(P  OR  Q)");
		impliedSentence = (Sentence) parser.parse("(P  =>  Q)");
		biConditionalSentence = (Sentence) parser.parse("(P  <=>  Q)");
		m = new Model();
	}

	@Test
	public void testEmptyModel() {
		Assert.assertEquals(null, m.getStatus(new Symbol("P")));
		Assert.assertEquals(true, m.isUnknown(new Symbol("P")));
	}

	@Test
	public void testExtendModel() {
		String p = "P";
		m = m.extend(new Symbol(p), true);
		Assert.assertEquals(Boolean.TRUE, m.getStatus(new Symbol("P")));
	}

	@Test
	public void testTrueFalseEvaluation() {
		Assert.assertEquals(true, m.isTrue(trueSentence));
		Assert.assertEquals(false, m.isFalse(trueSentence));
		Assert.assertEquals(false, m.isTrue(falseSentence));
		Assert.assertEquals(true, m.isFalse(falseSentence));
	}

	@Test
	public void testSentenceStatusWhenPTrueAndQTrue() {
		String p = "P";
		String q = "Q";
		m = m.extend(new Symbol(p), true);
		m = m.extend(new Symbol(q), true);
		Assert.assertEquals(true, m.isTrue(andSentence));
		Assert.assertEquals(true, m.isTrue(orSentence));
		Assert.assertEquals(true, m.isTrue(impliedSentence));
		Assert.assertEquals(true, m.isTrue(biConditionalSentence));
	}

	@Test
	public void testSentenceStatusWhenPFalseAndQFalse() {
		String p = "P";
		String q = "Q";
		m = m.extend(new Symbol(p), false);
		m = m.extend(new Symbol(q), false);
		Assert.assertEquals(true, m.isFalse(andSentence));
		Assert.assertEquals(true, m.isFalse(orSentence));
		Assert.assertEquals(true, m.isTrue(impliedSentence));
		Assert.assertEquals(true, m.isTrue(biConditionalSentence));
	}

	@Test
	public void testSentenceStatusWhenPTrueAndQFalse() {
		String p = "P";
		String q = "Q";
		m = m.extend(new Symbol(p), true);
		m = m.extend(new Symbol(q), false);
		Assert.assertEquals(true, m.isFalse(andSentence));
		Assert.assertEquals(true, m.isTrue(orSentence));
		Assert.assertEquals(true, m.isFalse(impliedSentence));
		Assert.assertEquals(true, m.isFalse(biConditionalSentence));
	}

	@Test
	public void testSentenceStatusWhenPFalseAndQTrue() {
		String p = "P";
		String q = "Q";
		m = m.extend(new Symbol(p), false);
		m = m.extend(new Symbol(q), true);
		Assert.assertEquals(true, m.isFalse(andSentence));
		Assert.assertEquals(true, m.isTrue(orSentence));
		Assert.assertEquals(true, m.isTrue(impliedSentence));
		Assert.assertEquals(true, m.isFalse(biConditionalSentence));
	}

	@Test
	public void testComplexSentence() {
		String p = "P";
		String q = "Q";
		m = m.extend(new Symbol(p), true);
		m = m.extend(new Symbol(q), false);
		Sentence sent = (Sentence) parser.parse("((P OR Q) AND  (P => Q))");
		Assert.assertFalse(m.isTrue(sent));
		Assert.assertTrue(m.isFalse(sent));
		Sentence sent2 = (Sentence) parser.parse("((P OR Q) AND  (Q))");
		Assert.assertFalse(m.isTrue(sent2));
		Assert.assertTrue(m.isFalse(sent2));
	}
}