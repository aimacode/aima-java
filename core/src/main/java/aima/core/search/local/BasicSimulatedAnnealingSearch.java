package aima.core.search.local;

import java.util.function.Function;

import aima.core.api.search.Node;
import aima.core.api.search.local.SimulatedAnnealingSearch;
import aima.core.search.BasicSearchFunction;

/**
 * @author Anurag Rai
 */
public class BasicSimulatedAnnealingSearch<S> extends BasicSearchFunction<S> implements SimulatedAnnealingSearch<S> {

    private final Function<Node<S>, Double> heuristicFn;
    private final Scheduler scheduler;
	
    /**
	 * Constructs a simulated annealing search from the specified heuristic
	 * function and a default scheduler.
	 * 
	 * @param heuristicFn
	 *            a heuristic function
	 */
    public BasicSimulatedAnnealingSearch(Function<Node<S>, Double> heuristicFn) {
		this.heuristicFn = heuristicFn;
		scheduler = new Scheduler();
	}

	@Override
	public Scheduler getScheduler() {
		return this.scheduler;
	}

	@Override
	public Function<Node<S>, Double> getHeuristicFunction() {
		return this.heuristicFn;
	}
}
