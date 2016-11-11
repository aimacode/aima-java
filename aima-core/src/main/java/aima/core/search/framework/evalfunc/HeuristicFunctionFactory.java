package aima.core.search.framework.evalfunc;

/**
 * A heuristic function factory creates a heuristic function for a given goal.
 * Autonomously acting problem solving agents can profit from this kind of
 * factories after selecting a new goal.
 * 
 * @author Ruediger Lunde
 *
 */
public interface HeuristicFunctionFactory {

	HeuristicFunction createHeuristicFunction(Object goal);

}
