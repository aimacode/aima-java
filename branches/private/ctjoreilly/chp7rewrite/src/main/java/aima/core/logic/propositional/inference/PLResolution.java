package aima.core.logic.propositional.inference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import aima.core.logic.propositional.LogicUtils;
import aima.core.logic.propositional.PropositionSymbolComparator;
import aima.core.logic.propositional.kb.KnowledgeBase;
import aima.core.logic.propositional.parsing.PLParser;
import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.Connective;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.propositional.visitors.CNFClauseGatherer;
import aima.core.logic.propositional.visitors.CNFTransformer;
import aima.core.logic.propositional.visitors.SymbolClassifier;
import aima.core.util.Converter;
import aima.core.util.SetOps;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 255.<br>
 * <br>
 * 
 * <pre>
 * <code>
 * function PL-RESOLUTION(KB, &alpha;) returns true or false
 *    inputs: KB, the knowledge base, a sentence in propositional logic
 *            &alpha;, the query, a sentence in propositional logic
 *            
 *    clauses &larr; the set of clauses in the CNF representation of KB &and; &not;&alpha;
 *    new &larr; {}
 *    loop do
 *       for each pair of clauses C<sub>i</sub>, C<sub>j</sub> in clauses do
 *          resolvents &larr; PL-RESOLVE(C<sub>i</sub>, C<sub>j</sub>)
 *          if resolvents contains the empty clause then return true
 *          new &larr; new &cup; resolvents
 *       if new &sube; clauses then return false
 *       clauses &larr; clauses &cup; new
 * </code>
 * </pre>
 * 
 * Figure 7.12 A simple resolution algorithm for propositional logic. The
 * function PL-RESOLVE returns the set of all possible clauses obtained by
 * resolving its two inputs.
 * 
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class PLResolution {

	/**
	 * Returns the answer to the specified question using PL-Resolution.
	 * 
	 * @param kb
	 *            the knowledge base, a sentence in propositional logic
	 * @param alpha
	 *            the query, a sentence in propositional logic
	 * 
	 * @return the answer to the specified question using PL-Resolution.
	 */
	public boolean plResolution(KnowledgeBase kb, String alpha) {
		return plResolution(kb, (Sentence) new PLParser().parse(alpha));
	}

	/**
	 * Returns the answer to the specified question using PL-Resolution.
	 * 
	 * @param kb
	 *            the knowledge base, a sentence in propositional logic
	 * @param alpha
	 *            the query, a sentence in propositional logic
	 * 
	 * @return the answer to the specified question using PL-Resolution.
	 */
	public boolean plResolution(KnowledgeBase kb, Sentence alpha) {
		Sentence kBAndNotAlpha = new ComplexSentence(Connective.AND, kb.asSentence(), new ComplexSentence(Connective.NOT, alpha));
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
				resolvents = filterOutClausesWithTwoComplementaryLiterals(resolvents);

				if (resolvents.contains(new PropositionSymbol("EMPTY_CLAUSE"))) {
					return true;
				}
				newClauses = SetOps.union(newClauses, resolvents);
				// System.out.println("clauseslist size = " +clauses.size());

			}
			if (SetOps.intersection(newClauses, clauses).size() == newClauses
					.size()) {// subset test
				return false;
			}
			clauses = SetOps.union(newClauses, clauses);
			clauses = filterOutClausesWithTwoComplementaryLiterals(clauses);
		}

	}

	public Set<Sentence> plResolve(Sentence clause1, Sentence clause2) {
		Set<Sentence> resolvents = new HashSet<Sentence>();
		ClauseSymbols cs = new ClauseSymbols(clause1, clause2);
		Iterator<PropositionSymbol> iter = cs.getComplementedSymbols().iterator();
		while (iter.hasNext()) {
			PropositionSymbol symbol = iter.next();
			resolvents.add(createResolventClause(cs, symbol));
		}

		return resolvents;
	}

	public boolean plResolution(String kbs, String alphaString) {
		KnowledgeBase kb = new KnowledgeBase();
		kb.tell(kbs);
		Sentence alpha = (Sentence) new PLParser().parse(alphaString);
		return plResolution(kb, alpha);
	}

	//
	// PRIVATE METHODS
	//

	private Set<Sentence> filterOutClausesWithTwoComplementaryLiterals(
			Set<Sentence> clauses) {
		Set<Sentence> filtered = new HashSet<Sentence>();
		SymbolClassifier classifier = new SymbolClassifier();
		Iterator<Sentence> iter = clauses.iterator();
		while (iter.hasNext()) {
			Sentence clause = iter.next();
			Set<PropositionSymbol> positiveSymbols = classifier
					.getPositiveSymbolsIn(clause);
			Set<PropositionSymbol> negativeSymbols = classifier
					.getNegativeSymbolsIn(clause);
			if ((SetOps.intersection(positiveSymbols, negativeSymbols).size() == 0)) {
				filtered.add(clause);
			}
		}
		return filtered;
	}

	private Sentence createResolventClause(ClauseSymbols cs, PropositionSymbol toRemove) {
		List<PropositionSymbol> positiveSymbols = new Converter<PropositionSymbol>().setToList(SetOps
				.union(cs.clause1PositiveSymbols, cs.clause2PositiveSymbols));
		List<PropositionSymbol> negativeSymbols = new Converter<PropositionSymbol>().setToList(SetOps
				.union(cs.clause1NegativeSymbols, cs.clause2NegativeSymbols));
		if (positiveSymbols.contains(toRemove)) {
			positiveSymbols.remove(toRemove);
		}
		if (negativeSymbols.contains(toRemove)) {
			negativeSymbols.remove(toRemove);
		}

		Collections.sort(positiveSymbols, new PropositionSymbolComparator());
		Collections.sort(negativeSymbols, new PropositionSymbolComparator());

		List<Sentence> sentences = new ArrayList<Sentence>();
		for (int i = 0; i < positiveSymbols.size(); i++) {
			sentences.add(positiveSymbols.get(i));
		}
		for (int i = 0; i < negativeSymbols.size(); i++) {
			sentences.add(new ComplexSentence(Connective.NOT, negativeSymbols.get(i)));
		}
		if (sentences.size() == 0) {
			return new PropositionSymbol("EMPTY_CLAUSE"); // == empty clause
		} else {
			return LogicUtils.chainWith(Connective.OR, sentences);
		}

	}

	private List<List<Sentence>> getCombinationPairs(List<Sentence> clausesList) {
		// int odd = clausesList.size() % 2;
		// int midpoint = 0;
		// if (odd == 1) {
		// midpoint = (clausesList.size() / 2) + 1;
		// } else {
		// midpoint = (clausesList.size() / 2);
		// }

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
		Set<PropositionSymbol> clause1Symbols, clause1PositiveSymbols,
				clause1NegativeSymbols;

		Set<PropositionSymbol> clause2Symbols, clause2PositiveSymbols,
				clause2NegativeSymbols;

		Set<PropositionSymbol> positiveInClause1NegativeInClause2,
				negativeInClause1PositiveInClause2;

		public ClauseSymbols(Sentence clause1, Sentence clause2) {

			SymbolClassifier classifier = new SymbolClassifier();

			clause1Symbols = classifier.getSymbolsIn(clause1);
			clause1PositiveSymbols = classifier.getPositiveSymbolsIn(clause1);
			clause1NegativeSymbols = classifier.getNegativeSymbolsIn(clause1);

			clause2Symbols = classifier.getSymbolsIn(clause2);
			clause2PositiveSymbols = classifier.getPositiveSymbolsIn(clause2);
			clause2NegativeSymbols = classifier.getNegativeSymbolsIn(clause2);

			positiveInClause1NegativeInClause2 = SetOps.intersection(
					clause1PositiveSymbols, clause2NegativeSymbols);
			negativeInClause1PositiveInClause2 = SetOps.intersection(
					clause1NegativeSymbols, clause2PositiveSymbols);

		}

		public Set<PropositionSymbol> getComplementedSymbols() {
			return SetOps.union(positiveInClause1NegativeInClause2,
					negativeInClause1PositiveInClause2);
		}

	}
}