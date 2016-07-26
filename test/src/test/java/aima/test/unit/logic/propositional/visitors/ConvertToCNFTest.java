package aima.test.unit.logic.propositional.visitors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.basic.propositional.parsing.PLParser;
import aima.core.logic.basic.propositional.parsing.ast.Sentence;
import aima.core.logic.basic.propositional.visitors.ConvertToCNF;
import aima.extra.logic.propositional.parser.PLParserWrapper;

/**
 * @author Ravi Mohan
 * 
 */
public class ConvertToCNFTest {
	private PLParser parser = new PLParserWrapper();

	@Before
	public void setUp() {
	}

	@Test
	public void testSymbolTransform() {
		Sentence symbol = parser.parse("A");
		Sentence transformed = ConvertToCNF.convert(symbol);
		Assert.assertEquals("A", transformed.toString());
	}

	@Test
	public void testBasicSentenceTransformation() {
		Sentence and = parser.parse("A & B");
		Sentence transformedAnd = ConvertToCNF.convert(and);
		Assert.assertEquals("A & B", transformedAnd.toString());

		Sentence or = parser.parse("A | B");
		Sentence transformedOr = ConvertToCNF.convert(or);
		Assert.assertEquals("A | B", transformedOr.toString());

		Sentence not = parser.parse("~C");
		Sentence transformedNot = ConvertToCNF.convert(not);
		Assert.assertEquals("~C", transformedNot.toString());
	}

	@Test
	public void testImplicationTransformation() {
		Sentence impl = parser.parse("A => B");
		Sentence transformedImpl = ConvertToCNF.convert(impl);
		Assert.assertEquals("~A | B", transformedImpl.toString());
	}

	@Test
	public void testBiConditionalTransformation() {
		Sentence bic = parser.parse("A <=> B");
		Sentence transformedBic = ConvertToCNF.convert(bic);
		Assert.assertEquals("(~A | B) & (~B | A)", transformedBic.toString());
	}

	@Test
	public void testTwoSuccessiveNotsTransformation() {
		Sentence twoNots = parser.parse("~~A");
		Sentence transformed = ConvertToCNF.convert(twoNots);
		Assert.assertEquals("A", transformed.toString());
	}

	@Test
	public void testThreeSuccessiveNotsTransformation() {
		Sentence threeNots = parser.parse("~~~A");
		Sentence transformed = ConvertToCNF.convert(threeNots);
		Assert.assertEquals("~A", transformed.toString());
	}

	@Test
	public void testFourSuccessiveNotsTransformation() {
		Sentence fourNots = parser.parse("~~~~A");
		Sentence transformed = ConvertToCNF.convert(fourNots);
		Assert.assertEquals("A", transformed.toString());
	}

	@Test
	public void testDeMorgan1() {
		Sentence dm = parser.parse("~(A & B)");
		Sentence transformed = ConvertToCNF.convert(dm);
		Assert.assertEquals("~A | ~B", transformed.toString());
	}

	@Test
	public void testDeMorgan2() {
		Sentence dm = parser.parse("~(A | B)");
		Sentence transformed = ConvertToCNF.convert(dm);
		Assert.assertEquals("~A & ~B", transformed.toString());
	}

	@Test
	public void testOrDistribution1() {
		Sentence or =  parser.parse("A & B | C)");
		Sentence transformed = ConvertToCNF.convert(or);
		Assert.assertEquals("(A | C) & (B | C)", transformed.toString());
	}

	@Test
	public void testOrDistribution2() {
		Sentence or = parser.parse("A | B & C");
		Sentence transformed = ConvertToCNF.convert(or);
		Assert.assertEquals("(A | B) & (A | C)", transformed.toString());
	}

	@Test
	public void testAimaExample() {
		Sentence aimaEg = parser.parse("B11 <=> P12 | P21");
		Sentence transformed = ConvertToCNF.convert(aimaEg);
		Assert.assertEquals("(~B11 | P12 | P21) & (~P12 | B11) & (~P21 | B11)", transformed.toString());
	}
	
	@Test
	public void testNested() {
		Sentence nested = parser.parse("A | (B | (C | (D & E)))");
		Sentence transformed = ConvertToCNF.convert(nested);
		Assert.assertEquals("(A | B | C | D) & (A | B | C | E)", transformed.toString());
		
		nested = parser.parse("A | (B | (C & (D & E)))");
		transformed = ConvertToCNF.convert(nested);
		Assert.assertEquals("(A | B | C) & (A | B | D) & (A | B | E)", transformed.toString());
		
		nested = parser.parse("A | (B | (C & (D & (E | F))))");
		transformed = ConvertToCNF.convert(nested);
		Assert.assertEquals("(A | B | C) & (A | B | D) & (A | B | E | F)", transformed.toString());
		
		nested = parser.parse("(A | (B | (C & D))) | E | (F | (G | (H & I)))");
		transformed = ConvertToCNF.convert(nested);
		Assert.assertEquals("(A | B | C | E | F | G | H) & (A | B | D | E | F | G | H) & (A | B | C | E | F | G | I) & (A | B | D | E | F | G | I)", transformed.toString());
		
		nested = parser.parse("(((~P | ~Q) => ~(P | Q)) => R)");
		transformed = ConvertToCNF.convert(nested);
		Assert.assertEquals("(~P | ~Q | R) & (P | Q | R)", transformed.toString());
		
		nested = parser.parse("~(((~P | ~Q) => ~(P | Q)) => R)");
		transformed = ConvertToCNF.convert(nested);
		Assert.assertEquals("(P | ~P) & (Q | ~P) & (P | ~Q) & (Q | ~Q) & ~R", transformed.toString());
	}
	
	@Test
	public void testIssue78() {
		// (  ( NOT J1007 )  OR  ( NOT ( OR J1008 J1009 J1010 J1011 J1012 J1013 J1014 J1015  )  )  )
		Sentence issue78Eg = parser.parse("(  ( ~ J1007 )  |  ( ~ ( J1008 | J1009 | J1010 | J1011 | J1012 | J1013 | J1014 | J1015  )  ) )");
		Sentence transformed = ConvertToCNF.convert(issue78Eg);
		Assert.assertEquals("(~J1007 | ~J1008) & (~J1007 | ~J1009) & (~J1007 | ~J1010) & (~J1007 | ~J1011) & (~J1007 | ~J1012) & (~J1007 | ~J1013) & (~J1007 | ~J1014) & (~J1007 | ~J1015)", transformed.toString());
	}
	
	@Test
	public void testDistributingOrCorrectly() {
		Sentence s = parser.parse("A & B & C & D & (E | (F & G)) & H & I & J & K");
		Sentence transformed = ConvertToCNF.convert(s);
		Assert.assertEquals("A & B & C & D & (E | F) & (E | G) & H & I & J & K", transformed.toString());
	}
}
