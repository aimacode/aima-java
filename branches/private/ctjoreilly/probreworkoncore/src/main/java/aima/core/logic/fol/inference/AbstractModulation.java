package aima.core.logic.fol.inference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.logic.fol.SubstVisitor;
import aima.core.logic.fol.Unifier;
import aima.core.logic.fol.VariableCollector;
import aima.core.logic.fol.parsing.FOLVisitor;
import aima.core.logic.fol.parsing.ast.AtomicSentence;
import aima.core.logic.fol.parsing.ast.ConnectedSentence;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Function;
import aima.core.logic.fol.parsing.ast.NotSentence;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.QuantifiedSentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.TermEquality;
import aima.core.logic.fol.parsing.ast.Variable;

/**
 * Abstract base class for Demodulation and Paramodulation algorithms. 
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public abstract class AbstractModulation {
	//
	// PROTECTED ATTRIBUTES
	protected VariableCollector variableCollector = new VariableCollector();
	protected Unifier unifier = new Unifier();
	protected SubstVisitor substVisitor = new SubstVisitor();

	//
	// PROTECTED METODS
	//
	protected abstract boolean isValidMatch(Term toMatch,
			Set<Variable> toMatchVariables, Term possibleMatch,
			Map<Variable, Term> substitution);

	protected IdentifyCandidateMatchingTerm getMatchingSubstitution(
			Term toMatch, AtomicSentence expression) {

		IdentifyCandidateMatchingTerm icm = new IdentifyCandidateMatchingTerm(
				toMatch, expression);

		if (icm.isMatch()) {
			return icm;
		}

		// indicates no match
		return null;
	}

	protected class IdentifyCandidateMatchingTerm implements FOLVisitor {
		private Term toMatch = null;
		private Set<Variable> toMatchVariables = null;
		private Term matchingTerm = null;
		private Map<Variable, Term> substitution = null;

		public IdentifyCandidateMatchingTerm(Term toMatch,
				AtomicSentence expression) {
			this.toMatch = toMatch;
			this.toMatchVariables = variableCollector
					.collectAllVariables(toMatch);

			expression.accept(this, null);
		}

		public boolean isMatch() {
			return null != matchingTerm;
		}

		public Term getMatchingTerm() {
			return matchingTerm;
		}

		public Map<Variable, Term> getMatchingSubstitution() {
			return substitution;
		}

		//
		// START-FOLVisitor
		public Object visitPredicate(Predicate p, Object arg) {
			for (Term t : p.getArgs()) {
				// Finish processing if have found a match
				if (null != matchingTerm) {
					break;
				}
				t.accept(this, null);
			}
			return p;
		}

		public Object visitTermEquality(TermEquality equality, Object arg) {
			for (Term t : equality.getArgs()) {
				// Finish processing if have found a match
				if (null != matchingTerm) {
					break;
				}
				t.accept(this, null);
			}
			return equality;
		}

		public Object visitVariable(Variable variable, Object arg) {

			if (null != (substitution = unifier.unify(toMatch, variable))) {
				if (isValidMatch(toMatch, toMatchVariables, variable,
						substitution)) {
					matchingTerm = variable;
				}
			}

			return variable;
		}

		public Object visitConstant(Constant constant, Object arg) {
			if (null != (substitution = unifier.unify(toMatch, constant))) {
				if (isValidMatch(toMatch, toMatchVariables, constant,
						substitution)) {
					matchingTerm = constant;
				}
			}

			return constant;
		}

		public Object visitFunction(Function function, Object arg) {
			if (null != (substitution = unifier.unify(toMatch, function))) {
				if (isValidMatch(toMatch, toMatchVariables, function,
						substitution)) {
					matchingTerm = function;
				}
			}

			if (null == matchingTerm) {
				// Try the Function's arguments
				for (Term t : function.getArgs()) {
					// Finish processing if have found a match
					if (null != matchingTerm) {
						break;
					}
					t.accept(this, null);
				}
			}

			return function;
		}

		public Object visitNotSentence(NotSentence sentence, Object arg) {
			throw new IllegalStateException(
					"visitNotSentence() should not be called.");
		}

		public Object visitConnectedSentence(ConnectedSentence sentence,
				Object arg) {
			throw new IllegalStateException(
					"visitConnectedSentence() should not be called.");
		}

		public Object visitQuantifiedSentence(QuantifiedSentence sentence,
				Object arg) {
			throw new IllegalStateException(
					"visitQuantifiedSentence() should not be called.");
		}

		// END-FOLVisitor
		//
	}

	protected class ReplaceMatchingTerm implements FOLVisitor {
		private Term toReplace = null;
		private Term replaceWith = null;
		private boolean replaced = false;

		public ReplaceMatchingTerm() {
		}

		public AtomicSentence replace(AtomicSentence expression,
				Term toReplace, Term replaceWith) {
			this.toReplace = toReplace;
			this.replaceWith = replaceWith;

			return (AtomicSentence) expression.accept(this, null);
		}

		//
		// START-FOLVisitor
		public Object visitPredicate(Predicate p, Object arg) {
			List<Term> newTerms = new ArrayList<Term>();
			for (Term t : p.getTerms()) {
				Term subsTerm = (Term) t.accept(this, arg);
				newTerms.add(subsTerm);
			}
			return new Predicate(p.getPredicateName(), newTerms);
		}

		public Object visitTermEquality(TermEquality equality, Object arg) {
			Term newTerm1 = (Term) equality.getTerm1().accept(this, arg);
			Term newTerm2 = (Term) equality.getTerm2().accept(this, arg);
			return new TermEquality(newTerm1, newTerm2);
		}

		public Object visitVariable(Variable variable, Object arg) {
			if (!replaced) {
				if (toReplace.equals(variable)) {
					replaced = true;
					return replaceWith;
				}
			}
			return variable;
		}

		public Object visitConstant(Constant constant, Object arg) {
			if (!replaced) {
				if (toReplace.equals(constant)) {
					replaced = true;
					return replaceWith;
				}
			}
			return constant;
		}

		public Object visitFunction(Function function, Object arg) {
			if (!replaced) {
				if (toReplace.equals(function)) {
					replaced = true;
					return replaceWith;
				}
			}

			List<Term> newTerms = new ArrayList<Term>();
			for (Term t : function.getTerms()) {
				Term subsTerm = (Term) t.accept(this, arg);
				newTerms.add(subsTerm);
			}
			return new Function(function.getFunctionName(), newTerms);
		}

		public Object visitNotSentence(NotSentence sentence, Object arg) {
			throw new IllegalStateException(
					"visitNotSentence() should not be called.");
		}

		public Object visitConnectedSentence(ConnectedSentence sentence,
				Object arg) {
			throw new IllegalStateException(
					"visitConnectedSentence() should not be called.");
		}

		public Object visitQuantifiedSentence(QuantifiedSentence sentence,
				Object arg) {
			throw new IllegalStateException(
					"visitQuantifiedSentence() should not be called.");
		}

		// END-FOLVisitor
		//
	}
}
