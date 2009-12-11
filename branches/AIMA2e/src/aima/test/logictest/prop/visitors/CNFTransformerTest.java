/*
 * Created on Dec 4, 2004
 *
 */
package aima.test.logictest.prop.visitors;

import junit.framework.TestCase;
import aima.logic.propositional.parsing.PEParser;
import aima.logic.propositional.parsing.ast.Sentence;
import aima.logic.propositional.visitors.CNFTransformer;

/**
 * @author Ravi Mohan
 * 
 */
public class CNFTransformerTest extends TestCase {
	private PEParser parser = new PEParser();

	private CNFTransformer transformer;

	@Override
	public void setUp() {
		transformer = new CNFTransformer();
	}

	public void testSymbolTransform() {
		Sentence symbol = (Sentence) parser.parse("A");
		Sentence transformed = transformer.transform(symbol);
		assertEquals("A", transformed.toString());
	}

	public void testBasicSentenceTransformation() {
		Sentence and = (Sentence) parser.parse("(A AND B)");
		Sentence transformedAnd = transformer.transform(and);
		assertEquals(and.toString(), transformedAnd.toString());

		Sentence or = (Sentence) parser.parse("(A OR B)");
		Sentence transformedOr = transformer.transform(or);
		assertEquals(or.toString(), transformedOr.toString());

		Sentence not = (Sentence) parser.parse("(NOT C)");
		Sentence transformedNot = transformer.transform(not);
		assertEquals(not.toString(), transformedNot.toString());
	}

	public void testImplicationTransformation() {
		Sentence impl = (Sentence) parser.parse("(A => B)");
		Sentence expected = (Sentence) parser.parse("((NOT A) OR B)");
		Sentence transformedImpl = transformer.transform(impl);
		assertEquals(expected.toString(), transformedImpl.toString());
	}

	public void testBiConditionalTransformation() {
		Sentence bic = (Sentence) parser.parse("(A <=> B)");
		Sentence expected = (Sentence) parser
				.parse("(((NOT A) OR B)  AND ((NOT B) OR A)) ");
		Sentence transformedBic = transformer.transform(bic);
		assertEquals(expected.toString(), transformedBic.toString());
	}

	public void testTwoSuccessiveNotsTransformation() {
		Sentence twoNots = (Sentence) parser.parse("(NOT (NOT A))");
		Sentence expected = (Sentence) parser.parse("A");
		Sentence transformed = transformer.transform(twoNots);
		assertEquals(expected.toString(), transformed.toString());
	}

	public void testThreeSuccessiveNotsTransformation() {
		Sentence threeNots = (Sentence) parser.parse("(NOT (NOT (NOT A)))");
		Sentence expected = (Sentence) parser.parse("(NOT A)");
		Sentence transformed = transformer.transform(threeNots);
		assertEquals(expected.toString(), transformed.toString());
	}

	public void testFourSuccessiveNotsTransformation() {
		Sentence fourNots = (Sentence) parser
				.parse("(NOT (NOT (NOT (NOT A))))");
		Sentence expected = (Sentence) parser.parse("A");
		Sentence transformed = transformer.transform(fourNots);
		assertEquals(expected.toString(), transformed.toString());
	}

	public void testDeMorgan1() {
		Sentence dm = (Sentence) parser.parse("(NOT (A AND B))");
		Sentence expected = (Sentence) parser.parse("((NOT A) OR (NOT B))");
		Sentence transformed = transformer.transform(dm);
		assertEquals(expected.toString(), transformed.toString());
	}

	public void testDeMorgan2() {
		Sentence dm = (Sentence) parser.parse("(NOT (A OR B))");
		Sentence expected = (Sentence) parser.parse("((NOT A) AND (NOT B))");
		Sentence transformed = transformer.transform(dm);
		assertEquals(expected.toString(), transformed.toString());
	}

	public void testOrDistribution1() {
		Sentence or = (Sentence) parser.parse("((A AND B) OR C)");
		Sentence expected = (Sentence) parser.parse("((C OR A) AND (C OR B))");
		Sentence transformed = transformer.transform(or);
		assertEquals(expected.toString(), transformed.toString());
	}

	public void testOrDistribution2() {
		Sentence or = (Sentence) parser.parse("(A OR (B AND C))");
		Sentence expected = (Sentence) parser.parse("((A OR B) AND (A OR C))");
		Sentence transformed = transformer.transform(or);
		assertEquals(expected.toString(), transformed.toString());
	}

	public void testAimaExample() {
		Sentence aimaEg = (Sentence) parser.parse("( B11 <=> (P12 OR P21))");
		Sentence expected = (Sentence) parser
				.parse(" (  (  ( NOT B11 )  OR  ( P12 OR P21 ) ) AND  (  ( B11 OR  ( NOT P12 )  ) AND  ( B11 OR  ( NOT P21 )  ) ) )");
		Sentence transformed = transformer.transform(aimaEg);
		assertEquals(expected.toString(), transformed.toString());
	}

}
