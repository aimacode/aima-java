package aima.core.search.online;

/*
 * @author Anurag Rai
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import aima.core.api.search.online.OnlineDFSAgent;
import aima.core.util.datastructure.TwoKeyHashMap;

public class BasicOnlineDFSAgent<A, P, S> implements OnlineDFSAgent<A, P, S> {

	private Function<P,S> perceptToStateFn;
	private OnlineSearchProblem<A, S> problem;
	//the previous state and action, initially null
	S s;
	A a;
	private final TwoKeyHashMap<S, A, S> result = new TwoKeyHashMap<S, A, S>();
	// untried, a table that lists, for each state, the actions not yet tried
	private final Map<S, List<A>> untried = new HashMap<S, List<A>>();
	// unbacktracked, a table that lists,
	// for each state, the backtracks not yet tried
	private final Map<S, List<S>> unbacktracked = new HashMap<S, List<S>>();
	
	public BasicOnlineDFSAgent(OnlineSearchProblem<A, S> problem, Function<P,S> perceptToStateFn) {
		this.problem = problem;
		this.perceptToStateFn = perceptToStateFn;
		a = null;
		s = null;
	}
	
	public Function<P, S> getPerceptToStateFunction() {
		return this.perceptToStateFn;
	}

	public OnlineSearchProblem<A, S> getProblem() {
		return this.problem;
	}
	
	public TwoKeyHashMap<S, A, S> getResult() {
		return this.result;
	}
	
	public Map<S, List<A>> getUntried() {
		return this.untried;
	}
	
	public Map<S, List<S>> getUnbacktracked() {
		return this.unbacktracked;
	}
	
	public A getPreviousAction() {
		return a;
	}
	
	public S getPreviousState() {
		return s;
	}
	
	public void setPreviousState(S s) {
		this.s = s;
	}
	
	public void setPreviousAction(A a) {
		this.a = a;
	}
}
