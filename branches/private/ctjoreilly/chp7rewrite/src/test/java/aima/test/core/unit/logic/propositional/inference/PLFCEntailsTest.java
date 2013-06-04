package aima.test.core.unit.logic.propositional.inference;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.propositional.inference.PLFCEntails;
import aima.core.logic.propositional.kb.KnowledgeBase;
import aima.core.logic.propositional.parsing.PLParser;

/**
 * @author Ravi Mohan
 * 
 */
public class PLFCEntailsTest {
	PLParser parser;

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