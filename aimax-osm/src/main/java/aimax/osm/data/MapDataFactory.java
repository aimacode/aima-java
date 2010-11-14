package aimax.osm.data;

import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.data.entities.Track;
import aimax.osm.data.impl.DefaultMapDataFactory;

public abstract class MapDataFactory {
	public final static String FACTORY_CLASS_PROPERTY = "aimax.osm.data.factoryclass";

	private static MapDataFactory instance;

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

	public abstract MapDataStorage createMapDataStorage();

	public abstract MapNode createMapNode(long id);

	public abstract MapWay createMapWay(long id);

	public abstract Track createTrack(long id, String name, String trackType);
}
