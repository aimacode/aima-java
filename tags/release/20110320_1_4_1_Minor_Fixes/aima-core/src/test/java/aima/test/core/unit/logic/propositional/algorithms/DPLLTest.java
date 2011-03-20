package aima.test.core.unit.logic.propositional.algorithms;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.propositional.algorithms.DPLL;
import aima.core.logic.propositional.algorithms.KnowledgeBase;
import aima.core.logic.propositional.algorithms.Model;
import aima.core.logic.propositional.parsing.PEParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.visitors.CNFClauseGatherer;
import aima.core.logic.propositional.visitors.CNFTransformer;
import aima.core.logic.propositional.visitors.SymbolCollector;
import aima.core.util.Converter;

/**
 * @author Ravi Mohan
 * 
 */
public class DPLLTest {

	private DPLL dpll;

	private PEParser parser;

	@Before
	public void setUp() {
		parser = new PEParser();
		dpll = new DPLL();
	}

	@Test
	public void testDPLLReturnsTrueWhenAllClausesTrueInModel() {
		Model model = new Model();
		model = model.extend(new Symbol("A"), true).extend(new Symbol("B"),
				true);
		Sentence sentence = (Sentence) parser.parse("((A AND B) AND (A OR B))");
		boolean satisfiable = dpll.dpllSatisfiable(sentence, model);
		Assert.assertEquals(true, satisfiable);
	}

	@Test
	public void testDPLLReturnsFalseWhenOneClauseFalseInModel() {
		Model model = new Model();
		model = model.extend(new Symbol("A"), true).extend(new Symbol("B"),
				false);
		Sentence sentence = (Sentence) parser.parse("((A OR B) AND (A => B))");
		boolean satisfiable = dpll.dpllSatisfiable(sentence, model);
		Assert.assertEquals(false, satisfiable);
	}

	@Test
	public void testDPLLFiltersClausesTheStatusOfWhichAreKnown() {
		Model model = new Model();
		model = model.extend(new Symbol("A"), true).extend(new Symbol("B"),
				true);
		Sentence sentence = (Sentence) parser
				.parse("((A AND B) AND (B AND C))");
		List<Sentence> clauseList = new Converter<Sentence>()
				.setToList(new CNFClauseGatherer()
						.getClausesFrom(new CNFTransformer()
								.transform(sentence)));
		List<Sentence> clausesWithNonTrueValues = dpll
				.clausesWithNonTrueValues(clauseList, model);
		Assert.assertEquals(1, clausesWithNonTrueValues.size());
		Sentence nonTrueClause = (Sentence) parser.parse("(B AND C)");
		clausesWithNonTrueValues.contains(nonTrueClause);
	}

	@Test
	public void testDPLLFilteringNonTrueClausesGivesNullWhenAllClausesAreKnown() {
		Model model = new Model();
		model = model.extend(new Symbol("A"), true).extend(new Symbol("B"),
				true).extend(new Symbol("C"), true);
		Sentence sentence = (Sentence) parser
				.parse("((A AND B) AND (B AND C))");
		List<Sentence> clauseList = new Converter<Sentence>()
				.setToList(new CNFClauseGatherer()
						.getClausesFrom(new CNFTransformer()
								.transform(sentence)));
		List<Sentence> clausesWithNonTrueValues = dpll
				.clausesWithNonTrueValues(clauseList, model);
		Assert.assertEquals(0, clausesWithNonTrueValues.size());
	}

	@Test
	public void testDPLLFindsPurePositiveSymbolsWhenTheyExist() {
		Model model = new Model();
		model = model.extend(new Symbol("A"), true).extend(new Symbol("B"),
				true);
		Sentence sentence = (Sentence) parser
				.parse("((A AND B) AND (B AND C))");
		List<Sentence> clauseList = new Converter<Sentence>()
				.setToList(new CNFClauseGatherer()
						.getClausesFrom(new CNFTransformer()
								.transform(sentence)));
		List<Symbol> symbolList = new Converter<Symbol>()
				.setToList(new SymbolCollector().getSymbolsIn(sentence));

		DPLL.SymbolValuePair sv = dpll.findPureSymbolValuePair(clauseList,
				model, symbolList);
		Assert.assertNotNull(sv);
		Assert.assertEquals(new Symbol("C"), sv.symbol);
		Assert.assertEquals(new Boolean(true), sv.value);
	}

	@Test
	public void testDPLLFindsPureNegativeSymbolsWhenTheyExist() {
		Model model = new Model();
		model = model.extend(new Symbol("A"), true).extend(new Symbol("B"),
				true);
		Sentence sentence = (Sentence) parser
				.parse("((A AND B) AND ( B  AND (NOT C) ))");
		List<Sentence> clauseList = new Converter<Sentence>()
				.setToList(new CNFClauseGatherer()
						.getClausesFrom(new CNFTransformer()
								.transform(sentence)));
		List<Symbol> symbolList = new Converter<Symbol>()
				.setToList(new SymbolCollector().getSymbolsIn(sentence));

		DPLL.SymbolValuePair sv = dpll.findPureSymbolValuePair(clauseList,
				model, symbolList);
		Assert.assertNotNull(sv);
		Assert.assertEquals(new Symbol("C"), sv.symbol);
		Assert.assertEquals(new Boolean(false), sv.value);
	}

	@Test
	public void testDPLLSucceedsWithAandNotA() {
		Sentence sentence = (Sentence) parser.parse("(A AND (NOT A))");
		boolean satisfiable = dpll.dpllSatisfiable(sentence);
		Assert.assertEquals(false, satisfiable);
	}

	@Test
	public void testDPLLSucceedsWithChadCarffsBugReport() {
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell("(B12 <=> (P11 OR (P13 OR (P22 OR P02))))");
		kb.tell("(B21 <=> (P20 OR (P22 OR (P31 OR P11))))");
		kb.tell("(B01 <=> (P00 OR (P02 OR P11)))");
		kb.tell("(B10 <=> (P11 OR (P20 OR P00)))");
		kb.tell("(NOT B21)");
		kb.tell("(NOT B12)");
		kb.tell("(B10)");
		kb.tell("(B01)");
		Assert.assertTrue(kb.askWithDpll("(P00)"));
		Assert.assertFalse(kb.askWithDpll("(NOT P00)"));
	}

	@Test
	public void testDPLLSucceedsWithStackOverflowBugReport1() {
		Sentence sentence = (Sentence) parser
				.parse("((A OR (NOT A)) AND (A OR B))");
		Assert.assertTrue(dpll.dpllSatisfiable(sentence));
	}

	@Test
	public void testDPLLSucceedsWithChadCarffsBugReport2() {
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell("(B10 <=> (P11 OR (P20 OR P00)))");
		kb.tell("(B01 <=> (P00 OR (P02 OR P11)))");
		kb.tell("(B21 <=> (P20 OR (P22 OR (P31 OR P11))))");
		kb.tell("(B12 <=> (P11 OR (P13 OR (P22 OR P02))))");
		kb.tell("(NOT B21)");
		kb.tell("(NOT B12)");
		kb.tell("(B10)");
		kb.tell("(B01)");
		Assert.assertTrue(kb.askWithDpll("(P00)"));
		Assert.assertFalse(kb.askWithDpll("(NOT P00)"));
	}
	
	@Test
	public void testDoesNotKnow() {
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell("A");
		
		Assert.assertFalse(kb.askWithDpll("B"));
		Assert.assertFalse(kb.askWithDpll("(NOT B)"));
	}
}
