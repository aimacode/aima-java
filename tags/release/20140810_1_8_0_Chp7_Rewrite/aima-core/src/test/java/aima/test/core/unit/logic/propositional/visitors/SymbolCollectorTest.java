package aima.test.core.unit.logic.propositional.visitors;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.propositional.parsing.PLParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.propositional.visitors.SymbolCollector;

/**
 * @author Ravi Mohan
 * 
 */
public class SymbolCollectorTest {
	private PLParser parser;

	@Before
	public void setUp() {
		parser = new PLParser();
	}

	@Test
	public void testCollectSymbolsFromComplexSentence() {
		Sentence sentence = (Sentence) parser.parse("(~B11 | P12 | P21) & (B11 | ~P12) & (B11 | ~P21)");
		Set<PropositionSymbol> s = SymbolCollector.getSymbolsFrom(sentence);
		Assert.assertEquals(3, s.size());
		Sentence b11 =  parser.parse("B11");
		Sentence p21 =  parser.parse("P21");
		Sentence p12 =  parser.parse("P12");
		Assert.assertTrue(s.contains(b11));
		Assert.assertTrue(s.contains(p21));
		Assert.assertTrue(s.contains(p12));
	}
}