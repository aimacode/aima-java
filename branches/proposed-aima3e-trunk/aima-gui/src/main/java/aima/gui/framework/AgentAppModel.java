package aima.gui.framework;

import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.agent.EnvironmentView;
import aima.core.agent.Percept;

/**
 * Facade hiding application specific design decisions from the
 * {@link AgentAppFrame}. Subclasses should add methods to access informations
 * about the environment's and the agent's state. Note that any data shown by an
 * agent view must be provided by a corresponding model. The
 * model-view-controller architecture forbids any component of the application
 * except the controller to directly access the view components and it forbids
 * the views to access information without consulting the model.
 * 
 * @author R. Lunde
 */
public class AgentAppModel implements EnvironmentView {
	/** Maintains all views. Typically, just one agent view is maintained here. */
	protected List<ModelChangedListener> listeners = new ArrayList<ModelChangedListener>();

	/** Adds a new view to the model. */
	public void addModelChangedListener(ModelChangedListener listener) {
		listeners.add(listener);
	}

	/** Signals to all registered listeners that the model has changed. */
	public void fireModelChanged() {
		for (AgentAppModel.ModelChangedListener listener : listeners)
			listener.modelChanged();
	}

	// 
	// START-EnvironmentView
	public void notify(String msg) {
		for (ModelChangedListener listener : listeners) {
			listener.logMessage(msg);
			listener.modelChanged();
		}
	}
	
	public void agentAdded(Agent agent, Percept perceives) {
		for (ModelChangedListener listener : listeners) {
			listener.modelChanged();
		}
	}
	
	/**
	 * Reacts on environment changes. This implementation informs all registered
	 * model change listeners and provides their log with the given command.
	 */
	public void agentActed(Agent agent, Action command, EnvironmentState state) {
		for (ModelChangedListener listener : listeners) {
			listener.logMessage(command.toString());
			listener.modelChanged();
		}
	}
	
	// END-EnvironmentView
	//

	// /////////////////////////////////////////////////////////
	// inner classes

	/**
	 * Observer interface for views which need to be informed about model state
	 * changes and log messages to be printed out.
	 */
	public static interface ModelChangedListener {
		public void modelChanged();

		public void logMessage(String message);
	}
}
