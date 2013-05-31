package aima.core.logic.propositional.visitors;

import java.util.Set;

import aima.core.logic.propositional.parsing.PLVisitor;
import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import aima.core.util.SetOps;

/**
 * Super class of Visitors that are "read only" and gather information from an
 * existing parse tree .
 * 
 * @author Ravi Mohan
 * 
 */
public class BasicTraverser implements PLVisitor {

	public Object visitPropositionSymbol(PropositionSymbol s, Object arg) {
		return arg;
	}

	@SuppressWarnings("unchecked")
	public Object visitUnarySentence(ComplexSentence s, Object arg) {
		Set set = (Set) arg;
		return SetOps.union(set, (Set) s.getSimplerSentence(0).accept(this, arg));
	}

	@SuppressWarnings("unchecked")
	public Object visitBinarySentence(ComplexSentence s, Object arg) {
		Set set = (Set) arg;
		Set termunion = SetOps.union((Set) s.getSimplerSentence(0).accept(this, arg), (Set) s.getSimplerSentence(1).accept(this, arg));
		return SetOps.union(set, termunion);
	}
}
