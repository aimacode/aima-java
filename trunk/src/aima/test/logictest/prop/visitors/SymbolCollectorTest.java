/*
 * Created on Dec 4, 2004
 *
 */
package aima.test.logictest.prop.visitors;

import java.util.Set;

import junit.framework.TestCase;
import aima.logic.propositional.parsing.PEParser;
import aima.logic.propositional.parsing.ast.Sentence;
import aima.logic.propositional.visitors.SymbolCollector;

/**
 * @author Ravi Mohan
 * 
 */

public class SymbolCollectorTest extends TestCase {
	private PEParser parser;

	private SymbolCollector collector;

	@Override
	public void setUp() {
		parser = new PEParser();
		collector = new SymbolCollector();
	}

	public void testCollectSymbolsFromComplexSentence() {
		Sentence sentence = (Sentence) parser
				.parse(" (  (  ( NOT B11 )  OR  ( P12 OR P21 ) ) AND  (  ( B11 OR  ( NOT P12 )  ) AND  ( B11 OR  ( NOT P21 )  ) ) )");
		Set s = collector.getSymbolsIn(sentence);
		assertEquals(3, s.size());
		Sentence b11 = (Sentence) parser.parse("B11");
		Sentence p21 = (Sentence) parser.parse("P21");
		Sentence p12 = (Sentence) parser.parse("P12");
		assertTrue(s.contains(b11));
		assertTrue(s.contains(p21));
		assertTrue(s.contains(p12));

	}

}