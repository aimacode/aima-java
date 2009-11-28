package aima.core.logic.propositional.parsing;

import aima.core.logic.common.Visitor;
import aima.core.logic.propositional.parsing.ast.BinarySentence;
import aima.core.logic.propositional.parsing.ast.FalseSentence;
import aima.core.logic.propositional.parsing.ast.MultiSentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.parsing.ast.TrueSentence;
import aima.core.logic.propositional.parsing.ast.UnarySentence;

/**
 * @author Ravi Mohan
 * 
 */
public interface PLVisitor extends Visitor {
	public Object visitSymbol(Symbol s, Object arg);

	public Object visitTrueSentence(TrueSentence ts, Object arg);

	public Object visitFalseSentence(FalseSentence fs, Object arg);

	public Object visitNotSentence(UnarySentence fs, Object arg);

	public Object visitBinarySentence(BinarySentence fs, Object arg);

	public Object visitMultiSentence(MultiSentence fs, Object arg);
}