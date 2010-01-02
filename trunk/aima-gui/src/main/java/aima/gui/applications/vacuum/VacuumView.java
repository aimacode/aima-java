package aima.gui.applications.vacuum;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.environment.vacuum.VacuumEnvironment;
import aima.gui.framework.EmptyEnvironmentView;

/**
 * Displays the informations provided by the <code>VacuumModel</code> on a
 * panel using 2D-graphics.
 */
public class VacuumView extends EmptyEnvironmentView {
	Hashtable<Object, int[]> dirtOvalLookup = new Hashtable<Object, int[]>();

	@Override
	public void agentActed(Agent agent, Action action,
			EnvironmentState resultingState) {
		super.agentActed(agent, action, resultingState);
		notify(action.toString());
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
		List<Object> locations = getLocations();
		this.adjustTransformation(0, 0, 11 * locations.size() - 1, 10);
		java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
		g2.setColor(Color.white);
		g2.fillRect(0, 0, getWidth(), getHeight());
		for (int i = 0; i < locations.size(); i++) {
			VacuumEnvironment.Location location =
				(VacuumEnvironment.Location) locations.get(i);
			g2.setColor(Color.black);
			g2.drawRect(x(11 * i), y(0), scale(10), scale(10));
			if (isDirty(location)) {
				int[] coords = getDirt(location);
				for (int j = 0; j < coords.length; j += 2) {
					g2.setColor(Color.lightGray);
					g2.fillOval(x(11 * i + coords[j]), y(coords[j + 1]),
							scale(3), scale(2));
				}
			}
			g2.setColor(Color.black);
			g2.drawString(location.toString(), x(11 * i) + 10, y(0) + 20);
			if (hasAgent(location)) {
				g2.setColor(Color.red);
				g2.fillOval(x(11 * i + 2), y(2), scale(6), scale(6));
			}
		}
	}
	
	/** Returns the names of all locations used. */
	protected List<Object> getLocations() {
		List<Object> result = new ArrayList<Object>();
		if (env != null) {
			result.add(VacuumEnvironment.Location.A);
			result.add(VacuumEnvironment.Location.B);
		}
		return result;
	}

	/** Checks whether the specified location is dirty. */
	protected boolean isDirty(Object location) {
		return VacuumEnvironment.LocationState.Dirty == getVacuumEnv()
				.getLocationState((VacuumEnvironment.Location) location);
	}

	/** Checks whether the agent is currently at the specified location. */
	protected boolean hasAgent(Object location) {
		VacuumEnvironment e = getVacuumEnv();
		for (Agent a : e.getAgents())
			if (location.equals(e.getAgentLocation(a)))
				return true;
		return false;
	}
	
	/** Returns x and y coordinates for printing dirt ovals. */
	private int[] getDirt(Object location) {
		int[] coords = dirtOvalLookup.get(location);
		if (coords == null) {
			java.util.Random rand = new java.util.Random();
			int size = rand.nextInt(8) + 4;
			coords = new int[2 * size];
			for (int i = 0; i < size; i++) {
				coords[2 * i] = rand.nextInt(6) + 1;
				coords[2 * i + 1] = rand.nextInt(8) + 1;
			}
		}
		dirtOvalLookup.put(location, coords);
		return coords;
	}
}

