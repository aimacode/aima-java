package aimax.osm.gui.fx.applications;

import aima.core.agent.*;
import aima.core.environment.map.MapAgent;
import aima.core.environment.map.MapEnvironment;
import aima.core.environment.map.MapFunctions;
import aima.core.environment.map.MoveToAction;
import aima.core.search.framework.Metrics;
import aima.core.search.framework.Node;
import aima.core.search.framework.SearchForActions;
import aima.core.util.Tasks;
import aima.core.util.math.geom.shapes.Point2D;
import aima.gui.fx.framework.IntegrableApplication;
import aima.gui.fx.framework.Parameter;
import aima.gui.fx.framework.TaskExecutionPaneBuilder;
import aima.gui.fx.framework.TaskExecutionPaneCtrl;
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
import java.util.Arrays;
import java.util.List;
import java.util.function.ToDoubleFunction;

/**
 * Simple OSM route finding agent application which can be used as base class
 * for more advanced OSM agent applications. For example, by overriding some of
 * the factory methods, it is easy to add parameters and change agent as well as
 * environment implementations.
 *
 * @author Ruediger Lunde
 *
 */
public class RouteFindingAgentOsmApp extends IntegrableApplication {

	public static void main(String[] args) {
		launch(args);
	}

	public static String PARAM_WAY_SELECTION = "waySelection";
	public static String PARAM_SEARCH = "search";
	public static String PARAM_Q_SEARCH_IMPL = "qsearch";
	public static String PARAM_HEURISTIC = "heuristic";
	public static String TRACK_NAME = "Track";

	protected MapPaneCtrl mapPaneCtrl;
	protected SimpleEnvironmentViewCtrl envViewCtrl;
	protected TaskExecutionPaneCtrl taskPaneCtrl;

	protected MapAdapter map;
	protected MapEnvironment env;

	@Override
	public String getTitle() {
		return "Route Finding Agent OSM App";
	}

	/** Loads a map of the city of Ulm, Germany. Override to change the map. */
	protected void loadMap() {
		mapPaneCtrl.loadMap(DataResource.getUlmFileResource());
	}

	/** Defines the parameters to be shown in the simulation pane tool bar. */
	protected List<Parameter> createParameters() {
		Parameter p1 = new Parameter(PARAM_WAY_SELECTION, "Use any way", "Travel by car", "Travel by bicycle");
		Parameter p2 = new Parameter(PARAM_SEARCH, (Object[]) SearchFactory.getInstance().getSearchStrategyNames());
		p2.setDefaultValueIndex(5);
		Parameter p3 = new Parameter(PARAM_Q_SEARCH_IMPL, (Object[]) SearchFactory.getInstance().getQSearchImplNames());
		p3.setDefaultValueIndex(1);
		p3.setDependency(PARAM_SEARCH, "Depth First", "Breadth First", "Uniform Cost", "Greedy Best First", "A*");
		Parameter p4 = new Parameter(PARAM_HEURISTIC, "0", "SLD");
		p4.setDefaultValueIndex(1);
		p4.setDependency(PARAM_SEARCH, "Greedy Best First", "A*", "Recursive Best First",
				"Recursive Best First No Loops", "Hill Climbing");
		return Arrays.asList(p1, p2, p3, p4);
	}

	/**
	 * Factory method which creates a new agent based on the current parameter
	 * settings.
	 */
	protected Agent createAgent(List<String> locations) {
		ToDoubleFunction<Node<String, MoveToAction>> heuristic;
		if (taskPaneCtrl.getParamValueIndex(PARAM_HEURISTIC) == 0)
			heuristic = node -> 0.0;
		else
			heuristic = MapFunctions.createSLDHeuristicFunction(locations.get(1), map);

		SearchForActions<String, MoveToAction> search = SearchFactory.getInstance().createSearch
				(taskPaneCtrl.getParamValueIndex(PARAM_SEARCH),
				taskPaneCtrl.getParamValueIndex(PARAM_Q_SEARCH_IMPL), heuristic);

		return new MapAgent(map, search, locations.get(1), envViewCtrl::notify);
	}

	/**
	 * Factory method which creates a new environment based on the current
	 * parameter settings.
	 */
	protected MapEnvironment createEnvironment() {
		return new MapEnvironment(map);
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
		StackPane envView = new StackPane();
		envViewCtrl = new SimpleEnvironmentViewCtrl(envView, mapPane, 0.75);

		TaskExecutionPaneBuilder builder = new TaskExecutionPaneBuilder();
		builder.defineParameters(params);
		builder.defineStateView(envView);
		builder.defineInitMethod(this::initialize);
		builder.defineTaskMethod(this::startExperiment);
		taskPaneCtrl = builder.getResultFor(root);
		taskPaneCtrl.setParam(TaskExecutionPaneCtrl.PARAM_EXEC_SPEED, 0);

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
		switch (taskPaneCtrl.getParamValueIndex(PARAM_WAY_SELECTION)) {
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
			taskPaneCtrl.setStatus("Error: Please set at least two markers with mouse-left.");
		} else {
			List<String> locations = new ArrayList<>(markers.size());
			for (MapNode node : markers) {
				Point2D pt = new Point2D(node.getLon(), node.getLat());
				locations.add(map.getNearestLocation(pt));
			}
			Agent agent = createAgent(locations);
			env = createEnvironment();
			env.addEnvironmentView(new TrackUpdater());
			env.addAgent(agent, locations.get(0));
			if (taskPaneCtrl.getParam(PARAM_SEARCH) != null)
				env.notifyViews("Using " + taskPaneCtrl.getParamValue(PARAM_SEARCH));
			while (!env.isDone() && !Tasks.currIsCancelled()) {
				env.step();
				taskPaneCtrl.waitAfterStep();
			}
			envViewCtrl.notify("");
			// taskPaneCtrl.setStatus(search.getMetrics().toString());
		}
	}

	@Override
	public void cleanup() {
		taskPaneCtrl.cancelExecution();
	}

	/** Visualizes agent positions. Call from simulation thread. */
	private void updateTrack(Agent agent, Metrics metrics) {
		MapAdapter map = (MapAdapter) env.getMap();
		MapNode node = map.getWayNode(env.getAgentLocation(agent));
		if (node != null) {
			Platform.runLater(() -> map.getOsmMap().addToTrack(TRACK_NAME, new Position(node.getLat(), node.getLon())));
		}
		taskPaneCtrl.setStatus(metrics.toString());
	}

	// helper classes...

	private class TrackUpdater implements EnvironmentView {
		int actionCounter = 0;

		@Override
		public void notify(String msg) {
			envViewCtrl.notify(msg);
		}

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
