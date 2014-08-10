package aimax.osm.reader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/** 
 * Adds bz2 extraction functionality to the <code>FilteringOsmReader</code>.
 * The implementation uses the Apache Commons Compress library
 * (see http://commons.apache.org/compress/). Please add the
 * corresponding jar file to your class path, otherwise the
 * unpack functionality will not be available.
 * @author Ruediger Lunde
 */
public class Bz2OsmReader extends FilteringOsmReader {

	private Class<?> compressorClass;
	
	/**
	 * Tries to find the <code>BZip2CompressorInputStream</code> class using
	 * reflection and creates the reader.
	 */
	public Bz2OsmReader() {
		try {
			compressorClass = Class.forName
			("org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream");
		} catch (ClassNotFoundException e) {
			// ok - just disable bz2 mode. 
		}
	}
	
	@SuppressWarnings("resource")
	protected InputStream createFileStream(File file) throws Exception {
		InputStream result = new BufferedInputStream(new FileInputStream(file));
		try {
			if (compressorClass != null && file.getName().endsWith(".bz2")) {
				Constructor<?> c = compressorClass.getConstructor
				(new Class[] {InputStream.class});
				result = (InputStream) c.newInstance(result);
			}
		} catch (InvocationTargetException e) {
			LOG.warning("Failure occured while reading a map. Possibly unpacking problem.");
			throw e;
		}
		return result;
	}
	
	public String[] fileFormatDescriptions() {
		if (compressorClass != null)
			return new String[] {"OSM File (osm)", "OSM BZip2 (osm.bz2)"};
		else
			return new String[] {"OSM File (osm)"};
	}
	
	public String[] fileFormatExtensions() {
		if (compressorClass != null)
			return new String[] {"osm", "bz2"};
		else
			return new String[] {"osm"};
	}
}
