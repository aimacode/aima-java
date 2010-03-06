package aima.gui.applications.search.map;

import java.util.ArrayList;

import aima.core.environment.map.AdaptableHeuristicFunction;
import aima.core.environment.map.ExtendableMap;
import aima.core.environment.map.MapAgent;
import aima.core.environment.map.MapEnvironment;
import aima.core.environment.map.Scenario;
import aima.core.environment.map.SimplifiedRoadMapOfAustralia;
import aima.core.environment.map.SimplifiedRoadMapOfPartOfRomania;
import aima.core.util.datastructure.Point2D;
import aima.gui.applications.search.SearchFactory;
import aima.gui.framework.AgentAppController;
import aima.gui.framework.AgentAppEnvironmentView;
import aima.gui.framework.AgentAppFrame;
import aima.gui.framework.MessageLogger;
import aima.gui.framework.SimpleAgentApp;

/**
 * Demo example of a route finding agent application with GUI. The main method
 * starts a map agent frame and supports runtime experiments. This
 * implementation is based on the {@link aima.core.environment.map.MapAgent} and
 * the {@link aima.core.environment.map.MapEnvironment}. It can be used as a
 * code template for creating new applications with different specialized kinds
 * of agents and environments.
 * 
 * @author R. Lunde
 */
public class RouteFindingAgentApp extends SimpleAgentApp {

	/** Creates a <code>MapAgentView</code>. */
	public AgentAppEnvironmentView createEnvironmentView() {
		return new ExtendedMapAgentView();
	}
	
	/** Creates and configures a <code>RouteFindingAgentFrame</code>. */
	@Override
	public AgentAppFrame createFrame() {
		return new RouteFindingAgentFrame();
	}

	/** Creates a <code>RouteFindingAgentController</code>. */
	@Override
	public AgentAppController createController() {
		return new RouteFindingAgentController();
	}

	// //////////////////////////////////////////////////////////
	// local classes

	/** Frame for a graphical route finding agent application. */
	protected static class RouteFindingAgentFrame extends MapAgentFrame {
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
		public RouteFindingAgentFrame() {
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
		protected void selectionChanged(String changedSelector) {
			SelectionState state = getSelection();
			int scenarioIdx = state.getValue(MapAgentFrame.SCENARIO_SEL);
			RouteFindingAgentFrame.MapType mtype = (scenarioIdx < 3) ? MapType.ROMANIA
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
			super.selectionChanged(changedSelector);
		}
	}

	/** Controller for a graphical route finding agent application. */
	protected static class RouteFindingAgentController extends
			AbstractMapAgentController {
		/**
		 * Configures a scenario and a list of destinations. Note that for route
		 * finding problems, the size of the list needs to be 1.
		 */
		@Override
		protected void selectScenarioAndDest(int scenarioIdx, int destIdx) {
			ExtendableMap map = new ExtendableMap();
			MapEnvironment env = new MapEnvironment(map);
			String agentLoc = null;
			switch (scenarioIdx) {
			case 0:
				SimplifiedRoadMapOfPartOfRomania.initMap(map);
				agentLoc = SimplifiedRoadMapOfPartOfRomania.ARAD;
				break;
			case 1:
				SimplifiedRoadMapOfPartOfRomania.initMap(map);
				agentLoc = SimplifiedRoadMapOfPartOfRomania.LUGOJ;
				break;
			case 2:
				SimplifiedRoadMapOfPartOfRomania.initMap(map);
				agentLoc = SimplifiedRoadMapOfPartOfRomania.FAGARAS;
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
					destinations
							.add(SimplifiedRoadMapOfPartOfRomania.BUCHAREST);
					break;
				case 1:
					destinations.add(SimplifiedRoadMapOfPartOfRomania.EFORIE);
					break;
				case 2:
					destinations.add(SimplifiedRoadMapOfPartOfRomania.NEAMT);
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
		 * Prepares the view for the previously specified scenario and
		 * destinations.
		 */
		@Override
		protected void prepareView() {
			ExtendedMapAgentView mEnv = (ExtendedMapAgentView) frame.getEnvView();
			mEnv.setData(scenario, destinations, null);
			mEnv.setEnvironment(scenario.getEnv());
		}

		/**
		 * Returns the trivial zero function or a simple heuristic which is
		 * based on straight-line distance computation.
		 */
		@Override
		protected AdaptableHeuristicFunction createHeuristic(int heuIdx) {
			AdaptableHeuristicFunction ahf = null;
			switch (heuIdx) {
			case 0:
				ahf = new H1();
				break;
			default:
				ahf = new H2();
			}
			return ahf.adaptToGoal(destinations.get(0), scenario
					.getAgentMap());
		}

		/**
		 * Creates a new agent if no agent exists in the current environment
		 * and executes one step.
		 */
		@Override
		public void step(MessageLogger logger) {
			MapEnvironment env = scenario.getEnv();
			if (env.getAgents().isEmpty()) {
				if (destinations.size() != 1) {
					logger.log("Error: This agent requires exact one destination.");
					return;
				}
				String goal = destinations.get(0);
				MapAgent agent = new MapAgent(env.getMap(), env, search, new String[] { goal });
				env.addAgent(agent, scenario.getInitAgentLocation());
			}
			env.step();
		}
	}
	

	/**
	 * Returns always the heuristic value 0.
	 */
	static class H1 extends AdaptableHeuristicFunction {

		public double h(Object state) {
			return 0.0;
		}
	}

	/**
	 * A simple heuristic which interprets <code>state</code> and {@link #goal}
	 * as location names and uses the straight-line distance between them as
	 * heuristic value.
	 */
	static class H2 extends AdaptableHeuristicFunction {

		public double h(Object state) {
			double result = 0.0;
			Point2D pt1 = map.getPosition((String) state);
			Point2D pt2 = map.getPosition((String) goal);
			if (pt1 != null && pt2 != null)
				result = pt1.distance(pt2);
			return result;
		}
	}

	// //////////////////////////////////////////////////////////
	// starter method

	/** Application starter. */
	public static void main(String args[]) {
		new RouteFindingAgentApp().startApplication();
	}
}
