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
import aima.gui.applications.search.map.MapAgentFrame;
import aima.gui.applications.search.map.SearchFactory;
import aima.gui.framework.AgentAppController;
import aima.gui.framework.MessageLogger;
import aima.gui.framework.SimulationThread;
import aimax.osm.data.MapWayAttFilter;
import aimax.osm.data.entities.MapNode;

/**
 * Controller for graphical OSM map agent applications.
 * @author Ruediger Lunde
 */
public class OsmAgentController extends AgentAppController {
	
	protected MapAdapter map;
	protected MapEnvironment env;
	/** Search method to be used. */
	protected Search search;
	/** Heuristic function to be used when performing informed search. */
	protected AdaptableHeuristicFunction heuristic;
	
	protected List<String> markedLocations;
	protected boolean isPrepared;
	/** Sleep time between two steps during simulation in msec. */
	protected long sleepTime = 0l;
	
	public OsmAgentController(MapAdapter map) {
		this.map = map;
		markedLocations = new ArrayList<String>();
	}
	
	@Override
	public void clear() {
		map.getOsmMap().clearMarkersAndTracks();
		prepare(null);
	}

	@Override
	public void prepare(String changedSelector) {
		env = new MapEnvironment(map);
		MapAgentFrame.SelectionState state = frame.getSelection();
		
		map.getOsmMap().getTracks().clear();
		switch (state.getValue(MapAgentFrame.SCENARIO_SEL)) {
		case 0: map.setMapWayFilter
		(MapWayAttFilter.createAnyWayFilter());
		map.ignoreOneways(true); break;
		case 1: map.setMapWayFilter
		(MapWayAttFilter.createCarWayFilter());
		map.ignoreOneways(false); break;
		case 2: map.setMapWayFilter
		(MapWayAttFilter.createBicycleWayFilter());
		map.ignoreOneways(false); break;
		}
		
		heuristic = createHeuristic(state.getValue(MapAgentFrame.HEURISTIC_SEL));
		search = SearchFactory.getInstance().createSearch(
				state.getValue(MapAgentFrame.SEARCH_SEL),
				state.getValue(MapAgentFrame.SEARCH_MODE_SEL),
				heuristic);
		frame.getEnvView().setEnvironment(env);
		isPrepared = true;
	}
	
	/**
	 * Checks whether the current environment is ready to start
	 * simulation.
	 */
	@Override
	public boolean isPrepared() {
		return isPrepared && (env.getAgents().isEmpty() || !env.isDone());
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
	 * Calls {@link #initAgents(MessageLogger)} if necessary and
	 * then starts simulation until done.
	 */
	@Override
	public void run(MessageLogger logger) {
		logger.log("<simulation-protocol>");
		logger.log("search: " + search.getClass().getName());
		logger.log("heuristic: " + heuristic.getClass().getName());
		if (env.getAgents().isEmpty())
			initAgents(logger);
		try {
			while (!env.isDone() && !frame.simulationPaused()) {
				Thread.sleep(sleepTime);
				env.step();
			}
		} catch (InterruptedException e) {}
		logger.log("</simulation-protocol>\n");
	}
	
	/**
	 * Calls {@link #initAgents(MessageLogger)} if necessary and
	 * then executes one simulation step.
	 */
	@Override
	public void step(MessageLogger logger) {
		if (env.getAgents().isEmpty())
			initAgents(logger);
		env.step();
	}

	/** Creates new agents and adds them to the current environment. */
	protected void initAgents(MessageLogger logger) {
		List<MapNode> marks = map.getOsmMap().getMarkers();
		if (marks.size() < 2) {
			logger.log("Error: Please set two marks with mouse-left.");
			return;
		}
		String[] locs = new String[marks.size()];
		for (int i = 0; i < marks.size(); i++) {
			MapNode node = marks.get(i);
			Point2D pt = new Point2D(node.getLon(), node.getLat());
			locs[i] = map.getNearestLocation(pt);
		}
		heuristic.adaptToGoal(locs[1], map);
		Agent agent = null;
		MapAgentFrame.SelectionState state = frame.getSelection();
		switch (state.getValue(MapAgentFrame.AGENT_SEL)) {
		case 0:
			agent = new MapAgent(map, env, search, new String[] { locs[1] });
			break;
		case 1:
			Problem p = new BidirectionalMapProblem(map, null, locs[1]);
			OnlineSearchProblem osp = new OnlineSearchProblem
			(p.getActionsFunction(), p.getGoalTest(), p.getStepCostFunction());
			agent = new LRTAStarAgent
			(osp, MapFunctionFactory.getPerceptToStateFunction(), heuristic);
			break;
		}
		env.addAgent(agent, locs[0]);
	}
	
	/** Updates the status of the frame. */
	@Override
	public void update(SimulationThread simulationThread) {
		if (simulationThread.isCanceled()) {
			frame.setStatus("Task canceled.");
			isPrepared = false;
		} else if (frame.simulationPaused()){
			frame.setStatus("Task paused.");
		} else {
			StringBuffer statusMsg = new StringBuffer();
			statusMsg.append("Task completed");
			List<Agent> agents = env.getAgents();
			if (agents.size() == 1) {
				Double travelDistance = env.getAgentTravelDistance(
						agents.get(0));
				if (travelDistance != null) {
					DecimalFormat f = new DecimalFormat("#0.0");
					statusMsg.append("; travel distance: "
							+ f.format(travelDistance) + "km");
				}
			}
			statusMsg.append(".");
			frame.setStatus(statusMsg.toString());
		}
	}
	

	////////////////////////////////////////////////////////////
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