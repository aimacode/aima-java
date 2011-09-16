package aima.core.learning.reinforcement;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 832.<br>
 * <br>
 * A percept that supplies both the current state and the reward received in
 * that state.
 * 
 * @param <S>
 *            the state type.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public interface PerceptStateReward<S> extends RewardPercept {
	/**
	 * 
	 * @return the current state associated with the percept.
	 */
	S state();
}
