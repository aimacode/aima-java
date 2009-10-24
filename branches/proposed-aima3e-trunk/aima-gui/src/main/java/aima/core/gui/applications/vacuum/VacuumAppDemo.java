package aima.core.gui.applications.vacuum;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.vacuum.ModelBasedReflexVaccumAgent;
import aima.core.agent.impl.vacuum.ReflexVaccumAgent;
import aima.core.agent.impl.vacuum.SimpleReflexVaccumAgent;
import aima.core.agent.impl.vacuum.TableDrivenVaccumAgent;
import aima.core.agent.impl.vacuum.VaccumEnvironment;
import aima.core.gui.framework.AgentAppController;
import aima.core.gui.framework.AgentAppFrame;
import aima.core.gui.framework.AgentAppModel;
import aima.core.gui.framework.AgentView;
import aima.core.gui.framework.SimpleAgentAppDemo;

/**
 * Simple graphical application for experiments with vacuum cleaner agents. It
 * can be used as a template for creating other graphical agent applications.
 * 
 * @author R. Lunde
 */
public class VacuumAppDemo extends SimpleAgentAppDemo {

	/** Returns a <code>VacuumModel</code>. */
	@Override
	public AgentAppModel createModel() {
		return new VacuumModel();
	}

	/** Returns a <code>VacuumFrame</code>. */
	@Override
	public AgentAppFrame createFrame() {
		return new VacuumFrame();
	}

	/** Returns a <code>VacuumController</code>. */
	@Override
	public AgentAppController createController() {
		return new VacuumController();
	}

	// ///////////////////////////////////////////////////////////////
	// inner classes

	/**
	 * Provides access to the used environment, the names of the locations
	 * (squares) maintained by the environment, and the current state of all
	 * locations.
	 */
	protected static class VacuumModel extends AgentAppModel {
		private VaccumEnvironment env;
		private AbstractAgent agent;

		public void setEnv(VaccumEnvironment env) {
			this.env = env;
		}

		public VaccumEnvironment getEnv() {
			return env;
		}

		public void setAgent(AbstractAgent agent) {
			this.agent = agent;
		}

		public AbstractAgent getAgent() {
			return agent;
		}

		/** Returns the names of all locations used. */
		public List<VaccumEnvironment.Location> getLocations() {
			List<VaccumEnvironment.Location> result = new ArrayList<VaccumEnvironment.Location>();
			if (env != null) {
				result.add(VaccumEnvironment.Location.A);
				result.add(VaccumEnvironment.Location.B);
			}
			return result;
		}

		/** Checks whether the specified location is dirty. */
		public boolean isDirty(VaccumEnvironment.Location location) {
			return VaccumEnvironment.LocationState.Dirty == env.getLocationState(location);
		}

		/** Checks whether the agent is currently at the specified location. */
		public boolean hasAgent(VaccumEnvironment.Location location) {
			return agent != null
					&& location.equals(env.getAgentLocation(agent));
		}
	}

	/**
	 * Adds some selectors to the base class and adjusts its size.
	 */
	protected static class VacuumFrame extends AgentAppFrame {
		public static String ENV_SEL = "EnvSelection";
		public static String AGENT_SEL = "AgentSelection";

		public VacuumFrame() {
			setAgentView(new VacuumView());
			setSelectors(new String[] { ENV_SEL, AGENT_SEL }, new String[] {
					"Select Environment", "Select Agent" });
			setSelectorItems(ENV_SEL, new String[] { "A/B Environment" }, 0);
			setSelectorItems(AGENT_SEL, new String[] {
					"TableDrivenVaccumAgent",
					"ReflexVaccumAgent",
					"SimpleReflexVaccumAgent", 
					"ModelBasedReflexVaccumAgent"
					}, 0);
			setTitle("Vacuum Agent Application");
			setSize(800, 400);
			setUpdateDelay(500);
		}
	}

	/**
	 * Displays the informations provided by the <code>VacuumModel</code> on a
	 * panel using 2D-graphics.
	 */
	protected static class VacuumView extends AgentView {
		Hashtable<VaccumEnvironment.Location, int[]> dirtLookup = new Hashtable<VaccumEnvironment.Location, int[]>();

		int[] getDirt(VaccumEnvironment.Location location) {
			int[] coords = dirtLookup.get(location);
			if (coords == null) {
				java.util.Random rand = new java.util.Random();
				int size = rand.nextInt(8) + 4;
				coords = new int[2 * size];
				for (int i = 0; i < size; i++) {
					coords[2 * i] = rand.nextInt(6) + 1;
					coords[2 * i + 1] = rand.nextInt(8) + 1;
				}
			}
			dirtLookup.put(location, coords);
			return coords;
		}

		/**
		 * Creates a 2D-graphics showing the agent in its environment. Locations
		 * are represented as rectangles, dirt as grey ovals, and the agent as
		 * red circle.
		 */
		@Override
		public void paint(java.awt.Graphics g) {
			VacuumModel vmodel = (VacuumModel) model;
			List<VaccumEnvironment.Location> locations = vmodel.getLocations();
			this.adjustTransformation(0, 0, 11 * locations.size() - 1, 10);
			java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
			g2.setColor(Color.white);
			g2.fillRect(0, 0, getWidth(), getHeight());
			for (int i = 0; i < locations.size(); i++) {
				VaccumEnvironment.Location location = locations.get(i);
				g2.setColor(Color.black);
				g2.drawRect(x(11 * i), y(0), scale(10), scale(10));
				if (vmodel.isDirty(location)) {
					int[] coords = getDirt(location);
					for (int j = 0; j < coords.length; j += 2) {
						g2.setColor(Color.lightGray);
						g2.fillOval(x(11 * i + coords[j]), y(coords[j + 1]),
								scale(3), scale(2));
					}
				}
				g2.setColor(Color.black);
				g2.drawString(location.toString(), x(11 * i) + 10, y(0) + 20);
				if (vmodel.hasAgent(location)) {
					g2.setColor(Color.red);
					g2.fillOval(x(11 * i + 2), y(2), scale(6), scale(6));
				}
			}
		}
	}

	/**
	 * Defines how to react on user button events.
	 */
	protected static class VacuumController extends AgentAppController {
		/** Does nothing. */
		@Override
		public void clearAgent() {
		}

		/**
		 * Creates a vacuum agent and a corresponding environment based on the
		 * selection state of the selectors and finally updates the model.
		 */
		@Override
		public void prepareAgent() {
			AgentAppFrame.SelectionState selState = frame.getSelection();
			VaccumEnvironment env = null;
			AbstractAgent agent = null;
			switch (selState.getValue(VacuumFrame.ENV_SEL)) {
			case 0:
				env = new VaccumEnvironment();
				break;
			}
			switch (selState.getValue(VacuumFrame.AGENT_SEL)) {
			case 0:
				agent = new TableDrivenVaccumAgent();
				break;
			case 1:
				agent = new ReflexVaccumAgent();
				break;
			case 2:
				agent = new SimpleReflexVaccumAgent();
				break;
			case 3:
				agent = new ModelBasedReflexVaccumAgent();
				break;
			}
			((VacuumModel) model).setEnv(env);
			((VacuumModel) model).setAgent(agent);
			if (env != null && agent != null) {
				env.addAgent(agent, VaccumEnvironment.Location.A);
				env.addEnvironmentView(model);
				frame.modelChanged();
			}
		}

		/** Starts the agent and afterwards updates the status of the frame. */
		@Override
		public void runAgent() {
			VacuumModel vmodel = (VacuumModel) model;
			frame.logMessage("<simulation-log>");
			vmodel.getEnv().stepUntilDone();
			frame.logMessage("Performance: "
					+ vmodel.getEnv().getPerformanceMeasure(vmodel.getAgent()));
			frame.logMessage("</simulation-log>");
			frame.setStatus("Task completed.");
		}
	}

	// ///////////////////////////////////////////////////////////////
	// main method

	/**
	 * Starts the application.
	 */
	public static void main(String args[]) {
		new VacuumAppDemo().startApplication();
	}
}
