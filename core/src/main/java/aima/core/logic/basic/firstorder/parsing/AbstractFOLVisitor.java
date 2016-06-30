package aima.core.logic.basic.firstorder.parsing;

import java.util.ArrayList;
import java.util.List;

import aima.core.logic.basic.firstorder.parsing.ast.ConnectedSentence;
import aima.core.logic.basic.firstorder.parsing.ast.Constant;
import aima.core.logic.basic.firstorder.parsing.ast.Function;
import aima.core.logic.basic.firstorder.parsing.ast.NotSentence;
import aima.core.logic.basic.firstorder.parsing.ast.Predicate;
import aima.core.logic.basic.firstorder.parsing.ast.QuantifiedSentence;
import aima.core.logic.basic.firstorder.parsing.ast.Sentence;
import aima.core.logic.basic.firstorder.parsing.ast.Term;
import aima.core.logic.basic.firstorder.parsing.ast.TermEquality;
import aima.core.logic.basic.firstorder.parsing.ast.Variable;

/**
 * @author Ravi Mohan
 * 
 */
public class AbstractFOLVisitor implements FOLVisitor {

	public AbstractFOLVisitor() {
	}

	protected Sentence recreate(Object ast) {
		return ((Sentence) ast).copy();
	}

	public Object visitVariable(Variable variable, Object arg) {
		return variable.copy();
	}

	public Object visitQuantifiedSentence(QuantifiedSentence sentence,
			Object arg) {
		List<Variable> variables = new ArrayList<Variable>();
		for (Variable var : sentence.getVariables()) {
			variables.add((Variable) var.accept(this, arg));
		}

		return new QuantifiedSentence(sentence.getQuantifier(), variables,
				(Sentence) sentence.getQuantified().accept(this, arg));
	}

	public Object visitPredicate(Predicate predicate, Object arg) {
		List<Term> terms = predicate.getTerms();
		List<Term> newTerms = new ArrayList<Term>();
		for (int i = 0; i < terms.size(); i++) {
			Term t = terms.get(i);
			Term subsTerm = (Term) t.accept(this, arg);
			newTerms.add(subsTerm);
		}
		return new Predicate(predicate.getPredicateName(), newTerms);

	}

	public Object visitTermEquality(TermEquality equality, Object arg) {
		Term newTerm1 = (Term) equality.getTerm1().accept(this, arg);
		Term newTerm2 = (Term) equality.getTerm2().accept(this, arg);
		return new TermEquality(newTerm1, newTerm2);
	}

	public Object visitConstant(Constant constant, Object arg) {
		return constant;
	}

	public Object visitFunction(Function function, Object arg) {
		List<Term> terms = function.getTerms();
		List<Term> newTerms = new ArrayList<Term>();
		for (int i = 0; i < terms.size(); i++) {
			Term t = terms.get(i);
			Term subsTerm = (Term) t.accept(this, arg);
			newTerms.add(subsTerm);
		}
		return new Function(function.getFunctionName(), newTerms);
	}

	public Object visitNotSentence(NotSentence sentence, Object arg) {
		return new NotSentence((Sentence) sentence.getNegated().accept(this,
				arg));
	}

	public Object visitConnectedSentence(ConnectedSentence sentence, Object arg) {
		Sentence substFirst = (Sentence) sentence.getFirst().accept(this, arg);
		Sentence substSecond = (Sentence) sentence.getSecond()
				.accept(this, arg);
		return new ConnectedSentence(sentence.getConnector(), substFirst,
				substSecond);
	}
}