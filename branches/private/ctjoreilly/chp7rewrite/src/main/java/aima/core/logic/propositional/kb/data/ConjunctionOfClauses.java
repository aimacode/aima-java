package aima.core.logic.propositional.kb.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aima.core.logic.propositional.parsing.ast.Connective;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 253.<br>
 * <br>
 * A conjunction of clauses, where each clause is a disjunction of literals.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class ConjunctionOfClauses {
	private List<Clause> conjunctionOfClauses = new ArrayList<Clause>();
	//
	private String cachedStringRep = null;
	private int cachedHashCode = -1;

	public ConjunctionOfClauses(List<Clause> conjunctionOfClauses) {
		this.conjunctionOfClauses.addAll(conjunctionOfClauses);
		// Make immutable
		conjunctionOfClauses = Collections
				.unmodifiableList(conjunctionOfClauses);
	}

	public int getNumberOfClauses() {
		return conjunctionOfClauses.size();
	}

	public List<Clause> getConjunctionOfClauses() {
		return conjunctionOfClauses;
	}
	
	public ConjunctionOfClauses extend(List<Clause> additionalClauses) {
		List<Clause> clauses = new ArrayList<Clause>();
		clauses.addAll(conjunctionOfClauses);
		clauses.addAll(additionalClauses);
		
		ConjunctionOfClauses result = new ConjunctionOfClauses(clauses);
		
		return result;
	}

	@Override
	public String toString() {
		if (cachedStringRep == null) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < conjunctionOfClauses.size(); i++) {
				if (i > 0) {
					sb.append(" ");
					sb.append(Connective.AND);
					sb.append(" ");
				}
				sb.append(conjunctionOfClauses.get(i).toString());
			}

			cachedStringRep = sb.toString();
		}

		return cachedStringRep;
	}

	@Override
	public int hashCode() {
		if (cachedHashCode == -1) {
			cachedHashCode = conjunctionOfClauses.hashCode();
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

		return othConjunctionOfClauses.conjunctionOfClauses
				.equals(this.conjunctionOfClauses);
	}
}
