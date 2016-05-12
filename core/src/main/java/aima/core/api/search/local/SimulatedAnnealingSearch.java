package aima.core.api.search.local;

import aima.core.api.search.SearchFunction;
import aima.core.search.api.Node;
import aima.core.search.api.Problem;
import aima.core.search.local.Scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 * <br>
 * 
 * <pre>
 * function SIMULATED-ANNEALING(problem, schedule) returns a solution state
 *                    
 *   current &lt;- MAKE-NODE(problem.INITIAL-STATE)
 *   for t = 1 to INFINITY do
 *     T &lt;- schedule(t)
 *     if T = 0 then return current
 *     next &lt;- a randomly selected successor of current
 *     /\E &lt;- next.VALUE - current.value
 *     if /\E &gt; 0 then current &lt;- next
 *     else current &lt;- next only with probability e&circ;(/\E/T)
 * </pre>
 * 
 * Figure ?? The simulated annealing search algorithm, a version of stochastic
 * hill climbing where some downhill moves are allowed. Downhill moves are
 * accepted readily early in the annealing schedule and then less often as time
 * goes on. The schedule input determines the value of the temperature T as a
 * function of time.
 * 
 * @author Anurag Rai
 * @author Ravi Mohan
 * @author Mike Stampone
 * @author Ruediger Lunde
 */

/*
 * @param <S>
 */
public interface SimulatedAnnealingSearch<A, S> extends SearchFunction<A, S> {

	Scheduler getScheduler();
	Function<Node<A, S>, Double> getHeuristicFunction();
	
	@Override
	default List<A> apply(Problem<A, S> problem) {
		Scheduler scheduler = getScheduler();
		Function<Node<A, S>, Double> hf = getHeuristicFunction();
		// current <- MAKE-NODE(problem.INITIAL-STATE)
		Node<A, S> current = newNode(problem.initialState(), 0);
		List<A> ret = new ArrayList<A>();
		// for t = 1 to INFINITY do
		int timeStep = 0;
		while ( true ) {
			// temperature <- schedule(t)		
			double temperature = scheduler.getTemp(timeStep);
			timeStep++;
			// if temperature = 0 then return current
			if ( temperature == 0.0 ) {
				ret = solution(current);
				break;
			}
			
			List<Node<A, S>> children = new ArrayList<>();
			// expand the current node to find the children
			for (A action : problem.actions(current.state())) {
	               children.add(childNode(problem, current, action));
	        }
			if ( !children.isEmpty() ) {
				// next <- a randomly selected successor of current
				Node<A, S> next = children.get(new Random().nextInt(children.size()));
				// /\E <- next.VALUE - current.value
				double deltaE = getValue(hf, next) - getValue(hf, current);

				if (shouldAccept(temperature, deltaE)) {
					current = next;
				}
			}
		}
		return ret;
	}
	

	/**
	 * Returns <em>e</em><sup>&delta<em>E / T</em></sup>
	 * 
	 * @param temperature
	 *            <em>T</em>, a "temperature" controlling the probability of
	 *            downward steps
	 * @param deltaE
	 *            VALUE[<em>next</em>] - VALUE[<em>current</em>]
	 * @return <em>e</em><sup>&delta<em>E / T</em></sup>
	 */
	default double probabilityOfAcceptance(double temperature, double deltaE) {
		return Math.exp(deltaE / temperature);
	}


	// if /\E > 0 then current <- next
	// else current <- next only with probability e^(/\E/T)
	default boolean shouldAccept(double temperature, double deltaE) {
		return (deltaE > 0.0)
				|| (new Random().nextDouble() <= probabilityOfAcceptance(
						temperature, deltaE));
	}

	default double getValue(Function<Node<A, S>, Double> hf, Node<A, S> n) {
		// assumption greater heuristic value =>
		// HIGHER on hill; 0 == goal state;
		// SA deals with gradient DESCENT
		return -1 * hf.apply(n);
	}
}
