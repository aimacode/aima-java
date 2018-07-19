package aima.core.search.basic.local;

import aima.core.search.api.Problem;
import aima.core.search.api.SearchForStateFunction;
import aima.core.search.basic.support.BasicSchedule;

import java.util.List;
import java.util.Random;
import java.util.function.ToDoubleFunction;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <br>
 *
 * <pre>
 * function SIMULATED-ANNEALING(problem,schedule) returns a solution state
 *
 *  current ← problem.INITIAL-STATE
 *  for t = 1 to ∞ do
 *    T ← schedule(t)
 *    if T = 0 then return current
 *    next ← a randomly selected successor of current
 *    ΔE ← VALUE(next) - VALUE(current)
 *    if ΔE > 0 then current ← next
 *    else current &larr; next only with probability e<sup>&Delta;E/T</sup>
 * </pre>
 * <p>
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
 * @author samagra
 */
public class SimulatedAnnealingSearch<A, S> implements SearchForStateFunction<A, S> {
    /*
     * Represents an objective (higher better) or cost/heuristic (lower better)
     * function. If a cost/heuristic function is passed in the
     * 'isGradientAscentVersion' should be set to false for the algorithm to
     * search for minimums.
     */
    protected ToDoubleFunction<S> stateValueFn;
    // The schedule input determines the value of the “temperature” T as a function of time;
    protected ToDoubleFunction<Integer> schedule;
    // Pseudo-random number generator for handling probabilities
    protected Random random = new Random();
    // Constructors
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

    // function SIMULATED-ANNEALING(problem, schedule) returns a solution state
    public S simulatedAnnealing(Problem<A, S> problem, ToDoubleFunction<Integer> schedule) {
        S current = problem.initialState();
        for (int t = 1; true; t++) {
            double T = schedule(t);
            if (T == 0) {
                return current;
            }
            S next = randomlySelectSuccessor(current, problem);
            double DeltaE = stateValueFn.applyAsDouble(next) -
                    stateValueFn.applyAsDouble(current);
            if (DeltaE > 0) {
                current = next;
            }
            // else current <- next only with probability e^&Delta;E/T
            else if (Math.exp(DeltaE / T) > random.nextDouble()) {
                current = next;
            }
        }
    }

    @Override
    public S apply(Problem<A, S> problem) {
        return simulatedAnnealing(problem, schedule);
    }

    public double schedule(int t) {
        double T = schedule.applyAsDouble(t);
        if (T < 0) {
            throw new IllegalArgumentException(
                    "Configured schedule returns negative temperatures: t=" + t + ", T=" + T);
        }
        return T;
    }

    public S randomlySelectSuccessor(S current, Problem<A, S> problem) {
        // Default successor to current, so that in the case we reach a dead-end
        // state i.e. one without reversible actions we will return something.
        // This will not break the code above as the loop will exit when the
        // temperature winds down to 0.
        S successor = current;
        List<A> actions = problem.actions(current);
        if (actions.size() > 0) {
            successor = problem.result(current, actions.get(random.nextInt(actions.size())));
        }
        return successor;
    }
}
