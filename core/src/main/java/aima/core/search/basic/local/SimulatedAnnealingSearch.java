package aima.core.search.basic.local;

import java.util.List;
import java.util.Random;
import java.util.function.ToDoubleFunction;

import aima.core.search.api.Problem;
import aima.core.search.api.SearchForStateFunction;
import aima.core.search.basic.support.BasicSchedule;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <br>
 * 
 * <pre>
 * function SIMULATED-ANNEALING(problem, schedule) returns a solution state
 *   inputs: problem, a problem
 *           schedule, a mapping from time to "temperature"
 *
 *   current &larr; MAKE-NODE(problem.INITIAL-STATE)
 *   for t = 1 to &infin; do
 *     T &larr; schedule(t)
 *     if T = 0 then return current
 *     next &larr; a randomly selected successor of current
 *     &Delta;E &larr; next.VALUE - current.value
 *     if &Delta;E &gt; 0 then current &larr; next
 *     else current &larr; next only with probability e<sup>&Delta;E/T</sup>
 * </pre>
 * 
 * Figure ?? The simulated annealing search algorithm, a version of stochastic
 * hill climbing where some downhill moves are allowed. Downhill moves are
 * accepted readily early in the annealing schedule and then less often as time
 * goes on. The schedule input determines the value of the temperature T as a
 * function of time.
 * 
 * @author Ciaran O'Reilly
 * @author Anurag Rai
 * @author Ravi Mohan
 * @author Mike Stampone
 * @author Ruediger Lunde
 */
public class SimulatedAnnealingSearch<A, S> implements SearchForStateFunction<A, S> {
	// function SIMULATED-ANNEALING(problem, schedule) returns a solution state
	public S simulatedAnnealing(Problem<A, S> problem, ToDoubleFunction<Integer> schedule) {
		// current <- MAKE-NODE(problem.INITIAL-STATE)
		Node<S> current = makeNode(problem.initialState());
		// for t = 1 to INFINITY do
		for (int t = 1; true; t++) {
			// T <- schedule(t)
			double T = schedule(t);
			// if T = 0 then return current
			if (T == 0) {
				return current.state;
			}
			// next <- a randomly selected successor of current
			Node<S> next = randomlySelectSuccessor(current, problem);
			// &Delta;E <- next.VALUE - current.VALUE
			double DeltaE = next.value - current.value;
			// if &Delta;E > 0 then current <- next
			if (DeltaE > 0) {
				current = next;
			}
			// else current <- next only with probability e^&Delta;E/T
			else if (Math.exp(DeltaE / T) > random.nextDouble()) {
				current = next;
			}
		}
	}

	//
	// Supporting Code
	@Override
	public S apply(Problem<A, S> problem) {
		return simulatedAnnealing(problem, schedule);
	}

	/**
	 * The algorithm does not maintain a search tree, so the data structure for
	 * the current node need only record the state and value of the
	 * objective/cost function.
	 * 
	 * @author oreilly
	 *
	 * @param <S>
	 *            the type of the state space
	 */
	public static class Node<S> {
		S state;
		double value;

		Node(S state, double value) {
			this.state = state;
			this.value = value;
		}

		@Override
		public String toString() {
			return "N(" + state + ", " + value + ")";
		}
	}

	/*
	 * Represents an objective (higher better) or cost/heuristic (lower better)
	 * function. If a cost/heuristic function is passed in the
	 * 'isGradientAscentVersion' should be set to false for the algorithm to
	 * search for minimums.
	 */
	protected ToDoubleFunction<S> stateValueFn;
	protected ToDoubleFunction<Integer> schedule;
	protected Random random = new Random();

	public SimulatedAnnealingSearch(ToDoubleFunction<S> stateValueFn) {
		this(stateValueFn, true);
	}

	public SimulatedAnnealingSearch(ToDoubleFunction<S> stateValueFn, boolean isGradientAscentVersion) {
		this(stateValueFn, isGradientAscentVersion, new BasicSchedule());
	}

	public SimulatedAnnealingSearch(ToDoubleFunction<S> stateValueFn, boolean isGradientAscentVersion,
			ToDoubleFunction<Integer> scheduler) {
		this.stateValueFn = stateValueFn;
		if (!isGradientAscentVersion) {
			// Convert from one to the other by switching the sign
			this.stateValueFn = (state) -> stateValueFn.applyAsDouble(state) * -1;
		}
		this.schedule = scheduler;
	}

	public Node<S> makeNode(S state) {
		return new Node<>(state, stateValueFn.applyAsDouble(state));
	}

	public double schedule(int t) {
		double T = schedule.applyAsDouble(t);
		if (T < 0) {
			throw new IllegalArgumentException(
					"Configured schedule returns negative temperatures: t=" + t + ", T=" + T);
		}
		return T;
	}

	public Node<S> randomlySelectSuccessor(Node<S> current, Problem<A, S> problem) {
		// Default successor to current, so that in the case we reach a dead-end
		// state i.e. one without reversible actions we will return something.
		// This will not break the code above as the loop will exit when the
		// temperature winds down to 0.
		Node<S> successor = current;
		List<A> actions = problem.actions(current.state);
		if (actions.size() > 0) {
			successor = makeNode(problem.result(current.state, actions.get(random.nextInt(actions.size()))));
		}
		return successor;
	}
}
