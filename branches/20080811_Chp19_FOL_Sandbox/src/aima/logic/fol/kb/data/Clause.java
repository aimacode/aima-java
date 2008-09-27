package aima.logic.fol.kb.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import aima.logic.fol.Unifier;
import aima.logic.fol.parsing.ast.Predicate;

/**
 * A Clause: A disjunction of literals.
 * 
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class Clause {

	private final List<Predicate> positiveLiterals = new ArrayList<Predicate>();
	private final List<Predicate> negativeLiterals = new ArrayList<Predicate>();
	private final List<Predicate> sortedPositiveLiterals = new ArrayList<Predicate>();
	private final List<Predicate> sortedNegativeLiterals = new ArrayList<Predicate>();
	private boolean immutable = false;
	private String approxIdentity = "";
	// TODO, may want to get another way
	// so that the occurs check happens?
	private Unifier unifier = new Unifier();
	private SortPredicatesByName predicateNameSorter = new SortPredicatesByName();

	public Clause() {
		// i.e. the empty clause
	}

	public Clause(List<Predicate> positiveLiterals,
			List<Predicate> negativeLiterals) {
		for (Predicate pl : positiveLiterals) {
			addPositiveLiteral(pl);
		}
		for (Predicate nl : negativeLiterals) {
			addNegativeLiteral(nl);
		}
	}

	public boolean isImmutable() {
		return immutable;
	}

	public void setImmutable() {
		immutable = true;
	}

	public boolean isEmpty() {
		return positiveLiterals.size() == 0 && negativeLiterals.size() == 0;
	}

	public boolean isHornClause() {
		// A Horn clause is a disjunction of literals of which at most one is
		// positive.
		return !isEmpty() && positiveLiterals.size() <= 1;
	}

	public boolean isDefiniteClause() {
		// A Definite Clause is a disjunction of literals of which exactly 1 is
		// positive.
		return !isEmpty() && positiveLiterals.size() == 1;
	}
	
	public boolean isAtomicClause() {
		return isDefiniteClause() && negativeLiterals.size() == 0;
	}

	public boolean isImplicationDefiniteClause() {
		// An Implication Definite Clause is a disjunction of literals of
		// which exactly 1 is positive and there is 1 or more negative
		// literals.
		return isDefiniteClause() && negativeLiterals.size() >= 1;
	}

	public void addPositiveLiteral(Predicate literal) {
		if (isImmutable()) {
			throw new IllegalStateException(
					"Clause is immutable, cannot be updated.");
		}
		if (!unifiesWithAny(positiveLiterals, literal)) {
			positiveLiterals.add(literal);
			recalculateIdentity();
		}
	}

	public List<Predicate> getPositiveLiterals() {
		return Collections.unmodifiableList(positiveLiterals);
	}

	public void addNegativeLiteral(Predicate literal) {
		if (isImmutable()) {
			throw new IllegalStateException(
					"Clause is immutable, cannot be updated.");
		}
		if (!unifiesWithAny(negativeLiterals, literal)) {
			negativeLiterals.add(literal);
			recalculateIdentity();
		}
	}

	public List<Predicate> getNegativeLiterals() {
		return Collections.unmodifiableList(negativeLiterals);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");

		List<Predicate> literals = new ArrayList<Predicate>();
		literals.addAll(negativeLiterals);
		literals.addAll(positiveLiterals);

		for (int i = 0; i < literals.size(); i++) {
			if (i > 0) {
				sb.append(" OR ");
			}
			if (i < negativeLiterals.size()) {
				sb.append("NOT(");
			}

			sb.append(literals.get(i).toString());

			if (i < negativeLiterals.size()) {
				sb.append(")");
			}
		}

		sb.append("]");

		return sb.toString();
	}

	public int hashCode() {
		return approxIdentity.hashCode();
	}

	public boolean equals(Object othObj) {
		if (null == othObj) {
			return false;
		}
		if (this == othObj) {
			return true;
		}
		if (!Clause.class.isInstance(othObj)) {
			return false;
		}
		Clause othClause = (Clause) othObj;
		if (!approxIdentity.equals(othClause.approxIdentity)) {
			return false;
		}

		// Note: If the clauses approx identities
		// match, then they contain the same #
		// of positive and negative literals
		// so no need to check.

		// Check if the collections of literals unify
		for (int i = 0; i < sortedNegativeLiterals.size(); i++) {
			Predicate mnl = sortedNegativeLiterals.get(i);
			Predicate onl = othClause.sortedNegativeLiterals.get(i);

			if (null == unifier.unify(mnl, onl)) {
				return false;
			}
		}
		for (int i = 0; i < sortedPositiveLiterals.size(); i++) {
			Predicate mpl = sortedPositiveLiterals.get(i);
			Predicate opl = othClause.sortedPositiveLiterals.get(i);

			if (null == unifier.unify(mpl, opl)) {
				return false;
			}
		}

		// If got this far then the two clauses are
		// unify equivalent.
		return true;
	}

	//
	// PRIVATE METHODS
	//
	private boolean unifiesWithAny(List<Predicate> existingLiterals,
			Predicate newLiteral) {
		for (Predicate el : existingLiterals) {
			if (null != unifier.unify(el, newLiteral)) {
				return true;
			}
		}
		return false;
	}

	private void recalculateIdentity() {
		synchronized (approxIdentity) {
			// Order the literals, so that clauses with the same literals
			// but different ordering will get the same approxIdentity.
			sortedNegativeLiterals.clear();
			sortedPositiveLiterals.clear();
			sortedNegativeLiterals.addAll(negativeLiterals);
			sortedPositiveLiterals.addAll(positiveLiterals);

			Collections.sort(sortedNegativeLiterals, predicateNameSorter);
			Collections.sort(sortedPositiveLiterals, predicateNameSorter);

			StringBuilder newIdentity = new StringBuilder("NOT(");
			boolean first = true;
			for (Predicate l : sortedNegativeLiterals) {
				if (first) {
					first = false;
				} else {
					newIdentity.append(",");
				}
				newIdentity.append(l.getPredicateName());
			}
			newIdentity.append(")");
			first = true;
			for (Predicate l : sortedPositiveLiterals) {
				if (first) {
					first = false;
				} else {
					newIdentity.append(",");
				}
				newIdentity.append(l.getPredicateName());
			}

			approxIdentity = newIdentity.toString();
		}
	}

	class SortPredicatesByName implements Comparator<Predicate> {
		public int compare(Predicate o1, Predicate o2) {
			return o1.getPredicateName().compareTo(o2.getPredicateName());
		}
	}
}