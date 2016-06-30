package aima.core.logic.basic.firstorder.parsing;

import aima.core.logic.basic.firstorder.parsing.ast.ConnectedSentence;
import aima.core.logic.basic.firstorder.parsing.ast.Constant;
import aima.core.logic.basic.firstorder.parsing.ast.Function;
import aima.core.logic.basic.firstorder.parsing.ast.NotSentence;
import aima.core.logic.basic.firstorder.parsing.ast.Predicate;
import aima.core.logic.basic.firstorder.parsing.ast.QuantifiedSentence;
import aima.core.logic.basic.firstorder.parsing.ast.TermEquality;
import aima.core.logic.basic.firstorder.parsing.ast.Variable;

/**
 * @author Ravi Mohan
 * 
 */
public interface FOLVisitor {
	public Object visitPredicate(Predicate p, Object arg);

	public Object visitTermEquality(TermEquality equality, Object arg);

	public Object visitVariable(Variable variable, Object arg);

	public Object visitConstant(Constant constant, Object arg);

	public Object visitFunction(Function function, Object arg);

	public Object visitNotSentence(NotSentence sentence, Object arg);

	public Object visitConnectedSentence(ConnectedSentence sentence, Object arg);

	public Object visitQuantifiedSentence(QuantifiedSentence sentence,
			Object arg);
}
