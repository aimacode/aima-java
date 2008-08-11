/*
 * Created on Sep 22, 2004
 *
 */
package aima.logic.fol;

import java.util.ArrayList;
import java.util.List;

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
 * @author Ravi Mohan
 * 
 */

public class PredicateCollector implements FOLVisitor {

	public PredicateCollector() {

	}

	public Object visitPredicate(Predicate p, Object arg) {

		List<Predicate> predicates = (List<Predicate>) arg;
		predicates.add(p);
		return predicates;
	}

	public Object visitTermEquality(TermEquality equality, Object arg) {
		return arg;
	}

	public Object visitVariable(Variable variable, Object arg) {

		return arg;
	}

	public Object visitConstant(Constant constant, Object arg) {
		return arg;
	}

	public Object visitFunction(Function function, Object arg) {
		return arg;
	}

	public Object visitNotSentence(NotSentence sentence, Object arg) {
		sentence.getNegated().accept(this, arg);
		return arg;
	}

	public Object visitConnectedSentence(ConnectedSentence sentence, Object arg) {
		sentence.getFirst().accept(this, arg);
		sentence.getSecond().accept(this, arg);
		return arg;
	}

	public Object visitParanthizedSentence(ParanthizedSentence sentence,
			Object arg) {
		sentence.getParanthized().accept(this, arg);
		return arg;
	}

	public Object visitQuantifiedSentence(QuantifiedSentence sentence,
			Object arg) {
		sentence.getQuantified().accept(this, arg);
		return arg;
	}

	public List getPredicates(Sentence s) {
		return (List) s.accept(this, new ArrayList());
	}

}