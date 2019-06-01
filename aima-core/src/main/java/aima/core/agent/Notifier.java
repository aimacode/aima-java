package aima.core.agent;

/**
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * 
 */
public interface Notifier {
	/**
	 * A simple notification message, to be forwarded to someone.
	 * 
	 * @param msg
	 *            the message to be forwarded.
	 */
	void notify(String msg);
}
