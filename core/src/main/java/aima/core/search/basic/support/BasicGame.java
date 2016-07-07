package aima.core.search.basic.support;

import java.util.List;

import aima.core.search.api.ActionsFunction;
import aima.core.search.api.Game;
import aima.core.search.api.InitialStateFunction;
import aima.core.search.api.PlayerFunction;
import aima.core.search.api.ResultFunction;
import aima.core.search.api.TerminalStateUtilityFunction;
import aima.core.search.api.TerminalTestPredicate;

/**
 * Basic implementation of the Game interface.
 * 
 * @author Ciaran O'Reilly
 */
public class BasicGame<S, A, P> implements Game<S, A, P> {
	private InitialStateFunction<S> initialStateFn;
	private PlayerFunction<S, P> playerFn;
	private ActionsFunction<A, S> actionsFn;
	private ResultFunction<A, S> resultFn;
	private TerminalTestPredicate<S> terminalTestPredicate;
	private TerminalStateUtilityFunction<S, P> utilityFn;

	public BasicGame(InitialStateFunction<S> initialStateFn, PlayerFunction<S, P> playerFn,
			ActionsFunction<A, S> actionsFn, ResultFunction<A, S> resultFn,
			TerminalTestPredicate<S> terminalTestPredicate, TerminalStateUtilityFunction<S, P> utilityFn) {
		this.initialStateFn = initialStateFn;
		this.playerFn = playerFn;
		this.actionsFn = actionsFn;
		this.resultFn = resultFn;
		this.terminalTestPredicate = terminalTestPredicate;
		this.utilityFn = utilityFn;
	}
	
	@Override
	public S initialState() {
		return initialStateFn.initialState();
	}

	@Override
	public P player(S state) {
		return playerFn.player(state);
	}

	@Override
	public List<A> actions(S s) {
		return actionsFn.actions(s);
	}

	@Override
	public S result(S s, A a) {
		return resultFn.result(s, a);
	}

	@Override
	public boolean isTerminalState(S state) {
		return terminalTestPredicate.isTerminalState(state);
	}

	@Override
	public double utility(S state, P player) {
		return utilityFn.utility(state, player);
	}
}
