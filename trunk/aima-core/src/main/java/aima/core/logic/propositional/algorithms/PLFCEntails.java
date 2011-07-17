package aima.core.logic.propositional.algorithms;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import aima.core.logic.propositional.parsing.ast.BinarySentence;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.visitors.SymbolCollector;
import aima.core.util.Converter;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class PLFCEntails {

	private Hashtable<HornClause, Integer> count;

	private Hashtable<Symbol, Boolean> inferred;

	private Stack<Symbol> agenda;

	public PLFCEntails() {
		count = new Hashtable<HornClause, Integer>();
		inferred = new Hashtable<Symbol, Boolean>();
		agenda = new Stack<Symbol>();
	}

	/**
	 * Return the answer to the specified question using the PL-FC-Entails
	 * algorithm
	 * 
	 * @param kb
	 *            the knowledge base, a set of propositional definite clauses
	 * @param s
	 *            the query, a proposition symbol
	 * 
	 * @return the answer to the specified question using the PL-FC-Entails
	 *         algorithm
	 */
	public boolean plfcEntails(KnowledgeBase kb, String s) {
		return plfcEntails(kb, new Symbol(s));
	}

	/**
	 * Return the answer to the specified question using the PL-FC-Entails
	 * algorithm
	 * 
	 * @param kb
	 *            the knowledge base, a set of propositional definite clauses
	 * @param q
	 *            the query, a proposition symbol
	 * 
	 * @return the answer to the specified question using the PL-FC-Entails
	 *         algorithm
	 */
	public boolean plfcEntails(KnowledgeBase kb, Symbol q) {
		List<HornClause> hornClauses = asHornClauses(kb.getSentences());
		while (agenda.size() != 0) {
			Symbol p = agenda.pop();
			while (!inferred(p)) {
				inferred.put(p, Boolean.TRUE);

				for (int i = 0; i < hornClauses.size(); i++) {
					HornClause hornClause = hornClauses.get(i);
					if (hornClause.premisesContainsSymbol(p)) {
						decrementCount(hornClause);
						if (countisZero(hornClause)) {
							if (hornClause.head().equals(q)) {
								return true;
							} else {
								agenda.push(hornClause.head());
							}
						}
					}
				}
			}
		}
		return false;
	}

	private List<HornClause> asHornClauses(List<Sentence> sentences) {
		List<HornClause> hornClauses = new ArrayList<HornClause>();
		for (int i = 0; i < sentences.size(); i++) {
			Sentence sentence = sentences.get(i);
			HornClause clause = new HornClause(sentence);
			hornClauses.add(clause);
		}
		return hornClauses;
	}

	private boolean countisZero(HornClause hornClause) {

		return (count.get(hornClause)).intValue() == 0;
	}

	private void decrementCount(HornClause hornClause) {
		int value = (count.get(hornClause)).intValue();
		count.put(hornClause, new Integer(value - 1));

	}

	private boolean inferred(Symbol p) {
		Object value = inferred.get(p);
		return ((value == null) || value.equals(Boolean.TRUE));
	}

	public class HornClause {
		List<Symbol> premiseSymbols;

		Symbol head;

		/**
		 * Constructs a horn clause from the specified sentence.
		 * 
		 * @param sentence
		 *            a sentence in propositional logic
		 */
		public HornClause(Sentence sentence) {
			if (sentence instanceof Symbol) {
				head = (Symbol) sentence;
				agenda.push(head);
				premiseSymbols = new ArrayList<Symbol>();
				count.put(this, new Integer(0));
				inferred.put(head, Boolean.FALSE);
			} else if (!isImpliedSentence(sentence)) {
				throw new RuntimeException("Sentence " + sentence
						+ " is not a horn clause");

			} else {
				BinarySentence bs = (BinarySentence) sentence;
				head = (Symbol) bs.getSecond();
				inferred.put(head, Boolean.FALSE);
				Set<Symbol> symbolsInPremise = new SymbolCollector()
						.getSymbolsIn(bs.getFirst());
				Iterator<Symbol> iter = symbolsInPremise.iterator();
				while (iter.hasNext()) {
					inferred.put(iter.next(), Boolean.FALSE);
				}
				premiseSymbols = new Converter<Symbol>()
						.setToList(symbolsInPremise);
				count.put(this, new Integer(premiseSymbols.size()));
			}

		}

		private boolean isImpliedSentence(Sentence sentence) {
			return ((sentence instanceof BinarySentence) && ((BinarySentence) sentence)
					.getOperator().equals("=>"));
		}

		/**
		 * Returns the conclusion of this horn clause. In horn form, the premise
		 * is called the body, and the conclusion is called the head.
		 * 
		 * @return the conclusion of this horn clause.
		 */
		public Symbol head() {

			return head;
		}

		/**
		 * Return <code>true</code> if the premise of this horn clause contains
		 * the specified symbol.
		 * 
		 * @param q
		 *            a symbol in propositional logic
		 * 
		 * @return <code>true</code> if the premise of this horn clause contains
		 *         the specified symbol.
		 */
		public boolean premisesContainsSymbol(Symbol q) {
			return premiseSymbols.contains(q);
		}

		/**
		 * Returns a list of all the symbols in the premise of this horn clause
		 * 
		 * @return a list of all the symbols in the premise of this horn clause
		 */
		public List<Symbol> getPremiseSymbols() {
			return premiseSymbols;
		}

		@Override
		public boolean equals(Object o) {

			if (this == o) {
				return true;
			}
			if ((o == null) || (this.getClass() != o.getClass())) {
				return false;
			}
			HornClause ohc = (HornClause) o;
			if (premiseSymbols.size() != ohc.premiseSymbols.size()) {
				return false;
			}
			for (Symbol s : premiseSymbols) {
				if (!ohc.premiseSymbols.contains(s)) {
					return false;
				}
			}

			return true;
		}

		@Override
		public int hashCode() {
			int result = 17;
			for (Symbol s : premiseSymbols) {
				result = 37 * result + s.hashCode();
			}
			return result;
		}

		@Override
		public String toString() {
			return premiseSymbols.toString() + " => " + head;
		}
	}
}