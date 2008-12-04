package aima.logic.fol.inference;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.logic.fol.kb.data.Clause;
import aima.logic.fol.kb.data.Literal;
import aima.logic.fol.parsing.ast.AtomicSentence;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.TermEquality;
import aima.logic.fol.parsing.ast.Variable;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): page 304.<br>
 * Paramodulation: For any terms x, y, z, where UNIFY(x,z) = theta,<br>
 * <pre>
 *     l1 OR ... l<sub>k</sub> OR x=y,     m1 OR ... OR m<sub>n</sub>[z]
 *     -----------------------------------------------------------------
 *     SUBST(theta, l1 OR ... l<sub>k</sub> OR m1 OR ... OR m<sub>n</sub>[y])
 * </pre>
 * Unlike demodulation, paramodulation yields a complete inference procedure for first-order
 * logic with equality.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class Paramodulation extends AbstractModulation {
	public Paramodulation() {
	}

	public Set<Clause> apply(Clause c1, Clause c2) {
		Set<Clause> paraExpressions = new LinkedHashSet<Clause>();

		for (int i = 0; i < 2; i++) {
			Clause topClause, equalityClause;
			if (i == 0) {
				topClause = c1;
				equalityClause = c2;
			} else {
				topClause = c2;
				equalityClause = c1;
			}

			for (Literal possEqLit : equalityClause.getLiterals()) {
				// Must be a positive term equality to be used
				// for paramodulation.
				if (possEqLit.isPositiveLiteral()
						&& possEqLit.getAtomicSentence() instanceof TermEquality) {
					TermEquality assertion = (TermEquality) possEqLit
							.getAtomicSentence();

					// Test matching for both sides of the equality
					for (int x = 0; x < 2; x++) {
						Term toMatch, toReplaceWith;
						if (x == 0) {
							toMatch = assertion.getTerm1();
							toReplaceWith = assertion.getTerm2();
						} else {
							toMatch = assertion.getTerm2();
							toReplaceWith = assertion.getTerm1();
						}

						for (Literal l1 : topClause.getLiterals()) {
							IdentifyCandidateMatchingTerm icm = getMatchingSubstitution(
									toMatch, l1.getAtomicSentence());

							if (null != icm) {
								Term replaceWith = substVisitor.subst(icm
										.getMatchingSubstitution(),
										toReplaceWith);

								// Want to ignore reflexivity axiom situation,
								// i.e. x = x
								if (icm.getMatchingTerm().equals(replaceWith)) {
									continue;
								}

								ReplaceMatchingTerm rmt = new ReplaceMatchingTerm();

								AtomicSentence altExpression = rmt.replace(l1
										.getAtomicSentence(), icm
										.getMatchingTerm(), replaceWith);

								// I have an alternative, create a new clause
								// with the alternative and the substitution
								// applied to all the literals before returning
								List<Literal> newLits = new ArrayList<Literal>();
								for (Literal l2 : topClause.getLiterals()) {
									if (l1.equals(l2)) {
										newLits
												.add(l1
														.newInstance((AtomicSentence) substVisitor
																.subst(
																		icm
																				.getMatchingSubstitution(),
																		altExpression)));
									} else {
										newLits
												.add(substVisitor
														.subst(
																icm
																		.getMatchingSubstitution(),
																l2));
									}
								}
								// Assign the equality clause literals,
								// excluding
								// the term equality used.
								for (Literal l2 : equalityClause.getLiterals()) {
									if (possEqLit.equals(l2)) {
										continue;
									}
									newLits.add(substVisitor.subst(icm
											.getMatchingSubstitution(), l2));
								}

								// Only apply paramodulation at most once
								// for each term equality.
								paraExpressions.add(new Clause(newLits));
								break;
							}
						}
					}
				}
			}
		}

		return paraExpressions;
	}

	//
	// PROTECTED METHODS
	//
	protected boolean isValidMatch(Term toMatch,
			Set<Variable> toMatchVariables, Term possibleMatch,
			Map<Variable, Term> substitution) {

		if (possibleMatch != null && substitution != null) {
			// Note:
			// [Brand 1975] showed that paramodulation into
			// variables is unnecessary.
			if (!(possibleMatch instanceof Variable)) {
				// TODO: Find out whether the following statement from:
				// http://www.cs.miami.edu/~geoff/Courses/CSC648-07F/Content/Paramodulation.shtml
				// is actually the case, as it was not positive but
				// intuitively makes sense:
				// "Similarly, depending on how paramodulation is used, it is
				// often unnecessary to paramodulate from variables."
				// if (!(toMatch instanceof Variable)) {
				return true;
				// }
			}
		}
		return false;
	}
}
