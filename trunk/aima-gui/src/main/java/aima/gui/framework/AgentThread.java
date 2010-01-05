package aima.gui.framework;

import javax.swing.SwingUtilities;

/** Thread, which is used to run agents in background. */
public class AgentThread extends Thread {
	AgentAppFrame frame;
	AgentAppController controller;
	
	public AgentThread(AgentAppFrame frame, AgentAppController controller) {
		this.frame = frame;
		this.controller = controller;
	}
	/** Remembers interrupt method calls. */
	boolean isCancelled = false;
	
	/** Returns true if the thread was interrupted. */
	public boolean isCancelled() {
		return isCancelled;
	}
	
	@Override
	public void interrupt() {
		isCancelled = true;
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
			controller.run(frame.getMessageLogger());
		} catch (Exception e) {
			logger.log
			("Error: Somthing went wrong running the agent (" + e + ").");
			e.printStackTrace(); // for debugging
		}
		try {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					frame.setButtonsEnabled(true);
					controller.update(AgentThread.this);
				}
			});
		} catch(Exception e) {
			logger.log
			("Error: Somthing went wrong when updating the GUI (" + e + ").");
			e.printStackTrace(); // for debugging
		}
	}
}

