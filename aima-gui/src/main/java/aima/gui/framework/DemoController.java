package aima.gui.framework;

/**
 * Simple controller implementation which shows the relationship between
 * buttons and selector activations and controller method calls.
 * @author R. Lunde
 */
public class DemoController extends AgentAppController {

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
	 * button is pressed, the selection state of the selectors changes, and also
	 * when the run button is pressed without previously performed preparation.
	 */
	public void prepare() {
		MessageLogger logger = frame.getMessageLogger();
		logger.log("preparing...");
		logger.log(frame.getSelection().toString());
		frame.setStatus("Task prepared.");
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
	}
	
	/**
	 * This method is automatically called after the run method has finished.
	 * Implementations are responsible for displaying status information in
	 * the frame.
	 * @param agentThread The thread which was used to run the agent.
	 */
	public void update(AgentThread agentThread) {
		if (agentThread.isCancelled())
			frame.setStatus("Task cancelled.");
		else
			frame.setStatus("Task completed.");
	}
}
