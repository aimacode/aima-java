package aima.test.core.unit.logic.propositional.visitors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.propositional.parsing.PLParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.visitors.ConvertToCNF;

/**
 * @author Ravi Mohan
 * 
 */
public class ConvertToCNFTest {
	private PLParser parser = new PLParser();

	private ConvertToCNF transformer;

	@Before
	public void setUp() {
		transformer = new ConvertToCNF();
	}

	@Test
	public void testSymbolTransform() {
		Sentence symbol = (Sentence) parser.parse("A");
		Sentence transformed = transformer.convert(symbol);
		Assert.assertEquals("A", transformed.toString());
	}

	@Test
	public void testBasicSentenceTransformation() {
		Sentence and = (Sentence) parser.parse("A & B");
		Sentence transformedAnd = transformer.convert(and);
		Assert.assertEquals(and.toString(), transformedAnd.toString());

		Sentence or = (Sentence) parser.parse("A | B");
		Sentence transformedOr = transformer.convert(or);
		Assert.assertEquals(or.toString(), transformedOr.toString());

		Sentence not = (Sentence) parser.parse("~C");
		Sentence transformedNot = transformer.convert(not);
		Assert.assertEquals(not.toString(), transformedNot.toString());
	}

	@Test
	public void testImplicationTransformation() {
		Sentence impl = (Sentence) parser.parse("A => B");
		Sentence expected = (Sentence) parser.parse("~A | B");
		Sentence transformedImpl = transformer.convert(impl);
		Assert.assertEquals(expected.toString(), transformedImpl.toString());
	}

	@Test
	public void testBiConditionalTransformation() {
		Sentence bic = (Sentence) parser.parse("A <=> B");
		Sentence expected = (Sentence) parser.parse("(~A | B) & (~B | A)");
		Sentence transformedBic = transformer.convert(bic);
		Assert.assertEquals(expected.toString(), transformedBic.toString());
	}

	@Test
	public void testTwoSuccessiveNotsTransformation() {
		Sentence twoNots = (Sentence) parser.parse("~~A");
		Sentence expected = (Sentence) parser.parse("A");
		Sentence transformed = transformer.convert(twoNots);
		Assert.assertEquals(expected.toString(), transformed.toString());
	}

	@Test
	public void testThreeSuccessiveNotsTransformation() {
		Sentence threeNots = (Sentence) parser.parse("~~~A");
		Sentence expected = (Sentence) parser.parse("~A");
		Sentence transformed = transformer.convert(threeNots);
		Assert.assertEquals(expected.toString(), transformed.toString());
	}

	@Test
	public void testFourSuccessiveNotsTransformation() {
		Sentence fourNots = (Sentence) parser.parse("~~~~A");
		Sentence expected = (Sentence) parser.parse("A");
		Sentence transformed = transformer.convert(fourNots);
		Assert.assertEquals(expected.toString(), transformed.toString());
	}

	@Test
	public void testDeMorgan1() {
		Sentence dm = (Sentence) parser.parse("~(A & B)");
		Sentence expected = (Sentence) parser.parse("~A | ~B");
		Sentence transformed = transformer.convert(dm);
		Assert.assertEquals(expected.toString(), transformed.toString());
	}

	@Test
	public void testDeMorgan2() {
		Sentence dm = (Sentence) parser.parse("~(A | B)");
		Sentence expected = (Sentence) parser.parse("~A & ~B");
		Sentence transformed = transformer.convert(dm);
		Assert.assertEquals(expected.toString(), transformed.toString());
	}

	@Test
	public void testOrDistribution1() {
		Sentence or = (Sentence) parser.parse("A & B | C)");
		Sentence expected = (Sentence) parser.parse("(A | C) & (B | C)");
		Sentence transformed = transformer.convert(or);
		Assert.assertEquals(expected.toString(), transformed.toString());
	}

	@Test
	public void testOrDistribution2() {
		Sentence or = (Sentence) parser.parse("A | B & C");
		Sentence expected = (Sentence) parser.parse("(A | B) & (A | C)");
		Sentence transformed = transformer.convert(or);
		Assert.assertEquals(expected.toString(), transformed.toString());
	}

	@Test
	public void testAimaExample() {
		Sentence aimaEg = parser.parse("B11 <=> P12 | P21");
		Sentence expected = parser.parse("(~B11 |  P12 | P21) & (~P12 | B11) & (~P21 | B11)");
		Sentence transformed = transformer.convert(aimaEg);
		Assert.assertEquals(expected.toString(), transformed.toString());
	}
	
	@Test
	public void testIssue78() {
		// (  ( NOT J1007 )  OR  ( NOT ( OR J1008 J1009 J1010 J1011 J1012 J1013 J1014 J1015  )  )  )
		Sentence issue78Eg = parser.parse("(  ( ~ J1007 )  |  ( ~ ( J1008 | J1009 | J1010 | J1011 | J1012 | J1013 | J1014 | J1015  )  ) )");
		Sentence expected = parser.parse("(~J1007 | ~J1008) & (~J1007 | ~J1009) & (~J1007 | ~J1010) & (~J1007 | ~J1011) & (~J1007 | ~J1012) & (~J1007 | ~J1013) & (~J1007 | ~J1014) & (~J1007 | ~J1015)");
		Sentence transformed = transformer.convert(issue78Eg);
		Assert.assertEquals(expected.toString(), transformed.toString());
	}
}
