package aima.logic.fol.kb.data;

import aima.logic.fol.parsing.ast.Predicate;

/**
 * @see http://logic.stanford.edu/classes/cs157/2008/lectures/lecture13.pdf
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ReducedLiteral extends Literal {
	private String strRep = null;
	
	public ReducedLiteral(Predicate p) {
		super(p);
	}

	public ReducedLiteral(Predicate p, boolean negated) {
		super(p, negated);
	}
	
	public Literal newInstance(Predicate p) {
		return new ReducedLiteral(p, isNegativeLiteral());
	}
	
	public String toString() {
		if (null == strRep) {
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			if (isNegativeLiteral()) {
				sb.append("~");
			}
			sb.append(getPredicate().toString());
			sb.append("]");
			strRep = sb.toString();
		}

		return strRep;
	}
}
