package aimax.osm.gui.fx.applications;

import aima.core.agent.Agent;
import aima.core.environment.map.MapAgent;
import aima.core.environment.map.MapFunctions;
import aima.core.environment.map.MoveToAction;
import aima.core.search.framework.Node;
import aima.core.search.framework.SearchForActions;
import aima.gui.util.SearchFactory;
import aimax.osm.data.EntityClassifier;
import aimax.osm.data.entities.EntityViewInfo;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.gui.swing.applications.SearchDemoOsmAgentApp;
import aimax.osm.viewer.DefaultEntityRenderer;
import aimax.osm.viewer.DefaultEntityViewInfo;
import aimax.osm.viewer.MapStyleFactory;
import aimax.osm.viewer.UColor;
import javafx.scene.layout.Pane;

import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

/**
 * Integrable application which demonstrates how different kinds of search
 * algorithms perform in a route finding scenario based on a real OSM map. This
 * implementation extends {@link RouteFindingAgentOsmApp} by two aspects: Map
 * locations corresponding to expanded nodes are highlighted in green. The user
 * can define several goals by placing more then two markers on the map.
 *
 * @author Ruediger Lunde
 *
 */
public class ExtendedRouteFindingAgentOsmApp extends RouteFindingAgentOsmApp {

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Stores those states (Strings with map node ids), whose corresponding
	 * search nodes have been expanded during the last search.
	 */
	private static final HashSet<Object> visitedStates = new HashSet<>();

	@Override
	public String getTitle() {
		return "Extended Route Finding Agent OSM App";
	}

	/**
	 * Defines state view, parameters, and call-back functions and calls the
	 * simulation pane builder to create layout and controller objects. This
	 * implementation modifies the inherited version by changing map renderer
	 * and entity classifier to prepare state space visualization.
	 */
	@Override
	public Pane createRootPane() {
		Pane root = super.createRootPane();
		mapPaneCtrl.setRenderer(new SDMapEntityRenderer());
		mapPaneCtrl.getMap().setEntityClassifier(createEntityClassifier());
		return root;
	}

	/**
	 * The method is called after each parameter selection change. This
	 * implementation clears visited states of the last simulation run, prepares
	 * the map for different kinds of vehicles and clears the currently
	 * displayed track.
	 */
	@Override
	public void initialize() {
		visitedStates.clear();
		super.initialize();
	}

	/**
	 * Factory method which creates a new agent based on the current parameter
	 * settings. The agent is provided with a heuristic function factory to
	 * adapt to different goals. A node listener is used to visualize search space exploration.
	 */
	protected Agent createAgent(List<String> locations) {
		SearchForActions<String, MoveToAction> search = SearchFactory.getInstance().createSearch
				(taskPaneCtrl.getParamValueIndex(PARAM_SEARCH), taskPaneCtrl.getParamValueIndex(PARAM_Q_SEARCH_IMPL),
						state -> 0.0);
		search.addNodeListener(node -> visitedStates.add(node.getState()));
		visitedStates.clear();

		Function<String, ToDoubleFunction<Node<String, MoveToAction>>> hFnFactory;
		if (taskPaneCtrl.getParamValueIndex(PARAM_HEURISTIC) == 0)
			hFnFactory = goal -> (node -> 0.0);
		else
			hFnFactory = goal -> MapFunctions.createSLDHeuristicFunction(goal, map);
		return new MapAgent(map, search, locations.subList(1, locations.size()), envViewCtrl::notify, hFnFactory);
	}

	// helper classes...

	/**
	 * Variant of the {@link DefaultEntityRenderer} which highlights way
	 * nodes mentioned in {@link ExtendedRouteFindingAgentOsmApp#visitedStates}.
	 */
	private static class SDMapEntityRenderer extends DefaultEntityRenderer {
		DefaultEntityViewInfo highlightProp = new MapStyleFactory().createPoiInfo(0, 0, 5, UColor.GREEN,
				MapStyleFactory.createRectangle(4, UColor.GREEN), false);

		@Override
		public void printWay(MapWay way, DefaultEntityViewInfo eprop, boolean asArea) {
			super.printWay(way, eprop, asArea);
			if (scale >= highlightProp.minVisibleScale * displayFactor) {
				for (MapNode node : getWayNodes(way))
					if (visitedStates.contains(Long.toString(node.getId())))
						printPoint(imageBdr, node, highlightProp, null);
			}
		}
	}

	/** Demonstrates how to choose a color for a certain track. */
	private EntityClassifier<EntityViewInfo> createEntityClassifier() {
		MapStyleFactory msf = new MapStyleFactory();
		// define colors for tracks
		EntityClassifier<EntityViewInfo> eClassifier = msf.createDefaultClassifier();
		eClassifier.addRule("track_type", TRACK_NAME, msf.createTrackInfo(UColor.RED));
		return eClassifier;
	}
}
