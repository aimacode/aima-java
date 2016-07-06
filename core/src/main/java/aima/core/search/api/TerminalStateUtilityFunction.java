package aima.core.search.api;

/**
 * UTILITY(s, p): A utility function (also called an objective function or
 * payoff function), defines the final numeric value for a game that ends in
 * terminal state s for a player p. In chess, the outcome is a win, loss, or
 * draw, with values +1, 0, or 1/2 . Some games have a wider variety of possible
 * outcomes; the payoffs in backgammon range from 0 to +192. A zero-sum game is
 * (confusingly) defined as one where the total payoff to all players is the
 * same for every instance of the game. Chess is zero-sum because every game has
 * payoff of either 0 + 1, 1 + 0 or 1/2 + 1/2 . "Constant-sum" would have been a
 * better term, but zero-sum is traditional and makes sense if you imagine each
 * player is charged an entry fee of 1/2.
 *
 * @param <S>
 *            Type which is used for states in the game.
 * @param <P>
 *            Type which is used for players in the game.
 * 
 * @author Ciaran O'Reilly
 * 
 */
@FunctionalInterface
public interface TerminalStateUtilityFunction<S, P> {
	double utility(S state, P player);
}
