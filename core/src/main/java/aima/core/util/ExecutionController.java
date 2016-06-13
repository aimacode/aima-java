package aima.core.util;

/**
 * An interface intended to be used to break long wronging loops if needed (for
 * example when canceling a run of an algorithm from a user interface).
 * 
 * @author oreilly
 *
 */
public interface ExecutionController {
	default boolean isExecuting() {
		return true;
	}
}
