package aimax.osm.reader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import aimax.osm.data.MapDataStore;

/** 
 * Adds bz2 extraction functionality to the basic OsmReader. The
 * implementation uses the Apache Commons Compress library
 * (see http://commons.apache.org/compress/). Please add the
 * corresponding jar file to your class path, otherwise the
 * unpack functionality will not be available.
 * @author R. Lunde
 */
public class OsmBz2Reader implements MapReader {

	private static Logger LOG = Logger.getLogger("aimax.osm");
	private OsmReader osmReader = new OsmReader();
	private Class compressorClass;
	
	/**
	 * Tries to find the <code>BZip2CompressorInputStream</code> class using
	 * reflection and creates the reader.
	 */
	public OsmBz2Reader () {
		try {
			compressorClass = Class.forName
			("org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream");
		} catch (ClassNotFoundException e) {
			// ok - just disable bz2 mode. 
		}
	}
	
	/**
	 * Reads all data from the file and send it to the sink.
	 */
	public void readMap(File file, MapDataStore mapData) {
		try  {
			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
			if (compressorClass != null && file.getName().endsWith(".bz2")) {
				Constructor c = compressorClass.getConstructor
				(new Class[] {InputStream.class});
				inputStream = (InputStream) c.newInstance(inputStream);
			}
			readMap(inputStream, mapData);
		} catch (FileNotFoundException e) {
			LOG.warning("File does not exist " + file);
		} catch (InvocationTargetException e) {
			LOG.warning("The map could not be read. Possibly unpacking problem. " + e);
		} catch (Exception e) {
			LOG.warning("The map could not be read. " + e);
		}
	}
	
	/**
	 * Reads all data from the file and send it to the sink.
	 */
	public void readMap(InputStream inputStream, MapDataStore mapData) {
		osmReader.readMap(inputStream, mapData);
	}
	
	public String[] fileFormatDescriptions() {
		if (compressorClass != null)
			return new String[] {"OSM BZip2 (osm.bz2)", "OSM File (osm)"};
		else
			return new String[] {"OSM File (osm)"};
	}
	
	public String[] fileFormatExtensions() {
		if (compressorClass != null)
			return new String[] {"bz2", "osm"};
		else
			return new String[] {"osm"};
	}
}
