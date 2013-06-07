package aima.core.logic.propositional.kb.data;

import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.propositional.parsing.ast.Connective;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 244.<br>
 * <br>
 * A literal is either an atomic sentence (a positive literal) or a negated
 * atomic sentence (a negative literal). In propositional logic the atomic
 * sentences consist of a single proposition symbol.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class Literal {
	private PropositionSymbol atom = null;
	private boolean negative = false; // Assume positive by default.
	//
	private String cachedStringRep = null;
	private int cachedHashCode = -1;

	public Literal(PropositionSymbol atom) {
		this.atom = atom;
	}

	public Literal(PropositionSymbol atom, boolean negated) {
		this.atom = atom;
		this.negative = negated;
	}

	public boolean isPositiveLiteral() {
		return !negative;
	}

	public boolean isNegativeLiteral() {
		return negative;
	}

	public PropositionSymbol getAtomicSentence() {
		return atom;
	}
	
	public boolean isAlwaysTrue() {
		// True | ~False
		if (isPositiveLiteral()) {
			return getAtomicSentence().isAlwaysTrue();
		}
		else {
			return getAtomicSentence().isAlwaysFalse();
		}
	}
	
	public boolean isAlwaysFalse() {
		// False | ~True
		if (isPositiveLiteral()) {
			return getAtomicSentence().isAlwaysFalse();
		}
		else {
			return getAtomicSentence().isAlwaysTrue();
		}
	}

	@Override
	public String toString() {
		if (null == cachedStringRep) {
			StringBuilder sb = new StringBuilder();
			if (isNegativeLiteral()) {
				sb.append(Connective.NOT.toString());
			}
			sb.append(getAtomicSentence().toString());
			cachedStringRep = sb.toString();
		}

		return cachedStringRep;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Literal)) {
			return false;
		}
		Literal l = (Literal) o;
		return l.isPositiveLiteral() == isPositiveLiteral()
				&& l.getAtomicSentence().equals(getAtomicSentence());
	}

	@Override
	public int hashCode() {
		if (cachedHashCode == -1) {
			cachedHashCode = 17;
			cachedHashCode = (cachedHashCode * 37)
					+ (isPositiveLiteral() ? "+".hashCode() : "-".hashCode());
			cachedHashCode = (cachedHashCode * 37) + atom.hashCode();
		}
		return cachedHashCode;
	}
}