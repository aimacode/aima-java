package aima.test.core.unit.logic.propositional.algorithms;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.propositional.algorithms.KnowledgeBase;
import aima.core.logic.propositional.algorithms.PLFCEntails;
import aima.core.logic.propositional.parsing.PEParser;

/**
 * @author Ravi Mohan
 * 
 */
public class PLFCEntailsTest {
	PEParser parser;

	PLFCEntails plfce;

	@Before
	public void setUp() {
		plfce = new PLFCEntails();
	}

	@Test
	public void testAIMAExample() {
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell(" (P => Q)");
		kb.tell("((L & M) => P)");
		kb.tell("((B & L) => M)");
		kb.tell("( (A & P) => L)");
		kb.tell("((A & B) => L)");
		kb.tell("(A)");
		kb.tell("(B)");

		Assert.assertEquals(true, plfce.plfcEntails(kb, "Q"));
	}
}