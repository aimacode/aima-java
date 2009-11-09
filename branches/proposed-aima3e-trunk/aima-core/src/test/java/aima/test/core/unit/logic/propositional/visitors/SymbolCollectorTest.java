package aima.test.core.unit.logic.propositional.visitors;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.propositional.parsing.PEParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.visitors.SymbolCollector;

/**
 * @author Ravi Mohan
 * 
 */
public class SymbolCollectorTest {
	private PEParser parser;

	private SymbolCollector collector;

	@Before
	public void setUp() {
		parser = new PEParser();
		collector = new SymbolCollector();
	}

	@Test
	public void testCollectSymbolsFromComplexSentence() {
		Sentence sentence = (Sentence) parser
				.parse(" (  (  ( NOT B11 )  OR  ( P12 OR P21 ) ) AND  (  ( B11 OR  ( NOT P12 )  ) AND  ( B11 OR  ( NOT P21 )  ) ) )");
		Set<Symbol> s = collector.getSymbolsIn(sentence);
		Assert.assertEquals(3, s.size());
		Sentence b11 = (Sentence) parser.parse("B11");
		Sentence p21 = (Sentence) parser.parse("P21");
		Sentence p12 = (Sentence) parser.parse("P12");
		Assert.assertTrue(s.contains(b11));
		Assert.assertTrue(s.contains(p21));
		Assert.assertTrue(s.contains(p12));
	}
}