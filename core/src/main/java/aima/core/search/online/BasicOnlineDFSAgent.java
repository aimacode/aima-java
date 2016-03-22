package aima.core.search.online;

/*
 * @author Anurag Rai
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import aima.core.api.agent.Action;
import aima.core.api.search.online.OnlineDFSAgent;
import aima.core.util.datastructure.TwoKeyHashMap;

public class BasicOnlineDFSAgent<P,S> implements OnlineDFSAgent<P, S> {

	private Function<P,S> perceptToStateFn;
	private OnlineSearchProblem<S> problem;
	//the previous state and action, initially null
	S s;
	Action a;
	private final TwoKeyHashMap<S, Action, S> result = new TwoKeyHashMap<S, Action, S>();
	// untried, a table that lists, for each state, the actions not yet tried
	private final Map<S, List<Action>> untried = new HashMap<S, List<Action>>();
	// unbacktracked, a table that lists,
	// for each state, the backtracks not yet tried
	private final Map<S, List<S>> unbacktracked = new HashMap<S, List<S>>();
	
	public BasicOnlineDFSAgent(OnlineSearchProblem<S> problem, Function<P,S> perceptToStateFn) {
		this.problem = problem;
		this.perceptToStateFn = perceptToStateFn;
		a = null;
		s = null;
	}
	
	public Function<P, S> getPerceptToStateFunction() {
		return this.perceptToStateFn;
	}

	public OnlineSearchProblem<S> getProblem() {
		return this.problem;
	}
	
	public TwoKeyHashMap<S, Action, S> getResult() {
		return this.result;
	}
	
	public Map<S, List<Action>> getUntried() {
		return this.untried;
	}
	
	public Map<S, List<S>> getUnbacktracked() {
		return this.unbacktracked;
	}
	
	public Action getPreviousAction() {
		return a;
	}
	
	public S getPreviousState() {
		return s;
	}
	
	public void setPreviousState(S s) {
		this.s = s;
	}
	
	public void setPreviousAction(Action a) {
		this.a = a;
	}
}
