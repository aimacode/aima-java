package aima.core.search.api;

/**
 * TERMINAL-TEST(s): A terminal test, which is true when the game is over
 * and false TERMINAL STATES otherwise. States where the game has ended are
 * called terminal states.
 *
 * @param <S>
 *            Type which is used for states in the game.
 * 
 * @author Ciaran O'Reilly
 * 
 */
@FunctionalInterface
public interface TerminalTestPredicate <S> {
	/**
	 * The terminal test, which determines if a given state indicates a game is over.
	 *
	 * @param state
	 *            a state to be tested.
	 * @return true if the given state indicates game over, false otherwise.
	 */
	boolean isTerminalState(S state);
}
