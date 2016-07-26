package aima.test.unit.logic.propositional.inference;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import aima.core.logic.basic.propositional.inference.PLResolution;
import aima.core.logic.basic.propositional.kb.BasicKnowledgeBase;
import aima.core.logic.basic.propositional.kb.data.Clause;
import aima.core.logic.basic.propositional.parsing.PLParser;
import aima.core.logic.basic.propositional.parsing.ast.Sentence;
import aima.core.logic.basic.propositional.visitors.ConvertToConjunctionOfClauses;
import aima.core.util.SetOps;
import aima.extra.logic.propositional.parser.PLParserWrapper;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Anurag Rai
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
		parser = new PLParserWrapper();
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
		BasicKnowledgeBase kb = new BasicKnowledgeBase(new PLParserWrapper());
		kb.tell("(B11 => ~P11) & B11");

		String alpha = "P11";
		Sentence query = parser.parse("P11");
		//test alpha as String
		Assert.assertEquals(false, resolution.plResolution(kb, alpha, new PLParserWrapper()));
		//test alpha as Sentence
		Assert.assertEquals(false, resolution.plResolution(kb, query));
		
	}

	@Test
	public void testPLResolve2() {
		BasicKnowledgeBase kb = new BasicKnowledgeBase(new PLParserWrapper());
		kb.tell("A & B");

		String alpha = "B";
		Sentence query = parser.parse("B");
		//test alpha as String
		Assert.assertEquals(true, resolution.plResolution(kb, alpha, new PLParserWrapper()));
		//test alpha as Sentence
		Assert.assertEquals(true, resolution.plResolution(kb, query));
	}

	@Test
	public void testPLResolve3() {
		BasicKnowledgeBase kb = new BasicKnowledgeBase(new PLParserWrapper());
		kb.tell("(B11 => ~P11) & B11");

		String alpha = "~P11";
		Sentence query = parser.parse("~P11");
		//test alpha as String
		Assert.assertEquals(true, resolution.plResolution(kb, alpha, new PLParserWrapper()));
		//test alpha as Sentence
		Assert.assertEquals(true, resolution.plResolution(kb, query));
	}

	@Test
	public void testPLResolve4() {
		BasicKnowledgeBase kb = new BasicKnowledgeBase(new PLParserWrapper());
		kb.tell("A | B");

		String alpha = "B";
		Sentence query = parser.parse("B");
		//test alpha as String
		Assert.assertEquals(false, resolution.plResolution(kb, alpha, new PLParserWrapper()));
		//test alpha as Sentence
		Assert.assertEquals(false, resolution.plResolution(kb, query));
	}

	@Test
	public void testPLResolve5() {
		BasicKnowledgeBase kb = new BasicKnowledgeBase(new PLParserWrapper());
		kb.tell("(B11 => ~P11) & B11");

		String alpha = "~B11";
		Sentence query = parser.parse("~B11");
		//test alpha as String
		Assert.assertEquals(false, resolution.plResolution(kb, alpha, new PLParserWrapper()));
		//test alpha as Sentence
		Assert.assertEquals(false, resolution.plResolution(kb, query));
	}
	
	@Test
	public void testPLResolve6() {
		BasicKnowledgeBase kb = new BasicKnowledgeBase(new PLParserWrapper());
		// e.g. from AIMA3e pg. 254
		kb.tell("(B11 <=> P12 | P21) & ~B11");

		String alpha = "~P21";
		Sentence query = parser.parse("~P21");
		//test alpha as String
		Assert.assertEquals(true, resolution.plResolution(kb, alpha, new PLParserWrapper()));
		//test alpha as Sentence
		Assert.assertEquals(true, resolution.plResolution(kb, query));
	}
	
	@Test
	public void testPLResolve7() {
		BasicKnowledgeBase kb = new BasicKnowledgeBase(new PLParserWrapper());
		kb.tell("P");
		kb.tell("P => Q");
		kb.tell("(P => Q) => (Q => R)");

		String alpha = "R";
		Sentence query = parser.parse("R");
		//test alpha as String
		Assert.assertEquals(true, resolution.plResolution(kb, alpha, new PLParserWrapper()));
		//test alpha as Sentence
		Assert.assertEquals(true, resolution.plResolution(kb, query));
	}

	@Test
	public void testMultipleClauseResolution() {
		// test (and fix) suggested by Huy Dinh. Thanks Huy!
		BasicKnowledgeBase kb = new BasicKnowledgeBase(new PLParserWrapper());
		kb.tell("(B11 <=> P12 | P21) & ~B11");
		
		String alpha = "B";
		Sentence query = parser.parse("B");
		//test alpha as String
		// false as KB says nothing about B
		Assert.assertEquals(false, resolution.plResolution(kb, alpha, new PLParserWrapper())); 
		//test alpha as Sentence
		Assert.assertEquals(false, resolution.plResolution(kb, query));
	}

	@Test
	public void testPLResolutionWithChadCarfBugReportData() {
		BasicKnowledgeBase kb = new BasicKnowledgeBase(new PLParserWrapper());
		kb.tell("B12 <=> P11 | P13 | P22 | P02");
		kb.tell("B21 <=> P20 | P22 | P31 | P11");
		kb.tell("B01 <=> P00 | P02 | P11");
		kb.tell("B10 <=> P11 | P20 | P00");
		kb.tell("~B21");
		kb.tell("~B12");
		kb.tell("B10");
		kb.tell("B01");
		
		String alpha = "P00";
		Sentence query = parser.parse("P00");
		//test alpha as String
		Assert.assertEquals(true, resolution.plResolution(kb, alpha, new PLParserWrapper()));
		//test alpha as Sentence
		Assert.assertEquals(true, resolution.plResolution(kb, query));
	}
	
	@Test
	public void testPLResolutionSucceedsWithChadCarffsBugReport2() {
		BasicKnowledgeBase kb = new BasicKnowledgeBase(new PLParserWrapper());
		kb.tell("B10 <=> P11 | P20 | P00");
		kb.tell("B01 <=> P00 | P02 | P11");
		kb.tell("B21 <=> P20 | P22 | P31 | P11");
		kb.tell("B12 <=> P11 | P13 | P22 | P02");
		kb.tell("~B21");
		kb.tell("~B12");
		kb.tell("B10");
		kb.tell("B01");
		
		String alpha = "P00";
		Sentence query = parser.parse("P00");
		//test alpha as String
		Assert.assertEquals(true, resolution.plResolution(kb, alpha, new PLParserWrapper()));
		//test alpha as Sentence
		Assert.assertEquals(true, resolution.plResolution(kb, query));
	}
}