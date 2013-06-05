package aima.test.core.unit.logic.propositional.visitors;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.propositional.SymbolClassifier;
import aima.core.logic.propositional.parsing.PLParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;

/**
 * @author Ravi Mohan
 * 
 */
public class SymbolClassifierTest {
	private SymbolClassifier classifier;

	private PLParser parser;

	@Before
	public void setUp() {
		classifier = new SymbolClassifier();
		parser = new PLParser();

	}

	@Test
	public void testSimpleNegativeSymbol() {
		Sentence sentence = (Sentence) parser.parse("~B");

		Set<PropositionSymbol> neg = classifier.getNegativeSymbolsIn(sentence);
		Set<PropositionSymbol> pos = classifier.getPositiveSymbolsIn(sentence);

		Set<PropositionSymbol> pureNeg = classifier.getPureNegativeSymbolsIn(sentence);
		Set<PropositionSymbol> purePos = classifier.getPurePositiveSymbolsIn(sentence);

		Set<PropositionSymbol> pure = classifier.getPureSymbolsIn(sentence);
		Set<PropositionSymbol> impure = classifier.getImpureSymbolsIn(sentence);

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
		Set<PropositionSymbol> neg = classifier.getNegativeSymbolsIn(sentence);
		Set<PropositionSymbol> pos = classifier.getPositiveSymbolsIn(sentence);

		Set<PropositionSymbol> pureNeg = classifier.getPureNegativeSymbolsIn(sentence);
		Set<PropositionSymbol> purePos = classifier.getPurePositiveSymbolsIn(sentence);

		Set<PropositionSymbol> pure = classifier.getPureSymbolsIn(sentence);
		Set<PropositionSymbol> impure = classifier.getImpureSymbolsIn(sentence);

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
		Sentence sentence = (Sentence) parser.parse("B & ~B");
		Set<PropositionSymbol> neg = classifier.getNegativeSymbolsIn(sentence);
		Set<PropositionSymbol> pos = classifier.getPositiveSymbolsIn(sentence);

		Set<PropositionSymbol> pureNeg = classifier.getPureNegativeSymbolsIn(sentence);
		Set<PropositionSymbol> purePos = classifier.getPurePositiveSymbolsIn(sentence);

		Set<PropositionSymbol> pure = classifier.getPureSymbolsIn(sentence);
		Set<PropositionSymbol> impure = classifier.getImpureSymbolsIn(sentence);

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
		Sentence sentence = (Sentence) parser.parse("(A | ~B) & (~B | ~C) & (C | A)");

		Set<PropositionSymbol> neg = classifier.getNegativeSymbolsIn(sentence);
		Set<PropositionSymbol> pos = classifier.getPositiveSymbolsIn(sentence);

		Set<PropositionSymbol> pureNeg = classifier.getPureNegativeSymbolsIn(sentence);
		Set<PropositionSymbol> purePos = classifier.getPurePositiveSymbolsIn(sentence);

		Set<PropositionSymbol> pure = classifier.getPureSymbolsIn(sentence);
		Set<PropositionSymbol> impure = classifier.getImpureSymbolsIn(sentence);

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