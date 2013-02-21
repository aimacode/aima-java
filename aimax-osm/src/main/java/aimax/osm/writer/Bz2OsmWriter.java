package aimax.osm.writer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import aimax.osm.data.BoundingBox;
import aimax.osm.data.OsmMap;

/** 
 * Adds bz2 extraction functionality to the basic OsmWriter. The
 * implementation uses the Apache Commons Compress library
 * (see http://commons.apache.org/compress/). Please add the
 * corresponding jar file to your class path, otherwise the
 * pack functionality will not be available.
 * @author Ruediger Lunde
 */
public class Bz2OsmWriter implements MapWriter {

	private static Logger LOG = Logger.getLogger("aimax.osm");
	private OsmWriter osmReader = new OsmWriter();
	private Class<?> compressorClass;
	
	/**
	 * Tries to find the <code>BZip2CompressorInputStream</code> class using
	 * reflection and creates the reader.
	 */
	public Bz2OsmWriter () {
		try {
			compressorClass = Class.forName
			("org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream");
		} catch (ClassNotFoundException e) {
			// ok - just disable bz2 mode. 
		}
	}
	
	/**
	 * Writes all data from <code>mapData</code> to file.
	 */
	@SuppressWarnings("resource")
	@Override
	public void writeMap(File file, OsmMap mapData, BoundingBox bb) {
		try  {
			OutputStream os = new BufferedOutputStream
			(new FileOutputStream(file));
			if (compressorClass != null && file.getName().endsWith(".bz2")) {
				Constructor<?> c = compressorClass.getConstructor
				(new Class[] {OutputStream.class});
				os = (OutputStream) c.newInstance(os);
			}
			OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
			writeMap(writer, mapData, bb);
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
	@Override
	public void writeMap(OutputStreamWriter writer, OsmMap mapData, BoundingBox bb) {
		osmReader.writeMap(writer, mapData, bb);
	}
	
	@Override
	public String[] fileFormatDescriptions() {
		if (compressorClass != null)
			return new String[] {"OSM File (osm)", "OSM BZip2 (osm.bz2)"};
		else
			return new String[] {"OSM File (osm)"};
	}
	
	@Override
	public String[] fileFormatExtensions() {
		if (compressorClass != null)
			return new String[] {"osm", "bz2"};
		else
			return new String[] {"osm"};
	}
}
