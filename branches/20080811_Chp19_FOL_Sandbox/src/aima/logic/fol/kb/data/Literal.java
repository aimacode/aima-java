package aima.logic.fol.kb.data;

import aima.logic.fol.parsing.ast.Predicate;

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
// TODO: Should contain an AtomicSentence not a predicate, change when support
// term equality.
public class Literal {
	private Predicate predicate = null;
	private boolean negativeLiteral = false;
	private String strRep = null;

	public Literal(Predicate p) {
		this.predicate = p;
	}

	public Literal(Predicate p, boolean negated) {
		this.predicate = p;
		this.negativeLiteral = negated;
	}
	
	public Literal newInstance(Predicate p) {
		return new Literal(p, negativeLiteral);
	}

	public boolean isPositiveLiteral() {
		return !negativeLiteral;
	}

	public boolean isNegativeLiteral() {
		return negativeLiteral;
	}

	public Predicate getPredicate() {
		return predicate;
	}
	
	public String toString() {
		if (null == strRep) {
			StringBuilder sb = new StringBuilder();
			if (isNegativeLiteral()) {
				sb.append("~");
			}
			sb.append(getPredicate().toString());
			strRep = sb.toString();
		}

		return strRep;
	}
}
