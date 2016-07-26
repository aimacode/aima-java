package aima.test.unit.logic.propositional.kb.data;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.basic.propositional.kb.data.ConjunctionOfClauses;
import aima.core.logic.basic.propositional.parsing.PLParser;
import aima.core.logic.basic.propositional.parsing.ast.Sentence;
import aima.core.logic.basic.propositional.visitors.ConvertToConjunctionOfClauses;
import aima.extra.logic.propositional.parser.PLParserWrapper;

/**
 * @author Ravi Mohan
 */
public class ConvertToConjunctionOfClausesTest {
	private PLParser parser = new PLParserWrapper();

	@Before
	public void setUp() {
	}

	@Test
	public void testSymbolTransform() {
		Sentence symbol = parser.parse("A");
		ConjunctionOfClauses transformed = ConvertToConjunctionOfClauses.convert(symbol);
		Assert.assertEquals("{{A}}", transformed.toString());
	}

	@Test
	public void testBasicSentenceTransformation() {
		Sentence and = parser.parse("A & B");
		ConjunctionOfClauses transformedAnd = ConvertToConjunctionOfClauses.convert(and);
		Assert.assertEquals("{{A}, {B}}", transformedAnd.toString());

		Sentence or = parser.parse("A | B");
		ConjunctionOfClauses transformedOr = ConvertToConjunctionOfClauses.convert(or);
		Assert.assertEquals("{{A, B}}", transformedOr.toString());

		Sentence not = parser.parse("~C");
		ConjunctionOfClauses transformedNot = ConvertToConjunctionOfClauses.convert(not);
		Assert.assertEquals("{{~C}}", transformedNot.toString());
	}

	@Test
	public void testImplicationTransformation() {
		Sentence impl = parser.parse("A => B");
		ConjunctionOfClauses transformedImpl = ConvertToConjunctionOfClauses.convert(impl);
		Assert.assertEquals("{{~A, B}}", transformedImpl.toString());
	}

	@Test
	public void testBiConditionalTransformation() {
		Sentence bic = parser.parse("A <=> B");
		ConjunctionOfClauses transformedBic = ConvertToConjunctionOfClauses.convert(bic);
		Assert.assertEquals("{{~A, B}, {~B, A}}", transformedBic.toString());
	}

	@Test
	public void testTwoSuccessiveNotsTransformation() {
		Sentence twoNots = parser.parse("~~A");
		ConjunctionOfClauses transformed = ConvertToConjunctionOfClauses.convert(twoNots);
		Assert.assertEquals("{{A}}", transformed.toString());
	}

	@Test
	public void testThreeSuccessiveNotsTransformation() {
		Sentence threeNots = parser.parse("~~~A");
		ConjunctionOfClauses transformed = ConvertToConjunctionOfClauses.convert(threeNots);
		Assert.assertEquals("{{~A}}", transformed.toString());
	}

	@Test
	public void testFourSuccessiveNotsTransformation() {
		Sentence fourNots = parser.parse("~~~~A");
		ConjunctionOfClauses transformed = ConvertToConjunctionOfClauses.convert(fourNots);
		Assert.assertEquals("{{A}}", transformed.toString());
	}

	@Test
	public void testDeMorgan1() {
		Sentence dm = parser.parse("~(A & B)");
		ConjunctionOfClauses transformed = ConvertToConjunctionOfClauses.convert(dm);
		Assert.assertEquals("{{~A, ~B}}", transformed.toString());
	}

	@Test
	public void testDeMorgan2() {
		Sentence dm = parser.parse("~(A | B)");
		ConjunctionOfClauses transformed = ConvertToConjunctionOfClauses.convert(dm);
		Assert.assertEquals("{{~A}, {~B}}", transformed.toString());
	}

	@Test
	public void testOrDistribution1() {
		Sentence or =  parser.parse("A & B | C)");
		ConjunctionOfClauses transformed = ConvertToConjunctionOfClauses.convert(or);
		Assert.assertEquals("{{A, C}, {B, C}}", transformed.toString());
	}

	@Test
	public void testOrDistribution2() {
		Sentence or = parser.parse("A | B & C");
		ConjunctionOfClauses transformed = ConvertToConjunctionOfClauses.convert(or);
		Assert.assertEquals("{{A, B}, {A, C}}", transformed.toString());
	}

	@Test
	public void testAimaExample() {
		Sentence aimaEg = parser.parse("B11 <=> P12 | P21");
		ConjunctionOfClauses transformed = ConvertToConjunctionOfClauses.convert(aimaEg);
		Assert.assertEquals("{{~B11, P12, P21}, {~P12, B11}, {~P21, B11}}", transformed.toString());
	}
	
	@Test
	public void testNested() {
		Sentence nested = parser.parse("A | (B | (C | (D & E)))");
		ConjunctionOfClauses transformed = ConvertToConjunctionOfClauses.convert(nested);
		Assert.assertEquals("{{A, B, C, D}, {A, B, C, E}}", transformed.toString());
		
		nested = parser.parse("A | (B | (C & (D & E)))");
		transformed = ConvertToConjunctionOfClauses.convert(nested);
		Assert.assertEquals("{{A, B, C}, {A, B, D}, {A, B, E}}", transformed.toString());
		
		nested = parser.parse("A | (B | (C & (D & (E | F))))");
		transformed = ConvertToConjunctionOfClauses.convert(nested);
		Assert.assertEquals("{{A, B, C}, {A, B, D}, {A, B, E, F}}", transformed.toString());
		
		nested = parser.parse("(A | (B | (C & D))) | E | (F | (G | (H & I)))");
		transformed = ConvertToConjunctionOfClauses.convert(nested);
		Assert.assertEquals("{{A, B, C, E, F, G, H}, {A, B, D, E, F, G, H}, {A, B, C, E, F, G, I}, {A, B, D, E, F, G, I}}", transformed.toString());
		
		nested = parser.parse("(((~P | ~Q) => ~(P | Q)) => R)");
		transformed = ConvertToConjunctionOfClauses.convert(nested);
		Assert.assertEquals("{{~P, ~Q, R}, {P, Q, R}}", transformed.toString());
		
		nested = parser.parse("~(((~P | ~Q) => ~(P | Q)) => R)");
		transformed = ConvertToConjunctionOfClauses.convert(nested);
		Assert.assertEquals("{{P, ~P}, {Q, ~P}, {P, ~Q}, {Q, ~Q}, {~R}}", transformed.toString());
	}
	
	@Test
	public void testIssue78() {
		// (  ( NOT J1007 )  OR  ( NOT ( OR J1008 J1009 J1010 J1011 J1012 J1013 J1014 J1015  )  )  )
		Sentence issue78Eg = parser.parse("(  ( ~ J1007 )  |  ( ~ ( J1008 | J1009 | J1010 | J1011 | J1012 | J1013 | J1014 | J1015  )  ) )");
		ConjunctionOfClauses transformed = ConvertToConjunctionOfClauses.convert(issue78Eg);
		Assert.assertEquals("{{~J1007, ~J1008}, {~J1007, ~J1009}, {~J1007, ~J1010}, {~J1007, ~J1011}, {~J1007, ~J1012}, {~J1007, ~J1013}, {~J1007, ~J1014}, {~J1007, ~J1015}}", transformed.toString());
	}
	
}
