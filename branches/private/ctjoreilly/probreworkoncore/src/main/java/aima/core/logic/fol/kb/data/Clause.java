package aima.core.logic.fol.kb.data;

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

import aima.core.logic.fol.StandardizeApart;
import aima.core.logic.fol.StandardizeApartIndexical;
import aima.core.logic.fol.StandardizeApartIndexicalFactory;
import aima.core.logic.fol.SubstVisitor;
import aima.core.logic.fol.Unifier;
import aima.core.logic.fol.VariableCollector;
import aima.core.logic.fol.inference.proof.ProofStep;
import aima.core.logic.fol.inference.proof.ProofStepClauseBinaryResolvent;
import aima.core.logic.fol.inference.proof.ProofStepClauseFactor;
import aima.core.logic.fol.inference.proof.ProofStepPremise;
import aima.core.logic.fol.parsing.FOLVisitor;
import aima.core.logic.fol.parsing.ast.AtomicSentence;
import aima.core.logic.fol.parsing.ast.ConnectedSentence;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Function;
import aima.core.logic.fol.parsing.ast.NotSentence;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.QuantifiedSentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.TermEquality;
import aima.core.logic.fol.parsing.ast.Variable;
import aima.core.util.math.MixedRadixNumber;

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
	private static Unifier _unifier = new Unifier();
	private static SubstVisitor _substVisitor = new SubstVisitor();
	private static VariableCollector _variableCollector = new VariableCollector();
	private static StandardizeApart _standardizeApart = new StandardizeApart();
	private static LiteralsSorter _literalSorter = new LiteralsSorter();
	//
	private final Set<Literal> literals = new LinkedHashSet<Literal>();
	private final List<Literal> positiveLiterals = new ArrayList<Literal>();
	private final List<Literal> negativeLiterals = new ArrayList<Literal>();
	private boolean immutable = false;
	private boolean saCheckRequired = true;
	private String equalityIdentity = "";
	private Set<Clause> factors = null;
	private Set<Clause> nonTrivialFactors = null;
	private String stringRep = null;
	private ProofStep proofStep = null;

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

	public ProofStep getProofStep() {
		if (null == proofStep) {
			// Assume was a premise
			proofStep = new ProofStepPremise(this);
		}
		return proofStep;
	}

	public void setProofStep(ProofStep proofStep) {
		this.proofStep = proofStep;
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

	public boolean isTautology() {

		for (Literal pl : positiveLiterals) {
			// Literals in a clause must be exact complements
			// for tautology elimination to apply. Do not
			// remove non-identical literals just because
			// they are complements under unification, see pg16:
			// http://logic.stanford.edu/classes/cs157/2008/notes/chap09.pdf
			for (Literal nl : negativeLiterals) {
				if (pl.getAtomicSentence().equals(nl.getAtomicSentence())) {
					return true;
				}
			}
		}

		return false;
	}

	public void addLiteral(Literal literal) {
		if (isImmutable()) {
			throw new IllegalStateException(
					"Clause is immutable, cannot be updated.");
		}
		int origSize = literals.size();
		literals.add(literal);
		if (literals.size() > origSize) {
			if (literal.isPositiveLiteral()) {
				positiveLiterals.add(literal);
			} else {
				negativeLiterals.add(literal);
			}
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
		return Collections.unmodifiableList(positiveLiterals);
	}

	public List<Literal> getNegativeLiterals() {
		return Collections.unmodifiableList(negativeLiterals);
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

	public boolean subsumes(Clause othC) {
		boolean subsumes = false;

		// Equality is not subsumption
		if (!(this == othC)) {
			// Ensure this has less literals total and that
			// it is a subset of the other clauses positive and negative counts
			if (this.getNumberLiterals() < othC.getNumberLiterals()
					&& this.getNumberPositiveLiterals() <= othC
							.getNumberPositiveLiterals()
					&& this.getNumberNegativeLiterals() <= othC
							.getNumberNegativeLiterals()) {

				Map<String, List<Literal>> thisToTry = collectLikeLiterals(this.literals);
				Map<String, List<Literal>> othCToTry = collectLikeLiterals(othC.literals);
				// Ensure all like literals from this clause are a subset
				// of the other clause.
				if (othCToTry.keySet().containsAll(thisToTry.keySet())) {
					boolean isAPossSubset = true;
					// Ensure that each set of same named literals
					// from this clause is a subset of the other
					// clauses same named literals.
					for (String pk : thisToTry.keySet()) {
						if (thisToTry.get(pk).size() > othCToTry.get(pk).size()) {
							isAPossSubset = false;
							break;
						}
					}
					if (isAPossSubset) {
						// At this point I know this this Clause's
						// literal/arity names are a subset of the
						// other clauses literal/arity names
						subsumes = checkSubsumes(othC, thisToTry, othCToTry);
					}
				}
			}
		}

		return subsumes;
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
			Map<Variable, Term> copyRBindings = new LinkedHashMap<Variable, Term>();
			for (Literal pl : trPosLits) {
				for (Literal nl : trNegLits) {
					copyRBindings.clear();
					if (null != _unifier.unify(pl.getAtomicSentence(), nl
							.getAtomicSentence(), copyRBindings)) {
						copyRPosLits.clear();
						copyRNegLits.clear();
						boolean found = false;
						for (Literal l : allPosLits) {
							if (!found && pl.equals(l)) {
								found = true;
								continue;
							}
							copyRPosLits.add(_substVisitor.subst(copyRBindings,
									l));
						}
						found = false;
						for (Literal l : allNegLits) {
							if (!found && nl.equals(l)) {
								found = true;
								continue;
							}
							copyRNegLits.add(_substVisitor.subst(copyRBindings,
									l));
						}
						// Ensure the resolvents are standardized apart
						Map<Variable, Term> renameSubstitituon = _standardizeApart
								.standardizeApart(copyRPosLits, copyRNegLits,
										_saIndexical);
						Clause c = new Clause(copyRPosLits, copyRNegLits);
						c.setProofStep(new ProofStepClauseBinaryResolvent(c,
								this, othC, copyRBindings, renameSubstitituon));
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

	@Override
	public String toString() {
		if (null == stringRep) {
			List<Literal> sortedLiterals = new ArrayList<Literal>(literals);
			Collections.sort(sortedLiterals, _literalSorter);

			stringRep = sortedLiterals.toString();
		}
		return stringRep;
	}

	@Override
	public int hashCode() {
		return equalityIdentity.hashCode();
	}

	@Override
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

		return equalityIdentity.equals(othClause.equalityIdentity);
	}

	public String getEqualityIdentity() {
		return equalityIdentity;
	}

	//
	// PRIVATE METHODS
	//
	private void recalculateIdentity() {
		synchronized (equalityIdentity) {

			// Sort the literals first based on negation, atomic sentence,
			// constant, function and variable.
			List<Literal> sortedLiterals = new ArrayList<Literal>(literals);
			Collections.sort(sortedLiterals, _literalSorter);

			// All variables are considered the same as regards
			// sorting. Therefore, to determine if two clauses
			// are equivalent you need to determine
			// the # of unique variables they contain and
			// there positions across the clauses
			ClauseEqualityIdentityConstructor ceic = new ClauseEqualityIdentityConstructor(
					sortedLiterals, _literalSorter);

			equalityIdentity = ceic.getIdentity();

			// Reset, these as will need to re-calcualte
			// if requested for again, best to only
			// access lazily.
			factors = null;
			nonTrivialFactors = null;
			// Reset the objects string representation
			// until it is requested for.
			stringRep = null;
		}
	}

	private void calculateFactors(Set<Clause> parentFactors) {
		nonTrivialFactors = new LinkedHashSet<Clause>();

		Map<Variable, Term> theta = new HashMap<Variable, Term>();
		List<Literal> lits = new ArrayList<Literal>();
		for (int i = 0; i < 2; i++) {
			lits.clear();
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
					Map<Variable, Term> substitution = _unifier.unify(litX
							.getAtomicSentence(), litY.getAtomicSentence(),
							theta);
					if (null != substitution) {
						List<Literal> posLits = new ArrayList<Literal>();
						List<Literal> negLits = new ArrayList<Literal>();
						if (i == 0) {
							posLits
									.add(_substVisitor
											.subst(substitution, litX));
						} else {
							negLits
									.add(_substVisitor
											.subst(substitution, litX));
						}
						for (Literal pl : positiveLiterals) {
							if (pl == litX || pl == litY) {
								continue;
							}
							posLits.add(_substVisitor.subst(substitution, pl));
						}
						for (Literal nl : negativeLiterals) {
							if (nl == litX || nl == litY) {
								continue;
							}
							negLits.add(_substVisitor.subst(substitution, nl));
						}
						// Ensure the non trivial factor is standardized apart
						_standardizeApart.standardizeApart(posLits, negLits,
								_saIndexical);
						Clause c = new Clause(posLits, negLits);
						c.setProofStep(new ProofStepClauseFactor(c, this));
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

		// If performing resolution with self
		// then need to standardize apart in
		// order to work correctly.
		if (isStandardizedApartCheckRequired() || this == othClause) {
			Set<Variable> mVariables = _variableCollector
					.collectAllVariables(this);
			Set<Variable> oVariables = _variableCollector
					.collectAllVariables(othClause);

			Set<Variable> cVariables = new HashSet<Variable>();
			cVariables.addAll(mVariables);
			cVariables.addAll(oVariables);

			if (cVariables.size() < (mVariables.size() + oVariables.size())) {
				othClause = _standardizeApart.standardizeApart(othClause,
						_saIndexical);
			}
		}

		return othClause;
	}

	private Map<String, List<Literal>> collectLikeLiterals(Set<Literal> literals) {
		Map<String, List<Literal>> likeLiterals = new HashMap<String, List<Literal>>();
		for (Literal l : literals) {
			// Want to ensure P(a, b) is considered different than P(a, b, c)
			// i.e. consider an atom's arity P/#.
			String literalName = (l.isNegativeLiteral() ? "~" : "")
					+ l.getAtomicSentence().getSymbolicName() + "/"
					+ l.getAtomicSentence().getArgs().size();
			List<Literal> like = likeLiterals.get(literalName);
			if (null == like) {
				like = new ArrayList<Literal>();
				likeLiterals.put(literalName, like);
			}
			like.add(l);
		}
		return likeLiterals;
	}

	private boolean checkSubsumes(Clause othC,
			Map<String, List<Literal>> thisToTry,
			Map<String, List<Literal>> othCToTry) {
		boolean subsumes = false;

		List<Term> thisTerms = new ArrayList<Term>();
		List<Term> othCTerms = new ArrayList<Term>();

		// Want to track possible number of permuations
		List<Integer> radixs = new ArrayList<Integer>();
		for (String literalName : thisToTry.keySet()) {
			int sizeT = thisToTry.get(literalName).size();
			int sizeO = othCToTry.get(literalName).size();

			if (sizeO > 1) {
				// The following is being used to
				// track the number of permutations
				// that can be mapped from the
				// other clauses like literals to this
				// clauses like literals.
				// i.e. n!/(n-r)!
				// where n=sizeO and r =sizeT
				for (int i = 0; i < sizeT; i++) {
					int r = sizeO - i;
					if (r > 1) {
						radixs.add(r);
					}
				}
			}
			// Track the terms for this clause
			for (Literal tl : thisToTry.get(literalName)) {
				thisTerms.addAll(tl.getAtomicSentence().getArgs());
			}
		}

		MixedRadixNumber permutation = null;
		long numPermutations = 1L;
		if (radixs.size() > 0) {
			permutation = new MixedRadixNumber(0, radixs);
			numPermutations = permutation.getMaxAllowedValue() + 1;
		}
		// Want to ensure none of the othCVariables are
		// part of the key set of a unification as
		// this indicates it is not a legal subsumption.
		Set<Variable> othCVariables = _variableCollector
				.collectAllVariables(othC);
		Map<Variable, Term> theta = new LinkedHashMap<Variable, Term>();
		List<Literal> literalPermuations = new ArrayList<Literal>();
		for (long l = 0L; l < numPermutations; l++) {
			// Track the other clause's terms for this
			// permutation.
			othCTerms.clear();
			int radixIdx = 0;
			for (String literalName : thisToTry.keySet()) {
				int sizeT = thisToTry.get(literalName).size();
				literalPermuations.clear();
				literalPermuations.addAll(othCToTry.get(literalName));
				int sizeO = literalPermuations.size();

				if (sizeO > 1) {
					for (int i = 0; i < sizeT; i++) {
						int r = sizeO - i;
						if (r > 1) {
							// If not a 1 to 1 mapping then you need
							// to use the correct permuation
							int numPos = permutation
									.getCurrentNumeralValue(radixIdx);
							othCTerms.addAll(literalPermuations.remove(numPos)
									.getAtomicSentence().getArgs());
							radixIdx++;
						} else {
							// is the last mapping, therefore
							// won't be on the radix
							othCTerms.addAll(literalPermuations.get(0)
									.getAtomicSentence().getArgs());
						}
					}
				} else {
					// a 1 to 1 mapping
					othCTerms.addAll(literalPermuations.get(0)
							.getAtomicSentence().getArgs());
				}
			}

			// Note: on unifier
			// unifier.unify(P(w, x), P(y, z)))={w=y, x=z}
			// unifier.unify(P(y, z), P(w, x)))={y=w, z=x}
			// Therefore want this clause to be the first
			// so can do the othCVariables check for an invalid
			// subsumes.
			theta.clear();
			if (null != _unifier.unify(thisTerms, othCTerms, theta)) {
				boolean containsAny = false;
				for (Variable v : theta.keySet()) {
					if (othCVariables.contains(v)) {
						containsAny = true;
						break;
					}
				}
				if (!containsAny) {
					subsumes = true;
					break;
				}
			}

			// If there is more than 1 mapping
			// keep track of where I am in the
			// possible number of mapping permutations.
			if (null != permutation) {
				permutation.increment();
			}
		}

		return subsumes;
	}
}

class LiteralsSorter implements Comparator<Literal> {
	public int compare(Literal o1, Literal o2) {
		int rVal = 0;
		// If literals are not negated the same
		// then positive literals are considered
		// (by convention here) to be of higher
		// order than negative literals
		if (o1.isPositiveLiteral() != o2.isPositiveLiteral()) {
			if (o1.isPositiveLiteral()) {
				return 1;
			}
			return -1;
		}

		// Check their symbolic names for order first
		rVal = o1.getAtomicSentence().getSymbolicName().compareTo(
				o2.getAtomicSentence().getSymbolicName());

		// If have same symbolic names
		// then need to compare individual arguments
		// for order.
		if (0 == rVal) {
			rVal = compareArgs(o1.getAtomicSentence().getArgs(), o2
					.getAtomicSentence().getArgs());
		}

		return rVal;
	}

	private int compareArgs(List<Term> args1, List<Term> args2) {
		int rVal = 0;

		// Compare argument sizes first
		rVal = args1.size() - args2.size();

		if (0 == rVal && args1.size() > 0) {
			// Move forward and compare the
			// first arguments
			Term t1 = args1.get(0);
			Term t2 = args2.get(0);

			if (t1.getClass() == t2.getClass()) {
				// Note: Variables are considered to have
				// the same order
				if (t1 instanceof Constant) {
					rVal = t1.getSymbolicName().compareTo(t2.getSymbolicName());
				} else if (t1 instanceof Function) {
					rVal = t1.getSymbolicName().compareTo(t2.getSymbolicName());
					if (0 == rVal) {
						// Same function names, therefore
						// compare the function arguments
						rVal = compareArgs(t1.getArgs(), t2.getArgs());
					}
				}

				// If the first args are the same
				// then compare the ordering of the
				// remaining arguments
				if (0 == rVal) {
					rVal = compareArgs(args1.subList(1, args1.size()), args2
							.subList(1, args2.size()));
				}
			} else {
				// Order for different Terms is:
				// Constant > Function > Variable
				if (t1 instanceof Constant) {
					rVal = 1;
				} else if (t2 instanceof Constant) {
					rVal = -1;
				} else if (t1 instanceof Function) {
					rVal = 1;
				} else {
					rVal = -1;
				}
			}
		}

		return rVal;
	}
}

class ClauseEqualityIdentityConstructor implements FOLVisitor {
	private StringBuilder identity = new StringBuilder();
	private int noVarPositions = 0;
	private int[] clauseVarCounts = null;
	private int currentLiteral = 0;
	private Map<String, List<Integer>> varPositions = new HashMap<String, List<Integer>>();

	public ClauseEqualityIdentityConstructor(List<Literal> literals,
			LiteralsSorter sorter) {

		clauseVarCounts = new int[literals.size()];

		for (Literal l : literals) {
			if (l.isNegativeLiteral()) {
				identity.append("~");
			}
			identity.append(l.getAtomicSentence().getSymbolicName());
			identity.append("(");
			boolean firstTerm = true;
			for (Term t : l.getAtomicSentence().getArgs()) {
				if (firstTerm) {
					firstTerm = false;
				} else {
					identity.append(",");
				}
				t.accept(this, null);
			}
			identity.append(")");
			currentLiteral++;
		}

		int min, max;
		min = max = 0;
		for (int i = 0; i < literals.size(); i++) {
			int incITo = i;
			int next = i + 1;
			max += clauseVarCounts[i];
			while (next < literals.size()) {
				if (0 != sorter.compare(literals.get(i), literals.get(next))) {
					break;
				}
				max += clauseVarCounts[next];
				incITo = next; // Need to skip to the end of the range
				next++;
			}
			// This indicates two or more literals are identical
			// except for variable naming (note: identical
			// same name would be removed as are working
			// with sets so don't need to worry about this).
			if ((next - i) > 1) {
				// Need to check each variable
				// and if it has a position within the
				// current min/max range then need
				// to include its alternative
				// sort order positions as well
				for (String key : varPositions.keySet()) {
					List<Integer> positions = varPositions.get(key);
					List<Integer> additPositions = new ArrayList<Integer>();
					// Add then subtract for all possible
					// positions in range
					for (int pos : positions) {
						if (pos >= min && pos < max) {
							int pPos = pos;
							int nPos = pos;
							for (int candSlot = i; candSlot < (next - 1); candSlot++) {
								pPos += clauseVarCounts[i];
								if (pPos >= min && pPos < max) {
									if (!positions.contains(pPos)
											&& !additPositions.contains(pPos)) {
										additPositions.add(pPos);
									}
								}
								nPos -= clauseVarCounts[i];
								if (nPos >= min && nPos < max) {
									if (!positions.contains(nPos)
											&& !additPositions.contains(nPos)) {
										additPositions.add(nPos);
									}
								}
							}
						}
					}
					positions.addAll(additPositions);
				}
			}
			min = max;
			i = incITo;
		}

		// Determine the maxWidth
		int maxWidth = 1;
		while (noVarPositions >= 10) {
			noVarPositions = noVarPositions / 10;
			maxWidth++;
		}
		String format = "%0" + maxWidth + "d";

		// Sort the individual position lists
		// And then add their string representations
		// together
		List<String> varOffsets = new ArrayList<String>();
		for (String key : varPositions.keySet()) {
			List<Integer> positions = varPositions.get(key);
			Collections.sort(positions);
			StringBuilder sb = new StringBuilder();
			for (int pos : positions) {
				sb.append(String.format(format, pos));
			}
			varOffsets.add(sb.toString());
		}
		Collections.sort(varOffsets);
		for (int i = 0; i < varOffsets.size(); i++) {
			identity.append(varOffsets.get(i));
			if (i < (varOffsets.size() - 1)) {
				identity.append(",");
			}
		}
	}

	public String getIdentity() {
		return identity.toString();
	}

	//
	// START-FOLVisitor
	public Object visitVariable(Variable var, Object arg) {
		// All variables will be marked with an *
		identity.append("*");

		List<Integer> positions = varPositions.get(var.getValue());
		if (null == positions) {
			positions = new ArrayList<Integer>();
			varPositions.put(var.getValue(), positions);
		}
		positions.add(noVarPositions);

		noVarPositions++;
		clauseVarCounts[currentLiteral]++;
		return var;
	}

	public Object visitConstant(Constant constant, Object arg) {
		identity.append(constant.getValue());
		return constant;
	}

	public Object visitFunction(Function function, Object arg) {
		boolean firstTerm = true;
		identity.append(function.getFunctionName());
		identity.append("(");
		for (Term t : function.getTerms()) {
			if (firstTerm) {
				firstTerm = false;
			} else {
				identity.append(",");
			}
			t.accept(this, arg);
		}
		identity.append(")");

		return function;
	}

	public Object visitPredicate(Predicate predicate, Object arg) {
		throw new IllegalStateException("Should not be called");
	}

	public Object visitTermEquality(TermEquality equality, Object arg) {
		throw new IllegalStateException("Should not be called");
	}

	public Object visitQuantifiedSentence(QuantifiedSentence sentence,
			Object arg) {
		throw new IllegalStateException("Should not be called");
	}

	public Object visitNotSentence(NotSentence sentence, Object arg) {
		throw new IllegalStateException("Should not be called");
	}

	public Object visitConnectedSentence(ConnectedSentence sentence, Object arg) {
		throw new IllegalStateException("Should not be called");
	}

	// END-FOLVisitor
	//
}