package aima.gui.swing.applications.agent.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import aima.core.environment.map.Map;
import aima.core.environment.map.Scenario;
import aima.core.util.math.geom.shapes.Point2D;
/**
 * Extends the <code>MapAgentView</code> by visualization of
 * scenario, destination, and agent map information.
 * 
 * @author Ruediger Lunde
 */
public class ExtendedMapAgentView extends MapAgentView {
	
	private static final long serialVersionUID = 1L;
	/** A scenario. */
	protected Scenario scenario;
	/** A list of location names, possibly null. */
	protected List<String> destinations;
	/** Map which reflects the agent's knowledge about the environment. */
	protected Map agentMap;

	/** Sets data to be displayed. All values may be null. */
	public void setData(Scenario scenario, List<String> destinations, Map agentMap) {
		this.scenario = scenario;
		this.destinations = destinations;
		this.agentMap = agentMap;
		repaint();
	}
	
	/**
	 * Represents roads by lines and locations by name-labeled points.
	 */
	protected void paintMap(java.awt.Graphics2D g2) {
		Map envMap = getMapEnv().getMap();
		Map aMap = (agentMap != null) ? agentMap : envMap;
		List<Roadblock> roadblocks = new ArrayList<Roadblock>();
		for (String l1 : envMap.getLocations()) {
			Point2D pt1 = envMap.getPosition(l1);
			List<String> linkedLocs = envMap.getPossibleNextLocations(l1);
			for (String l2 : aMap.getPossibleNextLocations(l1))
				if (!linkedLocs.contains(l2))
					linkedLocs.add(l2);
			for (String l2 : linkedLocs) {
				Point2D pt2 = envMap.getPosition(l2);
				g2.setColor(Color.lightGray);
				g2.drawLine(x(pt1), y(pt1), x(pt2), y(pt2));
				boolean blockedInEnv = !envMap.getPossibleNextLocations(l2)
						.contains(l1);
				boolean blockedInAgent = !aMap.getPossibleNextLocations(l2)
						.contains(l1);
				roadblocks.add(new Roadblock(pt1, pt2, blockedInEnv,
						blockedInAgent));
				if (blockedInEnv && blockedInAgent) {
					boolean blockedInEnvOtherDir = !envMap
							.getPossibleNextLocations(l1).contains(l2);
					boolean blockedInAgentOtherDir = !aMap
							.getPossibleNextLocations(l1).contains(l2);
					roadblocks.add(new Roadblock(pt2, pt1,
							blockedInEnvOtherDir, blockedInAgentOtherDir));
				}
			}
		}
		for (Roadblock block : roadblocks)
			paintRoadblock(g2, block);
	}
	
	/** Displays a map location. */
	protected void paintLoc(Graphics2D g2, String loc) {
		Map map = getMapEnv().getMap();
		Point2D pt = map.getPosition(loc);
		if (pt != null) {
			int x = x(pt);
			int y = y(pt);
			String info = "";
			List<String> track = new ArrayList<>();
			if (!env.getAgents().isEmpty())
				// show details only for track of first agent...
				track = getTrack(env.getAgents().get(0));
			ArrayList<Integer> list = new ArrayList<>();
			for (int i = 0; i < track.size(); i++)
				if (track.get(i).equals(loc))
					list.add(i + 1);
			if (!list.isEmpty())
				info = list.toString();
//			if (getMapEnv().hasObjects(loc)) {
//				g2.setColor(Color.green);
//				g2.fillOval(x - 5, y - 5, 10, 10);
//			}
			if (scenario != null && scenario.getInitAgentLocation().equals(loc)) {
				g2.setColor(Color.red);
				g2.fillOval(x - 7, y - 7, 14, 14);
			}
			if (getAgentLocs().contains(loc)) {
				g2.setColor(Color.red);
				g2.fillOval(x - 4, y - 4, 8, 8);
			}
//			if (maModel.hasInfos(loc)) {
//				g2.setColor(Color.blue);
//				g2.drawString("i", x, y + 12);
//			}
			// if (model.isStart(loc))
			// g2.setColor(Color.red);
			// else
			if (destinations != null && destinations.contains(loc))
				g2.setColor(Color.green);
			else if (track.contains(loc))
				g2.setColor(Color.black);
			else
				g2.setColor(Color.gray);
			g2.drawString(loc + info, x, y);
		}
	}
	
	/**
	 * Blocked roads are represented by filled rectangles. Blue denotes, the
	 * agent doesn't know it, red denotes, the road is no blocked, but the
	 * agent thinks so.
	 */
	private void paintRoadblock(java.awt.Graphics2D g2, Roadblock block) {
		if (block.inEnvMap || block.inAgentMap) {
			int x = (int) (0.2 * x(block.pos1) + 0.8 * x(block.pos2) - 4);
			int y = (int) (0.2 * y(block.pos1) + 0.8 * y(block.pos2) - 4);
			if (!block.inAgentMap)
				g2.setColor(Color.blue); // agent doesn't know the road block
			else if (!block.inEnvMap)
				g2.setColor(Color.red); // agent doesn't know the way
			else
				g2.setColor(Color.lightGray);
			g2.fillRect(x, y, 9, 9);
		}
	}
	
	/**
	 * Stores road block information. Informations about obstacles are
	 * generally printed after the roads itself so that they always appear
	 * in front.
	 */
	private static class Roadblock {
		Point2D pos1;
		Point2D pos2;
		boolean inEnvMap;
		boolean inAgentMap;
	
		private Roadblock(Point2D pos1, Point2D pos2, boolean inEnvMap,
				boolean inAgentMap) {
			this.pos1 = pos1;
			this.pos2 = pos2;
			this.inEnvMap = inEnvMap;
			this.inAgentMap = inAgentMap;
		}
	}
}
