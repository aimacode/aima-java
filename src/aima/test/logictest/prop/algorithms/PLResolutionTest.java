/*
 * Created on Dec 8, 2004
 *
 */
package aima.test.logictest.prop.algorithms;

import java.util.Set;

import junit.framework.TestCase;
import aima.logic.propositional.algorithms.PLResolution;
import aima.logic.propositional.parsing.PEParser;
import aima.logic.propositional.parsing.ast.Sentence;
import aima.logic.propositional.parsing.ast.Symbol;

/**
 * @author Ravi Mohan
 * 
 */

public class PLResolutionTest extends TestCase {
	private PLResolution resolution;

	private PEParser parser;

	@Override
	public void setUp() {
		resolution = new PLResolution();
		parser = new PEParser();
	}

	public void testPLResolveWithOneLiteralMatching() {
		Sentence one = (Sentence) parser.parse("(A OR B)");
		Sentence two = (Sentence) parser.parse("((NOT B) OR C)");
		Sentence expected = (Sentence) parser.parse("(A OR C)");
		Set resolvents = resolution.plResolve(one, two);
		assertEquals(1, resolvents.size());
		assertTrue(resolvents.contains(expected));
	}

	public void testPLResolveWithNoLiteralMatching() {
		Sentence one = (Sentence) parser.parse("(A OR B)");
		Sentence two = (Sentence) parser.parse("(C OR D)");
		Set resolvents = resolution.plResolve(one, two);
		assertEquals(0, resolvents.size());
	}

	public void testPLResolveWithOneLiteralSentencesMatching() {
		Sentence one = (Sentence) parser.parse("A");
		Sentence two = (Sentence) parser.parse("(NOT A)");
		// Sentence expected =(Sentence) parser.parse("(A OR C)");
		Set resolvents = resolution.plResolve(one, two);
		assertEquals(1, resolvents.size());
		assertTrue(resolvents.contains(new Symbol("EMPTY_CLAUSE")));
	}

	public void testPLResolveWithTwoLiteralsMatching() {
		Sentence one = (Sentence) parser.parse("((NOT P21) OR B11)");
		Sentence two = (Sentence) parser.parse("(((NOT B11) OR P21) OR P12)");
		Sentence expected1 = (Sentence) parser
				.parse("(  ( P12 OR P21 ) OR  ( NOT P21 )  )");
		Sentence expected2 = (Sentence) parser
				.parse("(  ( B11 OR P12 ) OR  ( NOT B11 )  )");
		Set resolvents = resolution.plResolve(one, two);

		assertEquals(2, resolvents.size());
		assertTrue(resolvents.contains(expected1));
		assertTrue(resolvents.contains(expected2));
	}

	public void testPLResolve1() {
		boolean b = resolution.plResolution("((B11 =>  (NOT P11)) AND B11)",
				"(P11)");
		assertEquals(false, b);
	}

	public void testPLResolve2() {
		boolean b = resolution.plResolution("(A AND B)", "B");
		assertEquals(true, b);
	}

	public void testPLResolve3() {
		boolean b = resolution.plResolution("((B11 =>  (NOT P11)) AND B11)",
				"(NOT P11)");
		assertEquals(true, b);
	}

	public void testPLResolve4() {
		boolean b = resolution.plResolution("(A OR B)", "B");
		assertEquals(false, b);
	}

	public void testPLResolve5() {
		boolean b = resolution.plResolution("((B11 =>  (NOT P11)) AND B11)",
				"(NOT B11)");
		assertEquals(false, b);
	}

	// public void testPLResolutionWithChadCarfBugReportData(){
	// //commented out coz too slow..
	// KnowledgeBase kb = new KnowledgeBase();
	// kb.tell("(B12 <=> (P11 OR (P13 OR (P22 OR P02))))");
	// kb.tell("(B21 <=> (P20 OR (P22 OR (P31 OR P11))))");
	// kb.tell("(B01 <=> (P00 OR (P02 OR P11)))");
	// kb.tell("(B10 <=> (P11 OR (P20 OR P00)))");
	// kb.tell("(NOT B21)");
	// kb.tell("(NOT B12)");
	// kb.tell("(B10)");
	// kb.tell("(B01)");
	// assertTrue(resolution.plResolution(kb.asSentence().toString(),"(P00)"));
	// //assertFalse(kb.askWithDpll("(NOT P00)"));
	//		
	//		
	// }

}