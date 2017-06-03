package aima.core.search.framework.problem;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 67.<br>
 * <br>
 * The goal test, which determines whether a given state is a goal state.
 *
 * @param <S> The type used to represent states
 *
 * @author Ruediger Lunde
 */
public interface GoalTest<S> extends Predicate<S> {

    /**
     * Returns a predicate that tests if two arguments are equal according
     * to {@link Objects#equals(Object, Object)}.
     *
     * @param <T> the type of arguments to the predicate
     * @param targetRef the object reference with which to compare for equality,
     *               which may be {@code null}
     * @return a predicate that tests if two arguments are equal according
     * to {@link Objects#equals(Object, Object)}
     */
    static <T> GoalTest<T> isEqual(Object targetRef) {
        return (null == targetRef) ? Objects::isNull : targetRef::equals;
    }

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * OR of this predicate and another.  When evaluating the composed
     * predicate, if this predicate is {@code true}, then the {@code other}
     * predicate is not evaluated.
     *
     * <p>Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this predicate throws an exception, the
     * {@code other} predicate will not be evaluated.
     *
     * @param other a predicate that will be logically-ORed with this
     *              predicate
     * @return a composed predicate that represents the short-circuiting logical
     * OR of this predicate and the {@code other} predicate
     * @throws NullPointerException if other is null
     */
    default GoalTest<S> or(GoalTest<? super S> other) {
        Objects.requireNonNull(other);
        return (state) -> test(state) || other.test(state);
    }
}
