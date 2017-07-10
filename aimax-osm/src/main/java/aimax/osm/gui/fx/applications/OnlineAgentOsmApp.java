package aimax.osm.gui.fx.applications;

import aima.core.agent.*;
import aima.core.environment.map.BidirectionalMapProblem;
import aima.core.environment.map.MapEnvironment;
import aima.core.environment.map.MapFunctions;
import aima.core.environment.map.MoveToAction;
import aima.core.search.framework.Metrics;
import aima.core.search.framework.problem.GeneralProblem;
import aima.core.search.framework.problem.OnlineSearchProblem;
import aima.core.search.framework.problem.Problem;
import aima.core.search.online.LRTAStarAgent;
import aima.core.search.online.OnlineDFSAgent;
import aima.core.util.Tasks;
import aima.core.util.math.geom.shapes.Point2D;
import aima.gui.fx.framework.IntegrableApplication;
import aima.gui.fx.framework.Parameter;
import aima.gui.fx.framework.TaskExecutionPaneBuilder;
import aima.gui.fx.framework.TaskExecutionPaneCtrl;
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
import java.util.Arrays;
import java.util.List;
import java.util.function.ToDoubleFunction;

/**
 * Integrable application which demonstrates how the Learning Real-Time A*
 * (LRTA*) search agent performs compared to the Online DFS agent in a route
 * finding scenario based on a real OSM map.
 *
 * @author Ruediger Lunde
 *
 */
public class OnlineAgentOsmApp extends IntegrableApplication {

	public static String PARAM_STRATEGY = "strategy";

	public static void main(String[] args) {
		launch(args);
	}

	public static String PARAM_WAY_SELECTION = "waySelection";
	public static String PARAM_HEURISTIC = "heuristic";
	public static String TRACK_NAME = "Track";

	protected MapPaneCtrl mapPaneCtrl;
	protected TaskExecutionPaneCtrl simPaneCtrl;

	protected MapAdapter map;
	protected MapEnvironment env;

	@Override
	public String getTitle() {
		return "Online Agent OSM App";
	}
	
	/** Loads a map of the city of Ulm, Germany. Override to change the map. */
	protected void loadMap() {
		mapPaneCtrl.loadMap(DataResource.getUlmFileResource());
	}
	
	protected List<Parameter> createParameters() {
		Parameter p1 = new Parameter(PARAM_WAY_SELECTION, "Use any way", "Travel by car", "Travel by bicycle");
		Parameter p2 = new Parameter(PARAM_STRATEGY, "Online DFS agent", "LRTA* agent");
		p2.setDefaultValueIndex(1);
		Parameter p3 = new Parameter(PARAM_HEURISTIC, "0", "SLD");
		p3.setDefaultValueIndex(1);
        p3.setDependency(PARAM_STRATEGY, "LRTA* agent");
		return Arrays.asList(p1, p2, p3);
	}
	
	/**
	 * Factory method which creates a new agent based on the current parameter
	 * settings.
	 */
	protected Agent createAgent(List<String> locations) {
		Problem<String, MoveToAction> p = new BidirectionalMapProblem(map, null, locations.get(1));
		OnlineSearchProblem<String, MoveToAction> osp = new GeneralProblem<>
				(null, p::getActions, null, p::testGoal, p::getStepCosts);

		ToDoubleFunction<String> heuristic;
		if (simPaneCtrl.getParamValueIndex(PARAM_HEURISTIC) == 0)
			heuristic = state -> 0.0;
		else
			heuristic = state -> MapFunctions.getSLD(state, locations.get(1), map);

		Agent agent;
		if (simPaneCtrl.getParamValueIndex(PARAM_STRATEGY) == 0)
			agent = new OnlineDFSAgent<>(osp, MapFunctions.createPerceptToStateFunction());
		else
			agent = new LRTAStarAgent<>(osp, MapFunctions.createPerceptToStateFunction(), heuristic);
		return agent;
	}
	
	/**
	 * Defines state view, parameters, and call-back functions and calls the
	 * simulation pane builder to create layout and controller objects.
	 */
	@Override
	public Pane createRootPane() {
		BorderPane root = new BorderPane();

		List<Parameter> params = createParameters();

		StackPane mapPane = new StackPane();
		mapPaneCtrl = new MapPaneCtrl(mapPane);
		loadMap();
		TaskExecutionPaneBuilder builder = new TaskExecutionPaneBuilder();
		builder.defineParameters(params);
		builder.defineStateView(mapPane);
		builder.defineInitMethod(this::initialize);
		builder.defineTaskMethod(this::startExperiment);
		simPaneCtrl = builder.getResultFor(root);
		simPaneCtrl.setParam(TaskExecutionPaneCtrl.PARAM_EXEC_SPEED, 0);

		return root;
	}

	/**
	 * Is called after each parameter selection change. This implementation
	 * prepares the map for different kinds of vehicles and clears the currently
	 * displayed track.
	 */
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
		map.getOsmMap().clearTrack(TRACK_NAME);
	}

	/** Starts the experiment. */
	public void startExperiment() {
		List<MapNode> markers = map.getOsmMap().getMarkers();
		if (markers.size() < 2) {
			simPaneCtrl.setStatus("Error: Please set two markers with mouse-left.");
		} else {
			List<String> locations = new ArrayList<>(markers.size());
			for (MapNode node : markers) {
				Point2D pt = new Point2D(node.getLon(), node.getLat());
				locations.add(map.getNearestLocation(pt));
			}
			Agent agent = createAgent(locations);
			env = new MapEnvironment(map);
			env.addEnvironmentView(new TrackUpdater());
			env.addAgent(agent, locations.get(0));
			while (!env.isDone() && !Tasks.currIsCancelled()) {
				env.step();
				simPaneCtrl.waitAfterStep();
			}
		}
	}

	@Override
	public void cleanup() {
		simPaneCtrl.cancelExecution();
	}

	/** Visualizes agent positions. Call from simulation thread. */
	private void updateTrack(Agent agent, Metrics metrics) {
		MapAdapter map = (MapAdapter) env.getMap();
		MapNode node = map.getWayNode(env.getAgentLocation(agent));
		if (node != null) {
			Platform.runLater(() -> map.getOsmMap().addToTrack(TRACK_NAME, new Position(node.getLat(), node.getLon())));
		}
		simPaneCtrl.setStatus(metrics.toString());
	}

	// helper classes...

	private class TrackUpdater implements EnvironmentView {
		int actionCounter = 0;

		@Override
		public void notify(String msg) {}

		@Override
		public void agentAdded(Agent agent, Environment source) {
			updateTrack(agent, new Metrics());
		}

		/**
		 * Reacts on environment changes and updates the tracks.
		 */
		@Override
		public void agentActed(Agent agent, Percept percept, Action command, Environment source) {
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
