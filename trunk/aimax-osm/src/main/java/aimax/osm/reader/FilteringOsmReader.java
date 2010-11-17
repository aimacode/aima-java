package aimax.osm.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import aimax.osm.data.BoundingBox;
import aimax.osm.data.EntityClassifier;
import aimax.osm.data.MapContentBuilderProxy;
import aimax.osm.data.MapContentBuilder;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;

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
	public void readMap(File file, MapContentBuilder builder) {
		try {
			MapContentBuilderProxy proxy;
			if (boundingBox != null)
				proxy = new BBBuilderProxy(builder, boundingBox);
			else if (attFilter != null)
				proxy = new FilteringBuilderProxy(builder, attFilter);
			else
				proxy = new MapContentBuilderProxy(builder);
			proxy.prepareForNewData();
			parseMap(createFileStream(file), proxy);
			if (proxy.nodesWithoutPositionAdded()) {
				if (boundingBox != null || attFilter != null) {
					LOG.info("Starting to parse the map file a second time.");
					proxy.incrementCounter();
					parseMap(createFileStream(file), proxy);
				} else {
					LOG.warning("Nodes were referenced in ways but not defined before.");
				}
			}
			proxy.compileResults();
		} catch (FileNotFoundException e) {
			LOG.warning("File does not exist " + file);
		} catch (Exception e) {
			LOG.warning("The map could not be read. " + e);
		} finally {
			boundingBox = null;
			attFilter = null;
		}
	}

	// ////////////////////////////////////////////////////////////////////
	// inner classes

	/** Builder proxy used for bounding box filtering. */
	private static class BBBuilderProxy extends MapContentBuilderProxy {
		BoundingBox bb;

		protected BBBuilderProxy(MapContentBuilder builder, BoundingBox bb) {
			super(builder);
			this.bb = bb;
		}

		@Override
		public void prepareForNewData() {
			super.prepareForNewData();
			builder.setBoundingBox(bb);
		}

		@Override
		public void setBoundingBox(BoundingBox bb) {
		}

		@Override
		public void addNode(MapNode node) {
			if (counter == 0 && node.getLat() >= bb.getLatMin()
					&& node.getLat() <= bb.getLatMax()
					&& node.getLon() >= bb.getLonMin()
					&& node.getLon() <= bb.getLonMax())
				super.addNode(node);
		}

		@Override
		public void addWay(MapWay way, List<MapNode> wayNodes) {
			if (counter == 0) {
				for (MapNode node : wayNodes)
					if (builder.getNode(node.getId()) != null) {
						super.addWay(way, wayNodes);
						break;
					}
			}
		}
	}

	/** Builder proxy used for attribute filtering. */
	private static class FilteringBuilderProxy extends
			aimax.osm.data.MapContentBuilderProxy {
		EntityClassifier<Boolean> attFilter;

		protected FilteringBuilderProxy(MapContentBuilder builder,
				EntityClassifier<Boolean> attFilter) {
			super(builder);
			this.attFilter = attFilter;
		}

		@Override
		public void addNode(MapNode node) {
			if (attFilter.classify(node) != null)
				super.addNode(node);
		}

		@Override
		public void addWay(MapWay way, List<MapNode> wayNodes) {
			if (attFilter.classify(way) != null) {
				super.addWay(way, wayNodes);
			}
		}
	}
}
