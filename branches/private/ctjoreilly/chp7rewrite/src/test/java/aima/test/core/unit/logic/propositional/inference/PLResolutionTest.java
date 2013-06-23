package aima.test.core.unit.logic.propositional.inference;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import aima.core.logic.propositional.inference.PLResolution;
import aima.core.logic.propositional.kb.KnowledgeBase;
import aima.core.logic.propositional.kb.data.Clause;
import aima.core.logic.propositional.parsing.PLParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.visitors.ConvertToConjunctionOfClauses;
import aima.core.util.SetOps;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * 
 */
@RunWith(Parameterized.class)
public class PLResolutionTest {
	private PLResolution resolution;
	private PLParser parser;
	
	@Parameters(name = "{index}: discardTautologies={0}")
    public static Collection<Object[]> inferenceAlgorithmSettings() {
        return Arrays.asList(new Object[][] {
        		{false}, // will not discard tautological clauses - slower!
        		{true}   // will discard tautological clauses - faster!
        });
    }

	public PLResolutionTest(boolean discardTautologies) {
		this.resolution = new PLResolution(discardTautologies);
		parser = new PLParser();
	}

	@Test
	public void testPLResolveWithOneLiteralMatching() {
		Clause one = ConvertToConjunctionOfClauses
				.convert(parser.parse("A | B")).getClauses().iterator().next();
		Clause two = ConvertToConjunctionOfClauses
				.convert(parser.parse("~B | C")).getClauses().iterator().next();
		Clause expected = ConvertToConjunctionOfClauses
				.convert(parser.parse("A | C")).getClauses().iterator().next();

		Set<Clause> resolvents = resolution.plResolve(one, two);
		Assert.assertEquals(1, resolvents.size());
		Assert.assertTrue(resolvents.contains(expected));
	}

	@Test
	public void testPLResolveWithNoLiteralMatching() {
		Clause one = ConvertToConjunctionOfClauses
				.convert(parser.parse("A | B")).getClauses().iterator().next();
		Clause two = ConvertToConjunctionOfClauses
				.convert(parser.parse("C | D")).getClauses().iterator().next();

		Set<Clause> resolvents = resolution.plResolve(one, two);
		Assert.assertEquals(0, resolvents.size());
	}

	@Test
	public void testPLResolveWithOneLiteralSentencesMatching() {
		Clause one = ConvertToConjunctionOfClauses.convert(parser.parse("A"))
				.getClauses().iterator().next();
		Clause two = ConvertToConjunctionOfClauses.convert(parser.parse("~A"))
				.getClauses().iterator().next();

		Set<Clause> resolvents = resolution.plResolve(one, two);
		Assert.assertEquals(1, resolvents.size());
		Assert.assertTrue(resolvents.iterator().next().isEmpty());
		Assert.assertTrue(resolvents.iterator().next().isFalse());
	}

	@Test
	public void testPLResolveWithTwoLiteralsMatching() {
		Clause one = ConvertToConjunctionOfClauses
				.convert(parser.parse("~P21 | B11")).getClauses().iterator()
				.next();
		Clause two = ConvertToConjunctionOfClauses
				.convert(parser.parse("~B11 | P21 | P12")).getClauses()
				.iterator().next();
		Set<Clause> expected = ConvertToConjunctionOfClauses.convert(
				parser.parse("(P12 | P21 | ~P21) & (B11 | P12 | ~B11)"))
				.getClauses();

		Set<Clause> resolvents = resolution.plResolve(one, two);

		int numberExpectedResolvents = 2;
		if (resolution.isDiscardTautologies()) {
			numberExpectedResolvents = 0; // due to being tautologies
		}
		Assert.assertEquals(numberExpectedResolvents, resolvents.size());
		Assert.assertEquals(numberExpectedResolvents, SetOps.intersection(expected, resolvents).size());
	}

	@Test
	public void testPLResolve1() {
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell("(B11 => ~P11) & B11");
		Sentence alpha = parser.parse("P11");

		boolean b = resolution.plResolution(kb, alpha);
		Assert.assertEquals(false, b);
	}

	@Test
	public void testPLResolve2() {
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell("A & B");
		Sentence alpha = parser.parse("B");

		boolean b = resolution.plResolution(kb, alpha);
		Assert.assertEquals(true, b);
	}

	@Test
	public void testPLResolve3() {
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell("(B11 => ~P11) & B11");
		Sentence alpha = parser.parse("~P11");

		boolean b = resolution.plResolution(kb, alpha);
		Assert.assertEquals(true, b);
	}

	@Test
	public void testPLResolve4() {
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell("A | B");
		Sentence alpha = parser.parse("B");

		boolean b = resolution.plResolution(kb, alpha);
		Assert.assertEquals(false, b);
	}

	@Test
	public void testPLResolve5() {
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell("(B11 => ~P11) & B11");
		Sentence alpha = parser.parse("~B11");

		boolean b = resolution.plResolution(kb, alpha);
		Assert.assertEquals(false, b);
	}
	
	@Test
	public void testPLResolve6() {
		KnowledgeBase kb = new KnowledgeBase();
		// e.g. from AIMA3e pg. 254
		kb.tell("(B11 <=> P12 | P21) & ~B11");
		Sentence alpha = parser.parse("~P21");

		boolean b = resolution.plResolution(kb, alpha);
		Assert.assertEquals(true, b);
	}

	@Test
	public void testMultipleClauseResolution() {
		// test (and fix) suggested by Huy Dinh. Thanks Huy!
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell("(B11 <=> P12 | P21) & ~B11");
		Sentence alpha = parser.parse("B");

		boolean b = resolution.plResolution(kb, alpha);
		Assert.assertEquals(false, b); // false as KB says nothing about B
	}

	@Test
	public void testPLResolutionWithChadCarfBugReportData() {
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell("B12 <=> P11 | P13 | P22 | P02");
		kb.tell("B21 <=> P20 | P22 | P31 | P11");
		kb.tell("B01 <=> P00 | P02 | P11");
		kb.tell("B10 <=> P11 | P20 | P00");
		kb.tell("~B21");
		kb.tell("~B12");
		kb.tell("B10");
		kb.tell("B01");
		
		Sentence alpha = parser.parse("P00");
		boolean b = resolution.plResolution(kb, alpha);
		Assert.assertEquals(true, b);
	}
	
	@Test
	public void testPLResolutionSucceedsWithChadCarffsBugReport2() {
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell("B10 <=> P11 | P20 | P00");
		kb.tell("B01 <=> P00 | P02 | P11");
		kb.tell("B21 <=> P20 | P22 | P31 | P11");
		kb.tell("B12 <=> P11 | P13 | P22 | P02");
		kb.tell("~B21");
		kb.tell("~B12");
		kb.tell("B10");
		kb.tell("B01");
		
		Sentence alpha = parser.parse("P00");
		boolean b = resolution.plResolution(kb, alpha);
		Assert.assertEquals(true, b);
	}
}