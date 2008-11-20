package aima.logic.fol.inference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.logic.fol.Connectors;
import aima.logic.fol.kb.FOLKnowledgeBase;
import aima.logic.fol.kb.data.Chain;
import aima.logic.fol.kb.data.Clause;
import aima.logic.fol.kb.data.Literal;
import aima.logic.fol.kb.data.ReducedLiteral;
import aima.logic.fol.parsing.ast.ConnectedSentence;
import aima.logic.fol.parsing.ast.NotSentence;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.Sentence;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

/**
 * Based on lecture notes from:
 * http://logic.stanford.edu/classes/cs157/2008/lectures/lecture13.pdf
 * 
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLModelElimination implements InferenceProcedure {

	// Ten seconds is default maximum query time permitted
	private long maxQueryTime = 10 * 1000;
	
	public FOLModelElimination() {

	}
	
	public FOLModelElimination(long maxQueryTime) {
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

	public Set<Map<Variable, Term>> ask(FOLKnowledgeBase kb, Sentence aQuery) {
		//
		// Get the background knowledge - are assuming this is satisfiable
		// as using Set of Support strategy.
		List<Chain> background = createChainsFromClauses(kb.getAllClauses());
		
		// Collect the information necessary for constructing
		// an answer (supports use of answer literals).
		AnswerHandler ansHandler = new AnswerHandler(kb, aQuery, maxQueryTime);

		IndexedFarParents ifps = new IndexedFarParents(kb, ansHandler
				.getSetOfSupport(), background);
		
		// Iterative deepening to be used
		for (int maxDepth = 1; maxDepth < Integer.MAX_VALUE; maxDepth++) {
			// Track the depth actually reached
			ansHandler.resetMaxDepthReached();
			
			for (Chain nearParent : ansHandler.getSetOfSupport()) {
				recursiveDLS(kb, maxDepth, 0, nearParent, ifps, ansHandler);
				if (ansHandler.isComplete()) {
					return ansHandler.getResult();
				}
			}
			// This means the search tree
			// has bottomed out (i.e. finite).
			// Return what I know based on exploring everything.
			if (ansHandler.getMaxDepthReached() < maxDepth) {
				return ansHandler.getResult();
			}
		}		
		
		return ansHandler.getResult();
	}
	// END-InferenceProcedure
	//

	//
	// PRIVATE METHODS
	//
	private List<Chain> createChainsFromClauses(Set<Clause> clauses) {
		List<Chain> chains = new ArrayList<Chain>();

		List<Literal> lits = new ArrayList<Literal>();
		for (Clause c : clauses) {
			lits.clear();
			for (Predicate p : c.getPositiveLiterals()) {
				lits.add(new Literal(p));
			}
			for (Predicate np : c.getNegativeLiterals()) {
				lits.add(new Literal(np, true));
			}
			Chain chn = new Chain(lits);
			chains.add(chn);
			chains.addAll(chn.getContrapositives());
		}

		return chains;
	}

	// Recursive Depth Limited Search
	private void recursiveDLS(FOLKnowledgeBase kb, int maxDepth,
			int currentDepth,
			Chain nearParent, IndexedFarParents indexedFarParents, AnswerHandler ansHandler) {
		
		// Keep track of the maximum depth reached.
		ansHandler.updateMaxDepthReached(currentDepth);
		
		if (currentDepth == maxDepth) {
			return;
		}
		
		int noCandidateFarParents = indexedFarParents.getNumberCandidateFarParents(nearParent);
		for (int farParentIdx = 0; farParentIdx < noCandidateFarParents; farParentIdx++) {
			// If have a complete answer, don't keep
			// checking candidate far parents
			if (ansHandler.isComplete()) {
				break;
			}
			
			// Reduction
			Chain nextNearParent = indexedFarParents.attemptReduction(kb, nearParent,
					farParentIdx);
			
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
				while (nextNearParent != (nextParent = tryCancellation(kb,
						nextNearParent))) {
					nextNearParent = nextParent;
					cancelled = true;
				}

				dropped = false;
				while (nextNearParent != (nextParent = tryDropping(kb,
						nextNearParent))) {
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
				indexedFarParents.addToIndex(kb, nextNearParent);
				
				// Check the next level
				recursiveDLS(kb, maxDepth, currentDepth + 1, nextNearParent,
						indexedFarParents,
						ansHandler);
				
				// Reset the number of far parents possible
				// when recursing back up.
				indexedFarParents.resetNumberFarParentsTo(nextNearParent,
						noNextFarParents);
			}
		}
	}

	// Returns c if no cancellation occurred
	private Chain tryCancellation(FOLKnowledgeBase kb, Chain c) {
		Literal head = c.getHead();
		if (null != head && !(head instanceof ReducedLiteral)) {
			for (Literal l : c.getTail()) {
				if (l instanceof ReducedLiteral) {
					// if they can be resolved
					if (head.isNegativeLiteral() != l.isNegativeLiteral()) {
						Map<Variable, Term> subst = kb.unify(head
								.getPredicate(), l.getPredicate());
						if (null != subst) {
							// I have a cancellation
							// Need to apply subst to all of the
							// literals in the cancellation
							List<Literal> cancLits = new ArrayList<Literal>();
							for (Literal lfc : c.getTail()) {
								Predicate p = (Predicate) kb.subst(subst, lfc
										.getPredicate());
								cancLits.add(lfc.newInstance(p));
							}
							return new Chain(cancLits);
						}
					}
				}
			}
		}
		return c;
	}

	// Returns c if no dropping occurred
	private Chain tryDropping(FOLKnowledgeBase kb, Chain c) {
		Literal head = c.getHead();
		if (null != head && (head instanceof ReducedLiteral)) {
			return new Chain(c.getTail());
		}

		return c;
	}
	
	class AnswerHandler {
		private Set<Map<Variable, Term>> result = new LinkedHashSet<Map<Variable, Term>>();
		private Chain answerChain = new Chain();
		private Set<Variable> answerLiteralVariables;
		private List<Chain> sos = null;
		private boolean complete = false;
		private long finishTime = 0L;
		private int maxDepthReached = 0;

		public AnswerHandler(FOLKnowledgeBase kb, Sentence aQuery,
				long maxQueryTime) {
			
			finishTime = System.currentTimeMillis() + maxQueryTime;

			Sentence refutationQuery = new NotSentence(aQuery);

			// Want to use an answer literal to pull
			// query variables where necessary
			Literal answerLiteral = new Literal(kb
					.createAnswerLiteral(refutationQuery));
			answerLiteralVariables = kb.collectAllVariables(answerLiteral
					.getPredicate());

			// Create the Set of Support based on the Query.
			if (answerLiteralVariables.size() > 0) {
				Sentence refutationQueryWithAnswer = new ConnectedSentence(
						Connectors.OR, refutationQuery, answerLiteral
								.getPredicate());

				sos = createChainsFromClauses(kb
						.convertToClauses(refutationQueryWithAnswer));

				answerChain.addLiteral(answerLiteral);
			} else {
				sos = createChainsFromClauses(kb
						.convertToClauses(refutationQuery));
			}
		}

		public List<Chain> getSetOfSupport() {
			return sos;
		}

		public boolean isComplete() {
			return complete;
		}

		public Set<Map<Variable, Term>> getResult() {
			return result;
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
					result.add(new HashMap<Variable, Term>());
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
						&& nearParent.getHead().getPredicate()
								.getPredicateName().equals(
										answerChain.getHead().getPredicate()
												.getPredicateName())) {
					Map<Variable, Term> answerBindings = new HashMap<Variable, Term>();
					Predicate ans = nearParent.getHead().getPredicate();
					List<Term> answerTerms = ans.getTerms();
					int idx = 0;
					for (Variable v : answerLiteralVariables) {
						answerBindings.put(v, answerTerms.get(idx));
						idx++;
					}
					result.add(answerBindings);
					isAns = true;
				}
			}
			
			if (System.currentTimeMillis() > finishTime) {
				complete = true;
				// If have run out of query time and no result
				// found yet (i.e. partial results via answer literal
				// bindings are allowed.)
				// return null to indicate answer unknown.
				if (0 == result.size()) {
					result = null;
				}
			}
			
			return isAns;
		}
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("isComplete=" + complete);
			sb.append("\n");
			sb.append("result=" + result);
			return sb.toString();
		}
	}
}

class IndexedFarParents {
	private Map<String, List<Chain>> posHeads = new LinkedHashMap<String, List<Chain>>();
	private Map<String, List<Chain>> negHeads = new LinkedHashMap<String, List<Chain>>();

	public IndexedFarParents(FOLKnowledgeBase kb, List<Chain> sos,
			List<Chain> background) {
		constructInternalDataStructures(kb, sos, background);
	}
	
	public int getNumberFarParents(Chain farParent) {
		Literal head = farParent.getHead();

		Map<String, List<Chain>> heads = null;
		if (head.isPositiveLiteral()) {
			heads = posHeads;
		} else {
			heads = negHeads;
		}
		String headKey = head.getPredicate().getPredicateName();

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
		String key = head.getPredicate().getPredicateName();
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

		String nearestKey = nearestHead.getPredicate().getPredicateName();

		List<Chain> farParents = candidateHeads.get(nearestKey);
		if (null != farParents) {
			return farParents.size();
		}
		return 0;
	}
	
	public Chain attemptReduction(FOLKnowledgeBase kb, Chain nearParent,
			int farParentIndex) {
		Chain nnpc = null;

		Literal nearLiteral = nearParent.getHead();
		
		Map<String, List<Chain>> candidateHeads = null;
		if (nearLiteral.isPositiveLiteral()) {
			candidateHeads = negHeads;
		} else {
			candidateHeads = posHeads;
		}

		Predicate nearPredicate = nearLiteral.getPredicate();
		String nearestKey = nearPredicate.getPredicateName();
		List<Chain> farParents = candidateHeads.get(nearestKey);
		if (null != farParents) {
			Chain farParent = farParents.get(farParentIndex);
			Literal farLiteral = farParent.getHead();
			Predicate farPredicate = farLiteral.getPredicate();
			Map<Variable, Term> subst = kb.unify(nearPredicate, farPredicate);
			
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
					Predicate p = (Predicate) kb.subst(subst, l.getPredicate());
					reduction.add(l.newInstance(p));
				}
				reduction.add(new ReducedLiteral((Predicate) kb.subst(subst,
						botLit.getPredicate()), botLit.isNegativeLiteral()));
				for (Literal l : botChain.getTail()) {
					Predicate p = (Predicate) kb.subst(subst, l.getPredicate());
					reduction.add(l.newInstance(p));
				}
				
				nnpc = new Chain(reduction);
			}
		}
		
		return nnpc;
	}
	
	public void addToIndex(FOLKnowledgeBase kb, Chain c) {
		Literal head = c.getHead();
		if (null != head) {
			Map<String, List<Chain>> toAddTo = null;
			if (head.isPositiveLiteral()) {
				toAddTo = posHeads;
			} else {
				toAddTo = negHeads;
			}
	
			String key = head.getPredicate().getPredicateName();
			List<Chain> farParents = toAddTo.get(key);
			if (null == farParents) {
				farParents = new ArrayList<Chain>();
				toAddTo.put(key, farParents);
			}
			// Ensure is standardized apart when added.
			farParents.add(kb.standardizeApart(c));
		}
	}
	
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
	private void constructInternalDataStructures(FOLKnowledgeBase kb,
			List<Chain> sos,
			List<Chain> background) {
		List<Chain> toIndex = new ArrayList<Chain>();
		toIndex.addAll(sos);
		toIndex.addAll(background);

		for (Chain c : toIndex) {
			addToIndex(kb, c);
		}
	}
}