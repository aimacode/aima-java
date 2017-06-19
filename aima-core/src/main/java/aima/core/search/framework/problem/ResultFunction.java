package aima.core.search.framework.problem;

import java.util.function.BiFunction;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 67.<br>
 * <br>
 * A description of what each action does; the formal name for this is the
 * transition model, specified by a function RESULT(s, a) that returns the state
 * that results from doing action a in state s. We also use the term successor
 * to refer to any state reachable from a given state by a single action.
 *
 * @param <S> The type used to represent states
 * @param <A> The type of the actions to be used to navigate through the state space
 *
 * @author Ruediger Lunde
 */
public interface ResultFunction<S, A> extends BiFunction<S, A, S> {}
