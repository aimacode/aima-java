package aimax.osm.reader;

import java.io.File;
import java.io.FileInputStream;
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
		InputStream inputStream = null;

		try {
			mapData.clearAll();
			inputStream = new FileInputStream(file);
			SAXParser parser = createParser();
			parser.parse(inputStream, new OsmHandler(mapData));
			mapData.compileData();

		} catch (SAXParseException e) {
			throw new OsmRuntimeException(
					"Unable to parse xml file " + file
					+ ".  publicId=(" + e.getPublicId()
					+ "), systemId=(" + e.getSystemId()
					+ "), lineNumber=" + e.getLineNumber()
					+ ", columnNumber=" + e.getColumnNumber() + ".",
					e);
		} catch (SAXException e) {
			throw new OsmRuntimeException("Unable to parse XML.", e);
		} catch (IOException e) {
			throw new OsmRuntimeException("Unable to read XML file " + file + ".", e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					LOG.log(Level.SEVERE, "Unable to close input stream.", e);
				}
				inputStream = null;
			}
		}
	}
	

	public String fileFormatDescription() {
		return "OSM File";
	}
	
	public String fileFormatExtension() {
		return "osm";
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
