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

import aimax.osm.data.MapDataStore;

/** 
 * Reads a map from file using the standard osm XML format.
 * The parser is a strongly simplified version of the Osmosis osm
 * file parser, written by Brett Henderson.
 * @author R. Lunde
 */
public class OsmReader implements MapReader {

	private static Logger LOG = Logger.getLogger("aimax.osm");
	
	/**
	 * Reads all data from the file and send it to the sink.
	 */
	public void readMap(File file, MapDataStore mapData) {
		try  {
			InputStream inputStream = new FileInputStream(file);
			readMap(inputStream, mapData);
		} catch (FileNotFoundException fnfe) {
			LOG.warning("File does not exist "+file);
		}
	}
	
	/**
	 * Reads all data from the file and send it to the sink.
	 */
	public void readMap(InputStream inputStream, MapDataStore mapData) {

		try {
			mapData.clearAll();
			SAXParser parser = createParser();
			parser.parse(inputStream, new OsmHandler(mapData));
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
	
}
