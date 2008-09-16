package aima.logic.fol;

import aima.logic.fol.kb.data.CNF;
import aima.logic.fol.parsing.FOLParser;
import aima.logic.fol.parsing.FOLVisitor;
import aima.logic.fol.parsing.ast.ConnectedSentence;
import aima.logic.fol.parsing.ast.Constant;
import aima.logic.fol.parsing.ast.Function;
import aima.logic.fol.parsing.ast.NotSentence;
import aima.logic.fol.parsing.ast.ParanthizedSentence;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.QuantifiedSentence;
import aima.logic.fol.parsing.ast.Sentence;
import aima.logic.fol.parsing.ast.TermEquality;
import aima.logic.fol.parsing.ast.Variable;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class CNFVisitor implements FOLVisitor {

	private FOLParser parser = null;

	public CNFVisitor(FOLParser parser) {
		this.parser = parser;
	}
	
	public CNF cnf(Sentence aSentence) {
		// TODO
		return null;
	}

	public Object visitPredicate(Predicate p, Object arg) {
		// TODO
		return null;
	}

	public Object visitTermEquality(TermEquality equality, Object arg) {
		// TODO
		return null;
	}

	public Object visitVariable(Variable variable, Object arg) {
		// TODO
		return null;
	}

	public Object visitConstant(Constant constant, Object arg) {
		// TODO
		return null;
	}

	public Object visitFunction(Function function, Object arg) {
		// TODO
		return null;
	}

	public Object visitNotSentence(NotSentence sentence, Object arg) {
		// TODO
		return null;
	}

	public Object visitConnectedSentence(ConnectedSentence sentence, Object arg) {
		// TODO
		return null;
	}

	public Object visitParanthizedSentence(ParanthizedSentence sentence,
			Object arg) {
		// TODO
		return null;
	}

	public Object visitQuantifiedSentence(QuantifiedSentence sentence,
			Object arg) {
		// TODO
		return null;
	}
}
