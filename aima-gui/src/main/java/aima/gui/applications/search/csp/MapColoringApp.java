package aima.gui.applications.search.csp;

import java.awt.Color;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.BacktrackingStrategy;
import aima.core.search.csp.CSP;
import aima.core.search.csp.CSPStateListener;
import aima.core.search.csp.Domain;
import aima.core.search.csp.ImprovedBacktrackingStrategy;
import aima.core.search.csp.MapCSP;
import aima.core.search.csp.MinConflictsStrategy;
import aima.core.search.csp.SolutionStrategy;
import aima.core.util.datastructure.FIFOQueue;
import aima.gui.framework.AgentAppController;
import aima.gui.framework.AgentAppEnvironmentView;
import aima.gui.framework.AgentAppFrame;
import aima.gui.framework.MessageLogger;
import aima.gui.framework.SimpleAgentApp;
import aima.gui.framework.SimulationThread;

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
		protected FIFOQueue<CSPEnvironment.StateChangeAction> actions;
		protected int actionCount;

		protected MapColoringController() {
			env = new CSPEnvironment();
			actions = new FIFOQueue<CSPEnvironment.StateChangeAction>();
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
			switch (selState.getValue(MapColoringFrame.ENV_SEL)) {
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
					env.executeAction(null, actions.pop());
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
				env.executeAction(null, actions.pop());
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
			ImprovedBacktrackingStrategy iStrategy = null;
			if (actions.isEmpty()) {
				SolutionStrategy strategy = null;
				switch (frame.getSelection().getValue(
						MapColoringFrame.STRATEGY_SEL)) {
				case 0:
					strategy = new BacktrackingStrategy();
					break;
				case 1: // MRV + DEG
					strategy = new ImprovedBacktrackingStrategy
					(true, true, false, false);
					break;
				case 2: // FC
					iStrategy = new ImprovedBacktrackingStrategy();
					iStrategy.setInference(ImprovedBacktrackingStrategy
									.Inference.FORWARD_CHECKING);
					break;
				case 3: // MRV + FC 
					iStrategy = new ImprovedBacktrackingStrategy
					(true, false, false, false);
					iStrategy.setInference(ImprovedBacktrackingStrategy
									.Inference.FORWARD_CHECKING);
					break;
				case 4: // FC + LCV
					iStrategy = new ImprovedBacktrackingStrategy
					(false, false, false, true);
					iStrategy.setInference(ImprovedBacktrackingStrategy
									.Inference.FORWARD_CHECKING);
					break;
				case 5: // AC3
					strategy = new ImprovedBacktrackingStrategy
					(false, false, true, false);
					break;
				case 6: // MRV + DEG + AC3 + LCV
					strategy = new ImprovedBacktrackingStrategy
					(true, true, true, true);
					break;
				case 7:
					strategy = new MinConflictsStrategy(50);
					break;
				}
				if (iStrategy != null)
					strategy = iStrategy;
				
				try {
					strategy.addCSPStateListener(new CSPStateListener() {
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
