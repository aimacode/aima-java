package aima.gui.applications.search.map;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.environment.map.Map;
import aima.core.environment.map.MapEnvironment;
import aima.core.util.datastructure.Point2D;
import aima.gui.framework.EmptyEnvironmentView;

/**
 * General graphical environment view implementation for map agent
 * applications. This view requires the used environment
 * to be of type {@link MapEnvironment}. All agents are tracked but
 * only the track of the first agent is shown.
 * 
 * @author Ruediger Lunde
 */
public class MapAgentView extends EmptyEnvironmentView {

	private static final long serialVersionUID = 1L;
	/** Stores for each agent the locations, it has already visited. */
	private final Hashtable<Agent,List<String>> agentTracks = new Hashtable<Agent,List<String>>();

	protected MapEnvironment getMapEnv() {
		return (MapEnvironment) env;
	}
	
	/** Returns a list of all already visited agent locations. */
	public List<String> getTrack(Agent agent) {
		return agentTracks.get(agent);
	}

	/** Clears the list of already visited locations. */
	public void clearTracks() {
		agentTracks.clear();
	}
	
	
	/**
	 * Reacts on environment changes and updates the agent tracks.
	 */
	@Override
	public void agentAdded(Agent agent, EnvironmentState resultingState) {
		updateTracks();
		super.agentAdded(agent, resultingState);
	}

	/**
	 * Reacts on environment changes and updates the agent tracks. The command
	 * is always send to the message logger as string.
	 */
	@Override
	public void agentActed(Agent agent, Action command, EnvironmentState state) {
		MapEnvironment mEnv = getMapEnv();
		String msg = "";
		if (mEnv.getAgents().size() > 1)
			msg = "A" + mEnv.getAgents().indexOf(agent) + ": ";
		notify(msg + command.toString());
		updateTracks();
		repaint();
	}

	/**
	 * Clears the panel, displays the map, the current agent locations,
	 * and the track of the first agent.
	 */
	@Override
	public void paint(java.awt.Graphics g) {
		super.paint(g);
		if (env != null) {
			Map map = getMapEnv().getMap();
			if (!map.getLocations().isEmpty()) {
				updateTracks(); // safety first!
				java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
				adjustTransformation();
				paintMap(g2);
				for (Agent a : env.getAgents())
					paintTrack(g2, a);
				for (String loc : map.getLocations())
					paintLoc(g2, loc);
			}
		}
	}

	/** Updates all tracks with respect to the current agent locations. */
	protected void updateTracks() {
		MapEnvironment mEnv = getMapEnv();
		if (mEnv != null)
			for (Agent a : mEnv.getAgents()) {
				List<String> aTrack = getTrack(a);
				String aLoc = mEnv.getAgentLocation(a);
				if (aTrack == null) {
					aTrack = new ArrayList<String>();
					agentTracks.put(a, aTrack);
				}
				if (aTrack.isEmpty() || !aTrack.get(aTrack.size()-1).equals(aLoc))
					aTrack.add(aLoc);
			}
	}
	
	/** Returns the locations of all agents. */
	protected List<String> getAgentLocs() {
		List<String> result = new ArrayList<String>();
		MapEnvironment mEnv = getMapEnv();
		for (Agent a : mEnv.getAgents())
			result.add(mEnv.getAgentLocation(a));
		return result;
	}
	
	/**
	 * Adjusts offsets and scale so that the whole map fits on the view
	 * without scrolling.
	 */
	private void adjustTransformation() {
		Map map = getMapEnv().getMap();
		List<String> locs = map.getLocations();
		// adjust coordinates relative to the left upper corner of the graph
		// area
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		for (String loc : locs) {
			Point2D xy = map.getPosition(loc);
			if (xy.getX() < minX)
				minX = xy.getX();
			if (xy.getY() < minY)
				minY = xy.getY();
			if (xy.getX() > maxX)
				maxX = xy.getX();
			if (xy.getY() > maxY)
				maxY = xy.getY();
		}
		this.setBorder(20, 20, 20, 100);
		adjustTransformation(minX, minY, maxX, maxY);
	}

	/**
	 * Represents roads by lines and locations by name-labeled points.
	 */
	protected void paintMap(java.awt.Graphics2D g2) {
		Map envMap = getMapEnv().getMap();
		for (String l1 : envMap.getLocations()) {
			Point2D pt1 = envMap.getPosition(l1);
			List<String> linkedLocs = envMap.getLocationsLinkedTo(l1);
			for (String l2 : linkedLocs) {
				Point2D pt2 = envMap.getPosition(l2);
				g2.setColor(Color.lightGray);
				g2.drawLine(x(pt1), y(pt1), x(pt2), y(pt2));
			}
		}
	}

	/** The track of the agent is visualized with red lines. */
	private void paintTrack(java.awt.Graphics2D g2, Agent a) {
		Map map = getMapEnv().getMap();
		Point2D lastPt = null;
		g2.setColor(Color.red);
		for (String loc : getTrack(a)) {
			Point2D pt = map.getPosition(loc);
			if (pt != null && lastPt != null) {
				g2.drawLine(x(pt), y(pt), x(lastPt), y(lastPt));
			}
			lastPt = pt;
		}
	}

	protected void paintLoc(java.awt.Graphics2D g2, String loc) {
		Map map = getMapEnv().getMap();
		Point2D pt = map.getPosition(loc);
		if (pt != null) {
			int x = x(pt);
			int y = y(pt);
			String info = "";
			List<String> track = new ArrayList<String>();
			if (!env.getAgents().isEmpty())
				// show details only for track of first agent...
				track = getTrack(env.getAgents().get(0));
			ArrayList<Integer> list = new ArrayList<Integer>();
			for (int i = 0; i < track.size(); i++)
				if (track.get(i).equals(loc))
					list.add(i + 1);
			if (!list.isEmpty())
				info = list.toString();
			if (getAgentLocs().contains(loc)) {
				g2.setColor(Color.red);
				g2.fillOval(x - 4, y - 4, 8, 8);
			}
			if (track.contains(loc))
				g2.setColor(Color.black);
			else
				g2.setColor(Color.gray);
			g2.drawString(loc + info, x, y);
		}
	}
}