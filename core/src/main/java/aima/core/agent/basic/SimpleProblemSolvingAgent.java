package aima.core.agent.basic;

import aima.core.agent.api.Agent;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchController;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <br>
 *
 * <pre>
 * function SIMPLE-PROBLEM-SOLVING-AGENT(percept) returns an action
 *   persistent: seq, an action sequence, initially empty
 *               state, some description of the current world state
 *               goal, a goal, initially null
 *               problem, a problem formulation
 *
 *   state &larr; UPDATE-STATE(state, percept)
 *   if seq is empty then
 *       goal    &larr; FORMULATE-GOAL(state)
 *       problem &larr; FORMULATE-PROBLEM(state, goal)
 *       seq     &larr; SEARCH(problem)
 *       if seq = failure then return a null action
 *   action &larr; FIRST(seq)
 *   seq &larr; REST(seq)
 *   return action
 * </pre>
 *
 * Figure ?? A simple problem-solving agent. It first formulates a goal and a
 * problem, searches for a sequence of actions that would solve the problem, and
 * then executes the actions one at a time. When this is complete, it formulates
 * another goal and starts over.<br>
 *
 * @param <S>
 *            the type of internal state representation used by the agent.
 * @param <G>
 *            the type of the goal(s) the agent wishes to reach. In simple cases
 *            this will be the same type as S.
 *
 * @author Ciaran O'Reilly
 */
public class SimpleProblemSolvingAgent<A, P, S, G> implements Agent<A, P> {
	// persistent: 
	private List<A> seq = new ArrayList<>(); // seq, an action sequence, initially empty
	private S state; //some description of the current world state
	private G goal; // a goal, initially null
	private Problem<A, S> problem; // a problem formulation

	// function SIMPLE-PROBLEM-SOLVING-AGENT(percept) returns an action
	@Override
	public A perceive(P percept) {
		// state <- UPDATE-STATE(state, percept)
		setState(updateState(getState(), percept));
		// if seq is empty then
		if (getSeq().isEmpty()) {
			// goal <- FORMULATE-GOAL(state)
			setGoal(formulateGoal(getState()));
			// problem <- FORMULATE-PROBLEM(state, goal)
			setProblem(formulateProblem(getState(), getGoal()));
			// seq <- SEARCH(problem)
			setSeq(search(getProblem()));
			// if seq = failure then return a null action
			if (isFailure(getSeq())) {
				return null;
			}
		}
		// action <- FIRST(seq)
		A action = getSeq().get(0);
		// seq <- REST(seq)
		setSeq(getSeq().subList(1, 0));
		// return action
		return action;
	}

	public S updateState(S currentState, P percept) {
		return updateStateFn.apply(currentState, percept);
	}

	public G formulateGoal(S state) {
		return formulateGoalFn.apply(state);
	}

	public Problem<A, S> formulateProblem(S state, G goal) {
		return formulateProblemFn.apply(state, goal);
	}

	public List<A> search(Problem<A, S> problem) {
		return searchFn.apply(problem);
	}

	public boolean isFailure(List<A> seq) {
		return searchController.isFailure(seq);
	}
	
	// 
	// Supporting Code
	//
	// Make composable the various logical functions
	// state <- UPDATE-STATE(state, percept)
	private BiFunction<S, P, S> updateStateFn;
	// goal <- FORMULATE-GOAL(state)
	private Function<S, G> formulateGoalFn; 
	// problem <- FORMULATE-PROBLEM(state, goal)
	private BiFunction<S, G, Problem<A, S>> formulateProblemFn;
	// seq <- SEARCH(problem)
	private Function<Problem<A, S>, List<A>> searchFn;
	private SearchController<A, S> searchController;

	public SimpleProblemSolvingAgent(BiFunction<S, P, S> updateStateFn, Function<S, G> formulateGoalFn,
			BiFunction<S, G, Problem<A, S>> formulateProblemFn, Function<Problem<A, S>, List<A>> searchFn, 
			SearchController<A, S> searchController) {
		this.updateStateFn = updateStateFn;
		this.formulateGoalFn = formulateGoalFn;
		this.formulateProblemFn = formulateProblemFn;
		this.searchFn = searchFn;
		this.searchController = searchController;
	}

	//
	// Getters and Setters
	public List<A> getSeq() {
		return seq;
	}

	public void setSeq(List<A> sequence) {
		this.seq = sequence;
	}

	//
	public S getState() {
		return state;
	}

	public void setState(S state) {
		this.state = state;
	}

	//
	public G getGoal() {
		return goal;
	}

	public void setGoal(G goal) {
		this.goal = goal;
	}

	//
	public Problem<A, S> getProblem() {
		return problem;
	}

	public void setProblem(Problem<A, S> problem) {
		this.problem = problem;
	}
}
