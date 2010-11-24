package aimax.osm.data;


/**
 * Factory which is used to create the default map builder.
 * @author Ruediger Lunde
 *
 */
public class MapDataFactory {
	public final static String MAPBUILDERCLASS_PROPERTY = "aimax.osm.data.mapbuilderclass";

//	private static MapDataFactory instance;
//
//	/** Provides access to the factory to be used for creating domain objects. */
//	public static MapDataFactory instance() {
//		if (instance == null) {
//			instance = new MapDataFactory();
//		}
//		return instance;
//	}

	/** Creates the default map builder. */
	public static MapBuilder createMapBuilder() {
		MapBuilder result = null;
		String className = System.getProperty(MAPBUILDERCLASS_PROPERTY);
		if (className == null) {
			className = "aimax.osm.data.impl.DefaultMapBuilder";
			System.setProperty(MAPBUILDERCLASS_PROPERTY, className);
		}
		try {
			Class<?> c = Class.forName(className);
			result = (MapBuilder) c.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
