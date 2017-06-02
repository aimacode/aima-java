package aima.core.search.informed;

import aima.core.search.framework.Node;

import java.util.function.ToDoubleFunction;

/**
 * A heuristic function factory creates a heuristic function for a given goal.
 * Autonomously acting problem solving agents can profit from this kind of
 * factories after selecting a new goal.
 * 
 * @author Ruediger Lunde
 *
 */
@FunctionalInterface
public interface HeuristicFunctionFactory<S, A> {
	ToDoubleFunction<Node<S, A>> createHeuristicFunction(S goal);
}
