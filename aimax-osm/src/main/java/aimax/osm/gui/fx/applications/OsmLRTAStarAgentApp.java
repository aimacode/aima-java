package aimax.osm.gui.fx.applications;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.agent.EnvironmentView;
import aima.core.environment.map.BidirectionalMapProblem;
import aima.core.environment.map.MapEnvironment;
import aima.core.environment.map.MapFunctionFactory;
import aima.core.environment.map.MoveToAction;
import aima.core.search.framework.Metrics;
import aima.core.search.framework.evalfunc.HeuristicFunction;
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
 * (LRTA*) search algorithm performs in a route finding scenario based on a real
 * OSM map. This GUI does not provide a text pane beside the map view.
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

	protected MapPaneCtrl mapPaneCtrl;
	protected SimulationPaneCtrl simPaneCtrl;

	protected MapAdapter map;
	protected MapEnvironment env;

	@Override
	public String getTitle() {
		return "OSM LRTA* Agent App";
	}
	
	/** Loads a map of the city of Ulm, Germany. Override to change the map. */
	protected void loadMap() {
		mapPaneCtrl.loadMap(DataResource.getULMFileResource());
	}
	
	protected List<Parameter> createParameters() {
		Parameter p1 = new Parameter(PARAM_WAY_SELECTION, "Use any way", "Travel by car", "Travel by bicycle");
		Parameter p2 = new Parameter(PARAM_HEURISTIC, "0", "SLD");
		p2.setDefaultValueIndex(1);
		return Arrays.asList(p1, p2);
	}
	
	/**
	 * Factory method which creates a new agent based on the current parameter
	 * settings.
	 */
	protected Agent createAgent(List<String> locations) {
		HeuristicFunction heuristic;
		switch (simPaneCtrl.getParamValueIndex(PARAM_HEURISTIC)) {
		case 0:
			heuristic = MapFunctionFactory.getZeroHeuristicFunction();
			break;
		default:
			heuristic = MapFunctionFactory.getSLDHeuristicFunction(locations.get(1), map);
		}
		Problem p = new BidirectionalMapProblem(map, null, locations.get(1));
		OnlineSearchProblem osp = new OnlineSearchProblem(p.getActionsFunction(), p.getGoalTest(),
				p.getStepCostFunction());
		return new LRTAStarAgent(osp, MapFunctionFactory.getPerceptToStateFunction(), heuristic);
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
		SimulationPaneBuilder builder = new SimulationPaneBuilder();
		builder.defineParameters(params);
		builder.defineStateView(mapPane);
		builder.defineInitMethod(this::initialize);
		builder.defineSimMethod(this::simulate);
		simPaneCtrl = builder.getResultFor(root);
		simPaneCtrl.setParam(SimulationPaneCtrl.PARAM_SIM_SPEED, 0);

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
	public void simulate() {
		List<MapNode> markers = map.getOsmMap().getMarkers();
		if (markers.size() < 2) {
			simPaneCtrl.setStatus("Error: Please set two markers with mouse-left.");
		} else {
			List<String> locations = new ArrayList<String>(markers.size());
			for (int i = 0; i < markers.size(); i++) {
				MapNode node = markers.get(i);
				Point2D pt = new Point2D(node.getLon(), node.getLat());
				locations.add(map.getNearestLocation(pt));
			}
			Agent agent = createAgent(locations);
			env = new MapEnvironment(map);
			env.addEnvironmentView(new TrackUpdater());
			env.addAgent(agent, locations.get(0));
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
			Platform.runLater(() -> map.getOsmMap().addToTrack(TRACK_NAME, new Position(node.getLat(), node.getLon())));
		}
		simPaneCtrl.setStatus(metrics.toString());
	}

	// helper classes...

	class TrackUpdater implements EnvironmentView {
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
