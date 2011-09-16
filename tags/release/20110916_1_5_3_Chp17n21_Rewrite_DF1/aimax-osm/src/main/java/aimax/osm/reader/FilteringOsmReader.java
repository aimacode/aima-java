package aimax.osm.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import aimax.osm.data.BoundingBox;
import aimax.osm.data.EntityClassifier;
import aimax.osm.data.MapBuilder;
import aimax.osm.data.MapBuilderProxy;
import aimax.osm.data.entities.EntityAttribute;
import aimax.osm.data.impl.DefaultMapNode;
import aimax.osm.data.impl.DefaultMapWay;

public class FilteringOsmReader extends OsmReader {
	BoundingBox boundingBox;
	EntityClassifier<Boolean> attFilter;

	/**
	 * Sets a bounding box for the next read action from file. Map nodes which
	 * are not inside will be ignored.
	 */
	public void setFilter(BoundingBox bb) {
		boundingBox = bb;
		attFilter = null;
	}

	/**
	 * Sets an attribute filter for the next read action from file. Map entities
	 * for which the classifier returns null will be ignored.
	 */
	public void setFilter(EntityClassifier<Boolean> attFilter) {
		this.attFilter = attFilter;
		boundingBox = null;
	}

	/**
	 * Reads all data from the file and send it to the sink.
	 */
	public void readMap(File file, MapBuilder builder) {
		try {
			MapBuilderProxy proxy;
			if (boundingBox != null)
				proxy = new BBBuilderProxy(builder, boundingBox);
			else if (attFilter != null)
				proxy = new FilteringBuilderProxy(builder, attFilter);
			else
				proxy = new MapBuilderProxy(builder);
			parseMap(createFileStream(file), proxy);
			if (proxy.nodeRefsWithoutDefsAdded()) {
				if (boundingBox != null || attFilter != null) {
					LOG.info("Starting to parse the map file a second time.");
					proxy.incrementCounter();
					parseMap(createFileStream(file), proxy);
				} else {
					LOG
							.warning("Nodes were referenced in ways but not defined before.");
				}
			}
		} catch (FileNotFoundException e) {
			LOG.warning("File does not exist " + file);
		} catch (Exception e) {
			LOG.warning("The map could not be read. " + e);
			e.printStackTrace();
		} finally {
			boundingBox = null;
			attFilter = null;
		}
	}

	// ////////////////////////////////////////////////////////////////////
	// inner classes

	/** Builder proxy used for bounding box filtering. */
	private static class BBBuilderProxy extends MapBuilderProxy {
		BoundingBox bb;

		protected BBBuilderProxy(MapBuilder builder, BoundingBox bb) {
			super(builder);
			this.bb = bb;
			builder.setBoundingBox(bb);
		}

		/** Does nothing. */
		@Override
		public void setBoundingBox(BoundingBox bb) {
		}

		@Override
		public void addNode(long id, String name, List<EntityAttribute> atts,
				float lat, float lon) {
			if (counter == 0 && lat >= bb.getLatMin() && lat <= bb.getLatMax()
					&& lon >= bb.getLonMin() && lon <= bb.getLonMax())
				super.addNode(id, name, atts, lat, lon);
			else if (counter == 1 && super.isNodeReferenced(id))
				super.addNode(id, name, atts, lat, lon);
		}

		@Override
		public void addWay(long id, String name, List<EntityAttribute> atts,
				List<Long> wayNodeIds) {
			if (counter == 0) {
				for (long nodeId : wayNodeIds)
					if (builder.isNodeDefined(nodeId, bb)) {
						super.addWay(id, name, atts, wayNodeIds);
						break;
					}
			}
		}
	}

	/** Builder proxy used for attribute filtering. */
	private static class FilteringBuilderProxy extends
			aimax.osm.data.MapBuilderProxy {
		EntityClassifier<Boolean> attFilter;

		protected FilteringBuilderProxy(MapBuilder builder,
				EntityClassifier<Boolean> attFilter) {
			super(builder);
			this.attFilter = attFilter;
		}

		@Override
		public void addNode(long id, String name, List<EntityAttribute> atts,
				float lat, float lon) {
			if (counter == 0) {
				DefaultMapNode node = new DefaultMapNode(id);
				node.setAttributes(atts);
				if (attFilter.classify(node) != null)
					super.addNode(id, name, atts, lat, lon);
			} else if (counter == 1 && super.isNodeReferenced(id))
				super.addNode(id, name, atts, lat, lon);
		}

		@Override
		public void addWay(long id, String name, List<EntityAttribute> atts,
				List<Long> wayNodeIds) {
			if (counter == 0) {
				DefaultMapWay way = new DefaultMapWay(id);
				way.setAttributes(atts);
				if (attFilter.classify(way) != null) {
					super.addWay(id, name, atts, wayNodeIds);
				}
			}
		}
	}
}
