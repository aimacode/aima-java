package aima.test.core.unit.logic.propositional.inference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import aima.core.logic.propositional.inference.DPLL;
import aima.core.logic.propositional.inference.OptimizedDPLL;
import aima.core.logic.propositional.kb.KnowledgeBase;
import aima.core.logic.propositional.kb.data.Clause;
import aima.core.logic.propositional.kb.data.Model;
import aima.core.logic.propositional.parsing.PLParser;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.transformations.ConvertToConjunctionOfClauses;
import aima.core.logic.propositional.transformations.SymbolCollector;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
@RunWith(Parameterized.class)
public class DPLLTest {

	private DPLL     dpll;
	private PLParser parser;
	
	@Parameters(name = "{index}: dpll={0}")
    public static Collection<Object[]> inferenceAlgorithmSettings() {
        return Arrays.asList(new Object[][] {
        		{new DPLL()},
        		{new OptimizedDPLL()}   
        });
    }

	public DPLLTest(DPLL dpll) {
		this.dpll   = dpll;
		this.parser = new PLParser();
	}

	@Test
	public void testDPLLReturnsTrueWhenAllClausesTrueInModel() {
		Model model = new Model();
		model = model.union(new PropositionSymbol("A"), true).union(
				new PropositionSymbol("B"), true);
		Sentence sentence = parser.parse("A & B & (A | B)");
		Set<Clause> clauses = ConvertToConjunctionOfClauses.apply(sentence)
				.getClauses();
		List<PropositionSymbol> symbols = new ArrayList<PropositionSymbol>(
				SymbolCollector.getSymbolsFrom(sentence));

		boolean satisfiable = dpll.dpll(clauses, symbols, model);
		Assert.assertEquals(true, satisfiable);
	}

	@Test
	public void testDPLLReturnsFalseWhenOneClauseFalseInModel() {
		Model model = new Model();
		model = model.union(new PropositionSymbol("A"), true).union(
				new PropositionSymbol("B"), false);
		Sentence sentence = parser.parse("(A | B) & (A => B)");
		Set<Clause> clauses = ConvertToConjunctionOfClauses.apply(sentence)
				.getClauses();
		List<PropositionSymbol> symbols = new ArrayList<PropositionSymbol>(
				SymbolCollector.getSymbolsFrom(sentence));

		boolean satisfiable = dpll.dpll(clauses, symbols, model);
		Assert.assertEquals(false, satisfiable);
	}

	@Test
	public void testDPLLSucceedsWithAandNotA() {
		Sentence sentence = parser.parse("A & ~A");
		boolean satisfiable = dpll.isSatisfiable(sentence);
		Assert.assertEquals(false, satisfiable);
	}

	@Test
	public void testDPLLSucceedsWithChadCarffsBugReport() {
		KnowledgeBase kb = new KnowledgeBase();

		kb.tell("B12 <=> P11 | P13 | P22 | P02");
		kb.tell("B21 <=> P20 | P22 | P31 | P11");
		kb.tell("B01 <=> P00 | P02 | P11");
		kb.tell("B10 <=> P11 | P20 | P00");
		kb.tell("~B21");
		kb.tell("~B12");
		kb.tell("B10");
		kb.tell("B01");

		Assert.assertTrue(dpll.isEntailed(kb, parser.parse("P00")));
		Assert.assertFalse(dpll.isEntailed(kb, parser.parse("~P00")));
	}

	@Test
	public void testDPLLSucceedsWithStackOverflowBugReport1() {
		Sentence sentence = (Sentence) parser.parse("(A | ~A) & (A | B)");
		Assert.assertTrue(dpll.isSatisfiable(sentence));
	}

	@Test
	public void testDPLLSucceedsWithChadCarffsBugReport2() {
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell("B10 <=> P11 | P20 | P00");
		kb.tell("B01 <=> P00 | P02 | P11");
		kb.tell("B21 <=> P20 | P22 | P31 | P11");
		kb.tell("B12 <=> P11 | P13 | P22 | P02");
		kb.tell("~B21");
		kb.tell("~B12");
		kb.tell("B10");
		kb.tell("B01");

		Assert.assertTrue(dpll.isEntailed(kb, parser.parse("P00")));
		Assert.assertFalse(dpll.isEntailed(kb, parser.parse("~P00")));
	}

	@Test
	public void testIssue66() {
		// http://code.google.com/p/aima-java/issues/detail?id=66
		Model model = new Model();
		model = model.union(new PropositionSymbol("A"), false)
				.union(new PropositionSymbol("B"), false)
				.union(new PropositionSymbol("C"), true);
		Sentence sentence = parser.parse("((A | B) | C)");
		Set<Clause> clauses = ConvertToConjunctionOfClauses.apply(sentence)
				.getClauses();
		List<PropositionSymbol> symbols = new ArrayList<PropositionSymbol>(
				SymbolCollector.getSymbolsFrom(sentence));

		boolean satisfiable = dpll.dpll(clauses, symbols, model);
		Assert.assertEquals(true, satisfiable);
	}

	@Test
	public void testDoesNotKnow() {
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell("A");

		Assert.assertFalse(dpll.isEntailed(kb, parser.parse("B")));
		Assert.assertFalse(dpll.isEntailed(kb, parser.parse("~B")));
	}
}
