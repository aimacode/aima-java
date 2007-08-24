package aima.test.logictest.prop.algorithms;

import junit.framework.TestCase;
import aima.logic.propositional.algorithms.KnowledgeBase;
import aima.logic.propositional.algorithms.PLFCEntails;
import aima.logic.propositional.parsing.PEParser;

/**
 * @author Ravi Mohan
 * 
 */

public class PLFCEntailsTest extends TestCase {
	PEParser parser;

	PLFCEntails plfce;

	@Override
	public void setUp() {
		plfce = new PLFCEntails();
	}

	public void testAIMAExample() {
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell(" (P => Q)");
		kb.tell("((L AND M) => P)");
		kb.tell("((B AND L) => M)");
		kb.tell("( (A AND P) => L)");
		kb.tell("((A AND B) => L)");
		kb.tell("(A)");
		kb.tell("(B)");

		assertEquals(true, plfce.plfcEntails(kb, "Q"));

	}

}