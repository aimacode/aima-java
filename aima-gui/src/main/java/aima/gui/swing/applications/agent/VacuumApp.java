package aima.gui.swing.applications.agent;

import aima.core.agent.Action;
import aima.core.agent.impl.DynamicPercept;
import aima.gui.swing.framework.AgentAppController;
import aima.gui.swing.framework.AgentAppEnvironmentView;
import aima.gui.swing.framework.AgentAppFrame;
import aima.gui.swing.framework.SimpleAgentApp;

/**
 * Simple graphical application for experiments with vacuum cleaner agents. It
 * can be used as a template for creating other graphical agent applications.
 * 
 * @author Ruediger Lunde
 */
public class VacuumApp extends SimpleAgentApp<DynamicPercept, Action> {

	/** Returns a <code>VacuumView</code> instance. */
	@Override
	public AgentAppEnvironmentView<DynamicPercept, Action> createEnvironmentView() {
		return new VacuumView();
	}
	
	/** Returns a <code>VacuumFrame</code> instance. */
	@Override
	public AgentAppFrame<DynamicPercept, Action> createFrame() {
		return new VacuumFrame();
	}

	/** Returns a <code>VacuumController</code> instance. */
	@Override
	public AgentAppController<DynamicPercept, Action> createController() {
		return new VacuumController();
	}

	
	/////////////////////////////////////////////////////////////////
	// main method

	/**
	 * Starts the application.
	 */
	public static void main(String args[]) {
		new VacuumApp().startApplication();
	}
}
