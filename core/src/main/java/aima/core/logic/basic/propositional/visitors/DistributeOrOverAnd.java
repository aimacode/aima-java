package aima.core.logic.basic.propositional.visitors;

import aima.core.logic.basic.propositional.parsing.AbstractPLVisitor;
import aima.core.logic.basic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.basic.propositional.parsing.ast.Connective;
import aima.core.logic.basic.propositional.parsing.ast.Sentence;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): page ???.<br>
 * <br>
 * Distributivity of | over &:<br>
 * (&alpha; | (&beta; & &gamma;))<br>
 * &equiv;<br>
 * ((&alpha; | &beta;) & (&alpha; | &gamma;))<br>
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class DistributeOrOverAnd extends AbstractPLVisitor<Object> {

	/**
	 * Distribute or (|) over and (&).
	 * 
	 * @param sentence
	 *            a propositional logic sentence. This sentence should already
	 *            have biconditionals, and implications removed and negations
	 *            moved inwards.
	 * @return an equivalent Sentence to the input with or's distributed over
	 *         and's.
	 */
	public static Sentence distribute(Sentence sentence) {
		Sentence result = null;

		DistributeOrOverAnd distributeOrOverAnd = new DistributeOrOverAnd();
		result = sentence.accept(distributeOrOverAnd, null);

		return result;
	}

	@Override
	public Sentence visitBinarySentence(ComplexSentence s, Object arg) {
		Sentence result = null;

		if (s.isOrSentence()) {
			Sentence s1 = s.getSimplerSentence(0).accept(this, arg);
			Sentence s2 = s.getSimplerSentence(1).accept(this, arg);
			if (s1.isAndSentence() || s2.isAndSentence()) {
				Sentence alpha, betaAndGamma;
				if (s2.isAndSentence()) {
					// (&alpha; | (&beta; & &gamma;))
					// Note: even if both are 'and' sentence
					// we will prefer to use s2
					alpha = s1;
					betaAndGamma = s2;
				} else {
					// Note: Need to handle this case too
					// ((&beta; & &gamma;) | &alpha;)
					alpha = s2;
					betaAndGamma = s1;
				}

				Sentence beta = betaAndGamma.getSimplerSentence(0);
				Sentence gamma = betaAndGamma.getSimplerSentence(1);

				if (s2.isAndSentence()) {
					// ((&alpha; | &beta;) & (&alpha; | &gamma;))
					Sentence alphaOrBeta = (new ComplexSentence(Connective.OR,
							alpha, beta)).accept(this, null);
					Sentence alphaOrGamma = (new ComplexSentence(Connective.OR,
							alpha, gamma)).accept(this, null);

					result = new ComplexSentence(Connective.AND, alphaOrBeta,
							alphaOrGamma);
				} else {
					// ((&beta; | &alpha;) & (&gamma; | &alpha;))
					Sentence betaOrAlpha = (new ComplexSentence(Connective.OR,
							beta, alpha)).accept(this, null);
					Sentence gammaOrAlpha = (new ComplexSentence(Connective.OR,
							gamma, alpha)).accept(this, null);

					result = new ComplexSentence(Connective.AND, betaOrAlpha,
							gammaOrAlpha);
				}
			} else {
				result = new ComplexSentence(Connective.OR, s1, s2);
			}
		} else {
			result = super.visitBinarySentence(s, arg);
		}

		return result;
	}
}
