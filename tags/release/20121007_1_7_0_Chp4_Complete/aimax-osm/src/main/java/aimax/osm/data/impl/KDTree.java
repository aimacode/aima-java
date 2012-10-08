package aimax.osm.data.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import aimax.osm.data.BoundingBox;
import aimax.osm.data.EntityVisitor;
import aimax.osm.data.entities.MapEntity;


/**
 * Implementation of a kd-tree (node) for map entities. Entities are
 * divided by their two-dimensional geographical coordinates and within each
 * kd-tree node ordered by ascending minimal visible scale. Note, that
 * inner nodes can contain entities, e.g. ways, whose nodes do
 * not completely fit into the bounding box of one of the child nodes.
 * @author Ruediger Lunde
 */
public class KDTree {
	private BoundingBox bb;
	private KDTree[] children; // null or array of length 2
	private int depth;
	private int maxEntities;
	private int maxDepth;
	private ArrayList<DefaultMapEntity> entities;
	private boolean splitAtLat;
	private float splitValue;
	private boolean isSorted;
	
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
		entities = new ArrayList<DefaultMapEntity>();
	}
	
	/** Constructor for inner and leaf nodes. */
	private KDTree(BoundingBox bb, int maxEntities, int maxDepth, int depth) {
		this(bb, maxEntities, maxDepth);
		this.depth = depth;
	}
	
	public BoundingBox getBoundingBox() {
		return bb;
	}
	
	/** Returns the depth of the tree (longest path length from root to leaf). */
	public int depth() {
		return children == null ? 1 : 1+Math.max(children[0].depth(), children[1].depth());
	}
	
	/** Must be called after classification of entities has been changed. */
	public void setUnsorted() {
		isSorted = false;
		if (children != null) {
			children[0].setUnsorted();
			children[1].setUnsorted();
		}
	}
	
	/**
	 * Adds an entity at the right position in the tree and extends
	 * the tree if necessary. It is assumed that the entity contains
	 * view information.
	 */
	public void insertEntity(DefaultMapEntity entity) {
		if (children == null) {
			entities.add(entity);
			isSorted = false;
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
				List<DefaultMapEntity> tmp = entities;
				entities = new ArrayList<DefaultMapEntity>();
				for (DefaultMapEntity ne : tmp)
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
			else {
				entities.add(entity);
				isSorted = false;
			}
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
	
	/**
	 * Enables to iterate across all contained entities within a given region
	 * in an intelligent manner. Only tree nodes, which have a chance to
	 * meet the location requirement are visited.
	 */
	public void visitEntities(EntityVisitor visitor, BoundingBox vbox, float scale) {
		if (!entities.isEmpty()) {
			if (!isSorted) {
				Collections.sort(entities, new EntityComparator());
				isSorted = true;
			}
			VisibilityTest vtest = new VisibilityTest(bb, vbox);
			for (DefaultMapEntity entity : entities) {
				if (entity.getViewInfo().getMinVisibleScale() > scale)
					break;
				if (vtest.isVisible(entity))
					entity.accept(visitor);
			}
		}
		if (children != null) {
			float vMin = (splitAtLat ? vbox.getLatMin() : vbox.getLonMin());
			float vMax = (splitAtLat ? vbox.getLatMax() : vbox.getLonMax());
			if (vMin <= splitValue)
				children[0].visitEntities(visitor, vbox, scale);
			if (vMax >= splitValue)
				children[1].visitEntities(visitor, vbox, scale);
		}
	}

	
	/////////////////////////////////////////////////////////////////
	// some inner classes
	
	/**
	 * Compares entities with respect to their minimal visible scale.
	 * Entities which are already visible in small scales are preferred.
	 */
	private static class EntityComparator implements Comparator<MapEntity> {
		@Override
		public int compare(MapEntity e1, MapEntity e2) {
			float vs1 = e1.getViewInfo().getMinVisibleScale();
			float vs2 = e2.getViewInfo().getMinVisibleScale();
			if (vs1 < vs2)
				return -1;
			else if (vs1 > vs2)
				return 1;
			else
				return 0;
		}
	}
	
	/**
	 * If a kd-tree node's bounding box is not completely visible,
	 * this class helps to check which entities are visible and which not.
	 * @author Ruediger Lunde
	 *
	 */ 
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
		
		boolean isVisible(DefaultMapEntity entity) {
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
