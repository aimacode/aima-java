package aima.logic.fol.kb.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.logic.fol.StandardizeApart;
import aima.logic.fol.StandardizeApartIndexical;
import aima.logic.fol.SubstVisitor;
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
	// TODO: A Better mechanism than this to ensure non colliding indexicals.
	private static StandardizeApartIndexical clauseIndexical = new StandardizeApartIndexical(
			"c");
	//
	private final List<Predicate> positiveLiterals = new ArrayList<Predicate>();
	private final List<Predicate> negativeLiterals = new ArrayList<Predicate>();
	private final List<Predicate> sortedPositiveLiterals = new ArrayList<Predicate>();
	private final List<Predicate> sortedNegativeLiterals = new ArrayList<Predicate>();
	private boolean immutable = false;
	private String approxIdentity = "";
	private Unifier unifier = new Unifier();
	private SubstVisitor substVisitor = new SubstVisitor();
	private StandardizeApart standardizeApart = new StandardizeApart();
	private SortPredicatesByName predicateNameSorter = new SortPredicatesByName();

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
		if (!factorsWithAny(positiveLiterals, literal, negativeLiterals)) {
			positiveLiterals.add(literal);			
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
		if (!factorsWithAny(negativeLiterals, literal, positiveLiterals)) {
			negativeLiterals.add(literal);		
			recalculateIdentity();
		}
	}

	public int getNumberNegativeLiterals() {
		return negativeLiterals.size();
	}

	public List<Predicate> getNegativeLiterals() {
		return Collections.unmodifiableList(negativeLiterals);
	}

	// Note: Applies binary resolution rule and factoring
	// Note: returns a set with an empty clause if both clauses
	// are empty, otherwise returns a set of binary resolvents.
	public Set<Clause> binaryResolvents(Clause othC) {
		Set<Clause> resolvents = new LinkedHashSet<Clause>();
		// Resolving two empty clauses
		// give you an empty clause
		if (isEmpty() && othC.isEmpty()) {
			resolvents.add(new Clause());
			return resolvents;
		}

		List<Predicate> trPosLits = new ArrayList<Predicate>();
		List<Predicate> trNegLits = new ArrayList<Predicate>();
		// To Resolve lists comprise the two lists
		// from each clause
		trPosLits.addAll(this.positiveLiterals);
		trPosLits.addAll(othC.positiveLiterals);
		trNegLits.addAll(this.negativeLiterals);
		trNegLits.addAll(othC.negativeLiterals);

		// Now check to see if they resolve
		for (Predicate pl : trPosLits) {
			for (Predicate nl : trNegLits) {
				Map<Variable, Term> copyRBindings = new LinkedHashMap<Variable, Term>();
				if (null != unifier.unify(pl, nl, copyRBindings)) {
					List<Predicate> copyRPosLits = new ArrayList<Predicate>();
					List<Predicate> copyRNegLits = new ArrayList<Predicate>();
					for (Predicate l : trPosLits) {
						if (!pl.equals(l)) {
							copyRPosLits.add((Predicate) substVisitor.subst(
									copyRBindings, l));
						}
					}
					for (Predicate l : trNegLits) {
						if (!nl.equals(l)) {
							copyRNegLits.add((Predicate) substVisitor.subst(
									copyRBindings, l));
						}
					}

					Clause rc = new Clause(copyRPosLits, copyRNegLits);
					// Ensure the resolvents are standardized apart
					resolvents.add(standardizeApart.standardizeApart(rc,
							clauseIndexical));
				}
			}
		}

		return resolvents;
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
		// of identically named positive and negative
		// literals.

		// Check if the collections of literals unify
		// Note: As the clauses may have multiple of the
		// same named literals, it is possilbe they
		// are ordered differently based on their
		// terms but that still unify, so need
		// check all of these.
		List<Predicate> possibleMatches = new ArrayList<Predicate>(
				othClause.sortedNegativeLiterals);
		for (int i = 0; i < sortedNegativeLiterals.size(); i++) {
			Predicate mnl = sortedNegativeLiterals.get(i);
			
			boolean unified = false;
			for (int j = 0; j < possibleMatches.size(); j++) {
				Predicate onl = possibleMatches.get(j);
				if (null != unifier.unify(mnl, onl)) {
					unified = true;
					possibleMatches.remove(j);
					break;
				}
			}

			if (!unified) {
				return false;
			}
		}
		
		possibleMatches = new ArrayList<Predicate>(
				othClause.sortedPositiveLiterals);
		for (int i = 0; i < sortedPositiveLiterals.size(); i++) {
			Predicate mpl = sortedPositiveLiterals.get(i);
			
			boolean unified = false;
			for (int j = 0; j < possibleMatches.size(); j++) {
				Predicate opl = possibleMatches.get(j);
				if (null != unifier.unify(mpl, opl)) {
					unified = true;
					possibleMatches.remove(j);
					break;
				}
			}

			if (!unified) {
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
	private boolean factorsWithAny(List<Predicate> toAddToLiterals,
			Predicate newLiteral, List<Predicate> oppositeLiterals) {
		for (Predicate el : toAddToLiterals) {
			Map<Variable, Term> bindings = new LinkedHashMap<Variable, Term>();
			if (null != unifier.unify(el, newLiteral, bindings)) {
				applySubstitution(bindings, toAddToLiterals, oppositeLiterals);
				return true;
			}
		}
		return false;
	}

	private void applySubstitution(Map<Variable, Term> subst,
			List<Predicate> list1Literals, List<Predicate> list2Literals) {

		List<Predicate> substList1Literals = new ArrayList<Predicate>();
		List<Predicate> substList2Literals = new ArrayList<Predicate>();

		// Ensure the subst is applied to all
		// of the clauses literals
		for (Predicate l : list1Literals) {
			substList1Literals.add((Predicate) substVisitor.subst(subst, l));
		}
		for (Predicate l : list2Literals) {
			substList2Literals.add((Predicate) substVisitor.subst(subst, l));
		}

		list1Literals.clear();
		list1Literals.addAll(substList1Literals);
		list2Literals.clear();
		list2Literals.addAll(substList2Literals);
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