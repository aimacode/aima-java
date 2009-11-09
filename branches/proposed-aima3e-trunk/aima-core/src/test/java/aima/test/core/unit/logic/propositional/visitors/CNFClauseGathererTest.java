package aima.test.core.unit.logic.propositional.visitors;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.propositional.parsing.PEParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.visitors.CNFClauseGatherer;
import aima.core.logic.propositional.visitors.CNFTransformer;

/**
 * @author Ravi Mohan
 * 
 */
public class CNFClauseGathererTest {
	private CNFClauseGatherer gatherer;

	private PEParser parser;

	@Before
	public void setUp() {
		parser = new PEParser();
		gatherer = new CNFClauseGatherer();
	}

	@Test
	public void testSymbol() {
		Sentence simple = (Sentence) parser.parse("A");
		Sentence a = (Sentence) parser.parse("A");
		Set<Sentence> clauses = gatherer.getClausesFrom(simple);
		Assert.assertNotNull(clauses);
		Assert.assertEquals(1, clauses.size());
		Assert.assertTrue(clauses.contains(a));
	}

	@Test
	public void testNotSentence() {
		Sentence simple = (Sentence) parser.parse("(NOT A)");
		Sentence a = (Sentence) parser.parse("(NOT A)");
		Set<Sentence> clauses = gatherer.getClausesFrom(simple);
		Assert.assertNotNull(clauses);
		Assert.assertEquals(1, clauses.size());
		Assert.assertTrue(clauses.contains(a));
	}

	@Test
	public void testSimpleAndClause() {
		Sentence simple = (Sentence) parser.parse("(A AND B)");
		Sentence a = (Sentence) parser.parse("A");
		Sentence b = (Sentence) parser.parse("B");
		Set<Sentence> clauses = gatherer.getClausesFrom(simple);
		Assert.assertEquals(2, clauses.size());
		Assert.assertTrue(clauses.contains(a));
		Assert.assertTrue(clauses.contains(b));
	}

	@Test
	public void testMultiAndClause() {
		Sentence simple = (Sentence) parser.parse("((A AND B) AND C)");
		Set<Sentence> clauses = gatherer.getClausesFrom(simple);
		Assert.assertEquals(3, clauses.size());
		Sentence a = (Sentence) parser.parse("A");
		Sentence b = (Sentence) parser.parse("B");
		Sentence c = (Sentence) parser.parse("C");
		Assert.assertTrue(clauses.contains(a));
		Assert.assertTrue(clauses.contains(b));
		Assert.assertTrue(clauses.contains(c));
	}

	@Test
	public void testMultiAndClause2() {
		Sentence simple = (Sentence) parser.parse("(A AND (B AND C))");
		Set<Sentence> clauses = gatherer.getClausesFrom(simple);
		Assert.assertEquals(3, clauses.size());
		Sentence a = (Sentence) parser.parse("A");
		Sentence b = (Sentence) parser.parse("B");
		Sentence c = (Sentence) parser.parse("C");
		Assert.assertTrue(clauses.contains(a));
		Assert.assertTrue(clauses.contains(b));
		Assert.assertTrue(clauses.contains(c));
	}

	@Test
	public void testAimaExample() {
		Sentence aimaEg = (Sentence) parser.parse("( B11 <=> (P12 OR P21))");
		CNFTransformer transformer = new CNFTransformer();
		Sentence transformed = transformer.transform(aimaEg);
		Set<Sentence> clauses = gatherer.getClausesFrom(transformed);
		Sentence clause1 = (Sentence) parser.parse("( B11 OR  ( NOT P12 )  )");
		Sentence clause2 = (Sentence) parser.parse("( B11 OR  ( NOT P21 )  )");
		Sentence clause3 = (Sentence) parser
				.parse("(  ( NOT B11 )  OR  ( P12 OR P21 ) )");
		Assert.assertEquals(3, clauses.size());
		Assert.assertTrue(clauses.contains(clause1));
		Assert.assertTrue(clauses.contains(clause2));
		Assert.assertTrue(clauses.contains(clause3));
	}
}
