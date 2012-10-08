package aima.core.logic.propositional.parsing.ast;

import aima.core.logic.propositional.parsing.PLVisitor;

/**
 * @author Ravi Mohan
 * 
 */
public class TrueSentence extends AtomicSentence {

	@Override
	public String toString() {
		return "TRUE";
	}

	@Override
	public Object accept(PLVisitor plv, Object arg) {
		return plv.visitTrueSentence(this, arg);
	}
}