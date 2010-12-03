package aimax.osm.data.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import aimax.osm.data.BoundingBox;
import aimax.osm.data.EntityClassifier;
import aimax.osm.data.MapBuilder;
import aimax.osm.data.OsmMap;
import aimax.osm.data.entities.EntityAttribute;
import aimax.osm.data.entities.EntityViewInfo;
import aimax.osm.data.entities.MapNode;

public class DefaultMapBuilder implements MapBuilder {
	private static Logger LOG = Logger.getLogger("aimax.osm");
	private DefaultMap result;
	private boolean nodeRefsWithoutDefsAdded;

	public DefaultMapBuilder() {
		result = new DefaultMap();
	}

	public DefaultMapBuilder(DefaultMap map) {
		result = map;
		result.clear();
	}

	/** {@inheritDoc} */
	@Override
	public void setEntityClassifier(EntityClassifier<EntityViewInfo> classifier) {
		result.setEntityClassifier(classifier);
	}

	/** {@inheritDoc} */
	@Override
	public void setBoundingBox(BoundingBox bb) {
		result.setBoundingBox(bb);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isNodeDefined(long id, BoundingBox bb) {
		MapNode node = result.getNode(id);
		boolean result = (node != null && node.hasPosition());
		if (result && bb != null)
			result = bb.isInside(node.getLat(), node.getLon());
		return result;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean isNodeReferenced(long id) {
		MapNode node = result.getNode(id);
		return node != null && !node.getWayRefs().isEmpty();
	}

	/**
	 * {@inheritDoc} If a node with the same ID but without position has already
	 * been added, it is redefined with the new data.
	 */
	@Override
	public void addNode(long id, String name, List<EntityAttribute> atts,
			float lat, float lon) {
		DefaultMapNode node = (DefaultMapNode) result.getNode(id);
		if (node == null || !node.hasPosition()) {
			if (node == null)
				node = new DefaultMapNode(id);
			node.setName(name);
			node.setAttributes(atts);
			node.setPosition(lat, lon);
			result.addNode(node);
			if (result.getNodeCount() % 500000 == 0)
				LOG.fine("Nodes: " + result.getNodeCount());
		}
	}

	/** {@inheritDoc} */
	@Override
	public boolean isWayDefined(long id) {
		return result.getWay(id) != null;
	}

	/**
	 * {@inheritDoc} Ways with less than two way nodes are ignored.
	 */
	@Override
	public void addWay(long id, String name, List<EntityAttribute> atts,
			List<Long> wayNodeIds) {
		if (wayNodeIds.size() > 1) {
			DefaultMapWay way = new DefaultMapWay(id);
			way.setName(name);
			way.setAttributes(atts);
			List<MapNode> wayNodes = new ArrayList<MapNode>(wayNodeIds.size());
			int i = 0;
			for (long nodeId : wayNodeIds) {
				DefaultMapNode node = (DefaultMapNode) result.getNode(nodeId);
				if (node == null) {
					node = new DefaultMapNode(nodeId);
					result.addNode(node);
					nodeRefsWithoutDefsAdded = true;
				}
				node.addWayRef(way, i++);
				wayNodes.add(node);
			}
			way.setNodes(wayNodes);
			result.addWay(way);
			if (result.getWayCount() % 50000 == 0)
				LOG.fine("Ways: " + result.getWayCount());
		}
	}

	/** {@inheritDoc} */
	@Override
	public boolean nodeRefsWithoutDefsAdded() {
		boolean result = nodeRefsWithoutDefsAdded;
		nodeRefsWithoutDefsAdded = false;
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public OsmMap buildMap() {
		result.compile();
		if (!result.isEmpty())
			LOG.fine("Loading completed. Ways: " + result.getWayCount()
					+ ", Nodes: " + result.getNodeCount() + ", POIs: "
					+ result.getPoiCount());
		return result;
	}
}
