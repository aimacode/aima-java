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

public class FalseSentence extends AtomicSentence {
	public String toString() {
		return "FALSE";
	}

	public Object accept(PLVisitor plv, Object arg) {
		return plv.visitFalseSentence(this, arg);
	}
}