package aimax.osm.viewer;

import java.awt.Color;

import aimax.osm.data.EntityClassifier;
import aimax.osm.data.entities.EntityViewInfo;

/**
 * Provides useful static methods for defining entity classifiers which
 * map entities on {@link aimax.osm.viewer.DefaultEntityViewInfo} objects.
 * Method {@link #createDefaultClassifier()} is perhaps the most interesting
 * one. Its implementation provides a useful example how this framework maps
 * presentation aspects like colors, line widths, symbols etc on OSM map
 * entities.
 * @author R. Lunde
 */
public class EntityViewInfoFactory {
	public static final Color GRAY_TRANS = new Color(0, 0, 0, 40);
	public static final Color LIGHT_GRAY_TRANS = new Color(0, 0, 0, 20);
	public static final Color LIGHT_RED_TRANS = new Color(255, 0, 0, 30);
	public static final Color LIGHT_YELLOW_TRANS = new Color(230, 255, 0, 60);
	public static final Color VERY_LIGHT_GREEN = new Color(200, 254, 184);
	public static final Color LIGHT_GREEN = new Color(116, 252, 133);
	public static final Color GREEN = new Color(91, 210, 95);
	public static final Color LIGHT_BLUE = new Color(164, 164, 255);
	
	/**
	 * Maintains default values. Used in specialized creator methods
	 * for unspecified attributes.
	 */
	private static DefaultEntityViewInfo DEFAULT_INFO = new DefaultEntityViewInfo(
			20000, 		// minVisibleScale
			200000, 	// minNameScale
			Color.LIGHT_GRAY.darker(), // nameColor
			EntityIcon.createRectangle(4, Color.GRAY), // icon
			false, 		// isWayIcon
			Color.GRAY, // wayColor
			1, 			// wayWidth
			false, 		// wayDashed
			LIGHT_GRAY_TRANS, // wayFillColor
			true, 		// fillAreasOnly
			10); 		// printOrder
	
	/** Changes the default values. */
	public static void setDefaults(DefaultEntityViewInfo info) {
		DEFAULT_INFO = info;
	}

	/**
	 * Creates an entity classifier, which maps entities on
	 * {@link aimax.osm.viewer.DefaultEntityViewInfo} instances and can be
	 * used to draw general purpose maps for traveling. Default rules are
	 * applied to be able to visualize every kind of entity, even completely
	 * unexpected ones.
	 */
	public static EntityClassifier<EntityViewInfo> createDefaultClassifier() {
		EntityClassifier<EntityViewInfo> result =
			new EntityClassifier<EntityViewInfo>();
		EntityClassifier<EntityViewInfo> sc;
		result.setDefaultEntityClass(createDefaultInfo());
		result.addRule("highway", "motorway", createWayInfo(0, 20000, Color.BLUE, 2, 50));
		result.addRule("highway", "motorway_link", createWayInfo(0, 20000, Color.BLUE, 2, 50));
		result.addRule("highway", "trunk", createWayInfo(0, 60000, Color.BLUE, 2, 50));
		result.addRule("highway", "trunk_link", createWayInfo(0, 90000, Color.BLUE, 2, 50));
		result.addRule("highway", "primary", createWayInfo(200, 90000, Color.PINK, 3, 49));
		result.addRule("highway", "primary_link", createWayInfo(200, 90000, Color.PINK, 3, 49));
		result.addRule("highway", "secondary", createWayInfo(500, 90000, Color.PINK, 2, 49));
		result.addRule("highway", "tertiary", createWayInfo(1000, 90000, Color.PINK, 1, 48));
		result.addRule("highway", "road", createWayInfo(1000, 90000, Color.PINK, 1, 48));
		result.addRule("highway", "residential", createWayInfo(2000, 100000, Color.LIGHT_GRAY, 1, 45));
		result.addRule("highway", "living_street", createWayInfo(2000, 100000, Color.LIGHT_GRAY, 1, 45));
		result.addRule("highway", "pedestrian", createWayInfo(2000, 100000, Color.ORANGE, 2, Color.ORANGE, true, 43));
		result.addRule("highway", "cycleway", createWayInfo(3000, 100000, Color.GREEN.darker(), 1, 40));
		result.addRule("highway", "service", createWayInfo(3000, 100000, Color.LIGHT_GRAY, 1, Color.LIGHT_GRAY, true, 39));
		result.addRule("highway", "path", createWayInfo(3000, 100000, Color.YELLOW, 1, 38));
		result.addRule("highway", "track", createWayInfo(6000, 100000, Color.YELLOW, 1, 37));
		result.addRule("highway", "footway", createWayInfo(10000, 200000, Color.ORANGE, 1, 36));
		result.addRule("highway", "steps", createWayInfo(10000, 200000, Color.ORANGE, 1, 35));
		result.addRule("highway", "unclassified", createWayInfo(10000, 200000, Color.LIGHT_GRAY, 1, 34));
		result.addRule("highway", "speed_camera", createPoiInfo(6000, 200000, Color.GRAY, EntityIcon.createTriangle(6, Color.RED, Color.WHITE), 32));

		result.addRule("natural", "land", createWayInfo(200, 30000, LIGHT_BLUE, 1, Color.WHITE, false, 29));
		result.addRule("natural", "island", createWayInfo(200, 30000, LIGHT_BLUE, 2, 23));
		result.addRule("natural", "coastline", createWayInfo(200, 30000, LIGHT_BLUE, 2, 22));
		result.addRule("natural", "water", createInfo(200, 30000, Color.GRAY, null, false, LIGHT_BLUE, 1, false, LIGHT_BLUE, false, 20));
		result.addRule("natural", "glacier", createWayInfo(200, 30000, LIGHT_BLUE.brighter(), 1, LIGHT_BLUE.brighter(), false, 19));
		result.addRule("natural", "beach", createWayInfo(3000, 30000, Color.YELLOW, 1, Color.YELLOW, false, 18));
		result.addRule("natural", "wood", createWayInfo(200, 30000, GREEN, 1, GREEN, false, 17));
		result.addRule("natural", "scrub", createWayInfo(200, 30000, LIGHT_GREEN, 1, LIGHT_GREEN, false, 16));
		result.addRule("natural", "heath", createWayInfo(500, 30000, LIGHT_GREEN, 1, LIGHT_GREEN, false, 16));
		result.addRule("natural", "peak", createPoiInfo(1000, 30000, Color.DARK_GRAY, EntityIcon.createTriangle(10, Color.ORANGE), 15));
		result.addRule("natural", null, createPoiInfo(10000, 30000, Color.DARK_GRAY, EntityIcon.createTriangle(10, Color.GREEN.darker()), 25));
		result.addRule("leisure", "park", createWayInfo(1000, 100000, VERY_LIGHT_GREEN, 1, VERY_LIGHT_GREEN, false, 14));
		result.addRule("leisure", "garden", createWayInfo(1000, 100000, VERY_LIGHT_GREEN, 1, VERY_LIGHT_GREEN, false, 14));
		result.addRule("landuse", "forest", createWayInfo(200, 30000, GREEN, 1, GREEN, false, 13));
		result.addRule("landuse", "farm", createWayInfo(500, 30000, LIGHT_YELLOW_TRANS, 1, LIGHT_YELLOW_TRANS, false, 13));
		result.addRule("landuse", "farmland", createWayInfo(500, 30000, LIGHT_YELLOW_TRANS, 1, LIGHT_YELLOW_TRANS, false, 13));
		result.addRule("landuse", "residential", createWayInfo(500, 30000, LIGHT_RED_TRANS, 1, LIGHT_RED_TRANS, false, 13));
		result.addRule("landuse", null, createInfo(1000, 200000, Color.GRAY, null, false, LIGHT_GRAY_TRANS, 1, false, LIGHT_GRAY_TRANS, false, 13));
		
		sc = result.addRule("boundary", null, createInfo(6000, 200000, Color.GRAY, null, false, Color.GRAY, 1, true, null, false, 13));
		sc.addRule("admin_level", "1", createInfo(0, 200000, Color.GRAY, null, false, Color.GRAY, 2, true, null, false, 13));
		sc.addRule("admin_level", "2", createInfo(0, 200000, Color.GRAY, null, false, Color.GRAY, 2, true, null, false, 13));
		sc.addRule("admin_level", "3", createInfo(500, 200000, Color.GRAY, null, false, Color.GRAY, 1, true, null, false, 13));
		sc.addRule("admin_level", "4", createInfo(500, 200000, Color.GRAY, null, false, Color.GRAY, 1, true, null, false, 13));
		
		
		result.addRule("waterway", "riverbank", createWayInfo(200, 30000, LIGHT_BLUE, 1, LIGHT_BLUE, false, 25));
		result.addRule("waterway", null, createWayInfo(3000, 30000, LIGHT_BLUE, 1, LIGHT_BLUE, true, 24));
		result.addRule("railway", "rail", createWayInfo(3000, 30000, Color.GRAY, 1, 30));
		result.addRule("railway", "station", createPoiInfo(10000, 60000, Color.DARK_GRAY, EntityIcon.createRectangle(4, Color.DARK_GRAY), 29));
		result.addRule("aerialway", null, createWayInfo(1000, 60000, LIGHT_BLUE.brighter(), 2, 28));

		result.addRule("historic", null, createInfo(6000, 200000, Color.GRAY, EntityIcon.createCircle(11, "H", Color.ORANGE, Color.WHITE), true, Color.GRAY, 1, false, null, false, 30));
		result.addRule("tourism", "caravan_site", createInfo(1000, 60000, Color.GRAY, EntityIcon.createRectangle(8, "P", Color.BLUE, Color.RED), true, Color.GRAY, 1, false, null, false, 28)); 
		result.addRule("tourism", "camp_site",    createInfo(1000, 60000, Color.GRAY, EntityIcon.createRectangle(8, "C", Color.GREEN.darker(), Color.WHITE), true, Color.GRAY, 1, false, null, false, 27));
		result.addRule("tourism", "attraction", createInfo(6000, 60000, Color.GRAY, EntityIcon.createCircle(11, "A", Color.GREEN.darker(), Color.WHITE), true, Color.GRAY, 1, false, null, false, 26));
		result.addRule("tourism", "viewpoint", createPoiInfo(6000, 200000, Color.GRAY, EntityIcon.createCircle(11, "V", Color.GREEN.darker(), Color.WHITE), 25));
		result.addRule("tourism", "museum", createPoiInfo(6000, 200000, Color.GRAY, EntityIcon.createCircle(11, "M", Color.GREEN.darker(), Color.WHITE), 25));
		result.addRule("tourism", "hotel", createPoiInfo(30000, 200000, Color.GRAY, EntityIcon.createRectangle(8, "H", Color.GREEN.darker(), Color.WHITE), 24));
		result.addRule("tourism", null, createPoiInfo(30000, 100000, Color.GREEN.darker(), EntityIcon.createRectangle(4, Color.GREEN.darker()), 23));
		result.addRule("amenity", "place_of_worship", createInfo(20000, 200000, Color.GRAY, EntityIcon.createTriangle(11, "+", Color.BLUE, Color.WHITE), true, GRAY_TRANS, 1, false, Color.LIGHT_GRAY, true, 15));
		result.addRule("amenity", "parking", createInfo(30000, 200000, Color.GRAY, EntityIcon.createRectangle(8, "P", Color.BLUE, Color.WHITE), true, GRAY_TRANS, 1, false, Color.LIGHT_GRAY, true, 15));
		result.addRule("amenity", null, createPoiInfo(30000, 300000, Color.BLUE, EntityIcon.createRectangle(4, Color.BLUE), 10));

		result.addRule("place", "city", createPoiInfo(0, 100, Color.BLACK, null, 60));
		result.addRule("place", "town", createPoiInfo(0, 1000, Color.BLACK, null, 59));
		result.addRule("place", "village", createPoiInfo(0, 3000, Color.DARK_GRAY, null, 58));
		result.addRule("place", null, createPoiInfo(0, 10000, Color.DARK_GRAY, null, 57));

		result.addRule("mountain_pass", null, createPoiInfo(0, 1000, Color.DARK_GRAY, null, 20));
		result.addRule("shop", null, createPoiInfo(40000, 300000, Color.CYAN, EntityIcon.createRectangle(4, Color.CYAN), 10));
		
		result.addRule("mark", "yes", createPoiInfo(0, 0, Color.RED, EntityIcon.createPin(12, Color.RED), 100));
		result.addRule("track_type", null, createTrackInfo(Color.RED));
		result.addRule("track_type", "GPS", createTrackInfo(Color.GREEN));
		
		return result;
	}
	
	/**
	 * Demonstrates, how an entity classifier for view information objects
	 * can be customized.
	 */
	public static EntityClassifier<EntityViewInfo> createNightViewClassifier() {
		EntityClassifier<EntityViewInfo> result = createDefaultClassifier();
		
		result.replaceRule("highway", "path", createWayInfo(3000, 100000, Color.YELLOW.darker(), 1, 38));
		result.replaceRule("highway", "track", createWayInfo(6000, 100000, Color.YELLOW.darker(), 1, 37));
		
		result.replaceRule("place", "city", createPoiInfo(0, 100, Color.WHITE, null, 30));
		result.replaceRule("place", "town", createPoiInfo(0, 1000, Color.WHITE, null, 29));
		result.replaceRule("place", "village", createPoiInfo(0, 3000, Color.GRAY, null, 29));
		result.replaceRule("place", null, createPoiInfo(0, 10000, Color.GRAY, null, 28));
	     
		result.replaceRule("mark", "yes", createPoiInfo(0, 0, Color.YELLOW, EntityIcon.createPin(12, Color.YELLOW), 100));
		result.replaceRule("track_type", null, createTrackInfo(Color.WHITE));

		return result;
	}
	
	/** Creates an entity view info with default values. */
	public static DefaultEntityViewInfo createDefaultInfo() {
		return new DefaultEntityViewInfo(
				DEFAULT_INFO.minVisibleScale,
				DEFAULT_INFO.minNameScale,
				DEFAULT_INFO.nameColor,
				DEFAULT_INFO.icon,
				DEFAULT_INFO.isWayIcon,
				DEFAULT_INFO.wayColor,
				DEFAULT_INFO.wayWidth,
				DEFAULT_INFO.wayDashed,
				DEFAULT_INFO.wayFillColor,
				DEFAULT_INFO.fillAreasOnly,
				DEFAULT_INFO.printOrder);
	}
	
	/** Creates an entity view info for points of interest (simple version). */
	public static DefaultEntityViewInfo createPoiInfo(float minScale, EntityIcon icon, int printOrder) {
		DefaultEntityViewInfo result = createDefaultInfo();
		result.minVisibleScale = minScale;
		result.icon = icon;
		result.printOrder = printOrder;
		return result;
	}
	
	/** Creates an entity view info for points of interest. */
	public static DefaultEntityViewInfo createPoiInfo(float minScale, float minNameScale, Color nameColor,
			EntityIcon icon, int printOrder) {
		DefaultEntityViewInfo result = createDefaultInfo();
		result.minVisibleScale = minScale;
		result.minNameScale = minNameScale;
		result.nameColor = nameColor;
		result.icon = icon;
		result.printOrder = printOrder;
		return result;
	}
	
	/** Creates an entity view info for ways (simple version). */
	public static DefaultEntityViewInfo createWayInfo(float minScale, float minNameScale,
			Color wayColor, float wayWidth, int printOrder) {
		DefaultEntityViewInfo result = createDefaultInfo();
		result.minVisibleScale = minScale;
		result.minNameScale = minNameScale;
		result.wayColor = wayColor;
		result.wayWidth = wayWidth;
		result.printOrder = printOrder;
		return result;
	}
	
	/** Creates an entity view info for ways. */
	public static DefaultEntityViewInfo createWayInfo(float minScale,
			float minNameScale,
			Color wayColor, float wayWidth,
			Color wayFillColor, boolean fillAreasOnly, int printOrder) {
		DefaultEntityViewInfo result = createDefaultInfo();
		result.minVisibleScale = minScale;
		result.minNameScale = minNameScale;
		result.wayColor = wayColor;
		result.wayWidth = wayWidth;
		result.wayFillColor = wayFillColor;
		result.fillAreasOnly = fillAreasOnly;
		result.printOrder = printOrder;
		return result;
	}
	
	/** Creates an entity view info for tracks. */
	public static DefaultEntityViewInfo createTrackInfo(Color color) {
		return new DefaultEntityViewInfo(0, 0, color,
				EntityIcon.createCircle(12, color, GRAY_TRANS), true, color, 2f, true, null, false, 0);
	}
	
	/** Creates an entity view info (equivalent to general constructor call). */
	public static DefaultEntityViewInfo createInfo(float minScale, float minNameScale, Color nameColor,
			EntityIcon icon, boolean isWayIcon, Color wayColor, float wayWidth, boolean wayDashed,
			Color wayFillColor, boolean fillAreasOnly, int printOrder) {
		return new DefaultEntityViewInfo(minScale, minNameScale, nameColor,
				icon, isWayIcon, wayColor, wayWidth, wayDashed,
				wayFillColor, fillAreasOnly, printOrder);
	}
}
