package aimax.osm.gui.fx.applications;

import java.util.HashSet;
import java.util.List;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.agent.EnvironmentView;
import aima.core.environment.map.AdaptableHeuristicFunction;
import aima.core.environment.map.BidirectionalMapProblem;
import aima.core.environment.map.MapEnvironment;
import aima.core.environment.map.MapFunctionFactory;
import aima.core.environment.map.MoveToAction;
import aima.core.search.framework.Metrics;
import aima.core.search.framework.problem.Problem;
import aima.core.search.online.LRTAStarAgent;
import aima.core.search.online.OnlineSearchProblem;
import aima.core.util.CancelableThread;
import aima.core.util.math.geom.shapes.Point2D;
import aima.gui.fx.framework.IntegrableApplication;
import aima.gui.fx.framework.Parameter;
import aima.gui.fx.framework.SimulationPaneBuilder;
import aima.gui.fx.framework.SimulationPaneCtrl;
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

/**
 * Integrable application which demonstrates how the Learning Real-Time A*
 * (LRTA*) search algorithm perform an a route finding scenario based on a real
 * OSM map.
 *
 * @author Ruediger Lunde
 *
 */
public class OsmLRTAStarAgentApp extends IntegrableApplication {

	public static void main(String[] args) {
		launch(args);
	}

	public static String PARAM_WAY_SELECTION = "waySelection";
	public static String PARAM_HEURISTIC = "heuristic";
    public static String TRACK_NAME = "Track";

	private MapPaneCtrl mapPaneCtrl;
	private SimulationPaneCtrl simPaneCtrl;

	protected MapAdapter map;
	protected MapEnvironment env;
	/** Heuristic function to be used when performing informed search. */
	protected AdaptableHeuristicFunction heuristic;

    /**
     * Stores those states (Strings with map node ids), whose corresponding
     * search nodes have been expanded during the last search. Quick and dirty
     * solution...
     */
    private static final HashSet<Object> visitedStates = new HashSet<Object>();


	@Override
	public String getTitle() { return "OSM LRTA* Agent App"; }

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

		SimulationPaneBuilder builder = new SimulationPaneBuilder();
		builder.defineParameters(params);
		builder.defineStateView(mapPane);
		builder.defineInitMethod(this::initialize);
		builder.defineSimMethod(this::simulate);
		simPaneCtrl = builder.getResultFor(root);
		simPaneCtrl.setParam(SimulationPaneCtrl.PARAM_SIM_SPEED, 0);

		return root;
	}

	protected Parameter[] createParameters() {
		Parameter p1 = new Parameter(PARAM_WAY_SELECTION, "Use any way", "Travel by car", "Travel by bicycle");
		Parameter p2 = new Parameter(PARAM_HEURISTIC, "0", "SLD");
		p2.setDefaultValueIndex(1);
		return new Parameter[] { p1, p2 };
	}

	/** Is called after each parameter selection change. */
	@Override
	public void initialize() {
		map = new MapAdapter(mapPaneCtrl.getMap());
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
				heuristic = MapFunctionFactory.getZeroHeuristicFunction();
				break;
			default:
				heuristic = MapFunctionFactory.getSLDHeuristicFunction();
		}

        map.getOsmMap().clearTrack(TRACK_NAME);
	}

	/** Creates a new agent and adds them to the current environment. */
	protected void initEnvironment(String[] locations) {

		heuristic.adaptToGoal(locations[1], map);

		Problem p = new BidirectionalMapProblem(map, null, locations[1]);
		OnlineSearchProblem osp = new OnlineSearchProblem(
				p.getActionsFunction(), p.getGoalTest(),
				p.getStepCostFunction());
		Agent agent = new LRTAStarAgent(osp,
				MapFunctionFactory.getPerceptToStateFunction(), heuristic);

		env = new MapEnvironment(map);
		env.addAgent(agent, locations[0]);
	}


	/** Starts the experiment. */
	public void simulate() {
        List<MapNode> markers = map.getOsmMap().getMarkers();
        if (markers.size() < 2) {
            simPaneCtrl.setStatus("Error: Please set two markers with mouse-left.");
        } else {
			String[] locations = new String[markers.size()];
			for (int i = 0; i < markers.size(); i++) {
				MapNode node = markers.get(i);
				Point2D pt = new Point2D(node.getLon(), node.getLat());
				locations[i] = map.getNearestLocation(pt);
			}
		    initEnvironment(locations);
			env.addEnvironmentView(new TrackUpdater());
			while (!env.isDone() && !CancelableThread.currIsCanceled()) {
				env.step();
				simPaneCtrl.waitAfterStep();
			}
		}
	}

	@Override
	public void finalize() {
		simPaneCtrl.cancelSimulation();
	}


	/** Visualizes agent positions. Call from simulation thread. */
    private void updateTrack(Agent agent, Metrics metrics) {
        MapAdapter map = (MapAdapter) env.getMap();
        MapNode node = map.getWayNode(env.getAgentLocation(agent));
        if (node != null) {
            Platform.runLater(() -> map.getOsmMap().addToTrack(TRACK_NAME,
                    new Position(node.getLat(), node.getLon()))
            );
        }
		simPaneCtrl.setStatus(metrics.toString());
    }

	// helper classes...

	class TrackUpdater implements EnvironmentView {
		int actionCounter = 0;

        @Override
        public void notify(String msg) { }

        @Override
        public void agentAdded(Agent agent, Environment source) {
			updateTrack(agent, new Metrics());
		}

        /**
         * Reacts on environment changes and updates the tracks.
         */
        @Override
        public void agentActed(Agent agent, Action command, Environment source) {
            if (command instanceof MoveToAction) {
				Metrics metrics = new Metrics();
				Double travelDistance = env.getAgentTravelDistance(env.getAgents().get(0));
				if (travelDistance != null)
					metrics.set("travelDistance[km]", travelDistance);
				metrics.set("actions", ++actionCounter);
                updateTrack(agent, metrics);
            }
        }
    }
}
