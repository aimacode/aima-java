// License: GPL. Copyright 2007-2008 by Brett Henderson and other contributors.
// Modified by Ruediger Lunde, 2009
package aimax.osm.reader;

import java.io.File;

import aimax.osm.data.MapDataStore;

/**
 * Common interface for reading maps from file.
 * @author R. Lunde
 */
public interface MapReader {
	public void readMap(File file, MapDataStore mapData);
	public String fileFormatDescription();
	public String fileFormatExtension();
}
