package aima.core.logic.fol.inference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.logic.fol.inference.proof.Proof;
import aima.core.logic.fol.inference.proof.ProofFinal;
import aima.core.logic.fol.inference.proof.ProofStep;
import aima.core.logic.fol.inference.proof.ProofStepFoChAlreadyAFact;
import aima.core.logic.fol.inference.proof.ProofStepFoChAssertFact;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.AtomicSentence;
import aima.core.logic.fol.parsing.ast.NotSentence;
import aima.core.logic.fol.parsing.ast.Sentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 9.3, page 332.
 * 
 * <pre>
 * function FOL-FC-ASK(KB, alpha) returns a substitution or false
 *   inputs: KB, the knowledge base, a set of first order definite clauses
 *           alpha, the query, an atomic sentence
 *   local variables: new, the new sentences inferred on each iteration
 *   
 *   repeat until new is empty
 *      new <- {}
 *      for each rule in KB do
 *          (p1 ^ ... ^ pn => q) <- STANDARDIZE-VARAIBLES(rule)
 *          for each theta such that SUBST(theta, p1 ^ ... ^ pn) = SUBST(theta, p'1 ^ ... ^ p'n)
 *                         for some p'1,...,p'n in KB
 *              q' <- SUBST(theta, q)
 *              if q' does not unify with some sentence already in KB or new then
 *                   add q' to new
 *                   theta <- UNIFY(q', alpha)
 *                   if theta is not fail then return theta
 *      add new to KB
 *   return false
 * </pre>
 * 
 * Figure 9.3 A conceptually straightforward, but very inefficient forward-chaining algo-
 * rithm. On each iteration, it adds to KB all the atomic sentences that can be inferred in one
 * step from the implication sentences and the atomic sentences already in KB. The function
 * STANDARDIZE-VARIABLES replaces all variables in its arguments with new ones that have
 * not been used before.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLFCAsk implements InferenceProcedure {

	public FOLFCAsk() {
	}

	//
	// START-InferenceProcedure

	/**
	 * <code>
	 * function FOL-FC-ASK(KB, alpha) returns a substitution or false
	 *   inputs: KB, the knowledge base, a set of first order definite clauses
	 *           alpha, the query, an atomic sentence
	 * </code>
	 */
	public InferenceResult ask(FOLKnowledgeBase KB, Sentence query) {
		// Assertions on the type of queries this Inference procedure
		// supports
		if (!(query instanceof AtomicSentence)) {
			throw new IllegalArgumentException(
					"Only Atomic Queries are supported.");
		}

		FCAskAnswerHandler ansHandler = new FCAskAnswerHandler();

		Literal alpha = new Literal((AtomicSentence) query);

		// local variables: new, the new sentences inferred on each iteration
		List<Literal> newSentences = new ArrayList<Literal>();

		// Ensure query is not already a know fact before
		// attempting forward chaining.
		Set<Map<Variable, Term>> answers = KB.fetch(alpha);
		if (answers.size() > 0) {
			ansHandler.addProofStep(new ProofStepFoChAlreadyAFact(alpha));
			ansHandler.setAnswers(answers);
			return ansHandler;
		}

		// repeat until new is empty
		do {

			// new <- {}
			newSentences.clear();
			// for each rule in KB do
			// (p1 ^ ... ^ pn => q) <-STANDARDIZE-VARIABLES(rule)
			for (Clause impl : KB.getAllDefiniteClauseImplications()) {
				impl = KB.standardizeApart(impl);
				// for each theta such that SUBST(theta, p1 ^ ... ^ pn) =
				// SUBST(theta, p'1 ^ ... ^ p'n)
				// --- for some p'1,...,p'n in KB
				for (Map<Variable, Term> theta : KB.fetch(invert(impl
						.getNegativeLiterals()))) {
					// q' <- SUBST(theta, q)
					Literal qPrime = KB.subst(theta, impl.getPositiveLiterals()
							.get(0));
					// if q' does not unify with some sentence already in KB or
					// new then do
					if (!KB.isRenaming(qPrime)
							&& !KB.isRenaming(qPrime, newSentences)) {
						// add q' to new
						newSentences.add(qPrime);
						ansHandler.addProofStep(impl, qPrime, theta);
						// theta <- UNIFY(q', alpha)
						theta = KB.unify(qPrime.getAtomicSentence(), alpha
								.getAtomicSentence());
						// if theta is not fail then return theta
						if (null != theta) {
							for (Literal l : newSentences) {
								Sentence s = null;
								if (l.isPositiveLiteral()) {
									s = l.getAtomicSentence();
								} else {
									s = new NotSentence(l.getAtomicSentence());
								}
								KB.tell(s);
							}
							ansHandler.setAnswers(KB.fetch(alpha));
							return ansHandler;
						}
					}
				}
			}
			// add new to KB
			for (Literal l : newSentences) {
				Sentence s = null;
				if (l.isPositiveLiteral()) {
					s = l.getAtomicSentence();
				} else {
					s = new NotSentence(l.getAtomicSentence());
				}
				KB.tell(s);
			}
		} while (newSentences.size() > 0);

		// return false
		return ansHandler;
	}

	// END-InferenceProcedure
	//

	//
	// PRIVATE METHODS
	//
	private List<Literal> invert(List<Literal> lits) {
		List<Literal> invLits = new ArrayList<Literal>();
		for (Literal l : lits) {
			invLits.add(new Literal(l.getAtomicSentence(), (l
					.isPositiveLiteral() ? true : false)));
		}
		return invLits;
	}

	class FCAskAnswerHandler implements InferenceResult {

		private ProofStep stepFinal = null;
		private List<Proof> proofs = new ArrayList<Proof>();

		public FCAskAnswerHandler() {

		}

		//
		// START-InferenceResult
		public boolean isPossiblyFalse() {
			return proofs.size() == 0;
		}

		public boolean isTrue() {
			return proofs.size() > 0;
		}

		public boolean isUnknownDueToTimeout() {
			return false;
		}

		public boolean isPartialResultDueToTimeout() {
			return false;
		}

		public List<Proof> getProofs() {
			return proofs;
		}

		// END-InferenceResult
		//

		public void addProofStep(Clause implication, Literal fact,
				Map<Variable, Term> bindings) {
			stepFinal = new ProofStepFoChAssertFact(implication, fact,
					bindings, stepFinal);
		}

		public void addProofStep(ProofStep step) {
			stepFinal = step;
		}

		public void setAnswers(Set<Map<Variable, Term>> answers) {
			for (Map<Variable, Term> ans : answers) {
				proofs.add(new ProofFinal(stepFinal, ans));
			}
		}
	}
}
