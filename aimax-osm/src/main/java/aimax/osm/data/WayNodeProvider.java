package aimax.osm.data;

import java.util.List;

import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;

/**
 * Provides different abstractions of a way for rendering.
 * @author Ruediger Lunde
 */
public interface WayNodeProvider {
	/**
	 * Returns a list of nodes describing the specified way which
	 * is suitable for the specified scale. Number of nodes will
	 * typically decrease with decreasing scale
	 * (e.g. 1/10 000 -> 80 nodes; 1/100 000 -> 10 nodes).
	 */
	public List<MapNode> getWayNodes(MapWay way, float scale);
}
