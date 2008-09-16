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
 * Artificial Intelligence A Modern Approach (2nd Edition): page 296.
 * Every sentence of first-order logic can be converted into an inferentially
 * equivalent CNF sentence.
 * 
 * Note: Transformation rules extracted from pg 215, 296, and 297.
 */

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
		
		// TODO get to be just ands and ors

		// Distribute V over ^:
		// (alpha V (beta ^ gamma)) equivalent to
		// ((alpha V beta) ^ (alpha V gamma))
		// Note: Step may also require flattening out nested
		// conjunctions and disjunctions.
		
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
		// CNF requires NOT (~) to appear only in literals, so we 'move ~
		// inwards' by repeated application of the following equivalences:
		// ~(~alpha) equivalent to alpha (double negation elimination)
		// ~(alpha ^ beta) equivalent to (~alpha V ~beta) (De Morgan)
		// ~(alpha V beta) equivalent to (~alpha ^ ~beta) (De Morgan)
		// in addition, rules for negated quantifiers:
		// ~FORALL x p becomes EXISTS x ~p
		// ~EXISTS x p becomes FORALL x ~p

		// TODO
		return null;
	}

	public Object visitConnectedSentence(ConnectedSentence sentence, Object arg) {
		// Eliminate <=>, bi-conditional elimination,
		// replace (alpha <=> beta) with (alpha => beta) ^ (beta => alpha).

		// Eliminate =>, implication elimination,
		// replacing (alpha => beta) with (~alpha V beta)
		
		
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
		
		// Standardize variables: For sentences like:
		// (FORALL x P(x)) V (EXISTS x Q(x)),
		// which use the same variable name twice, change the name of one of the
		// variables.
		
		// Skolemize: Skolemization is the process of removing existential
		// quantifiers by elimination. This is done by introducing Skolem
		// functions. The general rule is that the arguments of the Skolem
		// function are all the universally quantified variables in whose
		// scope the existential quantifier appears.

		// Drop universal quantifiers.
		
		// TODO
		return null;
	}
}
