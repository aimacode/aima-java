package aimax.osm.reader;

import java.io.File;
import java.io.InputStream;

import aimax.osm.data.BoundingBox;
import aimax.osm.data.MapDataStore;

/**
 * Common interface for reading maps from file.
 * @author R. Lunde
 */
public interface MapReader {
	/**
	 * Sets a bounding box for the next read action. Map nodes which are
	 * not inside will be ignored.
	 */
	public void setBoundingBox(BoundingBox bb);
	/** Reads a map from file. */
	public void readMap(File file, MapDataStore mapData);
	/** Reads a map from an input stream (needed for web starter). */
	public void readMap(InputStream is, MapDataStore mapData);
	/** Describes the supported file formats. */
	public String[] fileFormatDescriptions();
	/** Contains the file extensions of all supported formats. */
	public String[] fileFormatExtensions();
}
