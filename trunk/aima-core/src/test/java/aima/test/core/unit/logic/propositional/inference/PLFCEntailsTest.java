package aima.test.core.unit.logic.propositional.inference;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.propositional.inference.PLFCEntails;
import aima.core.logic.propositional.kb.KnowledgeBase;
import aima.core.logic.propositional.parsing.PLParser;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * 
 */
public class PLFCEntailsTest {
	private PLParser parser;
	private PLFCEntails plfce;

	@Before
	public void setUp() {
		parser = new PLParser();
		plfce = new PLFCEntails();
	}

	@Test
	public void testAIMAExample() {
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell("P => Q");
		kb.tell("L & M => P");
		kb.tell("B & L => M");
		kb.tell("A & P => L");
		kb.tell("A & B => L");
		kb.tell("A");
		kb.tell("B");
		PropositionSymbol q = (PropositionSymbol) parser.parse("Q");
		
		Assert.assertEquals(true, plfce.plfcEntails(kb, q));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testKBWithNonDefiniteClauses() {
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell("P => Q");
		kb.tell("L & M => P");
		kb.tell("B & L => M");
		kb.tell("~A & P => L"); // Not a definite clause
		kb.tell("A & B => L");
		kb.tell("A");
		kb.tell("B");
		PropositionSymbol q = (PropositionSymbol) parser.parse("Q");
		
		Assert.assertEquals(true, plfce.plfcEntails(kb, q));
	}
}