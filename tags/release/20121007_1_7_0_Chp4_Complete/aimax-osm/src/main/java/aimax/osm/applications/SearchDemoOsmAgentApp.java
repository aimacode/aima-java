package aimax.osm.applications;

import java.awt.Color;
import java.util.HashSet;
import java.util.List;

import aima.core.agent.Agent;
import aima.core.environment.map.BidirectionalMapProblem;
import aima.core.environment.map.MapAgent;
import aima.core.environment.map.MapEnvironment;
import aima.core.environment.map.MapFunctionFactory;
import aima.core.search.framework.DefaultGoalTest;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.online.LRTAStarAgent;
import aima.core.search.online.OnlineSearchProblem;
import aima.core.util.datastructure.Point2D;
import aima.gui.applications.search.map.MapAgentFrame;
import aima.gui.framework.AgentAppController;
import aima.gui.framework.AgentAppFrame;
import aima.gui.framework.MessageLogger;
import aimax.osm.data.DataResource;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.routing.agent.OsmAgentController;
import aimax.osm.routing.agent.OsmAgentFrame;
import aimax.osm.routing.agent.OsmAgentView;
import aimax.osm.routing.agent.MapAdapter;
import aimax.osm.viewer.DefaultEntityRenderer;
import aimax.osm.viewer.DefaultEntityViewInfo;
import aimax.osm.viewer.MapStyleFactory;


/**
 * This application demonstrates, how different search strategies
 * explore the state space. All locations which correspond to expanded
 * nodes are highlighted in the map. If you don't see any expanded node,
 * just zoom in!
 * @author Ruediger Lunde
 */
public class SearchDemoOsmAgentApp extends OsmAgentApp {
	
	/**
	 * Stores those states (Strings with map node ids), whose
	 * corresponding search nodes have been expanded
	 * during the last search. Quick and dirty solution...
	 */
	static final HashSet<Object> visitedStates = new HashSet<Object>();
	
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
	
	/////////////////////////////////////////////////////////////////
	// inner classes
	
	/** Variant of the <code>OsmAgentFrame</code>. */
	private class SDFrame extends OsmAgentFrame {
		
		private static final long serialVersionUID = 1L;

		SDFrame() {
			setTitle("OSDA - the OSM Search Demo Agent Application");
			//this.setSelectorItems(AGENT_SEL, new String[]{}, -1);
		}
	}
	
	/**
	 * Variant of the <code>OsmAgentController</code> which
	 * starts an agent with a slightly modified goal test function.
	 */
	private static class SDController extends OsmAgentController {
		SDController(MapAdapter map) {
			super(map);
		}
		
		@Override
		public void prepare(String changedSelector) {
			visitedStates.clear();
			super.prepare(changedSelector);
		}
	
		/** Creates new agents and adds them to the current environment. */
		protected void initAgents(MessageLogger logger) {
			List<MapNode> marks = map.getOsmMap().getMarkers();
			if (marks.size() < 2) {
				logger.log("Error: Please set two marks with mouse-left.");
				return;
			}
			visitedStates.clear();
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
				agent = new SDMapAgent(env, search, new String[] { locs[1] });
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
	}
	
	
	/** Variant of the <code>MapAgent</code>. */
	private static class SDMapAgent extends MapAgent {
		public SDMapAgent(MapEnvironment mapEnvironment, Search search,
				String[] goalTests) {
			super(mapEnvironment.getMap(), mapEnvironment, search, goalTests);
		}
		
		@Override
		protected Problem formulateProblem(Object goal) {
			BidirectionalMapProblem problem =
				(BidirectionalMapProblem) super.formulateProblem(goal);
			Problem result = new Problem(
					problem.getInitialState(),
					problem.getActionsFunction(),
					problem.getResultFunction(),
					new DefaultGoalTest((String) goal) {
						@Override
						public boolean isGoalState(Object state) {
							visitedStates.add(state);
							return super.isGoalState(state);
						}
					},
					problem.getStepCostFunction());
			return result;
		}
	}
	
	/**
	 * Variant of the <code>DefaultMapEntityRenderer</code>
	 * which highlights way nodes mentioned in
	 * {@link SearchDemoOsmAgentApp#visitedStates}.
	 */
	private static class SDMapEntityRenderer extends DefaultEntityRenderer {
		DefaultEntityViewInfo highlightProp = new MapStyleFactory().createPoiInfo(0, 0, 5, Color.GREEN, MapStyleFactory.createRectangle(4, Color.GREEN), false);
		@Override
		public void printWay(MapWay way, DefaultEntityViewInfo eprop, boolean asArea) {
			super.printWay(way, eprop, asArea);
			if (scale >= highlightProp.minVisibleScale * displayFactor ) {
				for (MapNode node : getWayNodes(way))
					if (visitedStates.contains(Long.toString(node.getId())))
						printPoint(g2, node, highlightProp, null);
						//highlight(node);	
			}
		}
		
//		private void highlight(MapNode node) {
//			int offs = (int) displayFactor;
//			int x = transformer.x(node.getLon()) - offs;
//			int y = transformer.y(node.getLat()) - offs;
//			g2.setColor(Color.YELLOW);
//			g2.setStroke(new BasicStroke(1f));
//			g2.fillRect(x, y, 2*offs, 2*offs);
//		}
		
	}
	
	
	/////////////////////////////////////////////////////////////////
	// starter method

	/** Application starter. */
	public static void main(String args[]) {
		//Logger.getLogger("aimax.osm").setLevel(Level.FINEST);
		//Logger.getLogger("").getHandlers()[0].setLevel(Level.FINE);
		
		SearchDemoOsmAgentApp demo = new SearchDemoOsmAgentApp();
		demo.readMap(DataResource.getULMFileResource());
		demo.startApplication();
	}
}
