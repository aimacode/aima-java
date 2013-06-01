package aima.core.logic.propositional.parsing;

import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;

/**
 * @author Ravi Mohan
 * 
 */
public interface PLVisitor {
	Object visitPropositionSymbol(PropositionSymbol sentence, Object arg);

	Object visitUnarySentence(ComplexSentence sentence, Object arg);

	Object visitBinarySentence(ComplexSentence sentence, Object arg);
}