package aima.core.logic.propositional.parsing;

import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;

/**
 * Abstract implementation of the PLVisitor interface that provides default
 * behavior for each of the methods.
 * 
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class AbstractPLVisitor implements PLVisitor {

	@Override
	public Object visitPropositionSymbol(PropositionSymbol s, Object arg) {
		// default behavior is to treat propositional symbols as atomic
		// and leave unchanged.
		return s;
	}

	@Override
	public Object visitUnarySentence(ComplexSentence s, Object arg) {
		// a new Complex Sentence with the same connective but possibly
		// with its simpler sentence replaced by the visitor.
		return new ComplexSentence(s.getConnective(), (Sentence) s
				.getSimplerSentence(0).accept(this, arg));
	}

	@Override
	public Object visitBinarySentence(ComplexSentence s, Object arg) {
		// a new Complex Sentence with the same connective but possibly
		// with its simpler sentences replaced by the visitor.
		return new ComplexSentence(s.getConnective(), (Sentence) s
				.getSimplerSentence(0).accept(this, arg), (Sentence) s
				.getSimplerSentence(1).accept(this, arg));
	}
}