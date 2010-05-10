package aimax.osm.writer;

import java.io.File;
import java.io.OutputStreamWriter;

import aimax.osm.data.MapDataStore;

/**
 * Common interface for writing maps to file.
 * @author R. Lunde
 */
public interface MapWriter {
	public void writeMap(File file, MapDataStore mapData);
	public void writeMap(OutputStreamWriter writer, MapDataStore mapData);
	public String[] fileFormatDescriptions();
	public String[] fileFormatExtensions();
}
