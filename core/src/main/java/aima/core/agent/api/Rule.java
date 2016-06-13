package aima.core.agent.api;

import java.util.function.Predicate;

/**
 * A simple condition-action rule definition.
 *
 * @param <A>
 *            the action type that the rule triggers.
 * @param <S>
 *            the state type that the condition predicate tests.
 *
 * @author Ciaran O'Reilly
 */
public interface Rule<A, S> {
	Predicate<S> condition();

	A action();
}