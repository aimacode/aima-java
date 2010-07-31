package aima.gui.applications.search.csp;

import java.awt.Color;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.BacktrackingStrategy;
import aima.core.search.csp.CSP;
import aima.core.search.csp.CSPStateListener;
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
			setEnvView(new CSPView());
			setSelectors(new String[] { ENV_SEL, STRATEGY_SEL }, new String[] {
					"Select Environment", "Select Solution Strategy" });
			setSelectorItems(ENV_SEL, new String[] { "Map of Australia" }, 0);
			setSelectorItems(STRATEGY_SEL, new String[] { "Backtracking",
					"Backtracking + Forward Checking",
					"Backtracking + AC3",
					"Min-Conflicts (50)" }, 0);
			setTitle("Map Coloring Application");
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
			case 0: // three moves
				csp = new MapCSP();
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
				break;
			}
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
		private void prepareActions() {
			if (actions.isEmpty()) {
				SolutionStrategy strategy = null;
				switch (frame.getSelection().getValue(
						MapColoringFrame.STRATEGY_SEL)) {
				case 0:
					strategy = new BacktrackingStrategy();
					break;
				case 1:
					strategy = new ImprovedBacktrackingStrategy();
					((ImprovedBacktrackingStrategy) strategy)
							.selectInference(ImprovedBacktrackingStrategy
									.Inference.FORWARD_CHECKING);
					break;
				case 2:
					strategy = new ImprovedBacktrackingStrategy();
					((ImprovedBacktrackingStrategy) strategy)
							.selectInference(ImprovedBacktrackingStrategy
									.Inference.AC3);
					break;
				case 3:
					strategy = new MinConflictsStrategy(50);
					break;
				}

				try {
					strategy.addCSPStateListener(new CSPStateListener() {
						@Override
						public void stateChanged(CSP csp) {
							actions.add(new CSPEnvironment.StateChangeAction(
									csp));
						}

						@Override
						public void stateChanged(Assignment assignment) {
							actions.add(new CSPEnvironment.StateChangeAction(
									assignment));
						}
					});
					strategy.solve(env.getCSP());
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
