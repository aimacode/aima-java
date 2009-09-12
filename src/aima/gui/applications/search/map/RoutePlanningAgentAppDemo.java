package aima.gui.applications.search.map;

import java.util.ArrayList;

import aima.gui.framework.AgentAppController;
import aima.gui.framework.AgentAppFrame;
import aima.gui.framework.AgentAppModel;
import aima.gui.framework.SimpleAgentAppDemo;
import aima.search.framework.SearchFactory;
import aima.search.map.AdaptableHeuristicFunction;
import aima.search.map.MapAgent;
import aima.search.map.MapEnvironment;
import aima.search.map.MapWithSLD;
import aima.search.map.Scenario;
import aima.search.map.SimplifiedRoadMapOfAustralia;
import aima.search.map.SimplifiedRoadMapOfRomania;

/**
 * Demo example of a route planning agent application with GUI. The main method
 * starts a map agent frame and supports runtime experiments. This
 * implementation is based on the {@link aima.search.map.MapAgent} and the
 * {@link aima.search.map.MapEnvironment} from the aima library. It can be used
 * as a code template for creating new applications with different specialized
 * kinds of agents and environments.
 * 
 * @author R. Lunde
 */
public class RoutePlanningAgentAppDemo extends SimpleAgentAppDemo {

	/** Creates a <code>MapAgentModel</code>. */
	@Override
	public AgentAppModel createModel() {
		return new MapAgentModel();
	}

	/** Creates and configures a <code>RoutePlanningAgentFrame</code>. */
	@Override
	public AgentAppFrame createFrame() {
		return new RoutePlanningAgentFrame();
	}

	/** Creates a <code>RoutePlanningAgentController</code>. */
	@Override
	public AgentAppController createController() {
		return new RoutePlanningAgentController();
	}

	// //////////////////////////////////////////////////////////
	// local classes

	/** Frame for a graphical route planning agent application. */
	protected static class RoutePlanningAgentFrame extends MapAgentFrame {
		public static enum MapType {
			ROMANIA, AUSTRALIA
		};

		private MapType usedMap = null;
		private static String[] ROMANIA_DESTS = new String[] {
				"D1 (to Bucharest)", "D2 (to Eforie)", "D3 (to Neamt)",
				"D4 (to random)" };
		private static String[] AUSTRALIA_DESTS = new String[] {
				"D1 (to Port Hedland)", "D2 (to Albany)", "D3 (to Melbourne)",
				"D4 (to random)" };

		/** Creates a new frame. */
		public RoutePlanningAgentFrame() {
			setTitle("RPA - the Route Planning Agent");
			setSelectorItems(SCENARIO_SEL, new String[] {
					"S1 (Romania, from Arad)", "S2 (Romania, from Lugoj)",
					"S3 (Romania, from Fagaras)",
					"S4 (Australia, from Sydney)",
					"S4 (Australia, from Random)" }, 0);
			setSelectorItems(SEARCH_MODE_SEL, SearchFactory.getInstance()
					.getSearchModeNames(), 1); // change the default!
			setSelectorItems(HEURISTIC_SEL, new String[] { "H1 (=0)",
					"H2 (sld to goal)" }, 1);
		}

		/**
		 * Changes the destination selector items depending on the scenario
		 * selection if necessary, and calls the super class implementation
		 * afterwards.
		 */
		@Override
		protected void selectionChanged() {
			SelectionState state = getSelection();
			int scenarioIdx = state.getValue(MapAgentFrame.SCENARIO_SEL);
			RoutePlanningAgentFrame.MapType mtype = (scenarioIdx < 3) ? MapType.ROMANIA
					: MapType.AUSTRALIA;
			if (mtype != usedMap) {
				usedMap = mtype;
				String[] items = null;
				switch (mtype) {
				case ROMANIA:
					items = ROMANIA_DESTS;
					break;
				case AUSTRALIA:
					items = AUSTRALIA_DESTS;
					break;
				}
				setSelectorItems(DESTINATION_SEL, items, 0);
			}
			super.selectionChanged();
		}
	}

	/** Controller for a graphical route planning agent application. */
	protected static class RoutePlanningAgentController extends
			AbstractMapAgentController {
		/**
		 * Configures a scenario and a list of destinations. Note that for route
		 * planning problems, the size of the list needs to be 1.
		 */
		@Override
		protected void selectScenarioAndDest(int scenarioIdx, int destIdx) {
			MapWithSLD map = new MapWithSLD();
			MapEnvironment env = new MapEnvironment(map);
			String agentLoc = null;
			switch (scenarioIdx) {
			case 0:
				SimplifiedRoadMapOfRomania.initMap(map);
				agentLoc = SimplifiedRoadMapOfRomania.ARAD;
				break;
			case 1:
				SimplifiedRoadMapOfRomania.initMap(map);
				agentLoc = SimplifiedRoadMapOfRomania.LUGOJ;
				break;
			case 2:
				SimplifiedRoadMapOfRomania.initMap(map);
				agentLoc = SimplifiedRoadMapOfRomania.FAGARAS;
				break;
			case 3:
				SimplifiedRoadMapOfAustralia.initMap(map);
				agentLoc = SimplifiedRoadMapOfAustralia.SYDNEY;
				break;
			case 4:
				SimplifiedRoadMapOfAustralia.initMap(map);
				agentLoc = map.randomlyGenerateDestination();
				break;
			}
			scenario = new Scenario(env, map, agentLoc);

			destinations = new ArrayList<String>();
			if (scenarioIdx < 3) {
				switch (destIdx) {
				case 0:
					destinations.add(SimplifiedRoadMapOfRomania.BUCHAREST);
					break;
				case 1:
					destinations.add(SimplifiedRoadMapOfRomania.EFORIE);
					break;
				case 2:
					destinations.add(SimplifiedRoadMapOfRomania.NEAMT);
					break;
				case 3:
					destinations.add(map.randomlyGenerateDestination());
					break;
				}
			} else {
				switch (destIdx) {
				case 0:
					destinations.add(SimplifiedRoadMapOfAustralia.PORT_HEDLAND);
					break;
				case 1:
					destinations.add(SimplifiedRoadMapOfAustralia.ALBANY);
					break;
				case 2:
					destinations.add(SimplifiedRoadMapOfAustralia.MELBOURNE);
					break;
				case 3:
					destinations.add(map.randomlyGenerateDestination());
					break;
				}
			}
		}

		/**
		 * Prepares the model for the previously specified scenario and
		 * destinations.
		 */
		@Override
		protected void prepareModel() {
			((MapAgentModel) model).prepare(scenario, destinations);
		}

		/**
		 * Returns the trivial zero function or a simple heuristic which is
		 * based on straight-line distance computation.
		 */
		@Override
		protected AdaptableHeuristicFunction createHeuristic(int heuIdx) {
			switch (heuIdx) {
			case 0:
				return new H1();
			default:
				return new H2();
			}
		}

		/**
		 * Creates environment and agent, starts the agent and initiates some
		 * text outputs describing the state of the agent.
		 */
		@Override
		protected void startAgent() {
			if (destinations.size() != 1) {
				frame
						.logMessage("Error: This agent requires exact one destination.");
				return;
			}
			frame.logMessage("<route-planning-simulation-protocol>");
			frame.logMessage("search: " + search.getClass().getName());
			MapEnvironment env = scenario.getEnv();
			String goal = destinations.get(0);
			MapAgent agent = new MapAgent(env, search, new String[] { goal });
			if (heuristic != null) {
				frame
						.logMessage("heuristic: "
								+ heuristic.getClass().getName());
				agent.setHeuristicFunction(heuristic.getAdaptation(goal,
						scenario.getAgentMap()));
			}
			env.addAgent(agent, scenario.getInitAgentLocation());
			env.stepUntilNoOp();
			frame.logMessage("</route-planning-simulation-protocol>\n");
		}
	}

	/**
	 * Returns always the heuristic value 0.
	 */
	static class H1 extends AdaptableHeuristicFunction {
		@Override
		public double getHeuristicValue(Object state) {
			return 0.0;
		}
	}

	/**
	 * A simple heuristic which interprets <code>state</code> and
	 * {@link #goal} as location names and uses the straight-line distance
	 * between them as heuristic value.
	 */
	static class H2 extends AdaptableHeuristicFunction {
		@Override
		public double getHeuristicValue(Object state) {
			return map.getStraightLineDistance((String) state, (String) goal);
		}
	}

	// //////////////////////////////////////////////////////////
	// starter method

	/** Application starter. */
	public static void main(String args[]) {
		new RoutePlanningAgentAppDemo().startApplication();
	}
}
