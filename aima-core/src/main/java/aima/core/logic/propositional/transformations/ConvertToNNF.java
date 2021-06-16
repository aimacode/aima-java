package aima.core.logic.propositional.transformations;

import aima.core.logic.propositional.parsing.ast.Sentence;

/**
 * Convert a Sentence into an equivalent Negation Normal Form (NNF) Sentence. A
 * Sentence is in NNF if negation is allowed only over atoms, and conjunction,
 * disjunction, and negation are the only allowed boolean connectives
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * 
 */
public class ConvertToNNF {

	/**
	 * Returns the specified sentence in its logically equivalent negation
	 * normal form.
	 * 
	 * @param s
	 *            a propositional logic sentence
	 * 
	 * @return the input sentence converted to it logically equivalent
	 *         negation normal form.
	 */
	public static Sentence apply(Sentence s) {
		Sentence biconditionalsRemoved = EliminateBiconditionals.apply(s);
		Sentence implicationsRemoved = EliminateImplications.apply(biconditionalsRemoved);
		return MoveNotInwards.apply(implicationsRemoved);
	}
}
