package aima.core.logic.basic.firstorder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aima.core.logic.basic.firstorder.kb.data.Literal;
import aima.core.logic.basic.firstorder.parsing.AbstractFOLVisitor;
import aima.core.logic.basic.firstorder.parsing.ast.AtomicSentence;
import aima.core.logic.basic.firstorder.parsing.ast.Function;
import aima.core.logic.basic.firstorder.parsing.ast.QuantifiedSentence;
import aima.core.logic.basic.firstorder.parsing.ast.Sentence;
import aima.core.logic.basic.firstorder.parsing.ast.Term;
import aima.core.logic.basic.firstorder.parsing.ast.Variable;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Anurag Rai
 */
public class SubstVisitor extends AbstractFOLVisitor {

	public SubstVisitor() {
	}

	/**
	 * Note: Refer to Artificial Intelligence A Modern Approach (4th Edition):
	 * page ???.
	 * 
	 * @param theta
	 *            a substitution.
	 * @param sentence
	 *            the substitution has been applied to.
	 * @return a new Sentence representing the result of applying the
	 *         substitution theta to aSentence.
	 * 
	 */
	public Sentence subst(Map<Variable, Term> theta, Sentence sentence) {
		return (Sentence) sentence.accept(this, theta);
	}

	public Term subst(Map<Variable, Term> theta, Term aTerm) {
		return (Term) aTerm.accept(this, theta);
	}

	public Function subst(Map<Variable, Term> theta, Function function) {
		return (Function) function.accept(this, theta);
	}

	public Literal subst(Map<Variable, Term> theta, Literal literal) {
		return literal.newInstance((AtomicSentence) literal
				.getAtomicSentence().accept(this, theta));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visitVariable(Variable variable, Object arg) {
		Map<Variable, Term> substitution = (Map<Variable, Term>) arg;
		if (substitution.containsKey(variable)) {
			return substitution.get(variable).copy();
		}
		return variable.copy();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visitQuantifiedSentence(QuantifiedSentence sentence,
			Object arg) {

		Map<Variable, Term> substitution = (Map<Variable, Term>) arg;

		Sentence quantified = sentence.getQuantified();
		Sentence quantifiedAfterSubs = (Sentence) quantified.accept(this, arg);

		List<Variable> variables = new ArrayList<Variable>();
		for (Variable v : sentence.getVariables()) {
			Term st = substitution.get(v);
			if (null != st) {
				if (st instanceof Variable) {
					// Only if it is a variable, replace it, otherwise
					// drop it.
					variables.add((Variable) st.copy());
				}
			} else {
				// No substitution for the quantified variable, so
				// keep it.
				variables.add(v.copy());
			}
		}

		// If not variables remaining on the quantifier, then drop it
		if (variables.size() == 0) {
			return quantifiedAfterSubs;
		}

		return new QuantifiedSentence(sentence.getQuantifier(), variables,
				quantifiedAfterSubs);
	}
}
