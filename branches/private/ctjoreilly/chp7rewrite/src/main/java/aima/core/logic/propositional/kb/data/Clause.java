package aima.core.logic.propositional.kb.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import aima.core.util.SetOps;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 253.<br>
 * <br>
 * A Clause: A disjunction of literals. Here we view a Clause as a set of
 * literals. This respects the restriction, under resolution, that a resulting
 * clause should contain only 1 copy of a resulting literal.
 * 
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class Clause {
	//
	private Set<Literal> literals = new LinkedHashSet<Literal>();
	//
	private Set<PropositionSymbol> cachedPositiveSymbols = new LinkedHashSet<PropositionSymbol>();
	private Set<PropositionSymbol> cachedNegativeSymbols = new LinkedHashSet<PropositionSymbol>();
	private String cachedStringRep = null;
	private int cachedHashCode = -1;

	public Clause() {
		// i.e. the empty clause
		this(new ArrayList<Literal>());
	}

	public Clause(Literal... lits) {
		this(Arrays.asList(lits));
	}

	public Clause(List<Literal> lits) {
		for (Literal l : lits) {
			if (l.isAlwaysFalse()) {
				// Don't add literals of the form
				// False | ~True
				continue;
			}
			if (this.literals.add(l)) {
				// Only add to caches if not already added
				if (l.isPositiveLiteral()) {
					this.cachedPositiveSymbols.add(l.getAtomicSentence());
				} else {
					this.cachedNegativeSymbols.add(l.getAtomicSentence());
				}
			}
		}

		// Make immutable
		literals = Collections.unmodifiableSet(literals);
		cachedPositiveSymbols = Collections
				.unmodifiableSet(cachedPositiveSymbols);
		cachedNegativeSymbols = Collections
				.unmodifiableSet(cachedNegativeSymbols);
	}

	// The empty clause - a disjunction of no disjuncts - is equivalent to False
	// because
	// a disjunction is true only if at least one of its disjuncts is true.
	public boolean isFalse() {
		return isEmpty();
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
		return !isEmpty() && cachedPositiveSymbols.size() == 1;
	}

	public boolean isImplicationDefiniteClause() {
		// An Implication Definite Clause is a disjunction of literals of
		// which exactly 1 is positive and there is 1 or more negative
		// literals.
		return isDefiniteClause() && cachedNegativeSymbols.size() >= 1;
	}

	public boolean isHornClause() {
		// A Horn clause is a disjunction of literals of which at most one is
		// positive.
		return !isEmpty() && cachedPositiveSymbols.size() <= 1;
	}

	public boolean isTautology() {

		for (Literal l : literals) {
			if (l.isAlwaysTrue()) {
				// (... | True | ...) is a tautology.
				// (... | ~False | ...) is a tautology
				return true;
			}
		}

		if (SetOps.intersection(cachedPositiveSymbols, cachedNegativeSymbols)
				.size() > 0) {
			// We have:
			// P | ~P
			// which is always true.
			return true;
		}

		return false;
	}

	public int getNumberLiterals() {
		return literals.size();
	}

	public int getNumberPositiveSymbols() {
		return cachedPositiveSymbols.size();
	}

	public int getNumberNegativeSymbols() {
		return cachedNegativeSymbols.size();
	}

	public Set<Literal> getLiterals() {
		return literals;
	}

	public Set<PropositionSymbol> getPositiveSymbols() {
		return cachedPositiveSymbols;
	}

	public Set<PropositionSymbol> getNegativeSymbols() {
		return cachedNegativeSymbols;
	}

	@Override
	public String toString() {
		if (cachedStringRep == null) {
			StringBuilder sb = new StringBuilder();
			boolean first = true;
			sb.append("{");
			for (Literal l : literals) {
				if (first) {
					first = false;
				} else {
					sb.append(", ");
				}
				sb.append(l);
			}
			sb.append("}");
			cachedStringRep = sb.toString();
		}
		return cachedStringRep;
	}

	@Override
	public int hashCode() {
		if (cachedHashCode == -1) {
			cachedHashCode = literals.hashCode();
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
		if (!(othObj instanceof Clause)) {
			return false;
		}
		Clause othClause = (Clause) othObj;

		return othClause.literals.equals(this.literals);
	}
}