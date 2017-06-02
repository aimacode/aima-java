package aima.core.search.framework.problem;

import java.util.List;
import java.util.function.Function;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 67.<br>
 * <br>
 * Given a particular state s, ACTIONS(s) returns the set of actions that can be
 * executed in s. We say that each of these actions is <b>applicable</b> in s.
 *
 * @param <S> the type used to represent states
 * @param <A> the type of the actions to be used to navigate in the state space
 *
 * @author Ruediger Lunde
 */
public interface ActionsFunction<S, A> extends Function<S, List<A>> {}
