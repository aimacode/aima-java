package aimax.osm.data;

import java.io.InputStream;

/**
 * Provides a stream with OSM map data describing the city of Ulm.
 * @author Ruediger Lunde
 */
public class DataResource {
	public static InputStream getUlmFileResource() {
		return DataResource.class.getResourceAsStream("ulm.osm");
	}
}
