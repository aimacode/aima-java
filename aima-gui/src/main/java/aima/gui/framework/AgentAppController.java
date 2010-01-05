package aima.gui.framework;

/**
 * Provides the base class for all controller implementations.
 * @author R. Lunde
 */
public abstract class AgentAppController {
	protected AgentAppFrame frame;

	/**
	 * Gives the controller access to the frame. This is useful to display
	 * status information.
	 */
	public void setFrame(AgentAppFrame frame) {
		this.frame = frame;
	}

	/**
	 * The associated {@link AgentAppFrame} calls this method when the clear
	 * button is pressed.
	 */
	public abstract void clear();

	/**
	 * The associated {@link AgentAppFrame} calls this method when the prepare
	 * button is pressed, the selection state of the selectors changes, and also
	 * when the run button is pressed without previously performed preparation.
	 */
	public abstract void prepare();

	/**
	 * The associated {@link AgentAppFrame} calls this method when the run
	 * button is activated. This code runs in a second thread, which can be
	 * stopped by the GUI at any time. Implementations should avoid to
	 * access swing components because they are not thread safe. 
	 */
	public abstract void run(MessageLogger logger);
	
	/**
	 * This method is automatically called after the run method has finished.
	 * Implementations are responsible for displaying status information in
	 * the frame.
	 * @param agentThread The thread which was used to run the agent.
	 */
	public abstract void update(AgentThread agentThread);
}
