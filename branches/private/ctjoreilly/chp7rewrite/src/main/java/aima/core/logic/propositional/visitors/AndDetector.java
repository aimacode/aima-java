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
public class AndDetector implements PLVisitor {

	public Object visitPropositionSymbol(PropositionSymbol s, Object arg) {

		return new Boolean(false);
	}

	public Object visitUnarySentence(ComplexSentence s, Object arg) {
		return s.get(0).accept(this, null);
	}

	public Object visitBinarySentence(ComplexSentence s, Object arg) {
		if (s.getConnective() == Connective.AND) {
			return new Boolean(true);
		} else {
			boolean first = ((Boolean) s.get(0).accept(this, null))
					.booleanValue();
			boolean second = ((Boolean) s.get(1).accept(this, null))
					.booleanValue();
			
			return new Boolean(first || second);
		}
	}

	public boolean containsEmbeddedAnd(Sentence s) {
		return ((Boolean) s.accept(this, null)).booleanValue();
	}
}