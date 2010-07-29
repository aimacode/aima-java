package aimax.osm.writer;

import java.io.File;
import java.io.OutputStreamWriter;

import aimax.osm.data.MapDataStore;

/**
 * Common interface for writing maps to file.
 * @author Ruediger Lunde
 */
public interface MapWriter {
	/** Writes a map to file. */
	public void writeMap(File file, MapDataStore mapData);
	/** Writes a map to an output stream writer. */
	public void writeMap(OutputStreamWriter writer, MapDataStore mapData);
	/** Describes the supported file formats. */
	public String[] fileFormatDescriptions();
	/** Contains the file extensions of all supported formats. */
	public String[] fileFormatExtensions();
}
