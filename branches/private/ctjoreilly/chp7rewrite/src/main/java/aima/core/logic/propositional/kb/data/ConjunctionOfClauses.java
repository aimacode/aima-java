package aima.core.logic.propositional.kb.data;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 253.<br>
 * <br>
 * A conjunction of clauses, where each clause is a disjunction of literals.
 * Here we represent a conjunction of clauses as a set of clauses, where each
 * clause is a set of literals.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class ConjunctionOfClauses {
	private Set<Clause> clauses = new LinkedHashSet<Clause>();
	//
	private String cachedStringRep = null;
	private int cachedHashCode = -1;

	public ConjunctionOfClauses(Collection<Clause> conjunctionOfClauses) {
		this.clauses.addAll(conjunctionOfClauses);
		// Make immutable
		this.clauses = Collections
				.unmodifiableSet(this.clauses);
	}

	public int getNumberOfClauses() {
		return clauses.size();
	}

	public Set<Clause> getClauses() {
		return clauses;
	}

	public ConjunctionOfClauses extend(Collection<Clause> additionalClauses) {
		Set<Clause> extendedClauses = new LinkedHashSet<Clause>();
		extendedClauses.addAll(clauses);
		extendedClauses.addAll(additionalClauses);

		ConjunctionOfClauses result = new ConjunctionOfClauses(extendedClauses);

		return result;
	}

	@Override
	public String toString() {
		if (cachedStringRep == null) {
			StringBuilder sb = new StringBuilder();
			boolean first = true;
			sb.append("{");
			for (Clause c : clauses) {
				if (first) {
					first = false;
				} else {
					sb.append(", ");
				}
				sb.append(c);
			}
			sb.append("}");
			cachedStringRep = sb.toString();
		}

		return cachedStringRep;
	}

	@Override
	public int hashCode() {
		if (cachedHashCode == -1) {
			cachedHashCode = clauses.hashCode();
		}
		return cachedHashCode;
	}

	@Override
	public boolean equals(Object othObj) {
		if (null == othObj) {
			return false;
		}
		if (this == othObj) {
			return true;
		}
		if (!(othObj instanceof ConjunctionOfClauses)) {
			return false;
		}
		ConjunctionOfClauses othConjunctionOfClauses = (ConjunctionOfClauses) othObj;

		return othConjunctionOfClauses.clauses
				.equals(this.clauses);
	}
}
