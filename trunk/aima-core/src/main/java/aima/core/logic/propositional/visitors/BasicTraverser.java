package aima.core.logic.propositional.visitors;

import java.util.Set;

import aima.core.logic.propositional.parsing.PLVisitor;
import aima.core.logic.propositional.parsing.ast.BinarySentence;
import aima.core.logic.propositional.parsing.ast.FalseSentence;
import aima.core.logic.propositional.parsing.ast.MultiSentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.parsing.ast.TrueSentence;
import aima.core.logic.propositional.parsing.ast.UnarySentence;
import aima.core.util.SetOps;

/**
 * Super class of Visitors that are "read only" and gather information from an
 * existing parse tree .
 * 
 * @author Ravi Mohan
 * 
 */
public class BasicTraverser implements PLVisitor {

	public Object visitSymbol(Symbol s, Object arg) {
		return arg;
	}

	public Object visitTrueSentence(TrueSentence ts, Object arg) {
		return arg;
	}

	public Object visitFalseSentence(FalseSentence fs, Object arg) {
		return arg;
	}

	@SuppressWarnings("unchecked")
	public Object visitNotSentence(UnarySentence ns, Object arg) {
		Set s = (Set) arg;
		return SetOps.union(s, (Set) ns.getNegated().accept(this, arg));
	}

	@SuppressWarnings("unchecked")
	public Object visitBinarySentence(BinarySentence bs, Object arg) {
		Set s = (Set) arg;
		Set termunion = SetOps.union((Set) bs.getFirst().accept(this, arg),
				(Set) bs.getSecond().accept(this, arg));
		return SetOps.union(s, termunion);
	}

	public Object visitMultiSentence(MultiSentence fs, Object arg) {
		throw new RuntimeException("Can't handle MultiSentence");
	}
}
