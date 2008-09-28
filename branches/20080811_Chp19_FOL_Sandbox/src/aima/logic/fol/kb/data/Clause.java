package aima.logic.fol.kb.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.logic.fol.Unifier;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

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
	// Keep track of any resolution bindings, used in constructing this clause
	private Map<Variable, Term> resolutionBindings = new LinkedHashMap<Variable, Term>();

	public Clause() {
		// i.e. the empty clause
	}

	public Clause(List<Predicate> positiveLiterals,
			List<Predicate> negativeLiterals) {
		for (Predicate literal : positiveLiterals) {
			addPositiveLiteral(literal);
		}
		for (Predicate literal : negativeLiterals) {
			addNegativeLiteral(literal);
		}
	}

	public boolean isImmutable() {
		return immutable;
	}

	public void setImmutable() {
		immutable = true;
	}
	
	public Map<Variable, Term> getResolutionBindings() {
		return Collections.unmodifiableMap(resolutionBindings);
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
		// Ensure common literals are factored out
		if (!unifiesWithAny(positiveLiterals, literal)) {
			// Ensure does not resolve with self
			if (!unifiesWithAnyThenRemove(negativeLiterals, literal)) {
				positiveLiterals.add(literal);
			}
			recalculateIdentity();
		}
	}

	public int getNumberPositiveLiterals() {
		return positiveLiterals.size();
	}

	public List<Predicate> getPositiveLiterals() {
		return Collections.unmodifiableList(positiveLiterals);
	}

	public void addNegativeLiteral(Predicate literal) {
		if (isImmutable()) {
			throw new IllegalStateException(
					"Clause is immutable, cannot be updated.");
		}
		// Ensure common literals are factored out
		if (!unifiesWithAny(negativeLiterals, literal)) {
			// Ensure does not resolve with self
			if (!unifiesWithAnyThenRemove(positiveLiterals, literal)) {
				negativeLiterals.add(literal);
			}
			recalculateIdentity();
		}
	}

	public int getNumberNegativeLiterals() {
		return negativeLiterals.size();
	}

	public List<Predicate> getNegativeLiterals() {
		return Collections.unmodifiableList(negativeLiterals);
	}

	// Note: Applies full resolution rule and factoring
	// Note: Returns null if no resolvents, except if both
	// are empty, in which case it returns a set with an
	// empty clause.
	public Clause resolvent(Clause othC) {		
		// Resolving two empty clauses
		// give you an empty clause
		if (isEmpty() && othC.isEmpty()) {
			return new Clause();
		}

		List<Predicate> trPosLits = new ArrayList<Predicate>();
		List<Predicate> trNegLits = new ArrayList<Predicate>();
		// To Resolve lists comprise the two lists
		// from each clause
		trPosLits.addAll(this.positiveLiterals);
		trPosLits.addAll(othC.positiveLiterals);
		trNegLits.addAll(this.negativeLiterals);
		trNegLits.addAll(othC.negativeLiterals);
		
		// Ensure factoring and resolving are not
		// confused with each other
		boolean resolved = false;
		// The resolved positive and negative literals
		List<Predicate> rPosLits = new ArrayList<Predicate>();
		List<Predicate> rNegLits = new ArrayList<Predicate>();
		
		for (Predicate pl : trPosLits) {
			// Ensure common positive literals are factored out
			if (!unifiesWithAny(rPosLits, pl)) {
				rPosLits.add(pl);
			}
		}
		
		for (Predicate nl : trNegLits) {
			// Ensure common negative literals are factored out
			if (!unifiesWithAny(rNegLits, nl)) {
				// Ensure does not resolve before adding
				if (!unifiesWithAnyThenRemove(rPosLits, nl)) {
					rNegLits.add(nl);
				} else {
					resolved = true;
				}
			}
		}
		
		if (!resolved) {
			return null;
		}

		// Create resolvent this way for efficiency
		// reasons, as we already know the
		// two lists are resolved and factored
		Clause rc = new Clause();
		rc.positiveLiterals.addAll(rPosLits);
		rc.negativeLiterals.addAll(rNegLits);
		rc.recalculateIdentity();
		
		return rc;
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
	
	private boolean unifiesWithAnyThenRemove(List<Predicate> existingLiterals,
			Predicate newLiteral) {
		List<Predicate> remainingAfterUnify = new ArrayList<Predicate>();
		
		boolean unifies = false;
		for (Predicate el : existingLiterals) {
			if (null != unifier.unify(el, newLiteral)) {
				unifies = true;
			} else {
				remainingAfterUnify.add(el);
			}
		}
		
		if (unifies) {
			existingLiterals.clear();
			existingLiterals.addAll(remainingAfterUnify);
		}
		
		return unifies;
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