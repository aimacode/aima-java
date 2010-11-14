package aimax.osm.data.impl;

import java.util.Collections;
import java.util.List;

import aimax.osm.data.BoundingBox;
import aimax.osm.data.EntityVisitor;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;

/**
 * Default implementation of a node.
 * @author Ruediger Lunde
 */
public class DefaultMapWay extends DefaultMapEntity implements MapWay {
	private List<MapNode> nodes;
	// Implicit (storage efficient) representation of a bounding box 
	private short latMinIdx;
	private short lonMinIdx;
	private short latMaxIdx;
	private short lonMaxIdx;
	
	/** Creates a way with a specified ID. */
	public DefaultMapWay(long id) {
		this.id = id;
		nodes = Collections.emptyList();
		latMinIdx = -1;
	}
	
	/** Assigns a way description (as list of nodes) to the way. */
	public void setNodes(List<MapNode> nodes) {
		this.nodes = nodes;
		latMinIdx = -1;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean isOneway() {
		return "yes".equals(getAttributeValue("oneway"));
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean isArea() {
		return "yes".equals(getAttributeValue("area"));
	}
	
	/** {@inheritDoc} */
	@Override
	public List<MapNode> getNodes() {
		return Collections.unmodifiableList(nodes);
	}

	/** {@inheritDoc} */
	@Override
	public void accept(EntityVisitor visitor) {
		visitor.visitMapWay(this);
	}
	
	/** {@inheritDoc} */
	@Override
	public BoundingBox computeBoundingBox() {
		updateBoundingBox();
		return new BoundingBox(nodes.get(latMinIdx).getLat(),
				nodes.get(lonMinIdx).getLon(), nodes.get(latMaxIdx).getLat(),
				nodes.get(lonMaxIdx).getLon());
	}
	
	/** {@inheritDoc} */
	@Override
	public float getBoundingBoxSize() {
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
	
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer("Way(" + id + ", [ ");
		for (MapNode node : nodes)
			result.append(node.getId() + " ");
		result.append("])");
		return result.toString();
	}
	
	/////////////////////////////////////////////////////////////////
	// extensions for KDTree

	/** {@inheritDoc} */
	@Override
	public int compareLatitude(float lat) {
		updateBoundingBox();
		int result = ((DefaultMapNode) nodes.get(latMinIdx))
				.compareLatitude(lat);
		if (result != ((DefaultMapNode) nodes.get(latMaxIdx))
				.compareLatitude(lat))
			result = 0;
		return result;
	}
	
	/** {@inheritDoc} */
	@Override
	public int compareLongitude(float lon) {
		updateBoundingBox();
		int result = ((DefaultMapNode) nodes.get(lonMinIdx))
				.compareLongitude(lon);
		if (result != ((DefaultMapNode) nodes.get(lonMaxIdx))
				.compareLongitude(lon))
			result = 0;
		return result;
	}
}
