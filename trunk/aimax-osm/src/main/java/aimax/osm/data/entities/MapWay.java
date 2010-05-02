package aimax.osm.data.entities;

import java.util.ArrayList;
import java.util.List;

import aimax.osm.data.EntityVisitor;

/**
 * Represents a way within a map. Ways are defined by lists of nodes.
 * A special dynamic attribute called "oneway" marks ways, which can only
 * be traveled in ascending node index order. 
 */
public class MapWay extends MapEntity {
	private ArrayList<MapNode> nodes;
	// Implicit (storage efficient) representation of a bounding box 
	private short latMinIdx;
	private short lonMinIdx;
	private short latMaxIdx;
	private short lonMaxIdx;
	
	public MapWay(long id) {
		this.id = id;
		nodes = new ArrayList<MapNode>();
		latMinIdx = -1;
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
		latMinIdx = -1;
	}

	public void accept(EntityVisitor visitor) {
		visitor.visitMapWay(this);
	}
	
	public int compareLatitude(float lat) {
		updateBoundingBox();
		int result = nodes.get(latMinIdx).compareLatitude(lat);
		if (result != nodes.get(latMaxIdx).compareLatitude(lat))
			result = 0;
		return result;
	}
	
	public int compareLongitude(float lon) {
		updateBoundingBox();
		int result = nodes.get(lonMinIdx).compareLongitude(lon);
		if (result != nodes.get(lonMaxIdx).compareLongitude(lon))
			result = 0;
		return result;
	}
	
	/**
	 * Returns the sum of the side lengths of the bounding box.
	 * A fast size measure is needed by the renderer for area sorting.
	 */
	public float getBBSize() {
		updateBoundingBox();
		float latDiff = nodes.get(latMaxIdx).getLat()
		- nodes.get(latMinIdx).getLat();
		float lonDiff = nodes.get(lonMaxIdx).getLon()
		- nodes.get(lonMinIdx).getLon();
		return latDiff + lonDiff;
//		latDiff * latDiff
//		+ (float) Math.cos(nodes.get(latMinIdx).getLat() / 180.0 * Math.PI) *
//		lonDiff * lonDiff;
	}
	
	private void updateBoundingBox() {
		if (latMinIdx == -1) {
			latMinIdx = 0;
			lonMinIdx = 0;
			latMaxIdx = 0;
			lonMaxIdx = 0;
			for (short i = 1; i < nodes.size(); i++) {
				float lat = nodes.get(i).getLat();
				float lon = nodes.get(i).getLon();
				if (lat < nodes.get(latMinIdx).getLat())
					latMinIdx = i;
				else if (lat > nodes.get(latMaxIdx).getLat())
					latMaxIdx = i;
				if (lon < nodes.get(lonMinIdx).getLon())
					lonMinIdx = i;
				else if (lon > nodes.get(lonMaxIdx).getLon())
					lonMaxIdx = i;
			}
		}
	}
}
