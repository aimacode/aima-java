package aima.gui.framework;

/**
 * Simple controller implementation which extends its base class
 * by maintaining references to frame and model, and by simple
 * demo implementations of the required three abstract methods.
 * @author R. Lunde
 */
public class AgentAppController implements AgentAppFrame.Controller {
	protected AgentAppFrame frame;
	protected AgentAppModel model;
	/**
	 * Gives the controller access to the frame. This
	 * is useful to display status information.
	 */
	public void setFrame(AgentAppFrame frame) { this.frame = frame; }
	public void setModel(AgentAppModel model) { this.model = model; }
	
	/**
	 * The associated {@link AgentAppFrame} calls this method
	 * when the clear button is pressed.
	 */
	public void clearAgent() {
		frame.logMessage("clearing...");
		frame.logMessage(frame.getSelection().toString());
		frame.setStatus("Agent cleared.");
	}
	
	/**
	 * The associated {@link AgentAppFrame} calls this method
	 * when the prepare button is pressed, the selection state of 
	 * the selectors changes, and also when the run button is pressed
	 * without previously performed preparation.
	 */
	public void prepareAgent() {
		frame.logMessage("preparing...");
		frame.logMessage(frame.getSelection().toString());
		frame.setStatus("Agent prepared.");
	}
	
	/**
	 * The associated {@link AgentAppFrame} calls this method
	 * when the run button is activated. This code runs in a second
	 * thread, which can be stopped by the GUI at any time.
	 */
	public void runAgent() {
		frame.logMessage("running...");
		frame.logMessage(frame.getSelection().toString());
		frame.modelChanged();
		frame.modelChanged();
		frame.setStatus("Task completed.");
	}
}
