package aimax.osm.data;

import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.data.entities.Track;
import aimax.osm.data.impl.DefaultMapDataFactory;

/**
 * Abstract factory for creating a consistent family of domain objects such
 * as nodes and ways as well as a suitable container for them. Whereas other
 * components like viewers, readers etc make no assumptions about the
 * implementations behind the interfaces, entities and corresponding 
 * containers will typically do so. Subclass should provide implementations
 * for the creation operations. This class is also a singleton which 
 * maintains the factory to be used. The factory to be used can be
 * selected by specifying a system property called:
 * <code>aimax.osm.data.factoryclass</code>. 
 * @author Ruediger Lunde
 *
 */
public abstract class MapDataFactory {
	public final static String FACTORY_CLASS_PROPERTY = "aimax.osm.data.factoryclass";

	private static MapDataFactory instance;

	/** Provides access to the factory to be used for creating domain objects. */
	public static MapDataFactory instance() {
		if (instance == null) {
			String className = System.getProperty(FACTORY_CLASS_PROPERTY);
			if (className != null) {
				try {
					Class<?> c = Class.forName(className);
					instance = (MapDataFactory) c.newInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (instance == null)
				instance = new DefaultMapDataFactory();
		}
		return instance;
	}

	/** Creates a map data storage object. */
	public abstract MapDataStorage createMapDataStorage();

	/** Creates a map node object with the specified id. */
	public abstract MapNode createMapNode(long id);

	/** Creates a map way object with the specified id. */
	public abstract MapWay createMapWay(long id);

	/** Creates a new track. */
	public abstract Track createTrack(long id, String name, String trackType);
}
