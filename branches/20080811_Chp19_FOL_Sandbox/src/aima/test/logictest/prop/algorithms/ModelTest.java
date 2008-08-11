/*
 * Created on Sep 2, 2003 by Ravi Mohan
 *  
 */
package aima.test.logictest.prop.algorithms;

import junit.framework.TestCase;
import aima.logic.propositional.algorithms.Model;
import aima.logic.propositional.parsing.PEParser;
import aima.logic.propositional.parsing.ast.Sentence;
import aima.logic.propositional.parsing.ast.Symbol;

/**
 * @author Ravi Mohan
 * 
 */

public class ModelTest extends TestCase {
	private Model m;

	private PEParser parser;

	Sentence trueSentence, falseSentence, andSentence, orSentence,
			impliedSentence, biConditionalSentence;

	@Override
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

	public void testEmptyModel() {
		assertEquals(null, m.getStatus(new Symbol("P")));
		assertEquals(true, m.isUnknown(new Symbol("P")));
	}

	public void testExtendModel() {
		String p = "P";
		m = m.extend(new Symbol(p), true);
		assertEquals(Boolean.TRUE, m.getStatus(new Symbol("P")));
	}

	public void testTrueFalseEvaluation() {
		assertEquals(true, m.isTrue(trueSentence));
		assertEquals(false, m.isFalse(trueSentence));
		assertEquals(false, m.isTrue(falseSentence));
		assertEquals(true, m.isFalse(falseSentence));
	}

	public void testSentenceStatusWhenPTrueAndQTrue() {
		String p = "P";
		String q = "Q";
		m = m.extend(new Symbol(p), true);
		m = m.extend(new Symbol(q), true);
		assertEquals(true, m.isTrue(andSentence));
		assertEquals(true, m.isTrue(orSentence));
		assertEquals(true, m.isTrue(impliedSentence));
		assertEquals(true, m.isTrue(biConditionalSentence));
	}

	public void testSentenceStatusWhenPFalseAndQFalse() {
		String p = "P";
		String q = "Q";
		m = m.extend(new Symbol(p), false);
		m = m.extend(new Symbol(q), false);
		assertEquals(true, m.isFalse(andSentence));
		assertEquals(true, m.isFalse(orSentence));
		assertEquals(true, m.isTrue(impliedSentence));
		assertEquals(true, m.isTrue(biConditionalSentence));
	}

	public void testSentenceStatusWhenPTrueAndQFalse() {
		String p = "P";
		String q = "Q";
		m = m.extend(new Symbol(p), true);
		m = m.extend(new Symbol(q), false);
		assertEquals(true, m.isFalse(andSentence));
		assertEquals(true, m.isTrue(orSentence));
		assertEquals(true, m.isFalse(impliedSentence));
		assertEquals(true, m.isFalse(biConditionalSentence));
	}

	public void testSentenceStatusWhenPFalseAndQTrue() {
		String p = "P";
		String q = "Q";
		m = m.extend(new Symbol(p), false);
		m = m.extend(new Symbol(q), true);
		assertEquals(true, m.isFalse(andSentence));
		assertEquals(true, m.isTrue(orSentence));
		assertEquals(true, m.isTrue(impliedSentence));
		assertEquals(true, m.isFalse(biConditionalSentence));
	}

	public void testComplexSentence() {
		String p = "P";
		String q = "Q";
		m = m.extend(new Symbol(p), true);
		m = m.extend(new Symbol(q), false);
		Sentence sent = (Sentence) parser.parse("((P OR Q) AND  (P => Q))");
		Sentence sent2 = (Sentence) parser.parse("((P OR Q) AND  (Q))");
		assertFalse(m.isTrue(sent));
		assertTrue(m.isFalse(sent));
	}

}