package aima.core.logic.propositional.transformations;

import aima.core.logic.propositional.parsing.AbstractPLVisitor;
import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.Connective;
import aima.core.logic.propositional.parsing.ast.Sentence;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 253.<br>
 * <br>
 * Eliminate =>, replacing &alpha; => &beta;<br>
 * with ~&alpha; | &beta;
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * 
 */
public class EliminateImplications extends AbstractPLVisitor<Object> {

	/**
	 * Eliminate the implications from a sentence.
	 * 
	 * @param sentence
	 *            a propositional logic sentence.
	 * @return an equivalent Sentence to the input with all implications
	 *         eliminated.
	 */
	public static Sentence apply(Sentence sentence) {
		return sentence.accept(new EliminateImplications(), null);
	}

	@Override
	public Sentence visitBinarySentence(ComplexSentence s, Object arg) {
		Sentence result;
		if (s.isImplicationSentence()) {
			// Eliminate =>, replacing &alpha; => &beta;
			// with ~&alpha; | &beta;
			Sentence alpha = s.getSimplerSentence(0).accept(this, arg);
			Sentence beta = s.getSimplerSentence(1).accept(this, arg);
			Sentence notAlpha = new ComplexSentence(Connective.NOT, alpha);
			
			result = new ComplexSentence(Connective.OR, notAlpha, beta);
		} else {
			result = super.visitBinarySentence(s, arg);
		}
		return result;
	}
}
