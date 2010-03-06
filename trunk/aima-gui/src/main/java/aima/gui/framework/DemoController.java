package aima.gui.framework;

/**
 * Simple controller implementation which shows the relationship between
 * buttons and selector activations and controller method calls.
 * @author R. Lunde
 */
public class DemoController extends AgentAppController {
	private boolean isPrepared = false;
	
	/**
	 * The associated {@link AgentAppFrame} calls this method when the clear
	 * button is pressed.
	 */
	public void clear() {
		MessageLogger logger = frame.getMessageLogger();
		logger.log("clearing...");
		logger.log(frame.getSelection().toString());
		frame.setStatus("Task cleared.");
	}

	/**
	 * The associated {@link AgentAppFrame} calls this method when the prepare
	 * button is pressed or the selection state of the selectors changes.
	 * @param changedSelector Name of the changed selector or null.
	 */
	public void prepare(String changedSelector) {
		MessageLogger logger = frame.getMessageLogger();
		logger.log("preparing..." +
				(changedSelector != null ? " ("+changedSelector+")" : ""));
		logger.log(frame.getSelection().toString());
		frame.setStatus("Task prepared.");
		isPrepared = true;
	}
	
	/**
	 * Checks whether the current environment is prepared for starting
	 * simulation.
	 */
	public boolean isPrepared() {
		return isPrepared;
	}

	/**
	 * The associated {@link AgentAppFrame} calls this method when the run
	 * button is activated. This code runs in a second thread, which can be
	 * stopped by the GUI at any time. Implementations should avoid to
	 * access swing components because they are not thread safe. 
	 */
	public void run(MessageLogger logger) {
		logger.log("running...");
		try {
			for (int i = 0; i < 10; i++) {
				Thread.sleep(500);
				logger.log(i + " ...");
			}
		} catch (InterruptedException e) {}
		isPrepared = false;
	}
	
	/**
	 * The associated {@link AgentAppFrame} calls this method when the step
	 * button is activated. This code runs in a second thread, which can be
	 * stopped by the GUI at any time. Implementations should avoid to
	 * access swing components because they are not thread-safe. 
	 */
	public void step(MessageLogger logger) {
		logger.log("executing a step...");
		try {
			Thread.sleep(2000);
			logger.log("ready");
		} catch (InterruptedException e) {}
	}
	
	/**
	 * This method is automatically called after the run and step methods
	 * have finished. Implementations are responsible for displaying status
	 * information in the frame and also for cleaning up the prepared
	 * environment if the simulation was canceled.
	 * @param agentThread The thread which was used to run the agent.
	 */
	public void update(AgentThread agentThread) {
		if (agentThread.isCanceled()) {
			frame.setStatus("Task canceled.");
			isPrepared = false;
		} else {
			frame.setStatus("Task completed.");
		}
	}
}
