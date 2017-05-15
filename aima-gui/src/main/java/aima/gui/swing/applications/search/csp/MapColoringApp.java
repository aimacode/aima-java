package aima.gui.swing.applications.search.csp;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Queue;

import aima.core.search.csp.*;
import aima.core.search.csp.examples.MapCSP;
import aima.core.search.csp.inference.AC3Strategy;
import aima.core.search.csp.inference.ForwardCheckingStrategy;
import aima.gui.swing.framework.AgentAppController;
import aima.gui.swing.framework.AgentAppEnvironmentView;
import aima.gui.swing.framework.AgentAppFrame;
import aima.gui.swing.framework.MessageLogger;
import aima.gui.swing.framework.SimpleAgentApp;
import aima.gui.swing.framework.SimulationThread;

/**
 * Application which demonstrates basic constraint algorithms based on map
 * coloring problems. It shows the constraint graph, lets the user select a
 * solution strategy, and allows then to follow the progress step by step. For
 * pragmatic reasons, the implementation uses the agent framework, even though
 * there is no agent and only a dummy environment.
 * 
 * @author Ruediger Lunde
 */
public class MapColoringApp extends SimpleAgentApp {

	/** Returns an <code>CSPView</code> instance. */
	@Override
	public AgentAppEnvironmentView createEnvironmentView() {
		return new CSPView();
	}

	/** Returns a <code>MapColoringFrame</code> instance. */
	@Override
	public AgentAppFrame createFrame() {
		return new MapColoringFrame();
	}

	/** Returns a <code>MapColoringController</code> instance. */
	@Override
	public AgentAppController createController() {
		return new MapColoringController();
	}

	
	// ///////////////////////////////////////////////////////////////
	// main method

	/**
	 * Starts the application.
	 */
	public static void main(String args[]) {
		new MapColoringApp().startApplication();
	}

	
	// ///////////////////////////////////////////////////////////////
	// some inner classes

	/**
	 * Adds some selectors to the base class and adjusts its size.
	 */
	protected static class MapColoringFrame extends AgentAppFrame {
		private static final long serialVersionUID = 1L;
		public static String ENV_SEL = "EnvSelection";
		public static String STRATEGY_SEL = "SearchSelection";

		public MapColoringFrame() {
			setTitle("Map Coloring Application");
			setSelectors(new String[] { ENV_SEL, STRATEGY_SEL }, new String[] {
					"Select Environment", "Select Solution Strategy" });
			setSelectorItems(ENV_SEL, new String[] { "Map of Australia",
					"Map of Australia NSW=BLUE (for LCV)",
					"Map of Australia WA=RED (for LCV)"}, 0);
			setSelectorItems(STRATEGY_SEL, new String[] { "Backtracking",
					"Backtracking + MRV & DEG",
					"Backtracking + Forward Checking",
					"Backtracking + Forward Checking + MRV",
					"Backtracking + Forward Checking + LCV",
					"Backtracking + AC3",
					"Backtracking + AC3 + MRV & DEG + LCV",
					"Min-Conflicts (50)" }, 0);
			setEnvView(new CSPView());
			setSize(800, 600);
		}
	}

	/**
	 * Defines how to react on standard simulation button events.
	 */
	protected static class MapColoringController extends AgentAppController {

		protected CSPEnvironment env;
		protected SolutionStrategy strategy;
		protected Queue<CSPEnvironment.StateChangeAction> actions; // FIFOQueue
		protected int actionCount;

		protected MapColoringController() {
			env = new CSPEnvironment();
			actions = new LinkedList<CSPEnvironment.StateChangeAction>();
		}

		protected CSPView getCSPView() {
			return (CSPView) frame.getEnvView();
		}

		/** Prepares next simulation. */
		@Override
		public void clear() {
			prepare(null);
		}

		/**
		 * Creates a CSP and updates the environment as well as its view.
		 */
		@Override
		public void prepare(String changedSelector) {
			AgentAppFrame.SelectionState selState = frame.getSelection();
			CSP csp = null;
			CSPView view = getCSPView();
			switch (selState.getIndex(MapColoringFrame.ENV_SEL)) {
			case 0:
				csp = new MapCSP();
				break;
			case 1: // three moves
				csp = new MapCSP();
				csp.setDomain(MapCSP.NSW, new Domain(new Object[]{MapCSP.BLUE}));
				break;
			case 2: // three moves
				csp = new MapCSP();
				csp.setDomain(MapCSP.WA, new Domain(new Object[]{MapCSP.RED}));
				break;
			}
			view.clearMappings();
			view.setPositionMapping(MapCSP.WA, 5, 10);
			view.setPositionMapping(MapCSP.NT, 15, 3);
			view.setPositionMapping(MapCSP.SA, 20, 15);
			view.setPositionMapping(MapCSP.Q, 30, 5);
			view.setPositionMapping(MapCSP.NSW, 35, 15);
			view.setPositionMapping(MapCSP.V, 30, 23);
			view.setPositionMapping(MapCSP.T, 33, 30);
			view.setColorMapping(MapCSP.RED, Color.RED);
			view.setColorMapping(MapCSP.GREEN, Color.GREEN);
			view.setColorMapping(MapCSP.BLUE, Color.BLUE);
			
			actions.clear();
			actionCount = 0;
			env.init(csp);
			view.setEnvironment(env);
		}

		/** Checks whether simulation can be started. */
		@Override
		public boolean isPrepared() {
			return env.getCSP() != null
					&& (!actions.isEmpty() || env.getAssignment() == null);
		}

		/** Starts simulation. */
		@Override
		public void run(MessageLogger logger) {
			logger.log("<simulation-log>");
			prepareActions();
			try {
				while (!actions.isEmpty() && !frame.simulationPaused()) {
					env.executeAction(null, actions.remove());
					actionCount++;
					Thread.sleep(200);
				}
				logger.log("Number of Steps: " + actionCount);
				// logger.log(getStatistics());
			} catch (InterruptedException e) {
				// nothing to do here.
			}
			logger.log("</simulation-log>\n");
		}

		/** Performs a simulation step. */
		@Override
		public void step(MessageLogger logger) {
			prepareActions();
			if (!actions.isEmpty()) {
				env.executeAction(null, actions.remove());
				actionCount++;
				if (actions.isEmpty())
					logger.log("Number of Steps: " + actionCount);
			}
		}

		/**
		 * Starts the selected constraint solver and fills the action list if
		 * necessary.
		 */
		protected void prepareActions() {
			if (actions.isEmpty()) {
				SolutionStrategy strategy = null;
				switch (frame.getSelection().getIndex(MapColoringFrame.STRATEGY_SEL)) {
				case 0:
					strategy = new BacktrackingStrategy();
					break;
				case 1: // MRV + DEG
					strategy = new BacktrackingStrategy().set(CspHeuristics.mrvDeg());
					break;
				case 2: // FC
					strategy = new BacktrackingStrategy().set(new ForwardCheckingStrategy());
					break;
				case 3: // MRV + FC 
					strategy = new BacktrackingStrategy().set(CspHeuristics.mrvDeg()).set(new ForwardCheckingStrategy());
					break;
				case 4: // LCV + FC
					strategy = new BacktrackingStrategy().set(CspHeuristics.lcv()).set(new ForwardCheckingStrategy());
					break;
				case 5: // AC3
					strategy = new BacktrackingStrategy().set(new AC3Strategy());
					break;
				case 6: // MRV + DEG + LCV + AC3
					strategy = new BacktrackingStrategy().set(CspHeuristics.mrvDeg()).set(CspHeuristics.lcv())
							.set(new AC3Strategy());
					break;
				case 7:
					strategy = new MinConflictsStrategy(50);
					break;
				}

				try {
					strategy.addCSPStateListener(new CspListener() {
						@Override
						public void stateChanged(Assignment assignment, CSP csp) {
							actions.add(new CSPEnvironment.StateChangeAction(
									assignment, csp));
						}
						@Override
						public void stateChanged(CSP csp) {
							actions.add(new CSPEnvironment.StateChangeAction(
									csp));
						}
					});
					strategy.solve(env.getCSP().copyDomains());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		/** Updates the status of the frame after simulation has finished. */
		public void update(SimulationThread simulationThread) {
			if (simulationThread.isCanceled()) {
				frame.setStatus("Task canceled.");
			} else if (frame.simulationPaused()) {
				frame.setStatus("Task paused.");
			} else {
				frame.setStatus("Task completed.");
			}
		}
	}
}
