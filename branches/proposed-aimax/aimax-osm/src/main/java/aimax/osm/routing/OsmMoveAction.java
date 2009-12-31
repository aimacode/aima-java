package aimax.osm.routing;

import aima.core.agent.Action;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;

public class OsmMoveAction implements Action {
	private MapWay way;
	private MapNode toNode;
	
	public OsmMoveAction(MapWay way, MapNode toNode) {
		this.way = way;
		this.toNode = toNode;
	}
	
	public MapWay getWay() {
		return way;
	}
	
	public MapNode getTo() {
		return toNode;
	}
	
	@Override
	public boolean isNoOp() {
		return false;
	}
	public String toString() {
		return "OsmMove[" + toNode.getId() + "]";
	}
}
