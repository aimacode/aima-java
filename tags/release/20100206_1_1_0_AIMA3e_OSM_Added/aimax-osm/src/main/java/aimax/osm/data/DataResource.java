package aimax.osm.data;

import java.io.InputStream;

public class DataResource {
	public static InputStream getULMFileResource() {
		return DataResource.class.getResourceAsStream("ulm.osm");
	}
}
