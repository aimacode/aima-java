/*
 * Created on Sep 15, 2003 by Ravi Mohan
 *  
 */
package aima.logic.propositional.parsing.ast;

import aima.logic.propositional.parsing.PLVisitor;

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