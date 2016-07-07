package aima.core.search.api;

import java.util.function.Function;

/**
 * Description of an adversarial search function for Games. Since players can
 * only control the next move, method <code>apply</code>, which represents a
 * decision, returns only a single action.
 *
 * @param <S>
 *            Type which is used for states in the game.
 * @param <A>
 *            Type which is used for actions in the game.
 *
 * @author Ruediger Lunde
 * @author Ciaran O'Reilly
 * 
 */
@FunctionalInterface
public interface SearchForAdversarialActionFunction<S, A> extends Function<S, A> {
	@Override
	A apply(S state);
}