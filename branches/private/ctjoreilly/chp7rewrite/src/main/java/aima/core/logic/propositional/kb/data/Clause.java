package aima.core.logic.propositional.kb.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import aima.core.logic.propositional.parsing.ast.Connective;

/**
 * A Clause: A disjunction of literals.
 * 
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class Clause {
	//
	private Set<Literal> literals = new LinkedHashSet<Literal>();
	private List<Literal> positiveLiterals = new ArrayList<Literal>();
	private List<Literal> negativeLiterals = new ArrayList<Literal>();
	//
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
			this.literals.add(l);
			if (l.isPositiveLiteral()) {
				this.positiveLiterals.add(l);
			} else {
				this.negativeLiterals.add(l);
			}
		}
		
		// Make immutable
		literals = Collections.unmodifiableSet(literals);
		positiveLiterals = Collections.unmodifiableList(positiveLiterals);
		negativeLiterals = Collections.unmodifiableList(negativeLiterals);
	}
	
	// The empty clause - a disjunction of no disjuncts - is equivalent to False because
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
		
		for (Literal l : literals) {
			if (l.isAlwaysTrue()) {
				// (... | True | ...) is a tautology.
				// (... | ~False | ...) is a tautology
				return true;
			}
		}

		for (Literal pl : positiveLiterals) {
			for (Literal nl : negativeLiterals) {
				// (... | P | ~P | ...) is a tautology
				if (pl.getAtomicSentence().equals(nl.getAtomicSentence())) {
					return true;
				}
			}
		}

		return false;
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
		return literals;
	}

	public List<Literal> getPositiveLiterals() {
		return positiveLiterals;
	}

	public List<Literal> getNegativeLiterals() {
		return negativeLiterals;
	}

	@Override
	public String toString() {
		if (cachedStringRep == null) {
			StringBuilder sb = new StringBuilder();
			boolean first = true;
			sb.append("(");
			for (Literal l : literals) {
				if (first) {
					first = false;
				} else {
					sb.append(" ");
					sb.append(Connective.OR);
					sb.append(" ");
				}
				sb.append(l.toString());
			}
			sb.append(")");
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