package aima.test.core.unit.logic.propositional.algorithms;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.propositional.algorithms.KnowledgeBase;
import aima.core.logic.propositional.algorithms.PLResolution;
import aima.core.logic.propositional.parsing.PEParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;

/**
 * @author Ravi Mohan
 * 
 */
public class PLResolutionTest {
	private PLResolution resolution;

	private PEParser parser;

	@Before
	public void setUp() {
		resolution = new PLResolution();
		parser = new PEParser();
	}

	@Test
	public void testPLResolveWithOneLiteralMatching() {
		Sentence one = (Sentence) parser.parse("(A OR B)");
		Sentence two = (Sentence) parser.parse("((NOT B) OR C)");
		Sentence expected = (Sentence) parser.parse("(A OR C)");
		Set<Sentence> resolvents = resolution.plResolve(one, two);
		Assert.assertEquals(1, resolvents.size());
		Assert.assertTrue(resolvents.contains(expected));
	}

	@Test
	public void testPLResolveWithNoLiteralMatching() {
		Sentence one = (Sentence) parser.parse("(A OR B)");
		Sentence two = (Sentence) parser.parse("(C OR D)");
		Set<Sentence> resolvents = resolution.plResolve(one, two);
		Assert.assertEquals(0, resolvents.size());
	}

	@Test
	public void testPLResolveWithOneLiteralSentencesMatching() {
		Sentence one = (Sentence) parser.parse("A");
		Sentence two = (Sentence) parser.parse("(NOT A)");
		// Sentence expected =(Sentence) parser.parse("(A OR C)");
		Set<Sentence> resolvents = resolution.plResolve(one, two);
		Assert.assertEquals(1, resolvents.size());
		Assert.assertTrue(resolvents.contains(new Symbol("EMPTY_CLAUSE")));
	}

	@Test
	public void testPLResolveWithTwoLiteralsMatching() {
		Sentence one = (Sentence) parser.parse("((NOT P21) OR B11)");
		Sentence two = (Sentence) parser.parse("(((NOT B11) OR P21) OR P12)");
		Sentence expected1 = (Sentence) parser
				.parse("(  ( P12 OR P21 ) OR  ( NOT P21 )  )");
		Sentence expected2 = (Sentence) parser
				.parse("(  ( B11 OR P12 ) OR  ( NOT B11 )  )");
		Set<Sentence> resolvents = resolution.plResolve(one, two);

		Assert.assertEquals(2, resolvents.size());
		Assert.assertTrue(resolvents.contains(expected1));
		Assert.assertTrue(resolvents.contains(expected2));
	}

	@Test
	public void testPLResolve1() {
		boolean b = resolution.plResolution("((B11 =>  (NOT P11)) AND B11)",
				"(P11)");
		Assert.assertEquals(false, b);
	}

	@Test
	public void testPLResolve2() {
		boolean b = resolution.plResolution("(A AND B)", "B");
		Assert.assertEquals(true, b);
	}

	@Test
	public void testPLResolve3() {
		boolean b = resolution.plResolution("((B11 =>  (NOT P11)) AND B11)",
				"(NOT P11)");
		Assert.assertEquals(true, b);
	}

	@Test
	public void testPLResolve4() {
		boolean b = resolution.plResolution("(A OR B)", "B");
		Assert.assertEquals(false, b);
	}

	@Test
	public void testPLResolve5() {
		boolean b = resolution.plResolution("((B11 =>  (NOT P11)) AND B11)",
				"(NOT B11)");
		Assert.assertEquals(false, b);
	}

	@Test
	public void testMultipleClauseResolution() {
		// test (and fix) suggested by Huy Dinh. Thanks Huy!
		PLResolution plr = new PLResolution();
		KnowledgeBase kb = new KnowledgeBase();
		String fact = "((B11 <=> (P12 OR P21)) AND (NOT B11))";
		kb.tell(fact);
		plr.plResolution(kb, "(B)");
	}

	// public void testPLResolutionWithChadCarfBugReportData() {
	// commented out coz this needs a major fix wait for a rewrite
	// KnowledgeBase kb = new KnowledgeBase();
	// kb.tell("(B12 <=> (P11 OR (P13 OR (P22 OR P02))))");
	// kb.tell("(B21 <=> (P20 OR (P22 OR (P31 OR P11))))");
	// kb.tell("(B01 <=> (P00 OR (P02 OR P11)))");
	// kb.tell("(B10 <=> (P11 OR (P20 OR P00)))");
	// kb.tell("(NOT B21)");
	// kb.tell("(NOT B12)");
	// kb.tell("(B10)");
	// kb.tell("(B01)");
	// assertTrue(resolution.plResolution(kb.asSentence().toString(), "(P00)"));
	// //assertFalse(kb.askWithDpll("(NOT P00)"));
	//		
	//		
	// }
}