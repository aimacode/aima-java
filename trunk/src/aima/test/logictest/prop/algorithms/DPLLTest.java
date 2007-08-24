/*
 * Created on Dec 4, 2004
 *
 */
package aima.test.logictest.prop.algorithms;

import java.util.List;

import junit.framework.TestCase;
import aima.logic.propositional.algorithms.DPLL;
import aima.logic.propositional.algorithms.KnowledgeBase;
import aima.logic.propositional.algorithms.Model;
import aima.logic.propositional.parsing.PEParser;
import aima.logic.propositional.parsing.ast.Sentence;
import aima.logic.propositional.parsing.ast.Symbol;
import aima.logic.propositional.visitors.CNFClauseGatherer;
import aima.logic.propositional.visitors.CNFTransformer;
import aima.logic.propositional.visitors.SymbolCollector;
import aima.util.Converter;

/**
 * @author Ravi Mohan
 * 
 */

public class DPLLTest extends TestCase {

	private KnowledgeBase one, two, three, four;

	private DPLL dpll;

	private PEParser parser;

	@Override
	public void setUp() {
		parser = new PEParser();
		dpll = new DPLL();
	}

	public void testDPLLReturnsTrueWhenAllClausesTrueInModel() {
		Model model = new Model();
		model = model.extend(new Symbol("A"), true).extend(new Symbol("B"),
				true);
		Sentence sentence = (Sentence) parser.parse("((A AND B) AND (A OR B))");
		boolean satisfiable = dpll.dpllSatisfiable(sentence, model);
		assertEquals(true, satisfiable);
	}

	public void testDPLLReturnsFalseWhenOneClauseFalseInModel() {
		Model model = new Model();
		model = model.extend(new Symbol("A"), true).extend(new Symbol("B"),
				false);
		Sentence sentence = (Sentence) parser.parse("((A OR B) AND (A => B))");
		boolean satisfiable = dpll.dpllSatisfiable(sentence, model);
		assertEquals(false, satisfiable);
	}

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
		List clausesWithNonTrueValues = dpll.clausesWithNonTrueValues(
				clauseList, model);
		assertEquals(1, clausesWithNonTrueValues.size());
		Sentence nonTrueClause = (Sentence) parser.parse("(B AND C)");
		clausesWithNonTrueValues.contains(nonTrueClause);
	}

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
		List clausesWithNonTrueValues = dpll.clausesWithNonTrueValues(
				clauseList, model);
		assertEquals(0, clausesWithNonTrueValues.size());
	}

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
		assertNotNull(sv);
		assertEquals(new Symbol("C"), sv.symbol);
		assertEquals(new Boolean(true), sv.value);
	}

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
		assertNotNull(sv);
		assertEquals(new Symbol("C"), sv.symbol);
		assertEquals(new Boolean(false), sv.value);
	}

	public void testDPLLSucceedsWithAandNotA() {
		Sentence sentence = (Sentence) parser.parse("(A AND (NOT A))");
		boolean satisfiable = dpll.dpllSatisfiable(sentence);
		assertEquals(false, satisfiable);
	}

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
		assertTrue(kb.askWithDpll("(P00)"));
		assertFalse(kb.askWithDpll("(NOT P00)"));

	}

	public void testDPLLSucceedsWithStackOverflowBugReport1() {
		KnowledgeBase kb = new KnowledgeBase();
		Sentence sentence = (Sentence) parser
				.parse("((A OR (NOT A)) AND (A OR B))");
		assertTrue(dpll.dpllSatisfiable(sentence));

	}

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
		assertTrue(kb.askWithDpll("(P00)"));
		assertFalse(kb.askWithDpll("(NOT P00)"));

	}

}
