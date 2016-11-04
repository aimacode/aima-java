package aimax.osm.gui.fx.applications;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.agent.EnvironmentView;
import aima.core.environment.map.*;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.Problem;
import aima.core.search.online.LRTAStarAgent;
import aima.core.search.online.OnlineSearchProblem;
import aima.core.util.CancelableThread;
import aima.core.util.math.geom.shapes.Point2D;
import aima.gui.fx.framework.IntegrableApplication;
import aima.gui.fx.framework.Parameter;
import aima.gui.fx.framework.SimulationPaneBuilder;
import aima.gui.fx.framework.SimulationPaneCtrl;
import aima.gui.fx.views.SimpleEnvironmentViewCtrl;
import aima.gui.util.SearchFactory;
import aimax.osm.data.DataResource;
import aimax.osm.data.MapWayAttFilter;
import aimax.osm.data.Position;
import aimax.osm.data.entities.MapNode;
import aimax.osm.gui.fx.viewer.MapPaneCtrl;
import aimax.osm.routing.MapAdapter;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

/**
 * Integrable application which demonstrates how different kinds of search
 * algorithms perform an a route finding scenario based on a real OSM map.
 *
 * @author Ruediger Lunde
 *
 */
public class OsmRouteFindingAgentApp extends IntegrableApplication {

	public static void main(String[] args) {
		launch(args);
	}

	public static String PARAM_WAY_SELECTION = "WaySelection";
	public static String PARAM_SEARCH = "Search";
	public static String PARAM_Q_SEARCH_IMPL = "QSearch";
	public static String PARAM_HEURISTIC = "Heuristic";

	private MapPaneCtrl mapPaneCtrl;
	private SimpleEnvironmentViewCtrl envViewCtrl;
	private SimulationPaneCtrl simPaneCtrl;

	protected MapAdapter map;
	protected MapEnvironment env;
	/** Search method to be used. */
	protected SearchForActions search;
	/** Heuristic function to be used when performing informed search. */
	protected AdaptableHeuristicFunction heuristic;

	protected List<String> markedLocations;



//	protected MapEnvironment env = null;
//	protected Agent agent = null;


	public OsmRouteFindingAgentApp() {
		markedLocations = new ArrayList<String>();
	}

	@Override
	public String getTitle() { return "OSM Route Finding Agent App"; }

	/**
	 * Defines state view, parameters, and call-back functions and calls the
	 * simulation pane builder to create layout and controller objects.
	 */
	@Override
	public Pane createRootPane() {
		BorderPane root = new BorderPane();

		Parameter[] params = createParameters();

		StackPane mapPane = new StackPane();
		mapPaneCtrl = new MapPaneCtrl(mapPane);
		mapPaneCtrl.loadMap(DataResource.getULMFileResource());
		StackPane envView = new StackPane();
		envViewCtrl = new SimpleEnvironmentViewCtrl(envView, mapPane, 0.75);

		SimulationPaneBuilder builder = new SimulationPaneBuilder();
		builder.defineParameters(params);
		builder.defineStateView(envView);
		builder.defineInitMethod(this::initialize);
		builder.defineSimMethod(this::simulate);
		simPaneCtrl = builder.getResultFor(root);
		simPaneCtrl.setParam(SimulationPaneCtrl.PARAM_SIM_SPEED, 0);

		return root;
	}

	protected Parameter[] createParameters() {
		Parameter p1 = new Parameter(PARAM_WAY_SELECTION, "Use any way", "Travel by car", "Travel by bicycle");
		Parameter p2 = new Parameter(PARAM_SEARCH, (Object[]) SearchFactory.getInstance().getSearchStrategyNames());
		p2.setDefaultValueIndex(5);
		Parameter p3 = new Parameter(PARAM_Q_SEARCH_IMPL, (Object[]) SearchFactory.getInstance().getQSearchImplNames());
		p3.setDefaultValueIndex(1);
		p3.setDependency(PARAM_SEARCH, "Depth First", "Breadth First", "Uniform Cost", "Greedy Best First", "A*");
		Parameter p4 = new Parameter(PARAM_HEURISTIC, "0", "SLD");
		p4.setDependency(PARAM_SEARCH, "Greedy Best First", "A*", "Recursive Best First",
				"Recursive Best First No Loops", "Hill Climbing");
		p4.setDefaultValueIndex(1);
		return new Parameter[] { p1, p2, p3, p4 };
	}

	/** Is called after each parameter selection change. */
	@Override
	public void initialize() {
		map = new MapAdapter(mapPaneCtrl.getMap());
		map.getOsmMap().getTracks().clear();
		switch (simPaneCtrl.getParamValueIndex(PARAM_WAY_SELECTION)) {
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

		switch (simPaneCtrl.getParamValueIndex(PARAM_HEURISTIC)) {
		case 0:
			heuristic = new H1();
			break;
		default:
			heuristic = new H2();
		}
		search = SearchFactory.getInstance().createSearch(simPaneCtrl.getParamValueIndex(PARAM_SEARCH),
				simPaneCtrl.getParamValueIndex(PARAM_Q_SEARCH_IMPL), heuristic);
	}

	/** Creates new agents and adds them to the current environment. */
	protected boolean initAgents() {
		List<MapNode> markers = map.getOsmMap().getMarkers();
		if (markers.size() < 2) {
			this.simPaneCtrl.setStatus("Error: Please set two markers with mouse-left.");
			return false;
		}
		String[] locs = new String[markers.size()];
		for (int i = 0; i < markers.size(); i++) {
			MapNode node = markers.get(i);
			Point2D pt = new Point2D(node.getLon(), node.getLat());
			locs[i] = map.getNearestLocation(pt);
		}
		heuristic.adaptToGoal(locs[1], map);
        env = new MapEnvironment(map);
		Agent agent = null;
		int idx = 0;
		switch (idx) {
			case 0:
				agent = new MapAgent(map, env, search, new String[] { locs[1] });
				break;
			case 1:
				Problem p = new BidirectionalMapProblem(map, null, locs[1]);
				OnlineSearchProblem osp = new OnlineSearchProblem(
						p.getActionsFunction(), p.getGoalTest(),
						p.getStepCostFunction());
				agent = new LRTAStarAgent(osp,
						MapFunctionFactory.getPerceptToStateFunction(), heuristic);
				break;
		}
		//env.addEnvironmentView(envViewCtrl);
        env.addEnvironmentView(new TrackUpdater());
		env.addAgent(agent, locs[0]);
		return true;
	}


	/** Starts the experiment. */
	public void simulate() {
		if (initAgents()) {
            env.notifyViews("Using " + simPaneCtrl.getParamValue(PARAM_SEARCH));
			while (!env.isDone() && !CancelableThread.currIsCanceled()) {
				env.step();
				simPaneCtrl.waitAfterStep();
			}
			envViewCtrl.notify("");
            simPaneCtrl.setStatus("Search metrics: " + search.getMetrics());
		}
	}

	@Override
	public void finalize() {
		simPaneCtrl.cancelSimulation();
	}


    private void updateTrack(Agent agent) {
        MapAdapter map = (MapAdapter) env.getMap();
        MapNode node = map.getWayNode(env.getAgentLocation(agent));
        if (node != null) {
            int aIdx = env.getAgents().indexOf(agent);
            map.getOsmMap().addToTrack("Track" + aIdx,
                    new Position(node.getLat(), node.getLon()));
        }
    }

	// helper classes...

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

	class TrackUpdater implements EnvironmentView {

        @Override
        public void notify(String msg) { envViewCtrl.notify(msg); }

        @Override
        public void agentAdded(Agent agent, Environment source) {}

        /**
         * Reacts on environment changes and updates the tracks.
         */
        @Override
        public void agentActed(Agent agent, Action command, Environment source) {
            if (command instanceof MoveToAction) {
                Platform.runLater(() -> updateTrack(agent));
            }
        }
    }
}
