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
import aimax.osm.data.EntityClassifier;
import aimax.osm.data.MapBuilder;

/** 
 * Reads a map from file using the standard osm XML format.
 * The parser is a strongly simplified version of the Osmosis osm
 * file parser, written by Brett Henderson.
 * @author R. Lunde
 */
public class OsmReader implements MapReader {

	protected static Logger LOG = Logger.getLogger("aimax.osm");
	
	/**
	 * This implementation throws an <code>UnsupportedOperationException</code>.
	 */
	public void setFilter(BoundingBox bb) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * This implementation throws an <code>UnsupportedOperationException</code>.
	 */
	public void setFilter(EntityClassifier<Boolean> attFilter) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Reads all data from the file and send it to the sink.
	 */
	public void readMap(File file, MapBuilder builder) {
		try  {
			parseMap(createFileStream(file), builder);
		} catch (FileNotFoundException e) {
			LOG.warning("File "  + file + " does not exist.");
		} catch (Exception e) {
			LOG.warning("The map could not be read. " + e);
		}
	}
	
	/**
	 * Reads all data from the specified stream and sends it to the consumer.
	 * The consumer is cleared before.
	 */
	public void readMap(InputStream inputStream, MapBuilder builder) {
		try {
			parseMap(inputStream, builder);
			if (builder.nodeRefsWithoutDefsAdded())
				LOG.warning("Nodes were referenced in ways but not defined before.");
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
	
	protected void parseMap(InputStream inputStream, MapBuilder consumer)
			throws SAXException, IOException {
		SAXParser parser = createParser();
		parser.parse(inputStream, new OsmHandler(consumer));
	}
	
	public String[] fileFormatDescriptions() {
		return new String[] {"OSM File (osm)"};
	}
	
	public String[] fileFormatExtensions() {
		return new String[] {"osm"};
	}

	/**
	 * Factory method, responsible for creating an input stream for a
	 * specified file. 
	 */
	protected InputStream createFileStream(File file) throws Exception {
		return new FileInputStream(file);
	}
	
	/**
	 * Factory method, responsible for creating a new SAX parser.
	 */
	protected SAXParser createParser() {
		try {
			return SAXParserFactory.newInstance().newSAXParser();
		} catch (ParserConfigurationException e) {
			throw new OsmRuntimeException("Unable to create SAX Parser.", e);
		} catch (SAXException e) {
			throw new OsmRuntimeException("Unable to create SAX Parser.", e);
		}
	}
}
