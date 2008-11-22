package aima.logic.fol.kb.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.logic.fol.StandardizeApart;
import aima.logic.fol.StandardizeApartIndexical;
import aima.logic.fol.StandardizeApartIndexicalFactory;
import aima.logic.fol.SubstVisitor;
import aima.logic.fol.Unifier;
import aima.logic.fol.VariableCollector;
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

// TODO-Convert over to using Literals and not Predicates.
public class Clause {
	//
	private static StandardizeApartIndexical _saIndexical = StandardizeApartIndexicalFactory
			.newStandardizeApartIndexical('c');
	//
	private final Set<Predicate> positiveLiterals = new LinkedHashSet<Predicate>();
	private final Set<Predicate> negativeLiterals = new LinkedHashSet<Predicate>();
	private final List<Predicate> sortedPositiveLiterals = new ArrayList<Predicate>();
	private final List<Predicate> sortedNegativeLiterals = new ArrayList<Predicate>();
	private final Map<String, List<Predicate>> cmpPosLiterals = new LinkedHashMap<String, List<Predicate>>();
	private final Map<String, List<Predicate>> cmpNegLiterals = new LinkedHashMap<String, List<Predicate>>();
	private boolean immutable = false;
	private boolean saCheckRequired = true;
	private String approxIdentity = "";
	private Unifier unifier = new Unifier();
	private SubstVisitor substVisitor = new SubstVisitor();
	private VariableCollector variableCollector = new VariableCollector();
	private StandardizeApart standardizeApart = new StandardizeApart();
	private SortPredicatesByName predicateNameSorter = new SortPredicatesByName();
	private Set<Clause> factors = null;
	private Set<Clause> nonTrivialFactors = null;

	public Clause() {
		// i.e. the empty clause
	}

	public Clause(List<Predicate> positiveLiterals,
			List<Predicate> negativeLiterals) {

		this.positiveLiterals.addAll(positiveLiterals);
		this.negativeLiterals.addAll(negativeLiterals);
		recalculateIdentity();
	}

	public boolean isImmutable() {
		return immutable;
	}

	public void setImmutable() {
		immutable = true;
	}

	public boolean isStandardizedApartCheckRequired() {
		return saCheckRequired;
	}

	public void setStandardizedApartCheckNotRequired() {
		saCheckRequired = false;
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
		positiveLiterals.add(literal);
		recalculateIdentity();
	}

	public int getNumberLiterals() {
		return getNumberPositiveLiterals() + getNumberNegativeLiterals();
	}

	public int getNumberPositiveLiterals() {
		return positiveLiterals.size();
	}

	public List<Predicate> getPositiveLiterals() {
		return Collections.unmodifiableList(sortedPositiveLiterals);
	}

	public void addNegativeLiteral(Predicate literal) {
		if (isImmutable()) {
			throw new IllegalStateException(
					"Clause is immutable, cannot be updated.");
		}
		negativeLiterals.add(literal);
		recalculateIdentity();
	}

	public int getNumberNegativeLiterals() {
		return negativeLiterals.size();
	}

	public List<Predicate> getNegativeLiterals() {
		return Collections.unmodifiableList(sortedNegativeLiterals);
	}

	public Set<Clause> getFactors() {
		if (null == factors) {
			calculateFactors(null);
		}
		return Collections.unmodifiableSet(factors);
	}

	public Set<Clause> getNonTrivialFactors() {
		if (null == nonTrivialFactors) {
			calculateFactors(null);
		}
		return Collections.unmodifiableSet(nonTrivialFactors);
	}

	// Note: Applies binary resolution rule and factoring
	// Note: returns a set with an empty clause if both clauses
	// are empty, otherwise returns a set of binary resolvents.
	public Set<Clause> binaryResolvents(Clause othC) {
		Set<Clause> resolvents = new LinkedHashSet<Clause>();
		// Resolving two empty clauses
		// gives you an empty clause
		if (isEmpty() && othC.isEmpty()) {
			resolvents.add(new Clause());
			return resolvents;
		}

		// Ensure Standardized Apart
		// Before attempting binary resolution
		othC = saIfRequired(othC);

		List<Predicate> allPosLits = new ArrayList<Predicate>();
		List<Predicate> allNegLits = new ArrayList<Predicate>();
		allPosLits.addAll(this.positiveLiterals);
		allPosLits.addAll(othC.positiveLiterals);
		allNegLits.addAll(this.negativeLiterals);
		allNegLits.addAll(othC.negativeLiterals);

		List<Predicate> trPosLits = new ArrayList<Predicate>();
		List<Predicate> trNegLits = new ArrayList<Predicate>();
		List<Predicate> copyRPosLits = new ArrayList<Predicate>();
		List<Predicate> copyRNegLits = new ArrayList<Predicate>();

		for (int i = 0; i < 2; i++) {
			trPosLits.clear();
			trNegLits.clear();

			if (i == 0) {
				// See if this clauses positives
				// unify with the other clauses
				// negatives
				trPosLits.addAll(this.positiveLiterals);
				trNegLits.addAll(othC.negativeLiterals);
			} else {
				// Try the other way round now
				trPosLits.addAll(othC.positiveLiterals);
				trNegLits.addAll(this.negativeLiterals);
			}

			// Now check to see if they resolve
			for (Predicate pl : trPosLits) {
				for (Predicate nl : trNegLits) {
					Map<Variable, Term> copyRBindings = new LinkedHashMap<Variable, Term>();
					if (null != unifier.unify(pl, nl, copyRBindings)) {
						copyRPosLits.clear();
						copyRNegLits.clear();
						for (Predicate l : allPosLits) {
							if (!pl.equals(l)) {
								copyRPosLits.add((Predicate) substVisitor
										.subst(copyRBindings, l));
							}
						}
						for (Predicate l : allNegLits) {
							if (!nl.equals(l)) {
								copyRNegLits.add((Predicate) substVisitor
										.subst(copyRBindings, l));
							}
						}
						// Ensure the resolvents are standardized apart
						standardizeApart.standardizeApart(copyRPosLits,
								copyRNegLits, _saIndexical);
						Clause c = new Clause(copyRPosLits, copyRNegLits);
						if (isImmutable()) {
							c.setImmutable();
						}
						if (!isStandardizedApartCheckRequired()) {
							c.setStandardizedApartCheckNotRequired();
						}
						resolvents.add(c);
					}
				}
			}
		}

		return resolvents;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");

		List<Predicate> literals = new ArrayList<Predicate>();
		literals.addAll(negativeLiterals);
		literals.addAll(positiveLiterals);

		for (int i = 0; i < literals.size(); i++) {
			if (i > 0) {
				sb.append(",");
			}
			if (i < negativeLiterals.size()) {
				sb.append("~");
			}

			sb.append(literals.get(i).toString());
		}

		sb.append("}");

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
		if (!(othObj instanceof Clause)) {
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

		// Ensure are standardized apart before
		// checking their equality.
		othClause = saIfRequired(othClause);

		// Check if the collections of literals unify
		// Note: As the clauses may have multiple of the
		// same named literals, it is possilbe they
		// are ordered differently based on their
		// terms but that still unify, so need
		// check all of these.
		Map<Variable, Term> theta = new HashMap<Variable, Term>();
		for (int i = 0; i < 2; i++) {
			Map<String, List<Predicate>> thisCmpLits = null;
			Map<String, List<Predicate>> othCmpLits = null;
			if (i == 0) {
				thisCmpLits = this.cmpNegLiterals;
				othCmpLits = othClause.cmpNegLiterals;
			} else {
				thisCmpLits = this.cmpPosLiterals;
				othCmpLits = othClause.cmpPosLiterals;
			}

			for (String pName : thisCmpLits.keySet()) {
				List<Predicate> thisLits = thisCmpLits.get(pName);
				List<Predicate> othLits = othCmpLits.get(pName);
				boolean[] matches = new boolean[thisLits.size()];

				for (int x = 0; x < matches.length; x++) {
					Predicate tl = thisLits.get(x);
					boolean unified = false;
					for (int z = 0; z < matches.length; z++) {
						Predicate ol = othLits.get(z);
						theta.clear();
						if (null != unifier.unify(tl, ol, theta)) {
							unified = true;
							matches[z] = true;
						}
					}
					if (!unified) {
						return false;
					}
				}
				// Check matches are all true
				for (int x = 0; x < matches.length; x++) {
					if (!matches[x]) {
						return false;
					}
				}
			}
		}

		// If got this far then the two clauses are
		// unify equivalent.
		return true;
	}

	//
	// PRIVATE METHODS
	//
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

			cmpNegLiterals.clear();
			cmpPosLiterals.clear();

			StringBuilder newIdentity = new StringBuilder("NOT(");
			boolean first = true;
			for (Predicate l : sortedNegativeLiterals) {
				if (first) {
					first = false;
				} else {
					newIdentity.append(",");
				}
				newIdentity.append(l.getPredicateName());
				if (!cmpNegLiterals.containsKey(l.getPredicateName())) {
					cmpNegLiterals.put(l.getPredicateName(),
							new ArrayList<Predicate>());
				}
				cmpNegLiterals.get(l.getPredicateName()).add(l);
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
				if (!cmpPosLiterals.containsKey(l.getPredicateName())) {
					cmpPosLiterals.put(l.getPredicateName(),
							new ArrayList<Predicate>());
				}
				cmpPosLiterals.get(l.getPredicateName()).add(l);
			}

			approxIdentity = newIdentity.toString();

			// Reset, these as will need to re-calcualte
			// if requested for again, best to only
			// access lazily.
			factors = null;
			nonTrivialFactors = null;
		}
	}

	private void calculateFactors(Set<Clause> parentFactors) {
		nonTrivialFactors = new LinkedHashSet<Clause>();

		Map<Variable, Term> theta = new HashMap<Variable, Term>();
		for (int i = 0; i < 2; i++) {
			List<Predicate> lits = new ArrayList<Predicate>();
			if (i == 0) {
				// Look at the positive literals
				lits.addAll(positiveLiterals);
			} else {
				// Look at the negative literals
				lits.addAll(negativeLiterals);
			}
			for (int x = 0; x < lits.size(); x++) {
				for (int y = x + 1; y < lits.size(); y++) {
					Predicate litX = lits.get(x);
					Predicate litY = lits.get(y);

					theta.clear();
					Map<Variable, Term> substitution = unifier.unify(litX,
							litY, theta);
					if (null != substitution) {
						List<Predicate> posLits = new ArrayList<Predicate>();
						List<Predicate> negLits = new ArrayList<Predicate>();
						if (i == 0) {
							posLits.add((Predicate) substVisitor.subst(
									substitution, litX));
						} else {
							negLits.add((Predicate) substVisitor.subst(
									substitution, litX));
						}
						for (Predicate pl : positiveLiterals) {
							if (pl == litX || pl == litY) {
								continue;
							}
							posLits.add((Predicate) substVisitor.subst(
									substitution, pl));
						}
						for (Predicate nl : negativeLiterals) {
							if (nl == litX || nl == litY) {
								continue;
							}
							negLits.add((Predicate) substVisitor.subst(
									substitution, nl));
						}
						// Ensure the non trivial factor is standardized apart
						standardizeApart.standardizeApart(posLits, negLits,
								_saIndexical);
						Clause c = new Clause(posLits, negLits);
						if (isImmutable()) {
							c.setImmutable();
						}
						if (!isStandardizedApartCheckRequired()) {
							c.setStandardizedApartCheckNotRequired();
						}
						if (null == parentFactors) {
							c.calculateFactors(nonTrivialFactors);
							nonTrivialFactors.addAll(c.getFactors());
						} else {
							if (!parentFactors.contains(c)) {
								c.calculateFactors(nonTrivialFactors);
								nonTrivialFactors.addAll(c.getFactors());
							}
						}
					}
				}
			}
		}

		factors = new LinkedHashSet<Clause>();
		// Need to add self, even though a non-trivial
		// factor. See: slide 30
		// http://logic.stanford.edu/classes/cs157/2008/lectures/lecture10.pdf
		// for example of incompleteness when
		// trivial factor not included.
		factors.add(this);
		factors.addAll(nonTrivialFactors);
	}

	private Clause saIfRequired(Clause othClause) {

		if (isStandardizedApartCheckRequired()) {
			Set<Variable> mVariables = variableCollector
					.collectAllVariables(this);
			Set<Variable> oVariables = variableCollector
					.collectAllVariables(othClause);

			Set<Variable> cVariables = new HashSet<Variable>();
			cVariables.addAll(mVariables);
			cVariables.addAll(oVariables);

			if (cVariables.size() < (mVariables.size() + oVariables.size())) {
				othClause = standardizeApart.standardizeApart(othClause,
						_saIndexical);
			}
		}

		return othClause;
	}

	class SortPredicatesByName implements Comparator<Predicate> {
		public int compare(Predicate o1, Predicate o2) {
			return o1.getPredicateName().compareTo(o2.getPredicateName());
		}
	}
}