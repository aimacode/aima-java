package aima.core.logic.propositional.parsing;

import aima.core.logic.common.Visitor;
import aima.core.logic.propositional.parsing.ast.BinarySentence;
import aima.core.logic.propositional.parsing.ast.FalseSentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.parsing.ast.TrueSentence;
import aima.core.logic.propositional.parsing.ast.UnarySentence;

/**
 * @author Ravi Mohan
 * 
 */
public interface PLVisitor extends Visitor {
	Object visitSymbol(Symbol s, Object arg);

	Object visitTrueSentence(TrueSentence ts, Object arg);

	Object visitFalseSentence(FalseSentence fs, Object arg);

	Object visitNotSentence(UnarySentence fs, Object arg);

	Object visitBinarySentence(BinarySentence fs, Object arg);
}