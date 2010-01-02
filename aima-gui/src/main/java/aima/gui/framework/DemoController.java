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
	public void clearAgent() {
		MessageLogger logger = frame.getMessageLogger();
		logger.log("clearing...");
		logger.log(frame.getSelection().toString());
		frame.setStatus("Views cleared.");
	}

	/**
	 * The associated {@link AgentAppFrame} calls this method when the prepare
	 * button is pressed, the selection state of the selectors changes, and also
	 * when the run button is pressed without previously performed preparation.
	 */
	public void prepareAgent() {
		MessageLogger logger = frame.getMessageLogger();
		logger.log("preparing...");
		logger.log(frame.getSelection().toString());
		frame.setStatus("Agent prepared.");
	}

	/**
	 * The associated {@link AgentAppFrame} calls this method when the run
	 * button is activated. This code runs in a second thread, which can be
	 * stopped by the GUI at any time.
	 */
	public void runAgent() {
		MessageLogger logger = frame.getMessageLogger();
		logger.log("running...");
		logger.log(frame.getSelection().toString());
		frame.setStatus("Task completed.");
	}
}
