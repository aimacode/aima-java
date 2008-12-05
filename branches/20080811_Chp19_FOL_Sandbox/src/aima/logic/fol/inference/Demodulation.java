package aima.logic.fol.inference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.logic.fol.inference.proof.ProofStepClauseDemodulation;
import aima.logic.fol.kb.data.Clause;
import aima.logic.fol.kb.data.Literal;
import aima.logic.fol.parsing.ast.AtomicSentence;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.TermEquality;
import aima.logic.fol.parsing.ast.Variable;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): page 304.<br>
 * Demodulation: For any terms x, y, z, where UNIFY(x,z) = theta and m<sub>n</sub>[z] is a
 * literal containing z:<br>
 * <pre>
 *     x=y, m1 OR ... OR m<sub>n</sub>[z]
 *     ----------------------------------
 *     m1 OR ... m<sub>n</sub>[SUBST(theta,y)]
 * </pre>
 * Demodulation is typically used for simplifying expressions using collections of assertions
 * such as x + 0 = x, x<sup>1</sup> = x, and so on.<br>
 * <br>
 * Some additional restrictions/clarifications highlighted in:<br>
 * http://logic.stanford.edu/classes/cs157/2008/lectures/lecture15.pdf<br>
 * 1. Unit Equations Only.<br>
 * 2. Variables substituted in Equation Only.<br>
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class Demodulation extends AbstractModulation {
	public Demodulation() {
	}

	public Clause apply(TermEquality assertion, Clause clExpression) {
		Clause altClExpression = null;

		for (Literal l1 : clExpression.getLiterals()) {
			AtomicSentence altExpression = apply(assertion, l1
					.getAtomicSentence());
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

		IdentifyCandidateMatchingTerm icm = getMatchingSubstitution(assertion
				.getTerm1(),
				expression);

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
