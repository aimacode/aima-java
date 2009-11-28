package aima.core.logic.propositional.parsing.ast;

import aima.core.logic.propositional.parsing.PLVisitor;

/**
 * @author Ravi Mohan
 * 
 */
public class FalseSentence extends AtomicSentence {
	@Override
	public String toString() {
		return "FALSE";
	}

	@Override
	public Object accept(PLVisitor plv, Object arg) {
		return plv.visitFalseSentence(this, arg);
	}
}