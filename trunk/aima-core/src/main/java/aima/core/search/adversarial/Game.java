package aima.core.search.adversarial;

import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 165.<br>
 * <br>
 * A game can be formally defined as a kind of search problem with the following
 * elements: <br>
 * <ul>
 * <li>S0: The initial state, which specifies how the game is set up at the
 * start.</li>
 * <li>PLAYER(s): Defines which player has the move in a state.</li>
 * <li>ACTIONS(s): Returns the set of legal moves in a state.</li>
 * <li>RESULT(s, a): The transition model, which defines the result of a move.</li>
 * <li>TERMINAL-TEST(s): A terminal test, which is true when the game is over
 * and false TERMINAL STATES otherwise. States where the game has ended are
 * called terminal states.</li>
 * <li>UTILITY(s, p): A utility function (also called an objective function or
 * payoff function), defines the final numeric value for a game that ends in
 * terminal state s for a player p. In chess, the outcome is a win, loss, or
 * draw, with values +1, 0, or 1/2 . Some games have a wider variety of possible
 * outcomes; the payoffs in backgammon range from 0 to +192. A zero-sum game is
 * (confusingly) defined as one where the total payoff to all players is the
 * same for every instance of the game. Chess is zero-sum because every game has
 * payoff of either 0 + 1, 1 + 0 or 1/2 + 1/2 . "Constant-sum" would have been a
 * better term, but zero-sum is traditional and makes sense if you imagine each
 * player is charged an entry fee of 1/2.</li>
 * </ul>
 * 
 * @author Ruediger Lunde
 * 
 * @param <STATE>
 *            Type which is used for states in the game.
 * @param <ACTION>
 *            Type which is used for actions in the game.
 * @param <PLAYER>
 *            Type which is used for players in the game.
 */
public interface Game<STATE, ACTION, PLAYER> {

	STATE getInitialState();

	PLAYER[] getPlayers();

	PLAYER getPlayer(STATE state);

	List<ACTION> getActions(STATE state);

	STATE getResult(STATE state, ACTION action);

	boolean isTerminal(STATE state);

	double getUtility(STATE state, PLAYER player);
}
