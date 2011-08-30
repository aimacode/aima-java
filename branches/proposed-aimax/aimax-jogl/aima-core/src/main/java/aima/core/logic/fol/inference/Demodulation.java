package aima.core.logic.fol.inference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.logic.fol.inference.proof.ProofStepClauseDemodulation;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.AtomicSentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.TermEquality;
import aima.core.logic.fol.parsing.ast.Variable;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 354.<br>
 * <br>
 * Demodulation: For any terms x, y, and z, where z appears somewhere in literal
 * m<sub>i</sub> and where UNIFY(x,z) = &theta;:<br>
 * 
 * <pre>
 *                 x=y,    m<sub>1</sub> OR ... OR m<sub>n</sub>[z]
 *     ------------------------------------------------------------
 *     SUB(SUBST(&theta;,x), SUBST(&theta;,y), m<sub>1</sub> OR ... OR m<sub>n</sub>)
 * </pre>
 * 
 * where SUBST is the usual substitution of a binding list, and SUB(x,y,m) means
 * to replace x with y everywhere that x occurs within m.<br>
 * <br>
 * Some additional restrictions/clarifications highlighted in:<br>
 * <a href="http://logic.stanford.edu/classes/cs157/2008/lectures/lecture15.pdf"
 * >Demodulation Restrictions</a> <br>
 * 1. Unit Equations Only.<br>
 * 2. Variables substituted in Equation Only.<br>
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class Demodulation extends AbstractModulation {
	public Demodulation() {
	}

	public Clause apply(TermEquality assertion, Clause clExpression) {
		Clause altClExpression = null;

		for (Literal l1 : clExpression.getLiterals()) {
			AtomicSentence altExpression = apply(assertion,
					l1.getAtomicSentence());
			if (null != altExpression) {
				// I have an alternative, create a new clause
				// with the alternative and return
				List<Literal> newLits = new ArrayList<Literal>();
				for (Literal l2 : clExpression.getLiterals()) {
					if (l1.equals(l2)) {
						newLits.add(l1.newInstance(altExpression));
					} else {
						newLits.add(l2);
					}
				}
				// Only apply demodulation at most once on
				// each call.
				altClExpression = new Clause(newLits);
				altClExpression.setProofStep(new ProofStepClauseDemodulation(
						altClExpression, clExpression, assertion));
				if (clExpression.isImmutable()) {
					altClExpression.setImmutable();
				}
				if (!clExpression.isStandardizedApartCheckRequired()) {
					altClExpression.setStandardizedApartCheckNotRequired();
				}
				break;
			}
		}

		return altClExpression;
	}

	public AtomicSentence apply(TermEquality assertion,
			AtomicSentence expression) {
		AtomicSentence altExpression = null;

		IdentifyCandidateMatchingTerm icm = getMatchingSubstitution(
				assertion.getTerm1(), expression);

		if (null != icm) {
			Term replaceWith = substVisitor.subst(
					icm.getMatchingSubstitution(), assertion.getTerm2());
			// Want to ignore reflexivity axiom situation, i.e. x = x
			if (!icm.getMatchingTerm().equals(replaceWith)) {
				ReplaceMatchingTerm rmt = new ReplaceMatchingTerm();

				// Only apply demodulation at most once on each call.
				altExpression = rmt.replace(expression, icm.getMatchingTerm(),
						replaceWith);
			}
		}

		return altExpression;
	}

	//
	// PROTECTED METHODS
	//
	@Override
	protected boolean isValidMatch(Term toMatch,
			Set<Variable> toMatchVariables, Term possibleMatch,
			Map<Variable, Term> substitution) {
		// Demodulation only allows substitution in the equation only,
		// if the substitution contains variables not in the toMatch
		// side of the equation (i.e. left hand side), then
		// it is not a legal demodulation match.
		// Note: see:
		// http://logic.stanford.edu/classes/cs157/2008/lectures/lecture15.pdf
		// slide 23 for an example.
		if (toMatchVariables.containsAll(substitution.keySet())) {
			return true;
		}

		return false;
	}
}
