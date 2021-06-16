package aima.core.logic.propositional.inference;

import aima.core.logic.propositional.kb.KnowledgeBase;
import aima.core.logic.propositional.kb.data.Clause;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.util.Tasks;

import java.util.*;

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
 * resolving its two inputs.<br>
 * <br>
 *
 * Note: Optional optimization added to implementation whereby tautological
 * clauses can be removed during processing of the algorithm - see pg. 254 of
 * AIMA3e:<br>
 * <blockquote> Inspection of Figure 7.13 reveals that many resolution steps are
 * pointless. For example, the clause B<sub>1,1</sub> &or; &not;B<sub>1,1</sub>
 * &or; P<sub>1,2</sub> is equivalent to <i>True</i> &or; P<sub>1,2</sub> which
 * is equivalent to <i>True</i>. Deducing that <i>True</i> is true is not very
 * helpful. Therefore, any clauses in which two complementary literals appear
 * can be discarded. </blockquote>
 * @see Clause#isTautology()
 *
 * Additionally, this optimized implementation reduces the number of repeated
 * inferences by making sure that whenever the resolution rule is applied, one
 * of the source clauses must be new.
 *
 * @author Ruediger Lunde
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class OptimizedPLResolution extends PLResolution {
	/**
	 * PL-RESOLUTION(KB, &alpha;)<br>
	 * An optimized resolution algorithm for propositional logic.
	 *
	 * @param kb
	 *            the knowledge base, a sentence in propositional logic.
	 * @param alpha
	 *            the query, a sentence in propositional logic.
	 * @return true if KB |= &alpha;, false otherwise.
	 */
	@Override
	public boolean isEntailed(KnowledgeBase kb, Sentence alpha) {
		/// clauses <- the set of clauses in the CNF representation of KB & ~alpha
		Set<Clause> clauses = convertKBAndNotAlphaIntoCNF(kb, alpha);
		int clausesOldSize = 0;

		/// loop do
		do {
			/// new <- {}
			// optimization: clear within the loop!
			Set<Clause> newClauses = new LinkedHashSet<>();

			/// for each pair of clauses C_i, C_j in clauses do
			List<Clause> clausesList = new ArrayList<>(clauses);
			for (int i = 0; i < clausesList.size() - 1 && !Tasks.currIsCancelled(); i++) {
				Clause ci = clausesList.get(i);
				// optimization: At least one of the two clauses should be new!
				for (int j = Math.max(i + 1, clausesOldSize); j < clausesList.size(); j++) {
					Clause cj = clausesList.get(j);
					/// resolvents <- PL-RESOLVE(C_i, C_j)
					Set<Clause> resolvents = plResolve(ci, cj);
					/// if resolvents contains the empty clause then return true
					if (resolvents.contains(Clause.EMPTY)) {
						return true;
					}
					/// new <- new U resolvents
					newClauses.addAll(resolvents);
				}
			}

			clausesOldSize = clauses.size();
			/// clauses <- clauses U new
			clauses.addAll(newClauses);

			/// if new is subset of clauses then return false
		} while (clausesOldSize < clauses.size() && !Tasks.currIsCancelled());
		return false;
	}

	/**
	 * Default constructor, which will set the algorithm to discard tautologies
	 * by default.
	 */
	public OptimizedPLResolution() {
		this(true);
	}

	/**
	 * Constructor.
	 *
	 * @param discardTautologies
	 *            true if the algorithm is to discard tautological clauses
	 *            during processing, false otherwise.
	 */
	public OptimizedPLResolution(boolean discardTautologies) {
		super(discardTautologies);
	}
}