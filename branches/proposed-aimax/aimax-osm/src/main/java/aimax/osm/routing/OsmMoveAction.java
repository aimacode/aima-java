package aimax.osm.routing;

import aima.core.agent.Action;
import aimax.osm.data.entities.MapNode;

public class OsmMoveAction implements Action {
	private MapNode toNode;
	
	OsmMoveAction(MapNode toNode) {
		this.toNode = toNode;
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
