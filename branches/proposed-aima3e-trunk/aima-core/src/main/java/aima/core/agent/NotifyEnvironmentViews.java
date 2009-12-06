package aima.core.agent;

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface NotifyEnvironmentViews {
	/**
	 * A simple notification message, to be forewarded to an Environment's registered EnvironmentViews.
	 * 
	 * @param msg
	 *            the message to be forwarded to the EnvironmentViews.
	 */
	void notifyViews(String msg);
}
