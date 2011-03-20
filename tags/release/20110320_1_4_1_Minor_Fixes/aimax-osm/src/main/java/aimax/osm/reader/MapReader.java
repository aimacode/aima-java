package aimax.osm.reader;

import java.io.File;
import java.io.InputStream;

import aimax.osm.data.BoundingBox;
import aimax.osm.data.EntityClassifier;
import aimax.osm.data.MapBuilder;

/**
 * Common interface for reading maps from file.
 * @author R. Lunde
 */
public interface MapReader {
	/**
	 * Sets a bounding box for the next read action from file.
	 * Map nodes which are not inside will be ignored.
	 */
	public void setFilter(BoundingBox bb) throws UnsupportedOperationException;
	/**
	 * Sets an attribute filter for the next read action from file.
	 * Map entities for which the classifier returns null will be ignored.
	 */
	public void setFilter(EntityClassifier<Boolean> attFilter)
		throws UnsupportedOperationException;
	/** Reads a map from file. */
	public void readMap(File file, MapBuilder mapData);
	/** Reads a map from an input stream (needed for web starter). */
	public void readMap(InputStream is, MapBuilder mapData);
	/** Describes the supported file formats. */
	public String[] fileFormatDescriptions();
	/** Contains the file extensions of all supported formats. */
	public String[] fileFormatExtensions();
}
