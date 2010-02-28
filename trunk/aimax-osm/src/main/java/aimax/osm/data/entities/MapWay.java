package aimax.osm.data.entities;

import java.util.ArrayList;
import java.util.List;

import aimax.osm.data.EntityVisitor;

/**
 * Represents a way within a map. Ways are defined by lists of nodes.
 * The <code>isOneway</code> attribute marks ways, which can only be traveled
 * in ascending node index order. 
 */
public class MapWay extends MapEntity {
	private ArrayList<MapNode> nodes;
	
	public MapWay(long id) {
		this.id = id;
		nodes = new ArrayList<MapNode>();
	}
	
	public boolean isOneway() {
		if (attributes != null)
			for (EntityAttribute a : attributes)
				if (a.getName().equals("oneway") && a.getValue().equals("yes"))
					return true;
		return false;
	}
	
	public boolean isArea() {
		if (attributes != null)
			for (EntityAttribute a : attributes)
				if (a.getName().equals("area") && a.getValue().equals("yes"))
					return true;
		return false;
	}
	
	public List<MapNode> getNodes() {
		return nodes;
	}

	public void addNode(MapNode node) {
		this.nodes.add(node);
	}

	public void accept(EntityVisitor visitor) {
		visitor.visitMapWay(this);
	}
	
	public int compareLatitude(float lat) {
		int result = nodes.get(0).compareLatitude(lat);
		for (int i = 1; i < nodes.size(); i++)
			if (result != nodes.get(i).compareLatitude(lat))
				return 0;
		return result;
	}
	
	public int compareLongitude(float lon) {
		int result = nodes.get(0).compareLongitude(lon);
		for (int i = 1; i < nodes.size(); i++)
			if (result != nodes.get(i).compareLongitude(lon))
				return 0;
		return result;
	}
}
