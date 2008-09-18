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
		// Strip out extra parenthesis (e.g. ((alpha ^ beta)))
		// in order to simplify remaining logic.
		Sentence noExtraParenthesis = (Sentence) aSentence.accept(
				new StripExtraParenthesis(), null);
		
		// TODO:!!!! this will simplify remaining logic
		// Standardize variables: For sentences like:
		// (FORALL x P(x)) V (EXISTS x Q(x)),
		// which use the same variable name twice, change the name of one of the
		// variables.
		
		// Convert the sentence to just
		// ands and ors
		Sentence andsAndOrs = (Sentence) noExtraParenthesis.accept(this, null);

		// TODO:
		// Distribute V over ^:
		// (alpha V (beta ^ gamma)) equivalent to
		// ((alpha V beta) ^ (alpha V gamma))
		// Note: Step may also require flattening out nested
		// conjunctions and disjunctions.
		
		// TODO: Construct CNF
		return null;
	}

	public Object visitPredicate(Predicate p, Object arg) {
		return p.copy();
	}

	public Object visitTermEquality(TermEquality equality, Object arg) {
		return equality.copy();
	}

	public Object visitVariable(Variable variable, Object arg) {
		return variable.copy();
	}

	public Object visitConstant(Constant constant, Object arg) {
		return constant.copy();
	}

	public Object visitFunction(Function function, Object arg) {
		return function.copy();
	}

	public Object visitNotSentence(NotSentence alpha, Object arg) {
		// CNF requires NOT (~) to appear only in literals, so we 'move ~
		// inwards' by repeated application of the following equivalences:
		Sentence negated = alpha.getNegated();
		
		// ~(~alpha) equivalent to alpha (double negation elimination)
		if (negated instanceof NotSentence) {
			return ((NotSentence) negated).getNegated().accept(this, arg);
		}

		if (negated instanceof ConnectedSentence) {
			ConnectedSentence negConnected = (ConnectedSentence) negated;
			// ~(alpha ^ beta) equivalent to (~alpha V ~beta) (De Morgan)
			if (Connectors.isAND(negConnected.getConnector())) {
				return new ConnectedSentence(Connectors.OR, new NotSentence(
						(Sentence) negConnected.getFirst().accept(this, arg)),
						new NotSentence((Sentence) negConnected.getSecond()
								.accept(this, arg)));
			}

			// ~(alpha V beta) equivalent to (~alpha ^ ~beta) (De Morgan)
			if (Connectors.isOR(negConnected.getConnector())) {
				return new ConnectedSentence(Connectors.AND, new NotSentence(
						(Sentence) negConnected.getFirst().accept(this, arg)),
						new NotSentence((Sentence) negConnected.getSecond()
								.accept(this, arg)));
			}
		}
		
		// in addition, rules for negated quantifiers:
		if (negated instanceof QuantifiedSentence) {
			QuantifiedSentence negQuantified = (QuantifiedSentence) negated;
			// ~FORALL x p becomes EXISTS x ~p
			if (Quantifiers.isFORALL(negQuantified.getQuantifier())) {
				return new QuantifiedSentence(Quantifiers.EXISTS, negQuantified
						.getVariables(), new NotSentence(
						(Sentence) negQuantified.getQuantified().accept(this,
								arg)));
			}

			// ~EXISTS x p becomes FORALL x ~p
			if (Quantifiers.isEXISTS(negQuantified.getQuantifier())) {
				return new QuantifiedSentence(Quantifiers.FORALL, negQuantified
						.getVariables(), new NotSentence(
						(Sentence) negQuantified.getQuantified().accept(this,
								arg)));
			}
		}

		return new NotSentence((Sentence) negated.accept(this, arg));
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
		// This should not be called as these should have already
		// been stripped. Throw an exception to indicat this
		throw new IllegalStateException(
				"All paranthized sentences should have been stripped out.");
	}

	public Object visitQuantifiedSentence(QuantifiedSentence sentence,
			Object arg) {
		
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

class StripExtraParenthesis implements FOLVisitor {

	public StripExtraParenthesis() {
	}

	public Object visitPredicate(Predicate p, Object arg) {
		return p;
	}

	public Object visitTermEquality(TermEquality equality, Object arg) {
		return equality;
	}

	public Object visitVariable(Variable variable, Object arg) {
		return variable;
	}

	public Object visitConstant(Constant constant, Object arg) {
		return constant;
	}

	public Object visitFunction(Function function, Object arg) {
		return function;
	}

	public Object visitNotSentence(NotSentence sentence, Object arg) {
		return new NotSentence((Sentence) sentence.getNegated().accept(this,
				arg));
	}

	public Object visitConnectedSentence(ConnectedSentence sentence, Object arg) {
		return new ConnectedSentence(sentence.getConnector(),
				(Sentence) sentence.getFirst().accept(this, arg),
				(Sentence) sentence.getSecond().accept(this, arg));
	}

	public Object visitParanthizedSentence(ParanthizedSentence sentence,
			Object arg) {
		// This causes nested parenthesis to be removed.
		return sentence.getParanthized().accept(this, arg);
	}

	public Object visitQuantifiedSentence(QuantifiedSentence sentence,
			Object arg) {
		
		return new QuantifiedSentence(sentence.getQuantifier(), sentence
				.getVariables(), (Sentence) sentence.getQuantified().accept(
				this, arg));
	}
}
