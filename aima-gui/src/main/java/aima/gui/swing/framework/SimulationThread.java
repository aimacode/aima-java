package aima.gui.swing.framework;

import java.awt.EventQueue;

import aima.core.util.CancellableThread;

/**
 * Background thread, which is used for simulation.
 * 
 * @author Ruediger Lunde
 */
public class SimulationThread extends CancellableThread {
	private AgentAppFrame frame;
	private AgentAppController controller;
	/** Decides whether the controller's run or step method is called. */
	private boolean stepMode;
	
	/** Standard constructor. */
	public SimulationThread(AgentAppFrame frame, AgentAppController controller, boolean stepMode) {
		this.frame = frame;
		this.controller = controller;
		this.stepMode = stepMode;
	}
	
	/** Cancels and interrupts the thread. */
	@Override
	public void interrupt() {
		cancel();
		super.interrupt();
	}
	
	/**
	 * Calls the run or step method of the controller and then lets the
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
			("Error: Something went wrong running the agent (" + e + ").");
			e.printStackTrace(); // for debugging
		}
		try {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					controller.update(SimulationThread.this);
					frame.setSimulationThread(null);
				}
			});
		} catch(Exception e) {
			logger.log
			("Error: Something went wrong when updating the GUI (" + e + ").");
			e.printStackTrace(); // for debugging
		}
	}
}

