package aima.core.logic.propositional.parsing;

import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;

/**
 * @author Ravi Mohan
 * 
 */
public class AbstractPLVisitor implements PLVisitor {

	public Object visitPropositionSymbol(PropositionSymbol s, Object arg) {
		return s;
	}

	public Object visitUnarySentence(ComplexSentence s, Object arg) {
		return new ComplexSentence(s.getConnective(), (Sentence) s.getSimplerSentence(0).accept(this, arg));
	}

	public Object visitBinarySentence(ComplexSentence s, Object arg) {
		return new ComplexSentence(s.getConnective(), 
				                  (Sentence) s.getSimplerSentence(0).accept(this, arg), 
				                  (Sentence) s.getSimplerSentence(1).accept(this, arg));
	}
}