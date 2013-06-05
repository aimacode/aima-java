package aima.core.logic.propositional.visitors;

import aima.core.logic.propositional.parsing.PLVisitor;
import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.Connective;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;

/**
 * @author Ravi Mohan
 * 
 */
public class AndDetector implements PLVisitor<Boolean, Boolean> {

	@Override
	public Boolean visitPropositionSymbol(PropositionSymbol s, Boolean arg) {
		return new Boolean(false);
	}

	@Override
	public Boolean visitUnarySentence(ComplexSentence s, Boolean arg) {
		return s.getSimplerSentence(0).accept(this, null);
	}

	@Override
	public Boolean visitBinarySentence(ComplexSentence s, Boolean arg) {
		if (s.getConnective() == Connective.AND) {
			return new Boolean(true);
		} else {
			boolean first  = s.getSimplerSentence(0).accept(this, null);
			boolean second = s.getSimplerSentence(1).accept(this, null);
			
			return new Boolean(first || second);
		}
	}

	public boolean containsEmbeddedAnd(Sentence s) {
		return s.accept(this, null).booleanValue();
	}
}