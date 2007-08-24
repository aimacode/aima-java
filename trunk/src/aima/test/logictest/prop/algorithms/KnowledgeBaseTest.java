/*
 * Created on Dec 3, 2004
 *
 */
package aima.test.logictest.prop.algorithms;

import junit.framework.TestCase;
import aima.logic.propositional.algorithms.KnowledgeBase;

/**
 * @author Ravi Mohan
 * 
 */

public class KnowledgeBaseTest extends TestCase {
	private KnowledgeBase kb;

	@Override
	public void setUp() {
		kb = new KnowledgeBase();
	}

	public void testTellInsertsSentence() {
		kb.tell("(A AND B)");
		assertEquals(1, kb.size());
	}

	public void testTellDoesNotInsertSameSentenceTwice() {
		kb.tell("(A AND B)");
		assertEquals(1, kb.size());
		kb.tell("(A AND B)");
		assertEquals(1, kb.size());
	}

	public void testEmptyKnowledgeBaseIsAnEmptyString() {
		assertEquals("", kb.toString());
	}

	public void testKnowledgeBaseWithOneSentenceToString() {
		kb.tell("(A AND B)");
		assertEquals(" ( A AND B )", kb.toString());
	}

	public void testKnowledgeBaseWithTwoSentencesToString() {
		kb.tell("(A AND B)");
		kb.tell("(C AND D)");
		assertEquals(" (  ( A AND B ) AND  ( C AND D ) )", kb.toString());
	}

	public void testKnowledgeBaseWithThreeSentencesToString() {
		kb.tell("(A AND B)");
		kb.tell("(C AND D)");
		kb.tell("(E AND F)");
		assertEquals(
				" (  (  ( A AND B ) AND  ( C AND D ) ) AND  ( E AND F ) )", kb
						.toString());
	}
}
