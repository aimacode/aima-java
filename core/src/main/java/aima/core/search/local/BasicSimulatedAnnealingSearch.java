package aima.core.search.local;

import java.util.function.Function;

import aima.core.api.search.Node;
import aima.core.api.search.local.SimulatedAnnealingSearch;
import aima.core.search.BasicSearchFunction;

/**
 * @author Anurag Rai
 */
public class BasicSimulatedAnnealingSearch<A, S> extends BasicSearchFunction<A, S> implements SimulatedAnnealingSearch<A, S> {

    private final Function<Node<A, S>, Double> heuristicFn;
    private final Scheduler scheduler;
	
    /**
	 * Constructs a simulated annealing search from the specified heuristic
	 * function and a default scheduler.
	 * 
	 * @param heuristicFn
	 *            a heuristic function
	 */
    public BasicSimulatedAnnealingSearch(Function<Node<A, S>, Double> heuristicFn) {
		this.heuristicFn = heuristicFn;
		scheduler = new Scheduler();
	}

	@Override
	public Scheduler getScheduler() {
		return this.scheduler;
	}

	@Override
	public Function<Node<A, S>, Double> getHeuristicFunction() {
		return this.heuristicFn;
	}
}
