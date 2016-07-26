package aima.core.logic.basic.propositional.inference;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import aima.core.logic.api.propositional.KnowledgeBase;
import aima.core.logic.basic.propositional.kb.data.Clause;
import aima.core.logic.basic.propositional.parsing.PLParser;
import aima.core.logic.basic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.basic.propositional.visitors.ConvertToConjunctionOfClauses;
import aima.core.logic.basic.propositional.visitors.SymbolCollector;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 258.<br>
 * <br>
 * 
 * <pre>
 * <code>
 * function PL-FC-ENTAILS?(KB, q) returns true or false
 *   inputs: KB, the knowledge base, a set of propositional definite clauses
 *           q, the query, a proposition symbol
 *   count &larr; a table, where count[c] is the number of symbols in c's premise
 *   inferred &larr; a table, where inferred[s] is initially false for all symbols
 *   agenda &larr; a queue of symbols, initially symbols known to be true in KB
 *   
 *   while agenda is not empty do
 *     p &larr; Pop(agenda)
 *     if p = q then return true
 *     if inferred[p] = false then
 *        inferred[p] &larr; true
 *        for each clause c in KB where p is in c.PREMISE do
 *            decrement count[c]
 *            if count[c] = 0 then add c.CONCLUSION to agenda
 *   return false
 * </code>
 * </pre>
 * 
 * Figure 7.15 the forward-chaining algorithm for propositional logic. The
 * <i>agenda</i> keeps track of symbols known to be true but not yet
 * "processed". The <i>count</i> table keeps track of how many premises of each
 * implication are as yet unknown. Whenever a new symbol p from the agenda is
 * processed, the count is reduced by one for each implication in whose premise
 * p appears (easily identified in constant time with appropriate indexing.) If
 * a count reaches zero, all the premises of the implication are known, so its
 * conclusion can be added to the agenda. Finally, we need to keep track of
 * which symbols have been processed; a symbol that is already in the set of
 * inferred symbols need not be added to the agenda again. This avoids redundant
 * work and prevents loops caused by implications such as P &rArr; Q and Q
 * &rArr; P.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Mike Stampone
 * @author Anurag Rai
 */
public class PLFCEntails {
	
	/**
	 * PL-FC-ENTAILS?(KB, q)<br>
	 * The forward-chaining algorithm for propositional logic.
	 * 
	 * @param kb
	 *            the knowledge base, a set of propositional definite clauses.
	 * @param q
	 *            q, the query, a proposition symbol
	 * @return true if KB |= q, false otherwise.
	 * @throws IllegalArgumentException
	 *             if KB contains any non-definite clauses.
	 */
	public boolean plfcEntails(KnowledgeBase kb, String query, PLParser plparser) {
		PLParser parser = plparser;
		PropositionSymbol q = (PropositionSymbol) parser.parse(query);
		return plfcEntails(kb, q);
	}
	
	/**
	 * function PL-FC-ENTAILS?(KB, q) returns true or false
	 * 
	 * @param kb
	 *            the knowledge base, a set of propositional definite clauses.
	 * @param q
	 *            q, the query, a proposition symbol
	 * @return true if KB |= q, false otherwise.
	 * @throws IllegalArgumentException
	 *             if KB contains any non-definite clauses.
	 */
	public boolean plfcEntails(KnowledgeBase kb, PropositionSymbol q) {
		// count <- a table, where count[c] is the number of symbols in c's
		// premise
		Map<Clause, Integer> count = initializeCount(kb);
		// inferred <- a table, where inferred[s] is initially false for all
		// symbols
		Map<PropositionSymbol, Boolean> inferred = initializeInferred(kb);
		// agenda <- a queue of symbols, initially symbols known to be true in
		// KB
		Queue<PropositionSymbol> agenda = initializeAgenda(count);
		// Note: an index for p to the clauses where p appears in the premise
		Map<PropositionSymbol, Set<Clause>> pToClausesWithPInPremise = initializeIndex(
				count, inferred);

		// while agenda is not empty do
		while (!agenda.isEmpty()) {
			// p <- Pop(agenda)
			PropositionSymbol p = agenda.remove();
			// if p = q then return true
			if (p.equals(q)) {
				return true;
			}
			// if inferred[p] = false then
			if (inferred.get(p).equals(Boolean.FALSE)) {
				// inferred[p] <- true
				inferred.put(p, true);
				// for each clause c in KB where p is in c.PREMISE do
				for (Clause c : pToClausesWithPInPremise.get(p)) {
					// decrement count[c]
					decrement(count, c);
					// if count[c] = 0 then add c.CONCLUSION to agenda
					if (count.get(c) == 0) {
						agenda.add(conclusion(c));
					}
				}
			}
		}

		// return false
		return false;
	}

	//
	// SUPPORTING CODE
	//

	//
	// PROTECTED
	//
	protected Map<Clause, Integer> initializeCount(KnowledgeBase kb) {
		// count <- a table, where count[c] is the number of symbols in c's
		// premise
		Map<Clause, Integer> count = new HashMap<Clause, Integer>();

		Set<Clause> clauses = ConvertToConjunctionOfClauses.convert(
				kb.asSentence()).getClauses();
		for (Clause c : clauses) {
			if (!c.isDefiniteClause()) {
				throw new IllegalArgumentException(
						"Knowledge Base contains non-definite clauses:" + c);
			}
			// Note: # of negative literals is equivalent to the number of
			// symbols in c's premise
			count.put(c, c.getNumberNegativeLiterals());
		}

		return count;
	}

	protected Map<PropositionSymbol, Boolean> initializeInferred(KnowledgeBase kb) {
		// inferred <- a table, where inferred[s] is initially false for all
		// symbols
		Map<PropositionSymbol, Boolean> inferred = new HashMap<PropositionSymbol, Boolean>();
		for (PropositionSymbol p : SymbolCollector.getSymbolsFrom(kb
				.asSentence())) {
			inferred.put(p, false);
		}
		return inferred;
	}

	// Note: at the point of calling this routine, count will contain all the
	// clauses in KB.
	protected Queue<PropositionSymbol> initializeAgenda(Map<Clause, Integer> count) {
		// agenda <- a queue of symbols, initially symbols known to be true in
		// KB
		Queue<PropositionSymbol> agenda = new LinkedList<PropositionSymbol>();
		for (Clause c : count.keySet()) {
			// No premise just a conclusion, then we know its true
			if (c.getNumberNegativeLiterals() == 0) {
				agenda.add(conclusion(c));
			}
		}
		return agenda;
	}

	// Note: at the point of calling this routine, count will contain all the
	// clauses in KB while inferred will contain all the proposition symbols.
	protected Map<PropositionSymbol, Set<Clause>> initializeIndex(
			Map<Clause, Integer> count, Map<PropositionSymbol, Boolean> inferred) {
		Map<PropositionSymbol, Set<Clause>> pToClausesWithPInPremise = new HashMap<PropositionSymbol, Set<Clause>>();
		for (PropositionSymbol p : inferred.keySet()) {
			Set<Clause> clausesWithPInPremise = new HashSet<Clause>();
			for (Clause c : count.keySet()) {
				// Note: The negative symbols comprise the premise
				if (c.getNegativeSymbols().contains(p)) {
					clausesWithPInPremise.add(c);
				}
			}
			pToClausesWithPInPremise.put(p, clausesWithPInPremise);
		}
		return pToClausesWithPInPremise;
	}

	protected void decrement(Map<Clause, Integer> count, Clause c) {
		int currentCount = count.get(c);
		// Note: a definite clause can just be a fact (i.e. 1 positive literal)
		// However, we only decrement those where the symbol is in the premise
		// so we don't need to worry about going < 0.
		count.put(c, currentCount - 1);
	}

	protected PropositionSymbol conclusion(Clause c) {
		// Note: the conclusion is from the single positive
		// literal in the definite clause (which we are
		// restricted to).
		return c.getPositiveSymbols().iterator().next();
	}
}