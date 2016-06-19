package aima.core.logic.basic.propositional.inference;

import java.util.List;
import java.util.Set;

import aima.core.logic.api.propositional.SATSolver;
import aima.core.logic.basic.propositional.kb.data.Clause;
import aima.core.logic.basic.propositional.kb.data.Model;
import aima.core.logic.basic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.basic.propositional.parsing.ast.Sentence;
import aima.core.logic.basic.propositional.visitors.ConvertToConjunctionOfClauses;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): page ???.<br>
 * <br>
 * 
 * <pre>
 * <code>
 * function SATPlan(init, transition, goal, T<sub>max</sub>) returns solution or failure
 *   inputs: init, transition, goal, constitute a description of the problem
 *           T<sub>max</sub>, an upper limit for plan length
 *   
 *   for t = 0 to T<sub>max</sub> do
 *       cnf &larr;  TRANSLATE-TO-SAT(init, transition, goal, t)
 *       model &larr; SAT-SOLVER(cnf)
 *       if model is not null then
 *           return EXTRACT-SOLUTION(model)
 *   return failure
 * </code>
 * </pre>
 * 
 * Figure ?.?? The SATPlan algorithm. The planning problem is translated into a CNF
 * sentence in which the goal is asserted to hold at a fixed time step t and axioms are included
 * for each time step up to t. If the satisfiability algorithm finds a model, then a plan is
 * extracted by looking at those proposition symbols that refer to actions and are assigned true
 * in the model. If no model exists, then the process is repeated with the goal moved one step later. 
 * 
 * @author Ciaran O'Reilly
 * @author Anurag Rai
 */
public class SATPlan<A> {	
	
	/**
	 * function SATPlan(init, transition, goal, T<sub>max</sub>) returns
	 * solution or failure<br>
	 * 
	 * @param init
	 *            provides a collection of assertions about the initial state.
	 * @param transition
	 *            provides the successor-state axioms for all possible actions
	 *            at each time step up to some maximum t.
	 * @param goal
	 *            provides the assertion that the goal is achieved at time t.
	 * @param tMax
	 *            the maximum number of time steps in which the goal is to be
	 *            achieved in.
	 * @return a list of actions describing a solution for the given problem or
	 *         null if no solution is found (i.e failure)
	 */
	public List<A> satPlan(Describe init, Describe transition, Describe goal, int tMax) {
		
		// for t = 0 to T<sub>max</sub> do
		for (int t = 0; t <= tMax; t++) {
			// cnf &larr;  TRANSLATE-TO-SAT(init, transition, goal, t)
			Set<Clause> cnf = translateToSAT(init, transition, goal, t);
			// model &larr; SAT-SOLVER(cnf)
			Model model = satSolver.solve(cnf);
			// if model is not null then
			if (model != null) {
				// return EXTRACT-SOLUTION(model)
				return solutionExtractor.extractSolution(model);
			}
		}
		
		// return failure
		return null;
	}
	
	//
	// SUPPORTING CODE
	/**
	 * Interface to be implemented to describe different aspects of a given problem.
	 *
	 */
	interface Describe {
		Sentence assertions(int t);
	}
	
	/**
	 * Interface to be implemented to extract a solution from a satisfiable model.
	 *
	 */
	interface SolutionExtractor<A> {
		List<A> extractSolution(Model model);
	}
	
	private SATSolver			 satSolver         = null;
	private SolutionExtractor<A> solutionExtractor = null;
	
	public SATPlan(SATSolver satSolver, SolutionExtractor<A> solutionExtractor) {
		this.satSolver         = satSolver;
		this.solutionExtractor = solutionExtractor;
	}
	
	//
	// PROTECTED
	//
	protected Set<Clause> translateToSAT(Describe init, Describe transition, Describe goal, int t) {
		Sentence s = ComplexSentence.newConjunction(init.assertions(t), transition.assertions(t), goal.assertions(t));		
		return ConvertToConjunctionOfClauses.convert(s).getClauses();
	}
}