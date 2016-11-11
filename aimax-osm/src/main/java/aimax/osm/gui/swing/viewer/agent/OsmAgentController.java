package aimax.osm.gui.swing.viewer.agent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Agent;
import aima.core.environment.map.BidirectionalMapProblem;
import aima.core.environment.map.MapEnvironment;
import aima.core.environment.map.MapFunctionFactory;
import aima.core.environment.map.SimpleMapAgent;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.evalfunc.HeuristicFunction;
import aima.core.search.framework.problem.Problem;
import aima.core.search.online.LRTAStarAgent;
import aima.core.search.online.OnlineSearchProblem;
import aima.core.util.math.geom.shapes.Point2D;
import aima.gui.swing.applications.agent.map.MapAgentFrame;
import aima.gui.swing.framework.AgentAppController;
import aima.gui.swing.framework.MessageLogger;
import aima.gui.swing.framework.SimulationThread;
import aima.gui.util.SearchFactory;
import aimax.osm.data.MapWayAttFilter;
import aimax.osm.data.entities.MapNode;
import aimax.osm.routing.MapAdapter;

/**
 * Controller for graphical OSM map agent applications.
 * 
 * @author Ruediger Lunde
 */
public class OsmAgentController extends AgentAppController {

	protected MapAdapter map;
	protected MapEnvironment env;
	/** Search method to be used. */
	protected SearchForActions search;
	/** Heuristic function to be used when performing informed search. */
	protected HeuristicFunction heuristic;

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
		switch (state.getIndex(MapAgentFrame.SCENARIO_SEL)) {
		case 0:
			map.setMapWayFilter(MapWayAttFilter.createAnyWayFilter());
			map.ignoreOneways(true);
			break;
		case 1:
			map.setMapWayFilter(MapWayAttFilter.createCarWayFilter());
			map.ignoreOneways(false);
			break;
		case 2:
			map.setMapWayFilter(MapWayAttFilter.createBicycleWayFilter());
			map.ignoreOneways(false);
			break;
		}
		frame.getEnvView().setEnvironment(env);
		isPrepared = true;
	}

	/**
	 * Checks whether the current environment is ready to start simulation.
	 */
	@Override
	public boolean isPrepared() {
		return isPrepared && (env.getAgents().isEmpty() || !env.isDone());
	}

	/**
	 * Returns the trivial zero function or a simple heuristic which is based on
	 * straight-line distance computation.
	 */
	protected HeuristicFunction createHeuristic(int heuIdx, String goal) {
		HeuristicFunction ahf = null;
		switch (heuIdx) {
		case 0:
			ahf = MapFunctionFactory.getZeroHeuristicFunction();
			break;
		default:
			ahf = MapFunctionFactory.getSLDHeuristicFunction(goal, map);
		}
		return ahf;
	}

	/**
	 * Calls {@link #initAgents(MessageLogger)} if necessary and then starts
	 * simulation until done.
	 */
	@Override
	public void run(MessageLogger logger) {
		if (env.getAgents().isEmpty())
			initAgents(logger);
		logger.log("<simulation-protocol>");
		logger.log("search: " + search.getClass().getName());
		logger.log("heuristic: " + heuristic.getClass().getName());
		try {
			while (!env.isDone() && !frame.simulationPaused()) {
				Thread.sleep(sleepTime);
				env.step();
			}
		} catch (InterruptedException e) {
		}
		logger.log("</simulation-protocol>\n");
	}

	/**
	 * Calls {@link #initAgents(MessageLogger)} if necessary and then executes
	 * one simulation step.
	 */
	@Override
	public void step(MessageLogger logger) {
		if (env.getAgents().isEmpty())
			initAgents(logger);
		env.step();
	}

	/** Creates new agents and adds them to the current environment. */
	protected void initAgents(MessageLogger logger) {
		List<MapNode> markers = map.getOsmMap().getMarkers();
		if (markers.size() < 2) {
			logger.log("Error: Please set two markers with mouse-left.");
			return;
		}
		String[] locs = new String[markers.size()];
		for (int i = 0; i < markers.size(); i++) {
			MapNode node = markers.get(i);
			Point2D pt = new Point2D(node.getLon(), node.getLat());
			locs[i] = map.getNearestLocation(pt);
		}
		MapAgentFrame.SelectionState state = frame.getSelection();
		heuristic = createHeuristic(state.getIndex(MapAgentFrame.HEURISTIC_SEL), locs[1]);
		search = SearchFactory.getInstance().createSearch(state.getIndex(MapAgentFrame.SEARCH_SEL),
				state.getIndex(MapAgentFrame.Q_SEARCH_IMPL_SEL), heuristic);
		
		Agent agent = null;
		switch (state.getIndex(MapAgentFrame.AGENT_SEL)) {
		case 0:
			agent = new SimpleMapAgent(map, env, search, new String[] { locs[1] });
			break;
		case 1:
			Problem p = new BidirectionalMapProblem(map, null, locs[1]);
			OnlineSearchProblem osp = new OnlineSearchProblem(p.getActionsFunction(), p.getGoalTest(),
					p.getStepCostFunction());
			agent = new LRTAStarAgent(osp, MapFunctionFactory.getPerceptToStateFunction(), heuristic);
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
		} else if (frame.simulationPaused()) {
			frame.setStatus("Task paused.");
		} else {
			StringBuffer statusMsg = new StringBuffer();
			statusMsg.append("Task completed");
			List<Agent> agents = env.getAgents();
			if (agents.size() == 1) {
				Double travelDistance = env.getAgentTravelDistance(agents.get(0));
				if (travelDistance != null) {
					DecimalFormat f = new DecimalFormat("#0.0");
					statusMsg.append("; travel distance: " + f.format(travelDistance) + "km");
				}
			}
			statusMsg.append(".");
			frame.setStatus(statusMsg.toString());
		}
	}
}