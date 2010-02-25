package aimax.osm.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import aimax.osm.data.entities.MapEntity;


/**
 * Simple implementation of a kd-tree for map entities. Entities are
 * divided by their two-dimensional geo coordinates. Note, that
 * inner nodes can contain entities, e.g. ways, whose nodes are
 * not contained in the bounding box of the same leaf node.
 * @author R. Lunde
 */
public class KDTree {
	private BoundingBox bb;
	private KDTree[] children;
	private int depth;
	private int maxEntities;
	private int maxDepth;
	private ArrayList<MapEntity> entities;
	private boolean splitAtLat;
	private float splitValue;
	
	/**
	 * Constructs the root of the tree.
	 * @param bb The bounding box. Only entities within the box can be added.
	 * @param maxEntities Defines, when a leaf node should be split.
	 * @param maxDepth Limits the depth of the tree.
	 */
	public KDTree(BoundingBox bb, int maxEntities, int maxDepth) {
		this.bb = bb;
		depth = 0;
		this.maxEntities = maxEntities;
		this.maxDepth = maxDepth;
		entities = new ArrayList<MapEntity>();
	}
	
	/** Constructor for inner and leaf nodes. */
	private KDTree(BoundingBox bb, int maxEntities, int maxDepth, int depth) {
		this(bb, maxEntities, maxDepth);
		this.depth = depth;
	}
	
	/** Returns the depth of the tree (longest path length from root to leaf). */
	public int depth() {
		return children == null ? 1 : 1+Math.max(children[0].depth(), children[1].depth());
	}
	
	/**
	 * Adds an entity at the right position in the tree and extends
	 * the tree if necessary.
	 */
	void insertEntity(MapEntity entity) {
		if (children == null) {
			entities.add(entity);
			if (entities.size() > maxEntities && depth < maxDepth) {
				computeSplitValues();
				BoundingBox c1bb;
				BoundingBox c2bb;
				if (splitAtLat) {
					c1bb = new BoundingBox
					(bb.getLatMin(), bb.getLonMin(), splitValue, bb.getLonMax());
					c2bb = new BoundingBox
					(splitValue, bb.getLonMin(), bb.getLatMax(), bb.getLonMax());
				} else {
					c1bb = new BoundingBox
					(bb.getLatMin(), bb.getLonMin(), bb.getLatMax(), splitValue);
					c2bb = new BoundingBox
					(bb.getLatMin(), splitValue, bb.getLatMax(), bb.getLonMax());
				}
				children = new KDTree[2];
				children[0] = new KDTree(c1bb, maxEntities, maxDepth, depth+1);
				children[1] = new KDTree(c2bb, maxEntities, maxDepth, depth+1);
				List<MapEntity> tmp = entities;
				entities = new ArrayList<MapEntity>();
				for (MapEntity ne : tmp)
					insertEntity(ne);
			}
		} else {
			int cr = (splitAtLat
					? entity.compareLatitude(splitValue)
					: entity.compareLongitude(splitValue));
			if (cr < 0)
				children[0].insertEntity(entity);
			else if (cr > 0)
				children[1].insertEntity(entity);
			else
				entities.add(entity);
		}
	}
	
	/**
	 * Splits the bounding box, so that the new boxes have equal size
	 * and look square-like as much as possible.
	 */
	private void computeSplitValues() {
		float latMid = (bb.getLatMin()+bb.getLatMax()) / 2f;
		double width = (bb.getLonMax()-bb.getLonMin()) * Math.cos(latMid * Math.PI / 180.0);
		double height = bb.getLatMax()-bb.getLatMin();
		if (height > width) {
			splitAtLat = true;
			splitValue = latMid;
		} else {
			splitAtLat = false;
			splitValue = (bb.getLonMin()+bb.getLonMax()) / 2;
		}
	}
	
	/** Returns for each split bar the end points (lat1, lon1, lat2, lon2). */
	public List<double[]> getSplitCoords() {
		List<double[]> result;
		double[] coords;
		if (splitAtLat) {
			double lonDiff = (bb.getLonMax()-bb.getLonMin()) / 20;
			coords = new double[] {
				splitValue, bb.getLonMin()+lonDiff,
				splitValue, bb.getLonMax()-lonDiff };
		} else {
			double latDiff = (bb.getLatMax()-bb.getLatMin()) / 20;
			coords = new double[] {
					bb.getLatMin()+latDiff, splitValue, 
					bb.getLatMax()-latDiff, splitValue };
		}
		if (children == null) {
			result = new ArrayList<double[]>();
		} else {
			result = children[0].getSplitCoords();
			result.addAll(children[1].getSplitCoords());
		}
		result.add(coords);
		return result;
	}
	
	public void sortEntities(Comparator<MapEntity> comp) {
		Collections.sort(entities, comp);
		if (children != null) {
			children[0].sortEntities(comp);
			children[1].sortEntities(comp);
		}
	}
	
	public void clearRenderData() {
		for (MapEntity e : entities)
			e.setRenderData(null);
		if (children != null) {
			children[0].clearRenderData();
			children[1].clearRenderData();
		}
	}
	
	/**
	 * Enables to iterate across all contained entities within a given region
	 * in an intelligent manner. Only tree nodes, which have a chance to
	 * meet the location requirement are visited.
	 */
	public void visitEntities(MapEntityVisitor visitor, BoundingBox vbox) {
		if (!entities.isEmpty()) {
			VisibilityTest vtest = new VisibilityTest(bb, vbox);
			for (MapEntity entity : entities)
				if (vtest.isVisible(entity))
					entity.accept(visitor);
		}
		if (children != null) {
			float vMin = (splitAtLat ? vbox.getLatMin() : vbox.getLonMin());
			float vMax = (splitAtLat ? vbox.getLatMax() : vbox.getLonMax());
			if (vMin <= splitValue)
				children[0].visitEntities(visitor, vbox);
			if (vMax >= splitValue)
				children[1].visitEntities(visitor, vbox);
		}
	}
	
	private static class VisibilityTest {
		private boolean isTrue = true;
		private float testLatMin = Float.NaN;
		private float testLonMin = Float.NaN;
		private float testLatMax = Float.NaN;
		private float testLonMax = Float.NaN;
		
		VisibilityTest(BoundingBox treeBox, BoundingBox visibleBox) {
			if (treeBox.getLatMin() < visibleBox.getLatMin()) {
				testLatMin = visibleBox.getLatMin();
				isTrue = false;
			}
			if (treeBox.getLonMin() < visibleBox.getLonMin()) {
				testLonMin = visibleBox.getLonMin();
				isTrue = false;
			}
			if (treeBox.getLatMax() > visibleBox.getLatMax()) {
				testLatMax = visibleBox.getLatMax();
				isTrue = false;
			}
			if (treeBox.getLonMax() > visibleBox.getLonMax()) {
				testLonMax = visibleBox.getLonMax();
				isTrue = false;
			}
		}
		
		boolean isVisible(MapEntity entity) {
			if (isTrue)
				return true;
			if (testLatMin != Float.NaN && entity.compareLatitude(testLatMin) < 0)
				return false; // entity below visible area
			if (testLonMin != Float.NaN && entity.compareLongitude(testLonMin) < 0)
				return false; // entity to the left of visible area
			if (testLatMax != Float.NaN && entity.compareLatitude(testLatMax) > 0)
				return false; // entity above visible area
			if (testLonMax != Float.NaN && entity.compareLongitude(testLonMax) > 0)
				return false; // entity to the right of visible area
			return true;
		}
	}
}
