/*
 * Created on Dec 8, 2004
 *
 */
package aima.logic.propositional.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import aima.logic.propositional.parsing.PEParser;
import aima.logic.propositional.parsing.ast.BinarySentence;
import aima.logic.propositional.parsing.ast.Sentence;
import aima.logic.propositional.parsing.ast.Symbol;
import aima.logic.propositional.parsing.ast.SymbolComparator;
import aima.logic.propositional.parsing.ast.UnarySentence;
import aima.logic.propositional.visitors.CNFClauseGatherer;
import aima.logic.propositional.visitors.CNFTransformer;
import aima.logic.propositional.visitors.SymbolClassifier;
import aima.util.Converter;
import aima.util.LogicUtils;
import aima.util.SetOps;

/**
 * @author Ravi Mohan
 * 
 */

public class PLResolution {

	public boolean plResolution(KnowledgeBase kb, String alpha) {
		return plResolution(kb, (Sentence) new PEParser().parse(alpha));
	}

	public boolean plResolution(KnowledgeBase kb, Sentence alpha) {
		Sentence kBAndNotAlpha = new BinarySentence("AND", kb.asSentence(),
				new UnarySentence(alpha));
		Set<Sentence> clauses = new CNFClauseGatherer()
				.getClausesFrom(new CNFTransformer().transform(kBAndNotAlpha));
		clauses = filterOutClausesWithTwoComplementaryLiterals(clauses);
		Set<Sentence> newClauses = new HashSet<Sentence>();
		while (true) {
			List<List<Sentence>> pairs = getCombinationPairs(new Converter<Sentence>()
					.setToList(clauses));

			for (int i = 0; i < pairs.size(); i++) {
				List<Sentence> pair = pairs.get(i);
				// System.out.println("pair number" + i+" of "+pairs.size());
				Set<Sentence> resolvents = plResolve(pair.get(0), pair.get(1));
				if (resolvents.contains(new Symbol("EMPTY_CLAUSE"))) {
					return true;
				}
				newClauses = new SetOps<Sentence>().union(newClauses,
						resolvents);
				// System.out.println("clauseslist size = " +clauses.size());

			}
			if (new SetOps<Sentence>().intersection(newClauses, clauses).size() == newClauses
					.size()) {// subset test
				return false;
			}
			clauses = new SetOps<Sentence>().union(newClauses, clauses);
			clauses = filterOutClausesWithTwoComplementaryLiterals(clauses);
		}

	}

	private Set<Sentence> filterOutClausesWithTwoComplementaryLiterals(
			Set<Sentence> clauses) {
		Set<Sentence> filtered = new HashSet<Sentence>();
		SymbolClassifier classifier = new SymbolClassifier();
		Iterator iter = clauses.iterator();
		while (iter.hasNext()) {
			Sentence clause = (Sentence) iter.next();
			Set<Symbol> positiveSymbols = classifier
					.getPositiveSymbolsIn(clause);
			Set<Symbol> negativeSymbols = classifier
					.getNegativeSymbolsIn(clause);
			if ((new SetOps<Symbol>().intersection(positiveSymbols,
					negativeSymbols).size() == 0)) {
				filtered.add(clause);
			}
		}
		return filtered;
	}

	public Set<Sentence> plResolve(Sentence clause1, Sentence clause2) {
		Set<Sentence> resolvents = new HashSet<Sentence>();
		ClauseSymbols cs = new ClauseSymbols(clause1, clause2);
		Iterator iter = cs.getComplementedSymbols().iterator();
		while (iter.hasNext()) {
			Symbol symbol = (Symbol) iter.next();
			resolvents.add(createResolventClause(cs, symbol));
		}

		return resolvents;
	}

	private Sentence createResolventClause(ClauseSymbols cs, Symbol toRemove) {
		List<Symbol> positiveSymbols = new Converter<Symbol>()
				.setToList(new SetOps<Symbol>().union(
						cs.clause1PositiveSymbols, cs.clause2PositiveSymbols));
		List<Symbol> negativeSymbols = new Converter<Symbol>()
				.setToList(new SetOps<Symbol>().union(
						cs.clause1NegativeSymbols, cs.clause2NegativeSymbols));
		if (positiveSymbols.contains(toRemove)) {
			positiveSymbols.remove(toRemove);
		}
		if (negativeSymbols.contains(toRemove)) {
			negativeSymbols.remove(toRemove);
		}

		Collections.sort(positiveSymbols, new SymbolComparator());
		Collections.sort(negativeSymbols, new SymbolComparator());

		List<Sentence> sentences = new ArrayList<Sentence>();
		for (int i = 0; i < positiveSymbols.size(); i++) {
			sentences.add(positiveSymbols.get(i));
		}
		for (int i = 0; i < negativeSymbols.size(); i++) {
			sentences.add(new UnarySentence(negativeSymbols.get(i)));
		}
		if (sentences.size() == 0) {
			return new Symbol("EMPTY_CLAUSE"); // == empty clause
		} else {
			return LogicUtils.chainWith("OR", sentences);
		}

	}

	private List<List<Sentence>> getCombinationPairs(List<Sentence> clausesList) {
		int odd = clausesList.size() % 2;
		int midpoint = 0;
		if (odd == 1) {
			midpoint = (clausesList.size() / 2) + 1;
		} else {
			midpoint = (clausesList.size() / 2);
		}

		List<List<Sentence>> pairs = new ArrayList<List<Sentence>>();
		for (int i = 0; i < clausesList.size(); i++) {
			for (int j = i; j < clausesList.size(); j++) {
				List<Sentence> pair = new ArrayList<Sentence>();
				Sentence first = clausesList.get(i);
				Sentence second = clausesList.get(j);

				if (!(first.equals(second))) {
					pair.add(first);
					pair.add(second);
					pairs.add(pair);
				}
			}
		}
		return pairs;
	}

	class ClauseSymbols {
		Set<Symbol> clause1Symbols, clause1PositiveSymbols,
				clause1NegativeSymbols;

		Set<Symbol> clause2Symbols, clause2PositiveSymbols,
				clause2NegativeSymbols;

		Set<Symbol> positiveInClause1NegativeInClause2,
				negativeInClause1PositiveInClause2;

		public ClauseSymbols(Sentence clause1, Sentence clause2) {

			SymbolClassifier classifier = new SymbolClassifier();

			clause1Symbols = classifier.getSymbolsIn(clause1);
			clause1PositiveSymbols = classifier.getPositiveSymbolsIn(clause1);
			clause1NegativeSymbols = classifier.getNegativeSymbolsIn(clause1);

			clause2Symbols = classifier.getSymbolsIn(clause2);
			clause2PositiveSymbols = classifier.getPositiveSymbolsIn(clause2);
			clause2NegativeSymbols = classifier.getNegativeSymbolsIn(clause2);

			positiveInClause1NegativeInClause2 = new SetOps<Symbol>()
					.intersection(clause1PositiveSymbols,
							clause2NegativeSymbols);
			negativeInClause1PositiveInClause2 = new SetOps<Symbol>()
					.intersection(clause1NegativeSymbols,
							clause2PositiveSymbols);

		}

		public Set getComplementedSymbols() {
			return new SetOps<Symbol>().union(
					positiveInClause1NegativeInClause2,
					negativeInClause1PositiveInClause2);
		}

	}

	public boolean plResolution(String kbs, String alphaString) {
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell(kbs);
		Sentence alpha = (Sentence) new PEParser().parse(alphaString);
		return plResolution(kb, alpha);
	}
}