package aima.core.logic.propositional.visitors;

import aima.core.logic.propositional.Connective;
import aima.core.logic.propositional.parsing.AbstractPLVisitor;
import aima.core.logic.propositional.parsing.ast.BinarySentence;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.UnarySentence;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class CNFTransformer extends AbstractPLVisitor {
	@Override
	public Object visitBinarySentence(BinarySentence bs, Object arg) {
		if (bs.isBiconditional()) {
			return transformBiConditionalSentence(bs);
		} else if (bs.isImplication()) {
			return transformImpliedSentence(bs);
		} else if (bs.isOrSentence()
				&& (bs.firstTermIsAndSentence() || bs.secondTermIsAndSentence())) {
			return distributeOrOverAnd(bs);
		} else {
			return super.visitBinarySentence(bs, arg);
		}
	}

	@Override
	public Object visitNotSentence(UnarySentence us, Object arg) {
		return transformNotSentence(us);
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

	private Sentence step(Sentence s) {
		return (Sentence) s.accept(this, null);
	}

	private Sentence transformBiConditionalSentence(BinarySentence bs) {
		Sentence first = new BinarySentence(Connective.IMPLICATION, (Sentence) bs.getFirst()
				.accept(this, null), (Sentence) bs.getSecond().accept(this,
				null));
		Sentence second = new BinarySentence(Connective.IMPLICATION, (Sentence) bs.getSecond()
				.accept(this, null), (Sentence) bs.getFirst()
				.accept(this, null));
		return new BinarySentence(Connective.AND, first, second);
	}

	private Sentence transformImpliedSentence(BinarySentence bs) {
		Sentence first = new UnarySentence(Connective.NOT, (Sentence) bs.getFirst().accept(
				this, null));
		return new BinarySentence(Connective.OR, first, (Sentence) bs.getSecond()
				.accept(this, null));
	}

	private Sentence transformNotSentence(UnarySentence us) {
		if (us.getFirst() instanceof UnarySentence) {
			return (Sentence) ((UnarySentence) us.getFirst()).getFirst()
					.accept(this, null);
		} else if (us.getFirst() instanceof BinarySentence) {
			BinarySentence bs = (BinarySentence) us.getFirst();
			if (bs.isAndSentence()) {
				Sentence first = new UnarySentence(Connective.NOT, (Sentence) bs.getFirst()
						.accept(this, null));
				Sentence second = new UnarySentence(Connective.NOT, (Sentence) bs.getSecond()
						.accept(this, null));
				return new BinarySentence(Connective.OR, first, second);
			} else if (bs.isOrSentence()) {
				Sentence first = new UnarySentence(Connective.NOT, (Sentence) bs.getFirst()
						.accept(this, null));
				Sentence second = new UnarySentence(Connective.NOT, (Sentence) bs.getSecond()
						.accept(this, null));
				return new BinarySentence(Connective.AND, first, second);
			} else {
				return (Sentence) super.visitNotSentence(us, null);
			}
		} else {
			return (Sentence) super.visitNotSentence(us, null);
		}
	}

	private Sentence distributeOrOverAnd(BinarySentence bs) {
		BinarySentence andTerm = bs.firstTermIsAndSentence() ? (BinarySentence) bs
				.getFirst() : (BinarySentence) bs.getSecond();
		Sentence otherterm = bs.firstTermIsAndSentence() ? bs.getSecond() : bs
				.getFirst();
		// (alpha or (beta and gamma) = ((alpha or beta) and (alpha or gamma))
		Sentence alpha = (Sentence) otherterm.accept(this, null);
		Sentence beta = (Sentence) andTerm.getFirst().accept(this, null);
		Sentence gamma = (Sentence) andTerm.getSecond().accept(this, null);
		Sentence distributed = new BinarySentence(Connective.AND, new BinarySentence(
				Connective.OR, alpha, beta), new BinarySentence(Connective.OR, alpha, gamma));
		return distributed;
	}
}