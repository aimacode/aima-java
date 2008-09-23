package aima.logic.fol.kb.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Conjunctive Normal Form (CNF) : a conjunction of clauses, where each 
 * clause is a disjunction of literals.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class CNF {

	private List<Clause> conjunctionOfClauses = new ArrayList<Clause>();

	public CNF(List<Clause> conjunctionOfClauses) {
		this.conjunctionOfClauses.addAll(conjunctionOfClauses);
	}

	public List<Clause> getConjunctionOfClauses() {
		return Collections.unmodifiableList(conjunctionOfClauses);
	}

	public boolean isHornClause() {
		return conjunctionOfClauses.size() == 1
				&& conjunctionOfClauses.get(0).isHornClause();
	}

	public boolean isDefiniteClause() {
		return conjunctionOfClauses.size() == 1
				&& conjunctionOfClauses.get(0).isDefiniteClause();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < conjunctionOfClauses.size(); i++) {
			if (i > 0) {
				sb.append(" AND ");
			}
			sb.append(conjunctionOfClauses.get(i).toString());
		}

		return sb.toString();
	}
}
