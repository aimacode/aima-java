package aimax.osm.routing.agent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Agent;
import aima.core.environment.map.AdaptableHeuristicFunction;
import aima.core.environment.map.BidirectionalMapProblem;
import aima.core.environment.map.MapAgent;
import aima.core.environment.map.MapEnvironment;
import aima.core.environment.map.MapFunctionFactory;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.online.LRTAStarAgent;
import aima.core.search.online.OnlineSearchProblem;
import aima.core.util.datastructure.Point2D;
import aima.gui.applications.search.SearchFactory;
import aima.gui.applications.search.map.MapAgentFrame;
import aima.gui.framework.AgentAppController;
import aimax.osm.data.MapDataEvent;
import aimax.osm.data.MapDataStore;
import aimax.osm.data.MapWayAttFilter;
import aimax.osm.data.entities.MapNode;

/** Controller for a graphical OSM agent application. */
public class OsmAgentController extends AgentAppController {
	
	protected OsmMap map;
	protected MapEnvironment env;
	/** Search method to be used. */
	protected Search search;
	/** Heuristic function to be used when performing informed search. */
	protected AdaptableHeuristicFunction heuristic;
	
	protected List<String> markedLocations;
	
	public OsmAgentController(OsmMap map) {
		this.map = map;
		markedLocations = new ArrayList<String>();
	}
	
	@Override
	public void clearAgent() {
		map.getMapData().clearMarksAndTracks();
	}

	@Override
	public void prepareAgent() {
		env = new MapEnvironment(map);
		env.addEnvironmentView(model);
		((OsmAgentModel) model).prepare(env);
		MapAgentFrame.SelectionState state = frame.getSelection();
		
		MapDataStore mapData = map.getMapData();
		switch (state.getValue(MapAgentFrame.SCENARIO_SEL)) {
		case 0: map.setMapWayFilter
		(MapWayAttFilter.createAnyWayFilter(mapData));
		map.ignoreOneways(true); break;
		case 1: map.setMapWayFilter
		(MapWayAttFilter.createCarWayFilter(mapData));
		map.ignoreOneways(false); break;
		case 2: map.setMapWayFilter
		(MapWayAttFilter.createBicycleWayFilter(mapData));
		map.ignoreOneways(false); break;
		}
		
		heuristic = createHeuristic(state.getValue(MapAgentFrame.HEURISTIC_SEL));
		search = SearchFactory.getInstance().createSearch(
				state.getValue(MapAgentFrame.SEARCH_SEL),
				state.getValue(MapAgentFrame.SEARCH_MODE_SEL),
				heuristic);
		
		mapData.getTracks().clear();
		mapData.fireMapDataEvent
		(new MapDataEvent(mapData, MapDataEvent.Type.MAP_MODIFIED));
		//frame.logMessage("prepared.");
	}

	/**
	 * Returns the trivial zero function or a simple heuristic which is
	 * based on straight-line distance computation.
	 */
	protected AdaptableHeuristicFunction createHeuristic(int heuIdx) {
		AdaptableHeuristicFunction ahf = null;
		switch (heuIdx) {
		case 0:
			ahf = new H1();
			break;
		default:
			ahf = new H2();
		}
		return ahf;
	}
	
	/**
	 * Template method, which starts the agent and afterwards updates
	 * the status bar of the frame.
	 */
	@Override
	public void runAgent() {
		startAgent();
		List<Agent> agents = env.getAgents();
		if (agents.size() == 1) {
			Double travelDistance = env.getAgentTravelDistance(
					agents.get(0));
			StringBuffer statusMsg = new StringBuffer();
			statusMsg.append("Task completed");
			if (travelDistance != null) {
				DecimalFormat f = new DecimalFormat("#0.0");
				statusMsg.append("; travel distance: "
						+ f.format(travelDistance));
			}
			statusMsg.append(".");
			frame.setStatus(statusMsg.toString());
		}
	}
	
	/**
	 * Primitive operation, which creates environment and agent,
	 * starts the agent and initiates some text outputs describing the
	 * state of the agent.
	 */
	protected void startAgent() {
		List<MapNode> marks = map.getMapData().getMarks();
		if (marks.size() < 2) {
			frame.logMessage("Error: Please set two marks with MouseLeft.");
			return;
		}
		String[] locs = new String[2];
		for (int i = 0; i < 2; i++) {
			MapNode node = (i==0) ? marks.get(0) : marks.get(marks.size()-1);
			Point2D pt = new Point2D(node.getLon(), node.getLat());
			locs[i] = map.getNearestLocation(pt);
		}
		frame.logMessage("<osm-agent-simulation-protocol>");
		Agent agent = null;
		MapAgentFrame.SelectionState state = frame.getSelection();
		switch (state.getValue(MapAgentFrame.AGENT_SEL)) {
		case 0:
			frame.logMessage("search: " + search.getClass().getName());
			heuristic.adaptToGoal(locs[1], map);
			agent = new MapAgent(map, env, search, new String[] { locs[1] });
			break;
		case 1:
			Problem p = new BidirectionalMapProblem(map, null, locs[1]);
			OnlineSearchProblem osp = new OnlineSearchProblem
			(p.getActionsFunction(), p.getGoalTest(), p.getStepCostFunction());
			heuristic.adaptToGoal(locs[1], map);
			agent = new LRTAStarAgent
			(osp, MapFunctionFactory.getPerceptToStateFunction(), heuristic);
			break;
		}
		frame.logMessage("heuristic: " + heuristic.getClass().getName());
		env.addAgent(agent, locs[0]);
		env.stepUntilDone();
		frame.logMessage("</osm-agent-simulation-protocol>\n");
	}

	
	// //////////////////////////////////////////////////////////
	// local classes
	
	/**
	 * Returns always the heuristic value 0.
	 */
	static class H1 extends AdaptableHeuristicFunction {

		public double h(Object state) {
			return 0.0;
		}
	}

	/**
	 * A simple heuristic which interprets <code>state</code> and
	 * {@link #goal} as location names and uses the straight-line distance
	 * between them as heuristic value.
	 */
	static class H2 extends AdaptableHeuristicFunction {

		public double h(Object state) {
			double result = 0.0;
			Point2D pt1 = map.getPosition((String) state);
			Point2D pt2 = map.getPosition((String) goal);
			if (pt1 != null && pt2 != null)
				result = pt1.distance(pt2);
			//System.out.println(state + ": " + result);
			return result;
		}
	}
}