package aima.core.logic.propositional.kb.data;

import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.propositional.parsing.ast.Connective;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 244.<br>
 * <br>
 * A literal is either an atomic sentence (a positive literal) or a negated
 * atomic sentence (a negative literal). In propositional logic the atomic
 * sentences consist of a single proposition symbol. In addition, a literal is
 * immutable.
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

	/**
	 * Constructor for a positive literal.
	 * 
	 * @param atom
	 *            the atomic sentence comprising the literal.
	 */
	public Literal(PropositionSymbol atom) {
		this(atom, false);
	}

	/**
	 * Constructor.
	 * 
	 * @param atom
	 *            the atomic sentence comprising the literal.
	 * @param negated
	 *            true if to be an negative literal, false to be a positive
	 *            literal.
	 */
	public Literal(PropositionSymbol atom, boolean negated) {
		this.atom = atom;
		this.negative = negated;
	}

	/**
	 * 
	 * @return true if a positive literal, false otherwise.
	 */
	public boolean isPositiveLiteral() {
		return !negative;
	}

	/**
	 * 
	 * @return true if a negative literal, false otherwise.
	 */
	public boolean isNegativeLiteral() {
		return negative;
	}

	/**
	 * 
	 * @return the atomic sentence comprising the literal.
	 */
	public PropositionSymbol getAtomicSentence() {
		return atom;
	}

	/**
	 * 
	 * @return true if the literal is representative of an always true
	 *         proposition (i.e. True or ~False), false otherwise.
	 */
	public boolean isAlwaysTrue() {
		// True | ~False
		if (isPositiveLiteral()) {
			return getAtomicSentence().isAlwaysTrue();
		} else {
			return getAtomicSentence().isAlwaysFalse();
		}
	}

	/**
	 * 
	 * @return true if the literal is representative of an always false
	 *         proposition (i.e. False or ~True), false othwerwise.
	 */
	public boolean isAlwaysFalse() {
		// False | ~True
		if (isPositiveLiteral()) {
			return getAtomicSentence().isAlwaysFalse();
		} else {
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