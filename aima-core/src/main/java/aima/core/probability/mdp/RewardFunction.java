package aima.core.probability.mdp;
import java.util.function.Function;

/**
 * An interface for MDP reward functions.
 *
 * @param <S> The type used to represent states.
 *
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public interface RewardFunction<S> extends Function<S, Double> {
	/**
	 * Extends inbuilt functional interface Function< T, R>.
	 * having single abstract method
	 *
	 * R apply(T)
	 *
	 * @param s
	 *            the state whose award is sought.
	 * @return the reward associated with being in state s.
	 */
}
