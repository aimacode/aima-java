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
import aima.logic.fol.parsing.ast.AtomicSentence;
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
	//
	private static StandardizeApartIndexical _saIndexical = StandardizeApartIndexicalFactory
			.newStandardizeApartIndexical('c');
	//
	private final Set<Literal> literals = new LinkedHashSet<Literal>();
	private final Set<Literal> positiveLiterals = new LinkedHashSet<Literal>();
	private final Set<Literal> negativeLiterals = new LinkedHashSet<Literal>();
	private final List<Literal> sortedPositiveLiterals = new ArrayList<Literal>();
	private final List<Literal> sortedNegativeLiterals = new ArrayList<Literal>();
	private final Map<String, List<Literal>> cmpPosLiterals = new LinkedHashMap<String, List<Literal>>();
	private final Map<String, List<Literal>> cmpNegLiterals = new LinkedHashMap<String, List<Literal>>();
	private boolean immutable = false;
	private boolean saCheckRequired = true;
	private String approxIdentity = "";
	private Unifier unifier = new Unifier();
	private SubstVisitor substVisitor = new SubstVisitor();
	private VariableCollector variableCollector = new VariableCollector();
	private StandardizeApart standardizeApart = new StandardizeApart();
	private SortLiteralsBySymbolicName literalNameSorter = new SortLiteralsBySymbolicName();
	private Set<Clause> factors = null;
	private Set<Clause> nonTrivialFactors = null;

	public Clause() {
		// i.e. the empty clause
	}

	public Clause(List<Literal> lits) {
		this.literals.addAll(lits);
		for (Literal l : literals) {
			if (l.isPositiveLiteral()) {
				this.positiveLiterals.add(l);
			} else {
				this.negativeLiterals.add(l);
			}
		}
		recalculateIdentity();
	}

	public Clause(List<Literal> lits1, List<Literal> lits2) {
		literals.addAll(lits1);
		literals.addAll(lits2);
		for (Literal l : literals) {
			if (l.isPositiveLiteral()) {
				this.positiveLiterals.add(l);
			} else {
				this.negativeLiterals.add(l);
			}
		}
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
		return literals.size() == 0;
	}

	public boolean isUnitClause() {
		return literals.size() == 1;
	}

	public boolean isDefiniteClause() {
		// A Definite Clause is a disjunction of literals of which exactly 1 is
		// positive.
		return !isEmpty() && positiveLiterals.size() == 1;
	}

	public boolean isImplicationDefiniteClause() {
		// An Implication Definite Clause is a disjunction of literals of
		// which exactly 1 is positive and there is 1 or more negative
		// literals.
		return isDefiniteClause() && negativeLiterals.size() >= 1;
	}

	public boolean isHornClause() {
		// A Horn clause is a disjunction of literals of which at most one is
		// positive.
		return !isEmpty() && positiveLiterals.size() <= 1;
	}

	public void addLiteral(Literal literal) {
		if (isImmutable()) {
			throw new IllegalStateException(
					"Clause is immutable, cannot be updated.");
		}
		literals.add(literal);
		if (literal.isPositiveLiteral()) {
			positiveLiterals.add(literal);
		} else {
			negativeLiterals.add(literal);
		}
		recalculateIdentity();
	}
	
	public void addPositiveLiteral(AtomicSentence atom) {
		addLiteral(new Literal(atom));
	}

	public void addNegativeLiteral(AtomicSentence atom) {
		addLiteral(new Literal(atom, true));
	}

	public int getNumberLiterals() {
		return literals.size();
	}

	public int getNumberPositiveLiterals() {
		return positiveLiterals.size();
	}

	public int getNumberNegativeLiterals() {
		return negativeLiterals.size();
	}

	public Set<Literal> getLiterals() {
		return Collections.unmodifiableSet(literals);
	}

	public List<Literal> getPositiveLiterals() {
		return Collections.unmodifiableList(sortedPositiveLiterals);
	}

	public List<Literal> getNegativeLiterals() {
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

		List<Literal> allPosLits = new ArrayList<Literal>();
		List<Literal> allNegLits = new ArrayList<Literal>();
		allPosLits.addAll(this.positiveLiterals);
		allPosLits.addAll(othC.positiveLiterals);
		allNegLits.addAll(this.negativeLiterals);
		allNegLits.addAll(othC.negativeLiterals);

		List<Literal> trPosLits = new ArrayList<Literal>();
		List<Literal> trNegLits = new ArrayList<Literal>();
		List<Literal> copyRPosLits = new ArrayList<Literal>();
		List<Literal> copyRNegLits = new ArrayList<Literal>();
		
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
			for (Literal pl : trPosLits) {
				for (Literal nl : trNegLits) {
					Map<Variable, Term> copyRBindings = new LinkedHashMap<Variable, Term>();
					if (null != unifier.unify(pl.getAtomicSentence(), nl
							.getAtomicSentence(), copyRBindings)) {
						copyRPosLits.clear();
						copyRNegLits.clear();
						for (Literal l : allPosLits) {
							if (!pl.equals(l)) {
								copyRPosLits.add(substVisitor.subst(
										copyRBindings, l));
							}
						}
						for (Literal l : allNegLits) {
							if (!nl.equals(l)) {
								copyRNegLits.add(substVisitor.subst(
										copyRBindings, l));
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

		boolean first = true;
		for (Literal l : literals) {
			if (first) {
				first = false;
			} else {
				sb.append(",");
			}
			sb.append(l.toString());
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
			Map<String, List<Literal>> thisCmpLits = null;
			Map<String, List<Literal>> othCmpLits = null;
			if (i == 0) {
				thisCmpLits = this.cmpNegLiterals;
				othCmpLits = othClause.cmpNegLiterals;
			} else {
				thisCmpLits = this.cmpPosLiterals;
				othCmpLits = othClause.cmpPosLiterals;
			}

			for (String pName : thisCmpLits.keySet()) {
				List<Literal> thisLits = thisCmpLits.get(pName);
				List<Literal> othLits = othCmpLits.get(pName);
				boolean[] matches = new boolean[thisLits.size()];

				for (int x = 0; x < matches.length; x++) {
					Literal tl = thisLits.get(x);
					boolean unified = false;
					for (int z = 0; z < matches.length; z++) {
						Literal ol = othLits.get(z);
						theta.clear();
						if (null != unifier.unify(tl.getAtomicSentence(), ol
								.getAtomicSentence(), theta)) {
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

			Collections.sort(sortedNegativeLiterals, literalNameSorter);
			Collections.sort(sortedPositiveLiterals, literalNameSorter);

			cmpNegLiterals.clear();
			cmpPosLiterals.clear();

			StringBuilder newIdentity = new StringBuilder("NOT(");
			boolean first = true;
			for (Literal l : sortedNegativeLiterals) {
				if (first) {
					first = false;
				} else {
					newIdentity.append(",");
				}
				newIdentity.append(l.getAtomicSentence().getSymbolicName());
				if (!cmpNegLiterals.containsKey(l.getAtomicSentence()
						.getSymbolicName())) {
					cmpNegLiterals.put(l.getAtomicSentence().getSymbolicName(),
							new ArrayList<Literal>());
				}
				cmpNegLiterals.get(l.getAtomicSentence().getSymbolicName())
						.add(l);
			}
			newIdentity.append(")");
			first = true;
			for (Literal l : sortedPositiveLiterals) {
				if (first) {
					first = false;
				} else {
					newIdentity.append(",");
				}
				newIdentity.append(l.getAtomicSentence().getSymbolicName());
				if (!cmpPosLiterals.containsKey(l.getAtomicSentence()
						.getSymbolicName())) {
					cmpPosLiterals.put(l.getAtomicSentence().getSymbolicName(),
							new ArrayList<Literal>());
				}
				cmpPosLiterals.get(l.getAtomicSentence().getSymbolicName())
						.add(l);
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
			List<Literal> lits = new ArrayList<Literal>();
			if (i == 0) {
				// Look at the positive literals
				lits.addAll(positiveLiterals);
			} else {
				// Look at the negative literals
				lits.addAll(negativeLiterals);
			}
			for (int x = 0; x < lits.size(); x++) {
				for (int y = x + 1; y < lits.size(); y++) {
					Literal litX = lits.get(x);
					Literal litY = lits.get(y);

					theta.clear();
					Map<Variable, Term> substitution = unifier.unify(litX
							.getAtomicSentence(), litY.getAtomicSentence(),
							theta);
					if (null != substitution) {
						List<Literal> posLits = new ArrayList<Literal>();
						List<Literal> negLits = new ArrayList<Literal>();
						if (i == 0) {
							posLits.add(substVisitor.subst(
									substitution, litX));
						} else {
							negLits.add(substVisitor.subst(
									substitution, litX));
						}
						for (Literal pl : positiveLiterals) {
							if (pl == litX || pl == litY) {
								continue;
							}
							posLits.add(substVisitor.subst(
									substitution, pl));
						}
						for (Literal nl : negativeLiterals) {
							if (nl == litX || nl == litY) {
								continue;
							}
							negLits.add(substVisitor.subst(
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

	class SortLiteralsBySymbolicName implements Comparator<Literal> {
		public int compare(Literal o1, Literal o2) {
			if (o1.isPositiveLiteral() != o2.isPositiveLiteral()) {
				if (o1.isPositiveLiteral()) {
					return 1;
				}
				return -1;
			}
			return o1.getAtomicSentence().getSymbolicName().compareTo(
					o2.getAtomicSentence().getSymbolicName());
		}
	}
}