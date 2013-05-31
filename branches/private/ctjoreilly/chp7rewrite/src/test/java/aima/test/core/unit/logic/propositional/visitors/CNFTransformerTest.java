package aima.test.core.unit.logic.propositional.visitors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.propositional.parsing.PEParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.visitors.CNFTransformer;

/**
 * @author Ravi Mohan
 * 
 */
public class CNFTransformerTest {
	private PEParser parser = new PEParser();

	private CNFTransformer transformer;

	@Before
	public void setUp() {
		transformer = new CNFTransformer();
	}

	@Test
	public void testSymbolTransform() {
		Sentence symbol = (Sentence) parser.parse("A");
		Sentence transformed = transformer.transform(symbol);
		Assert.assertEquals("A", transformed.toString());
	}

	@Test
	public void testBasicSentenceTransformation() {
		Sentence and = (Sentence) parser.parse("A & B");
		Sentence transformedAnd = transformer.transform(and);
		Assert.assertEquals(and.toString(), transformedAnd.toString());

		Sentence or = (Sentence) parser.parse("A | B");
		Sentence transformedOr = transformer.transform(or);
		Assert.assertEquals(or.toString(), transformedOr.toString());

		Sentence not = (Sentence) parser.parse("~C");
		Sentence transformedNot = transformer.transform(not);
		Assert.assertEquals(not.toString(), transformedNot.toString());
	}

	@Test
	public void testImplicationTransformation() {
		Sentence impl = (Sentence) parser.parse("A => B");
		Sentence expected = (Sentence) parser.parse("~A | B");
		Sentence transformedImpl = transformer.transform(impl);
		Assert.assertEquals(expected.toString(), transformedImpl.toString());
	}

	@Test
	public void testBiConditionalTransformation() {
		Sentence bic = (Sentence) parser.parse("A <=> B");
		Sentence expected = (Sentence) parser.parse("(~A | B) & (~B | A)");
		Sentence transformedBic = transformer.transform(bic);
		Assert.assertEquals(expected.toString(), transformedBic.toString());
	}

	@Test
	public void testTwoSuccessiveNotsTransformation() {
		Sentence twoNots = (Sentence) parser.parse("~~A");
		Sentence expected = (Sentence) parser.parse("A");
		Sentence transformed = transformer.transform(twoNots);
		Assert.assertEquals(expected.toString(), transformed.toString());
	}

	@Test
	public void testThreeSuccessiveNotsTransformation() {
		Sentence threeNots = (Sentence) parser.parse("~~~A");
		Sentence expected = (Sentence) parser.parse("~A");
		Sentence transformed = transformer.transform(threeNots);
		Assert.assertEquals(expected.toString(), transformed.toString());
	}

	@Test
	public void testFourSuccessiveNotsTransformation() {
		Sentence fourNots = (Sentence) parser.parse("~~~~A");
		Sentence expected = (Sentence) parser.parse("A");
		Sentence transformed = transformer.transform(fourNots);
		Assert.assertEquals(expected.toString(), transformed.toString());
	}

	@Test
	public void testDeMorgan1() {
		Sentence dm = (Sentence) parser.parse("~(A & B)");
		Sentence expected = (Sentence) parser.parse("~A | ~B");
		Sentence transformed = transformer.transform(dm);
		Assert.assertEquals(expected.toString(), transformed.toString());
	}

	@Test
	public void testDeMorgan2() {
		Sentence dm = (Sentence) parser.parse("~(A | B)");
		Sentence expected = (Sentence) parser.parse("~A & ~B");
		Sentence transformed = transformer.transform(dm);
		Assert.assertEquals(expected.toString(), transformed.toString());
	}

	@Test
	public void testOrDistribution1() {
		Sentence or = (Sentence) parser.parse("A & B | C)");
		Sentence expected = (Sentence) parser.parse("(C | A) & (C | B)");
		Sentence transformed = transformer.transform(or);
		Assert.assertEquals(expected.toString(), transformed.toString());
	}

	@Test
	public void testOrDistribution2() {
		Sentence or = (Sentence) parser.parse("A | B & C");
		Sentence expected = (Sentence) parser.parse("(A | B) & (A | C)");
		Sentence transformed = transformer.transform(or);
		Assert.assertEquals(expected.toString(), transformed.toString());
	}

	@Test
	public void testAimaExample() {
		Sentence aimaEg = (Sentence) parser.parse("B11 <=> P12 | P21");
		Sentence expected = (Sentence) parser.parse("(~B11 |  P12 | P21) & (B11 | ~P12) & (B11 | ~P21 )");
		Sentence transformed = transformer.transform(aimaEg);
		Assert.assertEquals(expected.toString(), transformed.toString());
	}
}
