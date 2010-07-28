package aima.core.search.local;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import aima.core.agent.Action;
import aima.core.search.framework.HeuristicFunction;
import aima.core.search.framework.Node;
import aima.core.search.framework.NodeExpander;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchUtils;
import aima.core.util.CancelableThread;
import aima.core.util.Util;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 4.5, page 126.
 * 
 * <code>
 * function SIMULATED-ANNEALING(problem, schedule) returns a solution state
 *                    
 *   current <- MAKE-NODE(problem.INITIAL-STATE)
 *   for t = 1 to INFINITY do
 *     T <- schedule(t)
 *     if T = 0 then return current
 *     next <- a randomly selected successor of current
 *     /\E <- next.VALUE - current.value
 *     if /\E > 0 then current <- next
 *     else current <- next only with probability e^(/\E/T)
 * </code>
 * Figure 4.5 The simulated annealing search algorithm, a version of
 * stochastic hill climbing where some downhill moves are allowed. Downhill
 * moves are accepted readily early in the annealing schedule and then less
 * often as time goes on. The schedule input determines the value of
 * the temperature T as a function of time.
 */

/**
 * @author Ravi Mohan
 * 
 */
public class SimulatedAnnealingSearch extends NodeExpander implements Search {

	public enum SearchOutcome {
		FAILURE, SOLUTION_FOUND
	};

	private final HeuristicFunction hf;
	private final Scheduler scheduler;

	private SearchOutcome outcome = SearchOutcome.FAILURE;

	private Object lastState = null;

	public SimulatedAnnealingSearch(HeuristicFunction hf) {
		this.hf = hf;
		this.scheduler = new Scheduler();
	}

	public SimulatedAnnealingSearch(HeuristicFunction hf, Scheduler scheduler) {
		this.hf = hf;
		this.scheduler = scheduler;
	}

	// function SIMULATED-ANNEALING(problem, schedule) returns a solution state
	public List<Action> search(Problem p) throws Exception {
		clearInstrumentation();
		outcome = SearchOutcome.FAILURE;
		lastState = null;
		// current <- MAKE-NODE(problem.INITIAL-STATE)
		Node current = new Node(p.getInitialState());
		Node next = null;
		List<Action> ret = new ArrayList<Action>();
		// for t = 1 to INFINITY do
		int timeStep = 0;
		while (!CancelableThread.currIsCanceled()) {
			// temperature <- schedule(t)
			double temperature = scheduler.getTemp(timeStep);
			timeStep++;
			// if temperature = 0 then return current
			if (temperature == 0.0) {
				if (SearchUtils.isGoalState(p, current)) {
					outcome = SearchOutcome.SOLUTION_FOUND;
				}
				ret = SearchUtils.actionsFromNodes(current.getPathFromRoot());
				lastState = current.getState();
				break;
			}

			List<Node> children = expandNode(current, p);
			if (children.size() > 0) {
				// next <- a randomly selected successor of current
				next = Util.selectRandomlyFromList(children);
				// /\E <- next.VALUE - current.value
				double deltaE = getValue(p, next) - getValue(p, current);

				if (shouldAccept(temperature, deltaE)) {
					current = next;
				}
			}
		}

		return ret;
	}

	public double probabilityOfAcceptance(double temperature, double deltaE) {
		return Math.exp(deltaE / temperature);
	}

	public SearchOutcome getOutcome() {
		return outcome;
	}

	public Object getLastSearchState() {
		return lastState;
	}

	//
	// PRIVATE METHODS
	//

	// if /\E > 0 then current <- next
	// else current <- next only with probability e^(/\E/T)
	private boolean shouldAccept(double temperature, double deltaE) {
		return (deltaE > 0.0)
				|| (new Random().nextDouble() <= probabilityOfAcceptance(
						temperature, deltaE));
	}

	private double getValue(Problem p, Node n) {
		// assumption greater heuristic value =>
		// HIGHER on hill; 0 == goal state;
		// SA deals with gardient DESCENT
		return -1 * hf.h(n.getState());
	}
}