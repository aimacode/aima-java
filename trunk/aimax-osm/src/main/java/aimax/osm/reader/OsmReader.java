package aimax.osm.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import aimax.osm.data.BoundingBox;
import aimax.osm.data.MapDataConsumer;
import aimax.osm.data.MapDataStore;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;

/** 
 * Reads a map from file using the standard osm XML format.
 * The parser is a strongly simplified version of the Osmosis osm
 * file parser, written by Brett Henderson.
 * @author R. Lunde
 */
public class OsmReader implements MapReader {

	private static Logger LOG = Logger.getLogger("aimax.osm");
	protected BoundingBox boundingBox;
	
	/**
	 * Sets a bounding box for the next read action. Map nodes which are
	 * not inside will be ignored.
	 */
	public void setBoundingBox(BoundingBox bb) {
		boundingBox = bb;
	}
	
	/**
	 * Reads all data from the file and send it to the sink.
	 */
	public void readMap(File file, MapDataStore mapData) {
		try  {
			InputStream inputStream = new FileInputStream(file);
			readMap(inputStream, mapData);
		} catch (FileNotFoundException fnfe) {
			LOG.warning("File does not exist "+file);
			boundingBox = null;
		}
	}
	
	/**
	 * Reads all data from the file and send it to the sink.
	 */
	public void readMap(InputStream inputStream, MapDataStore mapData) {

		try {
			mapData.clearAll();
			SAXParser parser = createParser();
			MapDataConsumer consumer = (boundingBox == null)
			? mapData : new BBMapDataConsumer(mapData, boundingBox);
			parser.parse(inputStream, new OsmHandler(consumer));
			mapData.compileData();

		} catch (SAXParseException e) {
			throw new OsmRuntimeException(
					"Unable to parse input stream"
					+ ".  publicId=(" + e.getPublicId()
					+ "), systemId=(" + e.getSystemId()
					+ "), lineNumber=" + e.getLineNumber()
					+ ", columnNumber=" + e.getColumnNumber() + ".",
					e);
		} catch (SAXException e) {
			throw new OsmRuntimeException("Unable to parse XML.", e);
		} catch (IOException e) {
			throw new OsmRuntimeException("Unable to read XML input stream.", e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					LOG.log(Level.SEVERE, "Unable to close input stream.", e);
				}
			}
			boundingBox = null;
		}
	}
	

	public String[] fileFormatDescriptions() {
		return new String[] {"OSM File (osm)"};
	}
	
	public String[] fileFormatExtensions() {
		return new String[] {"osm"};
	}


	/**
	 * Creates a new SAX parser.
	 * 
	 * @return The newly created SAX parser.
	 */
	private SAXParser createParser() {
		try {
			return SAXParserFactory.newInstance().newSAXParser();
			
		} catch (ParserConfigurationException e) {
			throw new OsmRuntimeException("Unable to create SAX Parser.", e);
		} catch (SAXException e) {
			throw new OsmRuntimeException("Unable to create SAX Parser.", e);
		}
	}
	
	
	//////////////////////////////////////////////////////////////////////
	// inner classes
	
	private static class BBMapDataConsumer implements MapDataConsumer {
		MapDataConsumer consumer;
		BoundingBox bb;
		
		protected BBMapDataConsumer(MapDataConsumer consumer, BoundingBox bb) {
			this.consumer = consumer;
			this.bb = bb;
		}
		
		@Override
		public void addNode(MapNode node) {
			if (node.getLat() >= bb.getLatMin()
					&& node.getLon() >= bb.getLonMin()
					&& node.getLat() <= bb.getLatMax()
					&& node.getLon() <= bb.getLonMax())
			consumer.addNode(node);
			
		}
		@Override
		public void addWay(MapWay way) {
			consumer.addWay(way);
			
		}
		@Override
		public MapNode getWayNode(long id) {
			return consumer.getWayNode(id);
		}
	}
}
