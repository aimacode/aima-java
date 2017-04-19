package aima.core.logic.fol.kb.data;

import java.util.*;

/**
 * Conjunctive Normal Form (CNF) : a conjunction of clauses, where each clause
 * is a disjunction of literals.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class CNF {

	private List<Clause> conjunctionOfClauses = new ArrayList<Clause>();

	public CNF(List<Clause> conjunctionOfClauses) {
		this.conjunctionOfClauses.addAll(conjunctionOfClauses);
	}

	public int getNumberOfClauses() {
		return conjunctionOfClauses.size();
	}

	public List<Clause> getConjunctionOfClauses() {
		return Collections.unmodifiableList(conjunctionOfClauses);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < conjunctionOfClauses.size(); i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(conjunctionOfClauses.get(i).toString());
		}

		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof CNF)) {
			return false;
		}

		CNF othCnf = (CNF)obj;

		//Convert the lists to sets as we don't care about order
		Set<Clause> thisSet = new HashSet<>(this.conjunctionOfClauses);
		Set<Clause> othSet = new HashSet<>(othCnf.conjunctionOfClauses);
		return thisSet.equals(othSet);
	}
}
