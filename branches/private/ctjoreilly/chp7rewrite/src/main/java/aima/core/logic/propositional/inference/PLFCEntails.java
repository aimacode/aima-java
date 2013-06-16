package aima.core.logic.propositional.inference;

import aima.core.logic.propositional.kb.KnowledgeBase;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;

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
 *   inferred &larr; a table, where inferred[s] is initally false for all symbols
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
	 */
	public boolean plfcEntails(KnowledgeBase kb, PropositionSymbol q) {
		throw new UnsupportedOperationException("TODO");
	}
}