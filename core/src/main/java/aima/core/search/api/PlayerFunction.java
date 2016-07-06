package aima.core.search.api;

/**
 * PLAYER(s): Defines which player has the move in a state..
 * 
 * @param <S>
 *            Type which is used for states in the game.
 * @param <P>
 *            Type which is used for players in the game. 
 * 
 * @author Ciaran O'Reilly
 */
@FunctionalInterface
public interface PlayerFunction<S, P> {
	P player(S state);
}
