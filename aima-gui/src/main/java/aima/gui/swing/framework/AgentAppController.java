package aima.gui.framework;

/**
 * Provides the base class for all controller implementations.
 * 
 * @author Ruediger Lunde
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
	 * button is pressed or the selection state of the selectors changes.
	 * @param changedSelector Name of the changed selector or null.
	 */
	public abstract void prepare(String changedSelector);

	/**
	 * Checks whether the current environment is prepared for starting
	 * simulation.
	 */
	public abstract boolean isPrepared();
	
	/**
	 * The associated {@link AgentAppFrame} calls this method when the run
	 * button is activated. This code runs in a second thread, which can be
	 * stopped by the GUI at any time. Implementations should avoid to
	 * access swing components because they are not thread-safe. 
	 */
	public abstract void run(MessageLogger logger);
	
	/**
	 * The associated {@link AgentAppFrame} calls this method when the step
	 * button is activated. This code runs in a second thread, which can be
	 * stopped by the GUI at any time. Implementations should avoid to
	 * access swing components because they are not thread-safe. 
	 */
	public abstract void step(MessageLogger logger);
	
	/**
	 * This method is automatically called after the run and step methods
	 * have finished. Implementations are responsible for displaying status
	 * information in the frame and also for cleaning up the prepared
	 * environment if the simulation was canceled.
	 * @param simulationThread The thread which was used to run the simulation.
	 */
	public abstract void update(SimulationThread simulationThread);
}
