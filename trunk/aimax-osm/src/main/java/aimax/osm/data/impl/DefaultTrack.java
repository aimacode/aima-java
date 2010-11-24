package aimax.osm.data.impl;

import java.util.ArrayList;
import java.util.List;

import aimax.osm.data.EntityVisitor;
import aimax.osm.data.Position;
import aimax.osm.data.entities.EntityAttribute;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.Track;

/**
 * Represents a track. This implementation works with any sub-type of
 * <code>MapNode</code>. 
 * @author Ruediger Lunde
 */
public class DefaultTrack extends DefaultMapEntity implements Track {
	private ArrayList<MapNode> trkpts;
	
	public DefaultTrack(long id, String name, String trackType) {
		this.id = id;
		setName(name);
		ArrayList<EntityAttribute> atts = new ArrayList<EntityAttribute>(1);
		atts.add(new EntityAttribute("track_type", trackType));
		setAttributes(atts);
		trkpts = new ArrayList<MapNode>();
	}

	@Override
	public List<MapNode> getNodes() {
		return trkpts;
	}
	
	@Override
	public MapNode getLastNode() {
		MapNode result = null;
		if (!trkpts.isEmpty())
			result = trkpts.get(trkpts.size()-1);
		return result;
	}

	@Override
	public void addNode(MapNode node) {
		trkpts.add(node);
	}
	
	@Override
	public void addNode(Position pos) {
		int idx = trkpts.isEmpty()
		? 0 : (int) trkpts.get(trkpts.size()-1).getId()+1;
		MapNode node = new DefaultMapNode(idx);
		node.setPosition(pos.getLat(), pos.getLon());
		addNode(node);
	}

	@Override
	public void accept(EntityVisitor visitor) {
		visitor.visitTrack(this);
	}
	
	/**
	 * Compares position relative to a given latitude value if
	 * the nodes of this track are of type <code>DefaultMapEntity</code>,
	 * otherwise returns zero.
	 */
	@Override
	public int compareLatitude(float lat) {
		DefaultMapEntity node = getTrkPt(0);
		if (node != null) {
			int result = node.compareLatitude(lat);
			for (int i = 1; i < trkpts.size(); i++)
				if (result != getTrkPt(i).compareLatitude(lat))
					return 0;
			 return result;
		} else {
			return 0;
		}
	}
	
	/**
	 * Compares position relative to a given longitude value if
	 * the nodes of this track are of type <code>DefaultMapEntity</code>,
	 * otherwise returns zero.
	 */
	@Override
	public int compareLongitude(float lon) {
		DefaultMapEntity node = getTrkPt(0);
		if (node != null) {
			int result = node.compareLongitude(lon);
			for (int i = 1; i < trkpts.size(); i++)
				if (result != getTrkPt(i).compareLongitude(lon))
					return 0;
			 return result;
		} else {
			return 0;
		}
	}
	
	private DefaultMapEntity getTrkPt(int i) {
		MapNode result = trkpts.get(i);
		if (result instanceof DefaultMapNode)
			return (DefaultMapEntity) result;
			else
		return null;
	}
}

