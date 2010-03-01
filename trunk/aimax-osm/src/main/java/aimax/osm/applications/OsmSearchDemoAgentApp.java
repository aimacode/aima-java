package aimax.osm.applications;

import java.awt.Color;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import aima.core.agent.Agent;
import aima.core.environment.map.BidirectionalMapProblem;
import aima.core.environment.map.MapAgent;
import aima.core.environment.map.MapEnvironment;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.util.datastructure.Point2D;
import aima.gui.framework.AgentAppController;
import aima.gui.framework.AgentAppFrame;
import aima.gui.framework.MessageLogger;
import aimax.osm.data.DataResource;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.routing.SimpleGoalTest;
import aimax.osm.routing.mapagent.OsmAgentController;
import aimax.osm.routing.mapagent.OsmAgentFrame;
import aimax.osm.routing.mapagent.OsmAgentView;
import aimax.osm.routing.mapagent.OsmMapAdapter;
import aimax.osm.viewer.DefaultEntityRenderer;
import aimax.osm.viewer.DefaultEntityViewInfo;
import aimax.osm.viewer.EntityIcon;
import aimax.osm.viewer.MapStyleFactory;


/**
 * This application demonstrates, how different search strategies
 * explore the state space. All locations which correspond to expanded
 * nodes are highlighted in the map. If you don't see any expanded node,
 * just zoom in!
 * @author R. Lunde
 */
public class OsmSearchDemoAgentApp extends OsmAgentApp {
	
	/**
	 * Stores those states (Strings with map node ids), whose
	 * corresponding search nodes have been expanded
	 * during the last search. Quick and dirty solution...
	 */
	static final HashSet visitedStates = new HashSet();
	
	public OsmSearchDemoAgentApp(InputStream osmFile) {
		super(osmFile);
	}
	
	/** Creates an <code>OsmAgentView</code>. */
	@Override
	public OsmAgentView createEnvironmentView() {
		OsmAgentView result = super.createEnvironmentView();
		result.getMapViewPane().setRenderer(new SDMapEntityRenderer());
		return result;
	}
	
	@Override
	public AgentAppFrame createFrame() {
		return new SDFrame(map);
	}
	
	@Override
	public AgentAppController createController() {
		return new SDController(map);
	}
	
	/////////////////////////////////////////////////////////////////
	// inner classes
	
	/** Variant of the <code>OsmAgentFrame</code>. */
	private class SDFrame extends OsmAgentFrame {
		SDFrame(OsmMapAdapter map) {
			super(map);
			setTitle("OSDA - the OSM Search Demo Agent Application");
			this.setSelectorItems(AGENT_SEL, new String[]{}, -1);
		}
	}
	
	/**
	 * Variant of the <code>OsmAgentController</code> which
	 * starts an agent with a slightly modified goal test function.
	 */
	private static class SDController extends OsmAgentController {
		SDController(OsmMapAdapter map) {
			super(map);
		}
		
		@Override
		public void clear() {
			visitedStates.clear();
			super.clear();
		}
		
		/**
		 * Creates environment and agent,
		 * starts the agent and initiates some text outputs describing the
		 * state of the agent.
		 */
		public void run(MessageLogger logger) {
			visitedStates.clear();
			List<MapNode> marks = map.getMapData().getMarks();
			if (marks.size() < 2) {
				logger.log("Error: Please set two marks with MouseLeft.");
				return;
			}
			String[] locs = new String[2];
			for (int i = 0; i < 2; i++) {
				MapNode node = (i==0) ? marks.get(0) : marks.get(marks.size()-1);
				Point2D pt = new Point2D(node.getLon(), node.getLat());
				locs[i] = map.getNearestLocation(pt);
			}
			logger.log("<osm-agent-simulation-protocol>");
			logger.log("search: " + search.getClass().getName());
			logger.log("heuristic: " + heuristic.getClass().getName());
			heuristic.adaptToGoal(locs[1], map);
			Agent agent = new SDMapAgent(env, search, new String[] { locs[1] });
			env.addAgent(agent, locs[0]);
			try {
				while (!env.isDone()) {
					Thread.sleep(20);
					env.step();
				}
			} catch (InterruptedException e) {}
			logger.log("</osm-agent-simulation-protocol>\n");
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
					new SimpleGoalTest((String) goal) {
						@Override
						public boolean isGoalState(Object state) {
							// TODO Auto-generated method stub
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
	 * {@link OsmSearchDemoAgentApp#visitedStates}.
	 */
	private static class SDMapEntityRenderer extends DefaultEntityRenderer {
		DefaultEntityViewInfo highlightProp = MapStyleFactory.createPoiInfo(0, 0, Color.GREEN, MapStyleFactory.createRectangle(4, Color.GREEN), false, 5);
		@Override
		public void printWay(MapWay way, DefaultEntityViewInfo eprop, boolean asArea) {
			super.printWay(way, eprop, asArea);
			if (transformer.getScale() >= highlightProp.minVisibleScale * displayFactor ) {
				for (MapNode node : way.getNodes())
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
		Logger.getLogger("aimax.osm").setLevel(Level.FINEST);
		Logger.getLogger("").getHandlers()[0].setLevel(Level.FINE);
		
		OsmSearchDemoAgentApp demo = new OsmSearchDemoAgentApp(DataResource.getULMFileResource());
		demo.startApplication();
	}
}
