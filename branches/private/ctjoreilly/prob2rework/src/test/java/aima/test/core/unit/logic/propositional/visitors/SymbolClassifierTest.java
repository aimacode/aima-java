package aima.test.core.unit.logic.propositional.visitors;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.propositional.parsing.PEParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.visitors.SymbolClassifier;

/**
 * @author Ravi Mohan
 * 
 */
public class SymbolClassifierTest {
	private SymbolClassifier classifier;

	private PEParser parser;

	@Before
	public void setUp() {
		classifier = new SymbolClassifier();
		parser = new PEParser();

	}

	@Test
	public void testSimpleNegativeSymbol() {
		Sentence sentence = (Sentence) parser.parse("(NOT B)");

		Set<Symbol> neg = classifier.getNegativeSymbolsIn(sentence);
		Set<Symbol> pos = classifier.getPositiveSymbolsIn(sentence);

		Set<Symbol> pureNeg = classifier.getPureNegativeSymbolsIn(sentence);
		Set<Symbol> purePos = classifier.getPurePositiveSymbolsIn(sentence);

		Set<Symbol> pure = classifier.getPureSymbolsIn(sentence);
		Set<Symbol> impure = classifier.getImpureSymbolsIn(sentence);

		Sentence b = (Sentence) parser.parse("B");

		Assert.assertEquals(1, neg.size());
		Assert.assertTrue(neg.contains(b));

		Assert.assertEquals(0, pos.size());

		Assert.assertEquals(1, pureNeg.size());
		Assert.assertTrue(pureNeg.contains(b));

		Assert.assertEquals(0, purePos.size());

		Assert.assertEquals(1, pure.size());
		Assert.assertTrue(pure.contains(b));

		Assert.assertEquals(0, impure.size());
	}

	@Test
	public void testSimplePositiveSymbol() {
		Sentence sentence = (Sentence) parser.parse("B");
		Set<Symbol> neg = classifier.getNegativeSymbolsIn(sentence);
		Set<Symbol> pos = classifier.getPositiveSymbolsIn(sentence);

		Set<Symbol> pureNeg = classifier.getPureNegativeSymbolsIn(sentence);
		Set<Symbol> purePos = classifier.getPurePositiveSymbolsIn(sentence);

		Set<Symbol> pure = classifier.getPureSymbolsIn(sentence);
		Set<Symbol> impure = classifier.getImpureSymbolsIn(sentence);

		Assert.assertEquals(0, neg.size());

		Assert.assertEquals(1, pos.size());
		Sentence b = (Sentence) parser.parse("B");
		Assert.assertTrue(pos.contains(b));

		Assert.assertEquals(1, purePos.size());
		Assert.assertTrue(purePos.contains(b));

		Assert.assertEquals(0, pureNeg.size());
		Assert.assertEquals(1, pure.size());

		Assert.assertTrue(pure.contains(b));

		Assert.assertEquals(0, impure.size());
	}

	@Test
	public void testSingleSymbolPositiveAndNegative() {
		Sentence sentence = (Sentence) parser.parse("(B AND (NOT B))");
		Set<Symbol> neg = classifier.getNegativeSymbolsIn(sentence);
		Set<Symbol> pos = classifier.getPositiveSymbolsIn(sentence);

		Set<Symbol> pureNeg = classifier.getPureNegativeSymbolsIn(sentence);
		Set<Symbol> purePos = classifier.getPurePositiveSymbolsIn(sentence);

		Set<Symbol> pure = classifier.getPureSymbolsIn(sentence);
		Set<Symbol> impure = classifier.getImpureSymbolsIn(sentence);

		Sentence b = (Sentence) parser.parse("B");

		Assert.assertEquals(1, neg.size());
		Assert.assertTrue(neg.contains(b));

		Assert.assertEquals(1, pos.size());
		Assert.assertTrue(pos.contains(b));

		Assert.assertEquals(0, pureNeg.size());
		Assert.assertEquals(0, purePos.size());
		Assert.assertEquals(0, pure.size());
		Assert.assertEquals(1, impure.size());
	}

	@Test
	public void testAIMA2eExample() {
		// 2nd Edition Pg 221
		Sentence sentence = (Sentence) parser
				.parse("(((A OR (NOT B)) AND ((NOT B) OR (NOT C))) AND (C OR A))");

		Set<Symbol> neg = classifier.getNegativeSymbolsIn(sentence);
		Set<Symbol> pos = classifier.getPositiveSymbolsIn(sentence);

		Set<Symbol> pureNeg = classifier.getPureNegativeSymbolsIn(sentence);
		Set<Symbol> purePos = classifier.getPurePositiveSymbolsIn(sentence);

		Set<Symbol> pure = classifier.getPureSymbolsIn(sentence);
		Set<Symbol> impure = classifier.getImpureSymbolsIn(sentence);

		Sentence a = (Sentence) parser.parse("A");
		Sentence b = (Sentence) parser.parse("B");
		Sentence c = (Sentence) parser.parse("C");

		Assert.assertEquals(2, neg.size());
		Assert.assertTrue(neg.contains(b));
		Assert.assertTrue(neg.contains(c));

		Assert.assertEquals(2, pos.size());
		Assert.assertTrue(pos.contains(a));
		Assert.assertTrue(pos.contains(c));

		Assert.assertEquals(1, pureNeg.size());
		Assert.assertTrue(pureNeg.contains(b));

		Assert.assertEquals(1, purePos.size());
		Assert.assertTrue(purePos.contains(a));

		Assert.assertEquals(2, pure.size());
		Assert.assertTrue(pure.contains(a));
		Assert.assertTrue(pure.contains(b));

		Assert.assertEquals(1, impure.size());
		Assert.assertTrue(impure.contains(c));
	}
}