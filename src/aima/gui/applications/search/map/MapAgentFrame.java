package aima.gui.applications.search.map;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import aima.gui.framework.AgentAppFrame;
import aima.gui.framework.AgentView;
import aima.search.framework.SearchFactory;
import aima.search.map.Map;


/**
 * Universal frame for experiments with route planning
 * agents. It configures the agent application frame with some selectors
 * and an agent view which is designed for cooperation with an
 * {@link AbstractMapAgentModel}. Since items for scenario, agent, destination,
 * agent, and heuristic selection are application specific, this general
 * implementation provides items only for search strategy and mode selection.
 * @author R. Lunde
 */
public class MapAgentFrame extends AgentAppFrame {
	public static String SCENARIO_SEL = "ScenarioSelection";
	public static String DESTINATION_SEL = "DestinationSelection";
	public static String AGENT_SEL = "AgentSelection";
	public static String SEARCH_SEL = "SearchSelection";
	public static String SEARCH_MODE_SEL = "SearchModeSelection";
	public static String HEURISTIC_SEL = "HeuristicSelection";

	/** Standard constructor. */
	public MapAgentFrame() {
		setSelectors(new String[]{
				SCENARIO_SEL, DESTINATION_SEL, AGENT_SEL,
				SEARCH_SEL, SEARCH_MODE_SEL, HEURISTIC_SEL},
				new String[]{
				"Select Scenario", "Select Destinations", "Select Agent",
				"Select Search Strategy", "Select Search Mode", "Select Heuristic"}
		);
		setSelectorItems(SEARCH_SEL,
				SearchFactory.getInstance().getSearchStrategyNames(), 5);
		setSelectorItems(SEARCH_MODE_SEL,
				SearchFactory.getInstance().getSearchModeNames(), 0);
		setAgentView(new MapAgentView());
		setSplitPaneResizeWeight(0.75);
		setUpdateDelay(500);
		setSize(1000, 700);
	}
	
	
	//////////////////////////////////////////////////////////
	// inner classes
	
	
	/**
	 * Agent view for map and tour agent applications. This
	 * view requires the used model to be of type
	 * {@link AbstractMapAgentModel}.
	 */
	class MapAgentView extends AgentView {
		
		/** Clears the panel and draws the map and the tour history. */
		public void paint(java.awt.Graphics g) {
			AbstractMapAgentModel maModel = (AbstractMapAgentModel) model;
		    java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
		    g2.setColor(Color.white);
		    g2.fillRect(0, 0, getWidth(), getHeight());
		    if (maModel != null && !maModel.isEmpty()) {
		    	adjustTransformation();
		    	paintMap(g2);
		    	paintTour(g2);
		    	for (String loc : maModel.getLocations())
		    		paintLoc(g2, loc);
		    }
		}
		
		/**
		 * Adjusts offsets and scale so that the whole map fits on the
		 * view without scrolling.
		 */
		private void adjustTransformation() {
			AbstractMapAgentModel maModel = (AbstractMapAgentModel) model;
			List<String> locs = maModel.getLocations();
	    	// adjust coordinates relative to the left upper corner of the graph area
	    	double minX = Double.POSITIVE_INFINITY;
	    	double minY = Double.POSITIVE_INFINITY;
	    	double maxX = Double.NEGATIVE_INFINITY;
	    	double maxY = Double.NEGATIVE_INFINITY;
	    	for (String loc : locs) {
	    		double[] xy = maModel.getLocCoords(loc);
	    		if (xy[0] < minX) minX = xy[0];
	    		if (xy[1] < minY) minY = xy[1];
	    		if (xy[0] > maxX) maxX = xy[0];
	    		if (xy[1] > maxY) maxY = xy[1];
	    	}
	    	this.setBorder(20, 20, 20, 100);
	    	adjustTransformation(minX, minY, maxX, maxY);
		}
		
		/**
		 * Represents roads by lines and locations by name-labeled points.
		 */
		private void paintMap(java.awt.Graphics2D g2) {
			AbstractMapAgentModel maModel = (AbstractMapAgentModel) model;
			Map envMap = maModel.getEnvMap();
			Map agentMap = maModel.getAgentMap();
			List<Roadblock> roadblocks = new ArrayList<Roadblock>();
			for (String l1 : maModel.getLocations()) {
				double[] xy1 = maModel.getLocCoords(l1);
				List<String> linkedLocs = envMap.getLocationsLinkedTo(l1);
				for (String l2 : agentMap.getLocationsLinkedTo(l1))
					if (!linkedLocs.contains(l2))
						linkedLocs.add(l2);
				for (String l2 : linkedLocs) {
					double[] xy2 = maModel.getLocCoords(l2);
					g2.setColor(Color.lightGray);
		    		g2.drawLine(x(xy1), y(xy1), x(xy2), y(xy2));
		    		boolean blockedInEnv =
		    			!envMap.getLocationsLinkedTo(l2).contains(l1);
		    		boolean blockedInAgent =
		    			!agentMap.getLocationsLinkedTo(l2).contains(l1);
		    		roadblocks.add(new Roadblock(xy1, xy2, blockedInEnv, blockedInAgent));
		    		if (blockedInEnv && blockedInAgent) {
		    			boolean blockedInEnvOtherDir =
			    			!envMap.getLocationsLinkedTo(l1).contains(l2);
		    			boolean blockedInAgentOtherDir =
			    			!agentMap.getLocationsLinkedTo(l1).contains(l2);
		    			roadblocks.add(new Roadblock(xy2, xy1, blockedInEnvOtherDir, blockedInAgentOtherDir));
		    		}
				}
			}
			for (Roadblock block : roadblocks)
				paintRoadblock(g2, block);
		}
		
		/** The track of the agent is visualized with red lines. */
		private void paintTour(java.awt.Graphics2D g2) {
			AbstractMapAgentModel maModel = (AbstractMapAgentModel) model;
		    double[] lastXY = null;
		    g2.setColor(Color.red);
		    for (String loc : maModel.getTourHistory()) {
		    	double[] xy = maModel.getLocCoords(loc);
		    	if (xy != null && lastXY != null) {
		    		g2.drawLine(x(xy), y(xy), x(lastXY), y(lastXY));
		    	}
		    	lastXY = xy;
		    }
		}
		
		/**
		 * Roadblocks are represented by filled rectangles.
		 * Blue denotes, the agent doesn't know it, red dentotes, there is
		 * no roadblock, but the agent thinks so.  
		 */
		private void paintRoadblock(java.awt.Graphics2D g2, Roadblock block) {
			if (block.inEnvMap || block.inAgentMap) {
				int x = (int) (0.2*x(block.pos1)+0.8*x(block.pos2)-4);
				int y = (int) (0.2*y(block.pos1)+0.8*y(block.pos2)-4);
				if (!block.inAgentMap)
					g2.setColor(Color.blue); // agent doesn't know the roadblock
				else if (!block.inEnvMap)
					g2.setColor(Color.red); // agent doesn't know the way
				else
					g2.setColor(Color.lightGray);
				g2.fillRect(x, y, 9, 9);
			}
		}
		
	    private void paintLoc(java.awt.Graphics2D g2, String loc) {
	    	AbstractMapAgentModel maModel = (AbstractMapAgentModel) model;
			double[] xy = maModel.getLocCoords(loc);
	    	if (xy != null) {
	    		int x = x(xy);
	    		int y = y(xy);
		    	String info = "";
		    	List<String> history = maModel.getTourHistory();
		    	ArrayList<Integer> list = new ArrayList<Integer>();
	    		for (int i =0; i < history.size(); i++)
			    	if (history.get(i).equals(loc))
			    		list.add(i+1);
	    		if (!list.isEmpty())
	    			info = list.toString();
		    	if (maModel.hasObjects(loc)) {
		    		g2.setColor(Color.green);
		    		g2.fillOval(x-5, y-5, 10, 10);
		    	}
		    	if (maModel.isStart(loc)) {
		    		g2.setColor(Color.red);
		    		g2.fillOval(x-7, y-7, 14, 14);
		    	}
		    	if (!history.isEmpty() && loc.equals(history.get(history.size()-1))) {
		    		g2.setColor(Color.red);
		    		g2.fillOval(x-4, y-4, 8, 8);
		    	}
		    	if (maModel.hasInfos(loc)) {
		    		g2.setColor(Color.blue);
		    		g2.drawString("i", x, y+12);
		    	}
		    	//if (model.isStart(loc))
		    	//	g2.setColor(Color.red);
		    	//else
		    	if (maModel.isDestination(loc))
		    		g2.setColor(Color.green);
		    	else if (history.contains(loc))
		    		g2.setColor(Color.black);
		    	else
		    		g2.setColor(Color.gray);
		    	g2.drawString(loc + info, x, y);
	    	}
	    }
	}
	
	/**
	 * Stores roadblock information. Roadblocks are generally printed after
	 * the road itself so that they always appear in front.
	 */
	private static class Roadblock {
		double[] pos1;
		double[] pos2;
		boolean inEnvMap;
		boolean inAgentMap;
		private Roadblock(double[] pos1, double[] pos2, boolean inEnvMap, boolean inAgentMap) {
			this.pos1 = pos1;
			this.pos2 = pos2;
			this.inEnvMap = inEnvMap;
			this.inAgentMap = inAgentMap;
		}
    }
}