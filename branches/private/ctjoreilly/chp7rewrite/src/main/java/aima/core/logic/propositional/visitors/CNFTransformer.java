package aima.core.logic.propositional.visitors;

import aima.core.logic.propositional.parsing.AbstractPLVisitor;
import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.Connective;
import aima.core.logic.propositional.parsing.ast.Sentence;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class CNFTransformer extends AbstractPLVisitor {
	
	@Override
	public Object visitUnarySentence(ComplexSentence us, Object arg) {
		return transformNotSentence(us);
	}
	
	@Override
	public Object visitBinarySentence(ComplexSentence bs, Object arg) {
		if (bs.getConnective() == Connective.BICONDITIONAL) {
			return transformBiConditionalSentence(bs);
		} else if (bs.getConnective() == Connective.IMPLICATION) {
			return transformImpliedSentence(bs);
		} else if (bs.getConnective() == Connective.OR
				&& (bs.getSimplerSentence(0).isAndSentence() || bs.getSimplerSentence(1).isAndSentence())) {
			return distributeOrOverAnd(bs);
		} else {
			return super.visitBinarySentence(bs, arg);
		}
	}

	/**
	 * Returns the specified sentence in conjunctive normal form.
	 * 
	 * @param s
	 *            a sentence of propositional logic
	 * 
	 * @return the specified sentence in conjunctive normal form.
	 */
	public Sentence transform(Sentence s) {
		Sentence toTransform = s;
		while (!(toTransform.equals(step(toTransform)))) {
			toTransform = step(toTransform);
		}

		return toTransform;
	}
	
	//
	// PRIVATE
	//
	private Sentence step(Sentence s) {
		return (Sentence) s.accept(this, null);
	}

	private Sentence transformBiConditionalSentence(ComplexSentence bs) {
		Sentence first = new ComplexSentence(Connective.IMPLICATION, (Sentence) bs.getSimplerSentence(0)
				.accept(this, null), (Sentence) bs.getSimplerSentence(1).accept(this,
				null));
		Sentence second = new ComplexSentence(Connective.IMPLICATION, (Sentence) bs.getSimplerSentence(1)
				.accept(this, null), (Sentence) bs.getSimplerSentence(0)
				.accept(this, null));
		return new ComplexSentence(Connective.AND, first, second);
	}

	private Sentence transformImpliedSentence(ComplexSentence bs) {
		Sentence first = new ComplexSentence(Connective.NOT, (Sentence) bs.getSimplerSentence(0).accept(
				this, null));
		return new ComplexSentence(Connective.OR, first, (Sentence) bs.getSimplerSentence(1).accept(this, null));
	}

	private Sentence transformNotSentence(ComplexSentence us) {
		if (us.getSimplerSentence(0).isUnarySentence()) {
			return (Sentence) ((ComplexSentence) us.getSimplerSentence(0)).getSimplerSentence(0).accept(this, null);
		} else if (us.getSimplerSentence(0).isBinarySentence()) {
			ComplexSentence bs = (ComplexSentence) us.getSimplerSentence(0);
			if (bs.isAndSentence()) {
				Sentence first = new ComplexSentence(Connective.NOT, (Sentence) bs.getSimplerSentence(0)
						.accept(this, null));
				Sentence second = new ComplexSentence(Connective.NOT, (Sentence) bs.getSimplerSentence(1)
						.accept(this, null));
				return new ComplexSentence(Connective.OR, first, second);
			} else if (bs.isOrSentence()) {
				Sentence first = new ComplexSentence(Connective.NOT, (Sentence) bs.getSimplerSentence(0)
						.accept(this, null));
				Sentence second = new ComplexSentence(Connective.NOT, (Sentence) bs.getSimplerSentence(1)
						.accept(this, null));
				return new ComplexSentence(Connective.AND, first, second);
			} else {
				return (Sentence) super.visitUnarySentence(us, null);
			}
		} else {
			return (Sentence) super.visitUnarySentence(us, null);
		}
	}

	private Sentence distributeOrOverAnd(ComplexSentence bs) {
		ComplexSentence andTerm = bs.getSimplerSentence(0).isAndSentence() ? (ComplexSentence) bs
				.getSimplerSentence(0) : (ComplexSentence) bs.getSimplerSentence(1);
		Sentence otherterm = bs.getSimplerSentence(0).isAndSentence() ? bs.getSimplerSentence(1) : bs
				.getSimplerSentence(0);
		// (alpha or (beta and gamma) = ((alpha or beta) and (alpha or gamma))
		Sentence alpha = (Sentence) otherterm.accept(this, null);
		Sentence beta = (Sentence) andTerm.getSimplerSentence(0).accept(this, null);
		Sentence gamma = (Sentence) andTerm.getSimplerSentence(1).accept(this, null);
		Sentence distributed = new ComplexSentence(Connective.AND, new ComplexSentence(
				Connective.OR, alpha, beta), new ComplexSentence(Connective.OR, alpha, gamma));
		return distributed;
	}
}