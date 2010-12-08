package aima.core.logic.fol.kb.data;

import aima.core.logic.fol.parsing.ast.AtomicSentence;

/**
 * @see http://logic.stanford.edu/classes/cs157/2008/lectures/lecture13.pdf
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ReducedLiteral extends Literal {
	private String strRep = null;

	public ReducedLiteral(AtomicSentence atom) {
		super(atom);
	}

	public ReducedLiteral(AtomicSentence atom, boolean negated) {
		super(atom, negated);
	}

	@Override
	public Literal newInstance(AtomicSentence atom) {
		return new ReducedLiteral(atom, isNegativeLiteral());
	}

	@Override
	public String toString() {
		if (null == strRep) {
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			if (isNegativeLiteral()) {
				sb.append("~");
			}
			sb.append(getAtomicSentence().toString());
			sb.append("]");
			strRep = sb.toString();
		}

		return strRep;
	}
}
