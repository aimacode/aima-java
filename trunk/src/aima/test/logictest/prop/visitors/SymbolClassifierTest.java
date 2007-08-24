/*
 * Created on Dec 4, 2004
 *
 */
package aima.test.logictest.prop.visitors;

import java.util.Set;

import junit.framework.TestCase;
import aima.logic.propositional.parsing.PEParser;
import aima.logic.propositional.parsing.ast.Sentence;
import aima.logic.propositional.visitors.SymbolClassifier;

/**
 * @author Ravi Mohan
 * 
 */

public class SymbolClassifierTest extends TestCase {
	private SymbolClassifier classifier;

	private PEParser parser;

	@Override
	public void setUp() {
		classifier = new SymbolClassifier();
		parser = new PEParser();

	}

	public void testSimpleNegativeSymbol() {
		Sentence sentence = (Sentence) parser.parse("(NOT B)");

		Set neg = classifier.getNegativeSymbolsIn(sentence);
		Set pos = classifier.getPositiveSymbolsIn(sentence);

		Set pureNeg = classifier.getPureNegativeSymbolsIn(sentence);
		Set purePos = classifier.getPurePositiveSymbolsIn(sentence);

		Set pure = classifier.getPureSymbolsIn(sentence);
		Set impure = classifier.getImpureSymbolsIn(sentence);

		Sentence b = (Sentence) parser.parse("B");

		assertEquals(1, neg.size());
		assertTrue(neg.contains(b));

		assertEquals(0, pos.size());

		assertEquals(1, pureNeg.size());
		assertTrue(pureNeg.contains(b));

		assertEquals(0, purePos.size());

		assertEquals(1, pure.size());
		assertTrue(pure.contains(b));

		assertEquals(0, impure.size());
	}

	public void testSimplePositiveSymbol() {
		Sentence sentence = (Sentence) parser.parse("B");
		Set neg = classifier.getNegativeSymbolsIn(sentence);
		Set pos = classifier.getPositiveSymbolsIn(sentence);

		Set pureNeg = classifier.getPureNegativeSymbolsIn(sentence);
		Set purePos = classifier.getPurePositiveSymbolsIn(sentence);

		Set pure = classifier.getPureSymbolsIn(sentence);
		Set impure = classifier.getImpureSymbolsIn(sentence);

		assertEquals(0, neg.size());

		assertEquals(1, pos.size());
		Sentence b = (Sentence) parser.parse("B");
		assertTrue(pos.contains(b));

		assertEquals(1, purePos.size());
		assertTrue(purePos.contains(b));

		assertEquals(0, pureNeg.size());
		assertEquals(1, pure.size());

		assertTrue(pure.contains(b));

		assertEquals(0, impure.size());
	}

	public void testSingleSymbolPositiveAndNegative() {
		Sentence sentence = (Sentence) parser.parse("(B AND (NOT B))");
		Set neg = classifier.getNegativeSymbolsIn(sentence);
		Set pos = classifier.getPositiveSymbolsIn(sentence);

		Set pureNeg = classifier.getPureNegativeSymbolsIn(sentence);
		Set purePos = classifier.getPurePositiveSymbolsIn(sentence);

		Set pure = classifier.getPureSymbolsIn(sentence);
		Set impure = classifier.getImpureSymbolsIn(sentence);

		Sentence b = (Sentence) parser.parse("B");

		assertEquals(1, neg.size());
		assertTrue(neg.contains(b));

		assertEquals(1, pos.size());
		assertTrue(pos.contains(b));

		assertEquals(0, pureNeg.size());
		assertEquals(0, purePos.size());
		assertEquals(0, pure.size());
		assertEquals(1, impure.size());
	}

	public void testAIMAExample() {
		// 2nd Edition Pg 221
		Sentence sentence = (Sentence) parser
				.parse("(((A OR (NOT B)) AND ((NOT B) OR (NOT C))) AND (C OR A))");

		Set neg = classifier.getNegativeSymbolsIn(sentence);
		Set pos = classifier.getPositiveSymbolsIn(sentence);

		Set pureNeg = classifier.getPureNegativeSymbolsIn(sentence);
		Set purePos = classifier.getPurePositiveSymbolsIn(sentence);

		Set pure = classifier.getPureSymbolsIn(sentence);
		Set impure = classifier.getImpureSymbolsIn(sentence);

		Sentence a = (Sentence) parser.parse("A");
		Sentence b = (Sentence) parser.parse("B");
		Sentence c = (Sentence) parser.parse("C");

		assertEquals(2, neg.size());
		assertTrue(neg.contains(b));
		assertTrue(neg.contains(c));

		assertEquals(2, pos.size());
		assertTrue(pos.contains(a));
		assertTrue(pos.contains(c));

		assertEquals(1, pureNeg.size());
		assertTrue(pureNeg.contains(b));

		assertEquals(1, purePos.size());
		assertTrue(purePos.contains(a));

		assertEquals(2, pure.size());
		assertTrue(pure.contains(a));
		assertTrue(pure.contains(b));

		assertEquals(1, impure.size());
		assertTrue(impure.contains(c));
	}

}