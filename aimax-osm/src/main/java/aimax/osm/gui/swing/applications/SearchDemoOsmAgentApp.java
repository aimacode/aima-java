package aimax.osm.gui.swing.applications;

import java.util.HashSet;
import java.util.List;

import aima.core.agent.Agent;
import aima.core.environment.map.BidirectionalMapProblem;
import aima.core.environment.map.MapFunctions;
import aima.core.environment.map.MoveToAction;
import aima.core.environment.map.SimpleMapAgent;
import aima.core.search.framework.Node;
import aima.core.search.framework.problem.GeneralProblem;
import aima.core.search.framework.problem.Problem;
import aima.core.search.online.LRTAStarAgent;
import aima.core.search.framework.problem.OnlineSearchProblem;
import aima.core.util.math.geom.shapes.Point2D;
import aima.gui.swing.applications.agent.map.MapAgentFrame;
import aima.gui.swing.framework.AgentAppController;
import aima.gui.swing.framework.AgentAppFrame;
import aima.gui.swing.framework.MessageLogger;
import aima.gui.util.SearchFactory;
import aimax.osm.data.DataResource;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.routing.MapAdapter;
import aimax.osm.gui.swing.viewer.agent.OsmAgentController;
import aimax.osm.gui.swing.viewer.agent.OsmAgentFrame;
import aimax.osm.gui.swing.viewer.agent.OsmAgentView;
import aimax.osm.viewer.DefaultEntityRenderer;
import aimax.osm.viewer.DefaultEntityViewInfo;
import aimax.osm.viewer.MapStyleFactory;
import aimax.osm.viewer.UColor;

/**
 * This application demonstrates, how different search strategies explore the
 * state space. All locations which correspond to expanded nodes are highlighted
 * in the map. If you don't see any expanded node, just zoom in!
 * 
 * @author Ruediger Lunde
 */
public class SearchDemoOsmAgentApp extends OsmAgentApp {

	/**
	 * Stores those states (Strings with map node ids), whose corresponding
	 * search nodes have been expanded during the last search. Quick and dirty
	 * solution...
	 */
	private static final HashSet<Object> visitedStates = new HashSet<>();

	/** Creates an <code>OsmAgentView</code>. */
	@Override
	public OsmAgentView createEnvironmentView() {
		OsmAgentView result = super.createEnvironmentView();
		result.getMapViewPane().setRenderer(new SDMapEntityRenderer());
		return result;
	}

	@Override
	public AgentAppFrame createFrame() {
		return new SDFrame();
	}

	@Override
	public AgentAppController createController() {
		return new SDController(map);
	}

	// ///////////////////////////////////////////////////////////////
	// inner classes

	/** Variant of the <code>OsmAgentFrame</code>. */
	private class SDFrame extends OsmAgentFrame {

		private static final long serialVersionUID = 1L;

		SDFrame() {
			setTitle("OSDA - the OSM Search Demo Agent Application");
			// this.setSelectorItems(AGENT_SEL, new String[]{}, -1);
		}
	}

	/**
	 * Variant of the <code>OsmAgentController</code> which starts an agent with
	 * a slightly modified goal test function.
	 */
	private static class SDController extends OsmAgentController {
		SDController(MapAdapter map) {
			super(map);
		}

		@Override
		public void prepare(String changedSelector) {
			super.prepare(changedSelector);
			visitedStates.clear();
		}

		/** Creates new agents and adds them to the current environment. */
		protected void initAgents(MessageLogger logger) {
			List<MapNode> markers = map.getOsmMap().getMarkers();
			if (markers.size() < 2) {
				logger.log("Error: Please set two markers with mouse-left.");
				return;
			}
			visitedStates.clear();
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
			search.addNodeListener((node) -> visitedStates.add(node.getState()));
			
			Agent agent = null;
			switch (state.getIndex(MapAgentFrame.AGENT_SEL)) {
			case 0:
				agent = new SimpleMapAgent(map, env, search, new String[] { locs[1] });
				break;
			case 1:
				Problem<String, MoveToAction> p = new BidirectionalMapProblem(map, null, locs[1]);
				OnlineSearchProblem<String, MoveToAction> osp = new GeneralProblem<>
						(null, p::getActions, null, p::testGoal, p::getStepCosts);
				agent = new LRTAStarAgent<>(osp, MapFunctions.createPerceptToStateFunction(),
						s -> heuristic.applyAsDouble(new Node<>(s)));
				break;
			}
			env.addAgent(agent, locs[0]);
		}
	}

	/**
	 * Variant of the <code>DefaultMapEntityRenderer</code> which highlights way
	 * nodes mentioned in {@link SearchDemoOsmAgentApp#visitedStates}.
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
				// highlight(node);
			}
		}

		// private void highlight(MapNode node) {
		// int offs = (int) displayFactor;
		// int x = transformer.x(node.getLon()) - offs;
		// int y = transformer.y(node.getLat()) - offs;
		// g2.setColor(Color.YELLOW);
		// g2.setStroke(new BasicStroke(1f));
		// g2.fillRect(x, y, 2*offs, 2*offs);
		// }

	}

	// ///////////////////////////////////////////////////////////////
	// starter method

	/** Application starter. */
	public static void main(String args[]) {
		// Logger.getLogger("aimax.osm").setLevel(Level.FINEST);
		// Logger.getLogger("").getHandlers()[0].setLevel(Level.FINE);

		SearchDemoOsmAgentApp demo = new SearchDemoOsmAgentApp();
		demo.readMap(DataResource.getUlmFileResource());
		demo.startApplication();
	}
}
