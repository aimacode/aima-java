/*
 * Created on Sep 20, 2004
 *
 */
package aima.logic.fol;

import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.logic.fol.parsing.AbstractFOLVisitor;
import aima.logic.fol.parsing.FOLParser;
import aima.logic.fol.parsing.ast.QuantifiedSentence;
import aima.logic.fol.parsing.ast.Sentence;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;
import aima.util.Converter;
import aima.util.SetOps;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class SubstVisitor extends AbstractFOLVisitor {

	public SubstVisitor(FOLParser parser) {
		super(parser);
	}

	/**
	 * Note: Refer to Artificial Intelligence A Modern Approach (2nd Edition):
	 * page 273.
	 * 
	 * @param theta
	 *            a substitution.
	 * @param aSentence
	 *            the substitution has been applied to.
	 * @return a new Sentence representing the result of applying the
	 *         substitution theta to aSentence.
	 * 
	 */
	public Sentence subst(Map<Variable, Term> theta, Sentence aSentence) {
		return recreate((Sentence) aSentence.accept(this, theta));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visitVariable(Variable variable, Object arg) {
		Map<Variable, Term> substitution = (Map<Variable, Term>) arg;
		if (substitution.containsKey(variable)) {
			return substitution.get(variable).copy();
		}
		return variable;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visitQuantifiedSentence(QuantifiedSentence sentence,
			Object arg) {

		Map<Variable, Term> substitution = (Map<Variable, Term>) arg;
		
		Sentence quantified = sentence.getQuantified();
		Sentence quantifiedAfterSubs = (Sentence) quantified.accept(this, arg);

		Set<Variable> sentenceVariables = new Converter<Variable>()
				.listToSet(sentence.getVariables());

		Set<Variable> substitutionVariables = substitution.keySet();

		Set<Variable> unmatchedVariables = new SetOps<Variable>().difference(
				sentenceVariables, substitutionVariables);

		if (!(unmatchedVariables.isEmpty())) {
			List<Variable> variables = new Converter<Variable>()
					.setToList(unmatchedVariables);
			QuantifiedSentence sen = new QuantifiedSentence(sentence
					.getQuantifier(), variables, quantifiedAfterSubs);
			return sen;
		} else {
			return recreate(quantifiedAfterSubs);
		}
	}
}