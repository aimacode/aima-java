/*
 * Created on Dec 4, 2004
 *
 */
package aima.test.logictest.prop.visitors;

import java.util.Set;

import junit.framework.TestCase;
import aima.logic.propositional.parsing.PEParser;
import aima.logic.propositional.parsing.ast.Sentence;
import aima.logic.propositional.visitors.CNFClauseGatherer;
import aima.logic.propositional.visitors.CNFTransformer;

/**
 * @author Ravi Mohan
 * 
 */
public class CNFClauseGathererTest extends TestCase {
	private CNFClauseGatherer gatherer;

	private PEParser parser;

	@Override
	public void setUp() {
		parser = new PEParser();
		gatherer = new CNFClauseGatherer();
	}

	public void testSymbol() {
		Sentence simple = (Sentence) parser.parse("A");
		Sentence a = (Sentence) parser.parse("A");
		Set clauses = gatherer.getClausesFrom(simple);
		assertNotNull(clauses);
		assertEquals(1, clauses.size());
		assertTrue(clauses.contains(a));

	}

	public void testNotSentence() {
		Sentence simple = (Sentence) parser.parse("(NOT A)");
		Sentence a = (Sentence) parser.parse("(NOT A)");
		Set clauses = gatherer.getClausesFrom(simple);
		assertNotNull(clauses);
		assertEquals(1, clauses.size());
		assertTrue(clauses.contains(a));

	}

	public void testSimpleAndClause() {
		Sentence simple = (Sentence) parser.parse("(A AND B)");
		Sentence a = (Sentence) parser.parse("A");
		Sentence b = (Sentence) parser.parse("B");
		Set clauses = gatherer.getClausesFrom(simple);
		assertEquals(2, clauses.size());
		assertTrue(clauses.contains(a));
		assertTrue(clauses.contains(b));

	}

	public void testMultiAndClause() {
		Sentence simple = (Sentence) parser.parse("((A AND B) AND C)");
		Set clauses = gatherer.getClausesFrom(simple);
		assertEquals(3, clauses.size());
		Sentence a = (Sentence) parser.parse("A");
		Sentence b = (Sentence) parser.parse("B");
		Sentence c = (Sentence) parser.parse("C");
		assertTrue(clauses.contains(a));
		assertTrue(clauses.contains(b));
		assertTrue(clauses.contains(c));

	}

	public void testMultiAndClause2() {
		Sentence simple = (Sentence) parser.parse("(A AND (B AND C))");
		Set clauses = gatherer.getClausesFrom(simple);
		assertEquals(3, clauses.size());
		Sentence a = (Sentence) parser.parse("A");
		Sentence b = (Sentence) parser.parse("B");
		Sentence c = (Sentence) parser.parse("C");
		assertTrue(clauses.contains(a));
		assertTrue(clauses.contains(b));
		assertTrue(clauses.contains(c));

	}

	public void testAimaExample() {
		Sentence aimaEg = (Sentence) parser.parse("( B11 <=> (P12 OR P21))");
		CNFTransformer transformer = new CNFTransformer();
		Sentence transformed = transformer.transform(aimaEg);
		Set clauses = gatherer.getClausesFrom(transformed);
		Sentence clause1 = (Sentence) parser.parse("( B11 OR  ( NOT P12 )  )");
		Sentence clause2 = (Sentence) parser.parse("( B11 OR  ( NOT P21 )  )");
		Sentence clause3 = (Sentence) parser
				.parse("(  ( NOT B11 )  OR  ( P12 OR P21 ) )");
		assertEquals(3, clauses.size());
		assertTrue(clauses.contains(clause1));
		assertTrue(clauses.contains(clause2));
		assertTrue(clauses.contains(clause3));

	}
}
