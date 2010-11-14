package aimax.osm.writer;

import java.io.File;
import java.io.OutputStreamWriter;

import aimax.osm.data.BoundingBox;
import aimax.osm.data.MapDataStorage;

/**
 * Common interface for writing maps to file.
 * 
 * @author Ruediger Lunde
 */
public interface MapWriter {
	/** Writes a map to file. */
	public void writeMap(File file, MapDataStorage mapData, BoundingBox bb);

	/** Writes a map to an output stream writer. */
	public void writeMap(OutputStreamWriter writer, MapDataStorage mapData,
			BoundingBox bb);

	/** Describes the supported file formats. */
	public String[] fileFormatDescriptions();

	/** Contains the file extensions of all supported formats. */
	public String[] fileFormatExtensions();
}
