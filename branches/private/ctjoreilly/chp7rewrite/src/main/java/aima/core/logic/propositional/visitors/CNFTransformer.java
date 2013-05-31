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
				&& (isAndSentence(bs.get(0)) || isAndSentence(bs.get(1)))) {
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
	private boolean isAndSentence(Sentence s) {
		boolean result = false;
		if (s instanceof ComplexSentence) {
			result = ((ComplexSentence)s).getConnective() == Connective.AND;
		}
		return result;
	}
	
	private boolean isOrSentence(Sentence s) {
		boolean result = false;
		if (s instanceof ComplexSentence) {
			result = ((ComplexSentence)s).getConnective() == Connective.OR;
		}
		return result;
	}
	
	private boolean isUnarySentence(Sentence s) {
		boolean result = false;
		if (s instanceof ComplexSentence) {
			result = ((ComplexSentence)s).isUnary();
		}
		return result;
	}
	
	private boolean isBinarySentence(Sentence s) {
		boolean result = false;
		if (s instanceof ComplexSentence) {
			result = ((ComplexSentence)s).isBinary();
		}
		return result;
	}

	private Sentence step(Sentence s) {
		return (Sentence) s.accept(this, null);
	}

	private Sentence transformBiConditionalSentence(ComplexSentence bs) {
		Sentence first = new ComplexSentence(Connective.IMPLICATION, (Sentence) bs.get(0)
				.accept(this, null), (Sentence) bs.get(1).accept(this,
				null));
		Sentence second = new ComplexSentence(Connective.IMPLICATION, (Sentence) bs.get(1)
				.accept(this, null), (Sentence) bs.get(0)
				.accept(this, null));
		return new ComplexSentence(Connective.AND, first, second);
	}

	private Sentence transformImpliedSentence(ComplexSentence bs) {
		Sentence first = new ComplexSentence(Connective.NOT, (Sentence) bs.get(0).accept(
				this, null));
		return new ComplexSentence(Connective.OR, first, (Sentence) bs.get(1).accept(this, null));
	}

	private Sentence transformNotSentence(ComplexSentence us) {
		if (isUnarySentence(us.get(0))) {
			return (Sentence) ((ComplexSentence) us.get(0)).get(0).accept(this, null);
		} else if (isBinarySentence(us.get(0))) {
			ComplexSentence bs = (ComplexSentence) us.get(0);
			if (isAndSentence(bs)) {
				Sentence first = new ComplexSentence(Connective.NOT, (Sentence) bs.get(0)
						.accept(this, null));
				Sentence second = new ComplexSentence(Connective.NOT, (Sentence) bs.get(1)
						.accept(this, null));
				return new ComplexSentence(Connective.OR, first, second);
			} else if (isOrSentence(bs)) {
				Sentence first = new ComplexSentence(Connective.NOT, (Sentence) bs.get(0)
						.accept(this, null));
				Sentence second = new ComplexSentence(Connective.NOT, (Sentence) bs.get(1)
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
		ComplexSentence andTerm = isAndSentence(bs.get(0)) ? (ComplexSentence) bs
				.get(0) : (ComplexSentence) bs.get(1);
		Sentence otherterm = isAndSentence(bs.get(0)) ? bs.get(1) : bs
				.get(0);
		// (alpha or (beta and gamma) = ((alpha or beta) and (alpha or gamma))
		Sentence alpha = (Sentence) otherterm.accept(this, null);
		Sentence beta = (Sentence) andTerm.get(0).accept(this, null);
		Sentence gamma = (Sentence) andTerm.get(1).accept(this, null);
		Sentence distributed = new ComplexSentence(Connective.AND, new ComplexSentence(
				Connective.OR, alpha, beta), new ComplexSentence(Connective.OR, alpha, gamma));
		return distributed;
	}
}