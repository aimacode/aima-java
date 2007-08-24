/*
 * Created on Dec 5, 2004
 *
 */
package aima.logic.propositional.visitors;

import aima.logic.propositional.parsing.PLVisitor;
import aima.logic.propositional.parsing.ast.BinarySentence;
import aima.logic.propositional.parsing.ast.FalseSentence;
import aima.logic.propositional.parsing.ast.MultiSentence;
import aima.logic.propositional.parsing.ast.Sentence;
import aima.logic.propositional.parsing.ast.Symbol;
import aima.logic.propositional.parsing.ast.TrueSentence;
import aima.logic.propositional.parsing.ast.UnarySentence;

/**
 * @author Ravi Mohan
 * 
 */

public class AndDetector implements PLVisitor {

	public Object visitSymbol(Symbol s, Object arg) {

		return new Boolean(false);
	}

	public Object visitTrueSentence(TrueSentence ts, Object arg) {
		return new Boolean(false);
	}

	public Object visitFalseSentence(FalseSentence fs, Object arg) {
		return new Boolean(false);
	}

	public Object visitNotSentence(UnarySentence fs, Object arg) {
		return fs.getNegated().accept(this, null);
	}

	public Object visitBinarySentence(BinarySentence fs, Object arg) {
		if (fs.isAndSentence()) {
			return new Boolean(true);
		} else {
			boolean first = ((Boolean) fs.getFirst().accept(this, null))
					.booleanValue();
			boolean second = ((Boolean) fs.getSecond().accept(this, null))
					.booleanValue();
			return new Boolean((first || second));
		}
	}

	public Object visitMultiSentence(MultiSentence fs, Object arg) {
		throw new RuntimeException("can't handle multisentences");
	}

	public boolean containsEmbeddedAnd(Sentence s) {
		return ((Boolean) s.accept(this, null)).booleanValue();
	}

}