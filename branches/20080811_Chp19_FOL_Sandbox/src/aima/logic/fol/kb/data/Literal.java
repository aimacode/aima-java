package aima.logic.fol.kb.data;

import aima.logic.fol.parsing.ast.AtomicSentence;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): page 204.
 * 
 * A literal is either an atomic sentence (a positive literal) or
 * a negated atomic sentence (a negative literal).
 * 
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class Literal {
	private AtomicSentence atom = null;
	private boolean negativeLiteral = false;
	private String strRep = null;

	public Literal(AtomicSentence atom) {
		this.atom = atom;
	}

	public Literal(AtomicSentence atom, boolean negated) {
		this.atom = atom;
		this.negativeLiteral = negated;
	}
	
	public Literal newInstance(AtomicSentence atom) {
		return new Literal(atom, negativeLiteral);
	}

	public boolean isPositiveLiteral() {
		return !negativeLiteral;
	}

	public boolean isNegativeLiteral() {
		return negativeLiteral;
	}

	public AtomicSentence getAtomicSentence() {
		return atom;
	}
	
	public String toString() {
		if (null == strRep) {
			StringBuilder sb = new StringBuilder();
			if (isNegativeLiteral()) {
				sb.append("~");
			}
			sb.append(getAtomicSentence().toString());
			strRep = sb.toString();
		}

		return strRep;
	}
}
