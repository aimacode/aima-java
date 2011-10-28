package aima.core.logic.fol.inference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.logic.fol.Connectors;
import aima.core.logic.fol.StandardizeApartInPlace;
import aima.core.logic.fol.SubstVisitor;
import aima.core.logic.fol.SubsumptionElimination;
import aima.core.logic.fol.Unifier;
import aima.core.logic.fol.inference.proof.Proof;
import aima.core.logic.fol.inference.proof.ProofFinal;
import aima.core.logic.fol.inference.proof.ProofStepChainCancellation;
import aima.core.logic.fol.inference.proof.ProofStepChainDropped;
import aima.core.logic.fol.inference.proof.ProofStepChainFromClause;
import aima.core.logic.fol.inference.proof.ProofStepChainReduction;
import aima.core.logic.fol.inference.proof.ProofStepGoal;
import aima.core.logic.fol.inference.trace.FOLModelEliminationTracer;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.kb.data.Chain;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.kb.data.ReducedLiteral;
import aima.core.logic.fol.parsing.ast.AtomicSentence;
import aima.core.logic.fol.parsing.ast.ConnectedSentence;
import aima.core.logic.fol.parsing.ast.NotSentence;
import aima.core.logic.fol.parsing.ast.Sentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;

/**
 * Based on lecture notes from:<br>
 * <a
 * href="http://logic.stanford.edu/classes/cs157/2008/lectures/lecture13.pdf">
 * http://logic.stanford.edu/classes/cs157/2008/lectures/lecture13.pdf</a>
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class FOLModelElimination implements InferenceProcedure {

	// Ten seconds is default maximum query time permitted
	private long maxQueryTime = 10 * 1000;
	//
	private FOLModelEliminationTracer tracer = null;
	//
	private Unifier unifier = new Unifier();
	private SubstVisitor substVisitor = new SubstVisitor();

	public FOLModelElimination() {

	}

	public FOLModelElimination(long maxQueryTime) {
		setMaxQueryTime(maxQueryTime);
	}

	public FOLModelElimination(FOLModelEliminationTracer tracer) {
		this.tracer = tracer;
	}

	public FOLModelElimination(FOLModelEliminationTracer tracer,
			long maxQueryTime) {
		this.tracer = tracer;
		setMaxQueryTime(maxQueryTime);
	}

	public long getMaxQueryTime() {
		return maxQueryTime;
	}

	public void setMaxQueryTime(long maxQueryTime) {
		this.maxQueryTime = maxQueryTime;
	}

	//
	// START-InferenceProcedure

	public InferenceResult ask(FOLKnowledgeBase kb, Sentence query) {
		//
		// Get the background knowledge - are assuming this is satisfiable
		// as using Set of Support strategy.
		Set<Clause> bgClauses = new LinkedHashSet<Clause>(kb.getAllClauses());
		bgClauses.removeAll(SubsumptionElimination
				.findSubsumedClauses(bgClauses));
		List<Chain> background = createChainsFromClauses(bgClauses);

		// Collect the information necessary for constructing
		// an answer (supports use of answer literals).
		AnswerHandler ansHandler = new AnswerHandler(kb, query, maxQueryTime);

		IndexedFarParents ifps = new IndexedFarParents(
				ansHandler.getSetOfSupport(), background);

		// Iterative deepening to be used
		for (int maxDepth = 1; maxDepth < Integer.MAX_VALUE; maxDepth++) {
			// Track the depth actually reached
			ansHandler.resetMaxDepthReached();

			if (null != tracer) {
				tracer.reset();
			}

			for (Chain nearParent : ansHandler.getSetOfSupport()) {
				recursiveDLS(maxDepth, 0, nearParent, ifps, ansHandler);
				if (ansHandler.isComplete()) {
					return ansHandler;
				}
			}
			// This means the search tree
			// has bottomed out (i.e. finite).
			// Return what I know based on exploring everything.
			if (ansHandler.getMaxDepthReached() < maxDepth) {
				return ansHandler;
			}
		}

		return ansHandler;
	}

	// END-InferenceProcedure
	//

	//
	// PRIVATE METHODS
	//
	private List<Chain> createChainsFromClauses(Set<Clause> clauses) {
		List<Chain> chains = new ArrayList<Chain>();

		for (Clause c : clauses) {
			Chain chn = new Chain(c.getLiterals());
			chn.setProofStep(new ProofStepChainFromClause(chn, c));
			chains.add(chn);
			chains.addAll(chn.getContrapositives());
		}

		return chains;
	}

	// Recursive Depth Limited Search
	private void recursiveDLS(int maxDepth, int currentDepth, Chain nearParent,
			IndexedFarParents indexedFarParents, AnswerHandler ansHandler) {

		// Keep track of the maximum depth reached.
		ansHandler.updateMaxDepthReached(currentDepth);

		if (currentDepth == maxDepth) {
			return;
		}

		int noCandidateFarParents = indexedFarParents
				.getNumberCandidateFarParents(nearParent);
		if (null != tracer) {
			tracer.increment(currentDepth, noCandidateFarParents);
		}
		indexedFarParents.standardizeApart(nearParent);
		for (int farParentIdx = 0; farParentIdx < noCandidateFarParents; farParentIdx++) {
			// If have a complete answer, don't keep
			// checking candidate far parents
			if (ansHandler.isComplete()) {
				break;
			}

			// Reduction
			Chain nextNearParent = indexedFarParents.attemptReduction(
					nearParent, farParentIdx);

			if (null == nextNearParent) {
				// Unable to remove the head via reduction
				continue;
			}

			// Handle Canceling and Dropping
			boolean cancelled = false;
			boolean dropped = false;
			do {
				cancelled = false;
				Chain nextParent = null;
				while (nextNearParent != (nextParent = tryCancellation(nextNearParent))) {
					nextNearParent = nextParent;
					cancelled = true;
				}

				dropped = false;
				while (nextNearParent != (nextParent = tryDropping(nextNearParent))) {
					nextNearParent = nextParent;
					dropped = true;
				}
			} while (dropped || cancelled);

			// Check if have answer before
			// going to the next level
			if (!ansHandler.isAnswer(nextNearParent)) {
				// Keep track of the current # of
				// far parents that are possible for the next near parent.
				int noNextFarParents = indexedFarParents
						.getNumberFarParents(nextNearParent);
				// Add to indexed far parents
				nextNearParent = indexedFarParents.addToIndex(nextNearParent);

				// Check the next level
				recursiveDLS(maxDepth, currentDepth + 1, nextNearParent,
						indexedFarParents, ansHandler);

				// Reset the number of far parents possible
				// when recursing back up.
				indexedFarParents.resetNumberFarParentsTo(nextNearParent,
						noNextFarParents);
			}
		}
	}

	// Returns c if no cancellation occurred
	private Chain tryCancellation(Chain c) {
		Literal head = c.getHead();
		if (null != head && !(head instanceof ReducedLiteral)) {
			for (Literal l : c.getTail()) {
				if (l instanceof ReducedLiteral) {
					// if they can be resolved
					if (head.isNegativeLiteral() != l.isNegativeLiteral()) {
						Map<Variable, Term> subst = unifier
								.unify(head.getAtomicSentence(),
										l.getAtomicSentence());
						if (null != subst) {
							// I have a cancellation
							// Need to apply subst to all of the
							// literals in the cancellation
							List<Literal> cancLits = new ArrayList<Literal>();
							for (Literal lfc : c.getTail()) {
								AtomicSentence a = (AtomicSentence) substVisitor
										.subst(subst, lfc.getAtomicSentence());
								cancLits.add(lfc.newInstance(a));
							}
							Chain cancellation = new Chain(cancLits);
							cancellation
									.setProofStep(new ProofStepChainCancellation(
											cancellation, c, subst));
							return cancellation;
						}
					}
				}
			}
		}
		return c;
	}

	// Returns c if no dropping occurred
	private Chain tryDropping(Chain c) {
		Literal head = c.getHead();
		if (null != head && (head instanceof ReducedLiteral)) {
			Chain dropped = new Chain(c.getTail());
			dropped.setProofStep(new ProofStepChainDropped(dropped, c));
			return dropped;
		}

		return c;
	}

	class AnswerHandler implements InferenceResult {
		private Chain answerChain = new Chain();
		private Set<Variable> answerLiteralVariables;
		private List<Chain> sos = null;
		private boolean complete = false;
		private long finishTime = 0L;
		private int maxDepthReached = 0;
		private List<Proof> proofs = new ArrayList<Proof>();
		private boolean timedOut = false;

		public AnswerHandler(FOLKnowledgeBase kb, Sentence query,
				long maxQueryTime) {

			finishTime = System.currentTimeMillis() + maxQueryTime;

			Sentence refutationQuery = new NotSentence(query);

			// Want to use an answer literal to pull
			// query variables where necessary
			Literal answerLiteral = kb.createAnswerLiteral(refutationQuery);
			answerLiteralVariables = kb.collectAllVariables(answerLiteral
					.getAtomicSentence());

			// Create the Set of Support based on the Query.
			if (answerLiteralVariables.size() > 0) {
				Sentence refutationQueryWithAnswer = new ConnectedSentence(
						Connectors.OR, refutationQuery, answerLiteral
								.getAtomicSentence().copy());

				sos = createChainsFromClauses(kb
						.convertToClauses(refutationQueryWithAnswer));

				answerChain.addLiteral(answerLiteral);
			} else {
				sos = createChainsFromClauses(kb
						.convertToClauses(refutationQuery));
			}

			for (Chain s : sos) {
				s.setProofStep(new ProofStepGoal(s));
			}
		}

		//
		// START-InferenceResult
		public boolean isPossiblyFalse() {
			return !timedOut && proofs.size() == 0;
		}

		public boolean isTrue() {
			return proofs.size() > 0;
		}

		public boolean isUnknownDueToTimeout() {
			return timedOut && proofs.size() == 0;
		}

		public boolean isPartialResultDueToTimeout() {
			return timedOut && proofs.size() > 0;
		}

		public List<Proof> getProofs() {
			return proofs;
		}

		// END-InferenceResult
		//

		public List<Chain> getSetOfSupport() {
			return sos;
		}

		public boolean isComplete() {
			return complete;
		}

		public void resetMaxDepthReached() {
			maxDepthReached = 0;
		}

		public int getMaxDepthReached() {
			return maxDepthReached;
		}

		public void updateMaxDepthReached(int depth) {
			if (depth > maxDepthReached) {
				maxDepthReached = depth;
			}
		}

		public boolean isAnswer(Chain nearParent) {
			boolean isAns = false;
			if (answerChain.isEmpty()) {
				if (nearParent.isEmpty()) {
					proofs.add(new ProofFinal(nearParent.getProofStep(),
							new HashMap<Variable, Term>()));
					complete = true;
					isAns = true;
				}
			} else {
				if (nearParent.isEmpty()) {
					// This should not happen
					// as added an answer literal to sos, which
					// implies the database (i.e. premises) are
					// unsatisfiable to begin with.
					throw new IllegalStateException(
							"Generated an empty chain while looking for an answer, implies original KB is unsatisfiable");
				}
				if (1 == nearParent.getNumberLiterals()
						&& nearParent
								.getHead()
								.getAtomicSentence()
								.getSymbolicName()
								.equals(answerChain.getHead()
										.getAtomicSentence().getSymbolicName())) {
					Map<Variable, Term> answerBindings = new HashMap<Variable, Term>();
					List<Term> answerTerms = nearParent.getHead()
							.getAtomicSentence().getArgs();
					int idx = 0;
					for (Variable v : answerLiteralVariables) {
						answerBindings.put(v, answerTerms.get(idx));
						idx++;
					}
					boolean addNewAnswer = true;
					for (Proof p : proofs) {
						if (p.getAnswerBindings().equals(answerBindings)) {
							addNewAnswer = false;
							break;
						}
					}
					if (addNewAnswer) {
						proofs.add(new ProofFinal(nearParent.getProofStep(),
								answerBindings));
					}
					isAns = true;
				}
			}

			if (System.currentTimeMillis() > finishTime) {
				complete = true;
				// Indicate that I have run out of query time
				timedOut = true;
			}

			return isAns;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("isComplete=" + complete);
			sb.append("\n");
			sb.append("result=" + proofs);
			return sb.toString();
		}
	}
}

class IndexedFarParents {
	//
	private int saIdx = 0;
	private Unifier unifier = new Unifier();
	private SubstVisitor substVisitor = new SubstVisitor();
	//
	private Map<String, List<Chain>> posHeads = new LinkedHashMap<String, List<Chain>>();
	private Map<String, List<Chain>> negHeads = new LinkedHashMap<String, List<Chain>>();

	public IndexedFarParents(List<Chain> sos, List<Chain> background) {
		constructInternalDataStructures(sos, background);
	}

	public int getNumberFarParents(Chain farParent) {
		Literal head = farParent.getHead();

		Map<String, List<Chain>> heads = null;
		if (head.isPositiveLiteral()) {
			heads = posHeads;
		} else {
			heads = negHeads;
		}
		String headKey = head.getAtomicSentence().getSymbolicName();

		List<Chain> farParents = heads.get(headKey);
		if (null != farParents) {
			return farParents.size();
		}
		return 0;
	}

	public void resetNumberFarParentsTo(Chain farParent, int toSize) {
		Literal head = farParent.getHead();
		Map<String, List<Chain>> heads = null;
		if (head.isPositiveLiteral()) {
			heads = posHeads;
		} else {
			heads = negHeads;
		}
		String key = head.getAtomicSentence().getSymbolicName();
		List<Chain> farParents = heads.get(key);
		while (farParents.size() > toSize) {
			farParents.remove(farParents.size() - 1);
		}
	}

	public int getNumberCandidateFarParents(Chain nearParent) {
		Literal nearestHead = nearParent.getHead();

		Map<String, List<Chain>> candidateHeads = null;
		if (nearestHead.isPositiveLiteral()) {
			candidateHeads = negHeads;
		} else {
			candidateHeads = posHeads;
		}

		String nearestKey = nearestHead.getAtomicSentence().getSymbolicName();

		List<Chain> farParents = candidateHeads.get(nearestKey);
		if (null != farParents) {
			return farParents.size();
		}
		return 0;
	}

	public Chain attemptReduction(Chain nearParent, int farParentIndex) {
		Chain nnpc = null;

		Literal nearLiteral = nearParent.getHead();

		Map<String, List<Chain>> candidateHeads = null;
		if (nearLiteral.isPositiveLiteral()) {
			candidateHeads = negHeads;
		} else {
			candidateHeads = posHeads;
		}

		AtomicSentence nearAtom = nearLiteral.getAtomicSentence();
		String nearestKey = nearAtom.getSymbolicName();
		List<Chain> farParents = candidateHeads.get(nearestKey);
		if (null != farParents) {
			Chain farParent = farParents.get(farParentIndex);
			standardizeApart(farParent);
			Literal farLiteral = farParent.getHead();
			AtomicSentence farAtom = farLiteral.getAtomicSentence();
			Map<Variable, Term> subst = unifier.unify(nearAtom, farAtom);

			// If I was able to unify with one
			// of the far heads
			if (null != subst) {
				// Want to always apply reduction uniformly
				Chain topChain = farParent;
				Literal botLit = nearLiteral;
				Chain botChain = nearParent;

				// Need to apply subst to all of the
				// literals in the reduction
				List<Literal> reduction = new ArrayList<Literal>();
				for (Literal l : topChain.getTail()) {
					AtomicSentence atom = (AtomicSentence) substVisitor.subst(
							subst, l.getAtomicSentence());
					reduction.add(l.newInstance(atom));
				}
				reduction.add(new ReducedLiteral((AtomicSentence) substVisitor
						.subst(subst, botLit.getAtomicSentence()), botLit
						.isNegativeLiteral()));
				for (Literal l : botChain.getTail()) {
					AtomicSentence atom = (AtomicSentence) substVisitor.subst(
							subst, l.getAtomicSentence());
					reduction.add(l.newInstance(atom));
				}

				nnpc = new Chain(reduction);
				nnpc.setProofStep(new ProofStepChainReduction(nnpc, nearParent,
						farParent, subst));
			}
		}

		return nnpc;
	}

	public Chain addToIndex(Chain c) {
		Chain added = null;
		Literal head = c.getHead();
		if (null != head) {
			Map<String, List<Chain>> toAddTo = null;
			if (head.isPositiveLiteral()) {
				toAddTo = posHeads;
			} else {
				toAddTo = negHeads;
			}

			String key = head.getAtomicSentence().getSymbolicName();
			List<Chain> farParents = toAddTo.get(key);
			if (null == farParents) {
				farParents = new ArrayList<Chain>();
				toAddTo.put(key, farParents);
			}

			added = c;
			farParents.add(added);
		}
		return added;
	}

	public void standardizeApart(Chain c) {
		saIdx = StandardizeApartInPlace.standardizeApart(c, saIdx);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("#");
		sb.append(posHeads.size());
		for (String key : posHeads.keySet()) {
			sb.append(",");
			sb.append(posHeads.get(key).size());
		}
		sb.append(" posHeads=");
		sb.append(posHeads.toString());
		sb.append("\n");
		sb.append("#");
		sb.append(negHeads.size());
		for (String key : negHeads.keySet()) {
			sb.append(",");
			sb.append(negHeads.get(key).size());
		}
		sb.append(" negHeads=");
		sb.append(negHeads.toString());

		return sb.toString();
	}

	//
	// PRIVATE METHODS
	//
	private void constructInternalDataStructures(List<Chain> sos,
			List<Chain> background) {
		List<Chain> toIndex = new ArrayList<Chain>();
		toIndex.addAll(sos);
		toIndex.addAll(background);

		for (Chain c : toIndex) {
			addToIndex(c);
		}
	}
}