package aima.gui.applications.vacuum;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.environment.vacuum.VacuumEnvironment;
import aima.gui.framework.EmptyEnvironmentView;

/**
 * Displays the informations provided by a <code>VacuumEnvironment</code> on a
 * panel using 2D-graphics.
 */
public class VacuumView extends EmptyEnvironmentView {
	
	@Override
	public void agentActed(Agent agent, Action action,
			EnvironmentState resultingState) {
		String prefix = "";
		if (env.getAgents().size() > 1)
			prefix = "A" + env.getAgents().indexOf(agent) + ": ";
		super.agentActed(agent, action, resultingState);
		notify(prefix + action.toString());
	}
	
	protected VacuumEnvironment getVacuumEnv() {
		return (VacuumEnvironment) env;
	}
	
	/**
	 * Creates a 2D-graphics showing the agent in its environment. Locations
	 * are represented as rectangles, dirt as grey ovals, and the agent as
	 * red circle.
	 */
	@Override
	public void paint(java.awt.Graphics g) {
		List<String> locations = getLocations();
		adjustTransformation(0, 0, 11 * locations.size() - 1, 10);
		java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
		g2.setColor(Color.white);
		g2.fillRect(0, 0, getWidth(), getHeight());
		for (int i = 0; i < locations.size(); i++) {
			String location = locations.get(i);
			if (isDirty(location)) {
				g2.setColor(Color.LIGHT_GRAY);
				g2.fillRect(x(11 * i), y(0), scale(10), scale(10));
			}
			g2.setColor(Color.black);
			g2.drawRect(x(11 * i), y(0), scale(10), scale(10));
			g2.drawString(location.toString(), x(11 * i) + 10, y(0) + 20);
			if (hasAgent(location)) {
				g2.setColor(Color.red);
				g2.fillArc(x(11 * i + 2), y(2), scale(6), scale(6),
						200, 320);
			}
		}
	}
	
	/** Returns the names of all locations used. */
	protected List<String> getLocations() {
		List<String> result = new ArrayList<String>();
		if (env != null) {
			result.add(VacuumEnvironment.LOCATION_A);
			result.add(VacuumEnvironment.LOCATION_B);
		}
		return result;
	}

	/** Checks whether the specified location is dirty. */
	protected boolean isDirty(String location) {
		return VacuumEnvironment.LocationState.Dirty == getVacuumEnv()
				.getLocationState(location);
	}

	/** Checks whether the agent is currently at the specified location. */
	protected boolean hasAgent(Object location) {
		VacuumEnvironment e = getVacuumEnv();
		for (Agent a : e.getAgents())
			if (location.equals(e.getAgentLocation(a)))
				return true;
		return false;
	}
}

