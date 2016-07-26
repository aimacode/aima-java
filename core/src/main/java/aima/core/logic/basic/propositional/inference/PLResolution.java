package aima.core.logic.basic.propositional.inference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import aima.core.logic.api.propositional.KnowledgeBase;
import aima.core.logic.basic.propositional.kb.data.Clause;
import aima.core.logic.basic.propositional.kb.data.Literal;
import aima.core.logic.basic.propositional.parsing.PLParser;
import aima.core.logic.basic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.basic.propositional.parsing.ast.Connective;
import aima.core.logic.basic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.basic.propositional.parsing.ast.Sentence;
import aima.core.logic.basic.propositional.visitors.ConvertToConjunctionOfClauses;
import aima.core.util.SetOps;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): page ???.<br>
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
 * Figure ?.?? A simple resolution algorithm for propositional logic. The
 * function PL-RESOLVE returns the set of all possible clauses obtained by
 * resolving its two inputs.<br>
 * <br>
 * Note: Optional optimization added to implementation whereby tautological
 * clauses can be removed during processing of the algorithm - see pg. 254 of
 * AIMA3e:<br>
 * <blockquote> Inspection of Figure ?.?? reveals that many resolution steps are
 * pointless. For example, the clause B<sub>1,1</sub> &or; &not;B<sub>1,1</sub>
 * &or; P<sub>1,2</sub> is equivalent to <i>True</i> &or; P<sub>1,2</sub> which
 * is equivalent to <i>True</i>. Deducing that <i>True</i> is true is not very
 * helpful. Therefore, any clauses in which two complementary literals appear
 * can be discarded. </blockquote>
 * 
 * @see Clause#isTautology()
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Mike Stampone
 * @author Anurag Rai
 */
public class PLResolution {
	
	/**
	 * PL-RESOLUTION(KB, &alpha;)<br>
	 * A simple resolution algorithm for propositional logic.
	 * 
	 * @param kb
	 *            the knowledge base, a sentence in propositional logic.
	 * @param alpha
	 *            the query, a sentence in propositional logic.
	 * @return true if KB |= &alpha;, false otherwise.
	 */
	public boolean plResolution(KnowledgeBase kb, String queryString, PLParser plparser) {
		PLParser parser = plparser;
		Sentence alpha = parser.parse(queryString);
		return plResolution(kb, alpha);
	}
	
	/**
	 * function PL-RESOLUTION(KB, &alpha;) returns true or false
	 * 
	 * @param kb
	 *            the knowledge base, a sentence in propositional logic.
	 * @param alpha
	 *            the query, a sentence in propositional logic.
	 * @return true if KB |= &alpha;, false otherwise.
	 */
	public boolean plResolution(KnowledgeBase kb, Sentence alpha) {
		// clauses <- the set of clauses in the CNF representation
		// of KB & ~alpha
		Set<Clause> clauses = setOfClausesInTheCNFRepresentationOfKBAndNotAlpha(
				kb, alpha);
		// new <- {}
		Set<Clause> newClauses = new LinkedHashSet<Clause>();
		// loop do
		do {
			// for each pair of clauses C_i, C_j in clauses do
			List<Clause> clausesAsList = new ArrayList<Clause>(clauses);
			for (int i = 0; i < clausesAsList.size() - 1; i++) {
				Clause ci = clausesAsList.get(i);
				for (int j = i + 1; j < clausesAsList.size(); j++) {
					Clause cj = clausesAsList.get(j);
					// resolvents <- PL-RESOLVE(C_i, C_j)
					Set<Clause> resolvents = plResolve(ci, cj);
					// if resolvents contains the empty clause then return true
					if (resolvents.contains(Clause.EMPTY)) {
						return true;
					}
					// new <- new U resolvents
					newClauses.addAll(resolvents);
				}
			}
			// if new is subset of clauses then return false
			if (clauses.containsAll(newClauses)) {
				return false;
			}

			// clauses <- clauses U new
			clauses.addAll(newClauses);

		} 
		while (true);
	}

	/**
	 * PL-RESOLVE(C<sub>i</sub>, C<sub>j</sub>)<br>
	 * Calculate the set of all possible clauses by resolving its two inputs.
	 * 
	 * @param ci
	 *            clause 1
	 * @param cj
	 *            clause 2
	 * @return the set of all possible clauses obtained by resolving its two
	 *         inputs.
	 */
	public Set<Clause> plResolve(Clause ci, Clause cj) {
		Set<Clause> resolvents = new LinkedHashSet<Clause>();

		// The complementary positive literals from C_i
		resolvePositiveWithNegative(ci, cj, resolvents);
		// The complementary negative literals from C_i
		resolvePositiveWithNegative(cj, ci, resolvents);

		return resolvents;
	}

	//
	// SUPPORTING CODE
	//

	private boolean discardTautologies;

	/**
	 * Default constructor, which will set the algorithm to discard tautologies
	 * by default.
	 */
	public PLResolution() {
		this(true);
	}

	/**
	 * Constructor.
	 * 
	 * @param discardTautologies
	 *            true if the algorithm is to discard tautological clauses
	 *            during processing, false otherwise.
	 */
	public PLResolution(boolean discardTautologies) {
		setDiscardTautologies(discardTautologies);
	}

	/**
	 * @return true if the algorithm will discard tautological clauses during
	 *         processing.
	 */
	public boolean isDiscardTautologies() {
		return discardTautologies;
	}

	/**
	 * Determine whether or not the algorithm should discard tautological
	 * clauses during processing.
	 * 
	 * @param discardTautologies
	 */
	public void setDiscardTautologies(boolean discardTautologies) {
		this.discardTautologies = discardTautologies;
	}

	//
	// PROTECTED
	//
	protected Set<Clause> setOfClausesInTheCNFRepresentationOfKBAndNotAlpha(
			KnowledgeBase kb, Sentence alpha) {

		// KB & ~alpha;
		Sentence isContradiction = new ComplexSentence(Connective.AND,
				kb.asSentence(), new ComplexSentence(Connective.NOT, alpha));
		// the set of clauses in the CNF representation
		Set<Clause> clauses = new LinkedHashSet<Clause>(
				ConvertToConjunctionOfClauses.convert(isContradiction)
						.getClauses());

		discardTautologies(clauses);

		return clauses;
	}

	protected void resolvePositiveWithNegative(Clause c1, Clause c2, Set<Clause> resolvents) {
		// Calculate the complementary positive literals from c1 with
		// the negative literals from c2
		Set<PropositionSymbol> complementary = SetOps.intersection(
				c1.getPositiveSymbols(), c2.getNegativeSymbols());
		// Construct a resolvent clause for each complement found
		for (PropositionSymbol complement : complementary) {
			// Retrieve the literals from c1 that are not the complement
			List<Literal> resolventLiterals = c1.getLiterals().stream().filter(
					c1l -> c1l.isNegativeLiteral() 
					|| !c1l.getAtomicSentence().equals(complement))
					.collect(Collectors.toList());		
			// Retrieve the literals from c2 that are not the complement
			c2.getLiterals().stream().filter(
					c2l -> c2l.isPositiveLiteral()
					|| !c2l.getAtomicSentence().equals(complement))
					.forEach(resolventLiterals::add);
			
			// Construct the resolvent clause
			Clause resolvent = new Clause(resolventLiterals);
			// Discard tautological clauses if this optimization is turned on.
			if (!(isDiscardTautologies() && resolvent.isTautology())) {
				resolvents.add(resolvent);
			}
		}
	}

	// Utility routine for removing the tautological clauses from a set (in
	// place).
	protected void discardTautologies(Set<Clause> clauses) {
		if (isDiscardTautologies()) {
			Set<Clause> toDiscard = new HashSet<Clause>();
			for (Clause c : clauses) {
				if (c.isTautology()) {
					toDiscard.add(c);
				}
			}
			clauses.removeAll(toDiscard);
		}
	}
}
