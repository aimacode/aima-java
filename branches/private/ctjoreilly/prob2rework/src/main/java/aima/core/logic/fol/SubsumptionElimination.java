package aima.core.logic.fol;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import aima.core.logic.fol.kb.data.Clause;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 356.<br>
 * <br>
 * The subsumption method eliminates all sentences that are subsumed by (that
 * is, more specific than) an existing sentence in the KB. For example, P(x) is
 * in the KB, then there is no sense in adding P(A) and even less sense in
 * adding P(A) V Q(B). Subsumption helps keep the KB small and thus helps keep
 * the search space small.<br>
 * <br>
 * <b>Note:</b> <a
 * href="http://logic.stanford.edu/classes/cs157/2008/lectures/lecture12.pdf"
 * >From slide 17.</a> <br>
 * <br>
 * Relational Subsumption<br>
 * <br>
 * A relational clause &Phi; subsumes &Psi; if and only if there is a
 * substitution &delta; that, when applied to &Phi;, produces a clause &Phi;'
 * that is a subset of &Psi;.
 * 
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public class SubsumptionElimination {
	/**
	 * Returns the clauses that are subsumed by (that is, more specific than) an
	 * existing clause in the specified set of clauses.
	 * 
	 * @param clauses
	 *            a set of clauses in first order logic
	 * 
	 * @return the clauses that are subsumed by (that is, more specific than) an
	 *         existing clause in the specified set of clauses.
	 */
	public static Set<Clause> findSubsumedClauses(Set<Clause> clauses) {
		Set<Clause> subsumed = new HashSet<Clause>();

		// Group the clauses by their # of literals.
		// Keep track of the min and max # of literals.
		int min = Integer.MAX_VALUE;
		int max = 0;
		Map<Integer, Set<Clause>> clausesGroupedBySize = new HashMap<Integer, Set<Clause>>();
		for (Clause c : clauses) {
			int size = c.getNumberLiterals();
			if (size < min) {
				min = size;
			}
			if (size > max) {
				max = size;
			}
			Set<Clause> cforsize = clausesGroupedBySize.get(size);
			if (null == cforsize) {
				cforsize = new HashSet<Clause>();
				clausesGroupedBySize.put(size, cforsize);
			}
			cforsize.add(c);
		}
		// Check if each smaller clause
		// subsumes any of the larger clauses.
		for (int i = min; i < max; i++) {
			Set<Clause> scs = clausesGroupedBySize.get(i);
			// Ensure there are clauses with this # of literals
			if (null != scs) {
				for (int j = i + 1; j <= max; j++) {
					Set<Clause> lcs = clausesGroupedBySize.get(j);
					// Ensure there are clauses with this # of literals
					if (null != lcs) {
						for (Clause sc : scs) {
							// Don't bother checking clauses
							// that are already subsumed.
							if (!subsumed.contains(sc)) {
								for (Clause lc : lcs) {
									if (!subsumed.contains(lc)) {
										if (sc.subsumes(lc)) {
											subsumed.add(lc);
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return subsumed;
	}
}
