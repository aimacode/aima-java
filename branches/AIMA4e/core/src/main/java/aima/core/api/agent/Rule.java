package aima.core.api.agent;

import java.util.function.Predicate;

/**
 * A simple condition-action rule definition.
 *
 * @param <S> the state that the condition predicate tests.
 *
 * @author Ciaran O'Reilly
 */
public interface Rule<S> {
    Predicate<S> condition();
    Action action();
}