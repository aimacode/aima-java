package aima.gui.framework;

import javax.swing.SwingUtilities;

import aima.core.util.CancelableThread;

/** Thread, which is used to run agents in background. */
public class AgentThread extends CancelableThread {
	private AgentAppFrame frame;
	private AgentAppController controller;
	private boolean stepMode;
	
	public AgentThread(AgentAppFrame frame, AgentAppController controller, boolean stepMode) {
		this.frame = frame;
		this.controller = controller;
		this.stepMode = stepMode;
	}
	
	@Override
	public void interrupt() {
		cancel();
		super.interrupt();
	}
	
	/**
	 * Calls the run method of the controller and then lets the
	 * event dispatching thread perform the update method.
	 */
	@Override
	public void run() {
		MessageLogger logger = frame.getMessageLogger();
		try {
			if (!stepMode)
				controller.run(frame.getMessageLogger());
			else
				controller.step(frame.getMessageLogger());
		} catch (Exception e) {
			logger.log
			("Error: Somthing went wrong running the agent (" + e + ").");
			e.printStackTrace(); // for debugging
		}
		try {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					controller.update(AgentThread.this);
					frame.setAgentThread(null);
				}
			});
		} catch(Exception e) {
			logger.log
			("Error: Somthing went wrong when updating the GUI (" + e + ").");
			e.printStackTrace(); // for debugging
		}
	}
}

