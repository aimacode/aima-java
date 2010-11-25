package aimax.osm.data;


/**
 * Factory which is used to create the default map.
 * @author Ruediger Lunde
 *
 */
public class MapDataFactory {
	public final static String MAPCLASS_PROPERTY = "aimax.osm.data.mapbuilderclass";

//	private static MapDataFactory instance;
//
//	/** Provides access to the factory to be used for creating domain objects. */
//	public static MapDataFactory instance() {
//		if (instance == null) {
//			instance = new MapDataFactory();
//		}
//		return instance;
//	}

	/** Creates the default map object. */
	public static OsmMap createMap() {
		OsmMap result = null;
		String className = System.getProperty(MAPCLASS_PROPERTY);
		if (className == null) {
			className = "aimax.osm.data.impl.DefaultMap";
			System.setProperty(MAPCLASS_PROPERTY, className);
		}
		try {
			Class<?> c = Class.forName(className);
			result = (OsmMap) c.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
