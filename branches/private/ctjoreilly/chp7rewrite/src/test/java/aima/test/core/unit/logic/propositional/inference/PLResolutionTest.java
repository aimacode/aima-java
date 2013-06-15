package aima.test.core.unit.logic.propositional.inference;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import aima.core.logic.propositional.inference.PLResolution;
import aima.core.logic.propositional.kb.data.Clause;
import aima.core.logic.propositional.parsing.PLParser;
import aima.core.logic.propositional.visitors.ConvertToConjunctionOfClauses;
import aima.core.util.SetOps;

/**
 * @author Ravi Mohan
 * 
 */
public class PLResolutionTest {
	private PLResolution resolution;

	private PLParser parser;

	@Before
	public void setUp() {
		resolution = new PLResolution();
		parser = new PLParser();
	}

	@Test
	public void testPLResolveWithOneLiteralMatching() {
		Clause one =  ConvertToConjunctionOfClauses.convert(parser.parse("A | B")).getClauses().iterator().next();
		Clause two =  ConvertToConjunctionOfClauses.convert(parser.parse("~B | C")).getClauses().iterator().next();
		Clause expected =  ConvertToConjunctionOfClauses.convert(parser.parse("A | C")).getClauses().iterator().next();
		
		Set<Clause> resolvents = resolution.plResolve(one, two);
		Assert.assertEquals(1, resolvents.size());
		Assert.assertTrue(resolvents.contains(expected));
	}

	@Test
	public void testPLResolveWithNoLiteralMatching() {
		Clause one =  ConvertToConjunctionOfClauses.convert(parser.parse("A | B")).getClauses().iterator().next();
		Clause two =  ConvertToConjunctionOfClauses.convert(parser.parse("C | D")).getClauses().iterator().next();

		Set<Clause> resolvents = resolution.plResolve(one, two);
		Assert.assertEquals(0, resolvents.size());
	}

	@Test
	public void testPLResolveWithOneLiteralSentencesMatching() {
		Clause one =  ConvertToConjunctionOfClauses.convert(parser.parse("A")).getClauses().iterator().next();
		Clause two =  ConvertToConjunctionOfClauses.convert(parser.parse("~A")).getClauses().iterator().next();

		Set<Clause> resolvents = resolution.plResolve(one, two);
		Assert.assertEquals(1, resolvents.size());
		Assert.assertTrue(resolvents.iterator().next().isEmpty());
		Assert.assertTrue(resolvents.iterator().next().isFalse());
	}

	@Test
	public void testPLResolveWithTwoLiteralsMatching() {
		Clause one =  ConvertToConjunctionOfClauses.convert(parser.parse("~P21 | B11")).getClauses().iterator().next();
		Clause two =  ConvertToConjunctionOfClauses.convert(parser.parse("~B11 | P21 | P12")).getClauses().iterator().next();
		Set<Clause> expected = ConvertToConjunctionOfClauses.convert(parser.parse("(P12 | P21 | ~P21) & (B11 | P12 | ~B11)")).getClauses();

		Set<Clause> resolvents = resolution.plResolve(one, two);

		Assert.assertEquals(2, resolvents.size());
		Assert.assertEquals(2, SetOps.intersection(expected, resolvents).size());
	}

	@Ignore("TODO")
	@Test
	public void testPLResolve1() {
//		boolean b = resolution.plResolution("((B11 =>  ~P11) & B11)", "P11");
//		Assert.assertEquals(false, b);
	}

	@Ignore("TODO")
	@Test
	public void testPLResolve2() {
//		boolean b = resolution.plResolution("A & B", "B");
//		Assert.assertEquals(true, b);
	}

	@Ignore("TODO")
	@Test
	public void testPLResolve3() {
//		boolean b = resolution.plResolution("(B11 =>  ~P11) & B11", "~P11");
//		Assert.assertEquals(true, b);
	}

	@Ignore("TODO")
	@Test
	public void testPLResolve4() {
//		boolean b = resolution.plResolution("A | B", "B");
//		Assert.assertEquals(false, b);
	}

	@Ignore("TODO")
	@Test
	public void testPLResolve5() {
//		boolean b = resolution.plResolution("(B11 =>  ~P11) & B11", "~B11");
//		Assert.assertEquals(false, b);
	}

	@Ignore("TODO")
	@Test
	public void testMultipleClauseResolution() {
//		// test (and fix) suggested by Huy Dinh. Thanks Huy!
//		PLResolution plr = new PLResolution();
//		KnowledgeBase kb = new KnowledgeBase();
//		String fact = "(B11 <=> P12 | P21) & ~B11";
//		kb.tell(fact);
//		plr.plResolution(kb, "B");
	}

	// TODO
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