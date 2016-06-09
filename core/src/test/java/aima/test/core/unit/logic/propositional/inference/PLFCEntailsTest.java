package aima.test.core.unit.logic.propositional.inference;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.basic.propositional.inference.PLFCEntails;
import aima.core.logic.basic.propositional.kb.BasicKnowledgeBase;
/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Anurag Rai
 * 
 */
public class PLFCEntailsTest {
	private PLFCEntails plfce;

	@Before
	public void setUp() {
		plfce = new PLFCEntails();
	}

	@Test
	public void testAIMAExample() {
		BasicKnowledgeBase kb = new BasicKnowledgeBase();
		kb.tell("P => Q");
		kb.tell("L & M => P");
		kb.tell("B & L => M");
		kb.tell("A & P => L");
		kb.tell("A & B => L");
		kb.tell("A");
		kb.tell("B");
		
		Assert.assertEquals(true, plfce.plfcEntails(kb, "Q"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testKBWithNonDefiniteClauses() {
		BasicKnowledgeBase kb = new BasicKnowledgeBase();
		kb.tell("P => Q");
		kb.tell("L & M => P");
		kb.tell("B & L => M");
		kb.tell("~A & P => L"); // Not a definite clause
		kb.tell("A & B => L");
		kb.tell("A");
		kb.tell("B");
		
		Assert.assertEquals(true, plfce.plfcEntails(kb, "Q"));
	}
}