package aimax.osm.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import aimax.osm.data.BoundingBox;
import aimax.osm.data.EntityClassifier;
import aimax.osm.data.MapDataConsumer;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;

public class FilteringOsmReader extends OsmReader {
	BoundingBox boundingBox;
	EntityClassifier<Boolean> attFilter;
	
	/**
	 * Sets a bounding box for the next read action from file. Map nodes which
	 * are not inside will be ignored.
	 */
	public void setBoundingBox(BoundingBox bb) {
		boundingBox = bb;
	}
	
	/**
	 * Sets an attribute filter for the next read action from file.
	 * Map entities for which the classifier returns null will be ignored.
	 */
	public void setAttFilter(EntityClassifier<Boolean> attFilter) {
		this.attFilter = attFilter;
	}
	
	/**
	 * Reads all data from the file and send it to the sink.
	 */
	public void readMap(File file, MapDataConsumer consumer) {
		if (boundingBox == null && attFilter == null) {
			super.readMap(file, consumer);
		} else {
			try  {
				InputStream inputStream = createFileStream(file);
				FilteringMapDataConsumer fcon = new FilteringMapDataConsumer
				(consumer, boundingBox, attFilter);
				if (attFilter != null) {
					fcon.setPreparationMode(true);
					readMap(inputStream, fcon);
					inputStream = createFileStream(file);
					fcon.setPreparationMode(false);
				}
				readMap(inputStream, fcon);
			} catch (FileNotFoundException e) {
				LOG.warning("File does not exist " + file);
			} catch (Exception e) {
				LOG.warning("The map could not be read. " + e);
			} finally {
				boundingBox = null;
				attFilter = null;
			}
		}
	}
	
	
	//////////////////////////////////////////////////////////////////////
	// inner classes
	
	private static class FilteringMapDataConsumer implements MapDataConsumer {
		MapDataConsumer consumer;
		BoundingBox bb;
		EntityClassifier<Boolean> attFilter;
		HashSet<Long> wayNodeIds;
		List<Long> wayNodeIdBuffer;
		boolean preparationMode;
		
		protected FilteringMapDataConsumer(MapDataConsumer consumer, BoundingBox bb, EntityClassifier<Boolean> attFilter) {
			this.consumer = consumer;
			this.bb = bb;
			this.attFilter = attFilter;
			wayNodeIds = new HashSet<Long>();
			wayNodeIdBuffer = new ArrayList<Long>();
			preparationMode = false;
		}
		
		public void setPreparationMode(boolean mode) {
			preparationMode = mode;
		}
		
		@Override
		public void clearAll() {
			if (!preparationMode)
				consumer.clearAll();
		}
		
		@Override
		public void compileData() {
			if (!preparationMode)
				consumer.compileData();
		}
		
		@Override
		public void addNode(MapNode node) {
			if (!preparationMode &&
					(bb == null ||
							node.getLat() >= bb.getLatMin()
							&& node.getLon() >= bb.getLonMin()
							&& node.getLat() <= bb.getLatMax()
							&& node.getLon() <= bb.getLonMax()) &&
					(attFilter == null
							|| wayNodeIds.contains(node.getId())
							|| attFilter.classify(node) != null))
				consumer.addNode(node);
		}
		
		@Override
		public void addWay(MapWay way) {
			if (attFilter == null || attFilter.classify(way) != null) {
				if (preparationMode)
					wayNodeIds.addAll(wayNodeIdBuffer);
				else
					consumer.addWay(way);
			}
			wayNodeIdBuffer.clear();
		}
		
		@Override
		public MapNode getWayNode(long id) {
			if (preparationMode) {
				wayNodeIdBuffer.add(id);
				return null;
			} else {
				return consumer.getWayNode(id);
			}
		}
	}
}
