package aima.core.logic.fol.inference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aima.core.logic.fol.inference.proof.Proof;
import aima.core.logic.fol.inference.proof.ProofFinal;
import aima.core.logic.fol.inference.proof.ProofStepBwChGoal;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.AtomicSentence;
import aima.core.logic.fol.parsing.ast.Sentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 9.6, page
 * 288.<br>
 * <br>
 * 
 * <pre>
 * function FOL-BC-ASK(KB, goals, theta) returns a set of substitutions
 *   input: KB, a knowledge base
 *          goals, a list of conjuncts forming a query (theta already applied)
 *          theta, the current substitution, initially the empty substitution {}
 *   local variables: answers, a set of substitutions, initially empty
 *   
 *   if goals is empty then return {theta}
 *   qDelta &lt;- SUBST(theta, FIRST(goals))
 *   for each sentence r in KB where STANDARDIZE-APART(r) = (p1 &circ; ... &circ; pn =&gt; q)
 *          and thetaDelta &lt;- UNIFY(q, qDelta) succeeds
 *       new_goals &lt;- [p1,...,pn|REST(goals)]
 *       answers &lt;- FOL-BC-ASK(KB, new_goals, COMPOSE(thetaDelta, theta)) U answers
 *   return answers
 * </pre>
 * 
 * Figure 9.6 A simple backward-chaining algorithm.
 * 
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public class FOLBCAsk implements InferenceProcedure {

	public FOLBCAsk() {

	}

	//
	// START-InferenceProcedure
	/**
	 * Returns a set of substitutions
	 * 
	 * @param KB
	 *            a knowledge base
	 * @param query
	 *            goals, a list of conjuncts forming a query
	 * 
	 * @return a set of substitutions
	 */
	public InferenceResult ask(FOLKnowledgeBase KB, Sentence query) {
		// Assertions on the type queries this Inference procedure
		// supports
		if (!(query instanceof AtomicSentence)) {
			throw new IllegalArgumentException(
					"Only Atomic Queries are supported.");
		}

		List<Literal> goals = new ArrayList<Literal>();
		goals.add(new Literal((AtomicSentence) query));

		BCAskAnswerHandler ansHandler = new BCAskAnswerHandler();

		List<List<ProofStepBwChGoal>> allProofSteps = folbcask(KB, ansHandler,
				goals, new HashMap<Variable, Term>());

		ansHandler.setAllProofSteps(allProofSteps);

		return ansHandler;
	}

	// END-InferenceProcedure
	//

	//
	// PRIVATE METHODS
	//

	/**
	 * <code>
	 * function FOL-BC-ASK(KB, goals, theta) returns a set of substitutions
	 *   input: KB, a knowledge base
	 *          goals, a list of conjuncts forming a query (theta already applied)
	 *          theta, the current substitution, initially the empty substitution {}
	 * </code>
	 */
	private List<List<ProofStepBwChGoal>> folbcask(FOLKnowledgeBase KB,
			BCAskAnswerHandler ansHandler, List<Literal> goals,
			Map<Variable, Term> theta) {
		List<List<ProofStepBwChGoal>> thisLevelProofSteps = new ArrayList<List<ProofStepBwChGoal>>();
		// local variables: answers, a set of substitutions, initially empty

		// if goals is empty then return {theta}
		if (goals.isEmpty()) {
			thisLevelProofSteps.add(new ArrayList<ProofStepBwChGoal>());
			return thisLevelProofSteps;
		}

		// qDelta <- SUBST(theta, FIRST(goals))
		Literal qDelta = KB.subst(theta, goals.get(0));

		// for each sentence r in KB where
		// STANDARDIZE-APART(r) = (p1 ^ ... ^ pn => q)
		for (Clause r : KB.getAllDefiniteClauses()) {
			r = KB.standardizeApart(r);
			// and thetaDelta <- UNIFY(q, qDelta) succeeds
			Map<Variable, Term> thetaDelta = KB.unify(r.getPositiveLiterals()
					.get(0).getAtomicSentence(), qDelta.getAtomicSentence());
			if (null != thetaDelta) {
				// new_goals <- [p1,...,pn|REST(goals)]
				List<Literal> newGoals = new ArrayList<Literal>(
						r.getNegativeLiterals());
				newGoals.addAll(goals.subList(1, goals.size()));
				// answers <- FOL-BC-ASK(KB, new_goals, COMPOSE(thetaDelta,
				// theta)) U answers
				Map<Variable, Term> composed = compose(KB, thetaDelta, theta);
				List<List<ProofStepBwChGoal>> lowerLevelProofSteps = folbcask(
						KB, ansHandler, newGoals, composed);

				ansHandler.addProofStep(lowerLevelProofSteps, r, qDelta,
						composed);

				thisLevelProofSteps.addAll(lowerLevelProofSteps);
			}
		}

		// return answers
		return thisLevelProofSteps;
	}

	// Artificial Intelligence A Modern Approach (2nd Edition): page 288.
	// COMPOSE(delta, tau) is the substitution whose effect is identical to
	// the effect of applying each substitution in turn. That is,
	// SUBST(COMPOSE(theta1, theta2), p) = SUBST(theta2, SUBST(theta1, p))
	private Map<Variable, Term> compose(FOLKnowledgeBase KB,
			Map<Variable, Term> theta1, Map<Variable, Term> theta2) {
		Map<Variable, Term> composed = new HashMap<Variable, Term>();

		// So that it behaves like:
		// SUBST(theta2, SUBST(theta1, p))
		// There are two steps involved here.
		// See: http://logic.stanford.edu/classes/cs157/2008/notes/chap09.pdf
		// for a detailed discussion:

		// 1. Apply theta2 to the range of theta1.
		for (Variable v : theta1.keySet()) {
			composed.put(v, KB.subst(theta2, theta1.get(v)));
		}

		// 2. Adjoin to delta all pairs from tau with different
		// domain variables.
		for (Variable v : theta2.keySet()) {
			if (!theta1.containsKey(v)) {
				composed.put(v, theta2.get(v));
			}
		}

		return cascadeSubstitutions(KB, composed);
	}

	// See:
	// http://logic.stanford.edu/classes/cs157/2008/miscellaneous/faq.html#jump165
	// for need for this.
	private Map<Variable, Term> cascadeSubstitutions(FOLKnowledgeBase KB,
			Map<Variable, Term> theta) {
		for (Variable v : theta.keySet()) {
			Term t = theta.get(v);
			theta.put(v, KB.subst(theta, t));
		}

		return theta;
	}

	class BCAskAnswerHandler implements InferenceResult {

		private List<Proof> proofs = new ArrayList<Proof>();

		public BCAskAnswerHandler() {

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

		public void setAllProofSteps(List<List<ProofStepBwChGoal>> allProofSteps) {
			for (List<ProofStepBwChGoal> steps : allProofSteps) {
				ProofStepBwChGoal lastStep = steps.get(steps.size() - 1);
				Map<Variable, Term> theta = lastStep.getBindings();
				proofs.add(new ProofFinal(lastStep, theta));
			}
		}

		public void addProofStep(
				List<List<ProofStepBwChGoal>> currentLevelProofSteps,
				Clause toProve, Literal currentGoal,
				Map<Variable, Term> bindings) {

			if (currentLevelProofSteps.size() > 0) {
				ProofStepBwChGoal predecessor = new ProofStepBwChGoal(toProve,
						currentGoal, bindings);
				for (List<ProofStepBwChGoal> steps : currentLevelProofSteps) {
					if (steps.size() > 0) {
						steps.get(0).setPredecessor(predecessor);
					}
					steps.add(0, predecessor);
				}
			}
		}
	}
}
