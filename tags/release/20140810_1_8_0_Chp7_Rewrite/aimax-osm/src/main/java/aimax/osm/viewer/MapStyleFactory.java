package aimax.osm.viewer;

import aimax.osm.data.EntityClassifier;
import aimax.osm.data.entities.EntityViewInfo;
import aimax.osm.viewer.EntityIcon.PinIcon;
import aimax.osm.viewer.EntityIcon.SimpleIcon;
import aimax.osm.viewer.EntityIcon.TentIcon;

/**
 * Provides useful static methods for defining map styles. Technically spoken,
 * in this framework a map style is entity classifiers which map entities on
 * {@link aimax.osm.data.entities.EntityViewInfo} objects. Method
 * {@link #createDefaultClassifier()} is perhaps the most interesting one. Its
 * implementation provides a useful example how to map presentation aspects like
 * colors, line widths, symbols etc on OSM map entities.
 * 
 * @author Ruediger Lunde
 */
public class MapStyleFactory {
	public static final UColor GRAY_TRANS = new UColor(0, 0, 0, 40);
	public static final UColor LIGHT_GRAY_TRANS = new UColor(0, 0,
			0, 20);
	public static final UColor LIGHT_RED_TRANS = new UColor(255, 0,
			0, 30);
	public static final UColor LIGHT_RED = new UColor(230, 130, 130);
	public static final UColor LIGHT_ORANGE = new UColor(255, 240,
			120);
	public static final UColor LIGHT_YELLOW = new UColor(255, 255,
			200);
	public static final UColor LIGHT_GREEN_TRANS = new UColor(114,
			254, 86, 30);
	public static final UColor VERY_LIGHT_GREEN = new UColor(200,
			254, 184);
	public static final UColor VERY_LIGHT_BLUE = new UColor(220,
			220, 230);
	public static final UColor LIGHT_GREEN = new UColor(116, 252,
			133);
	public static final UColor GREEN = new UColor(91, 210, 95);
	public static final UColor LIGHT_BLUE = new UColor(164, 164,
			255);

	/**
	 * Maintains default values. Used in specialized creator methods for
	 * unspecified attributes.
	 */
	private static DefaultEntityViewInfo DEFAULT_INFO = new DefaultEntityViewInfo(
			1f / 6000, // minVisibleScale
			1f / 1800, // minNameScale
			100, // printOrder
			UColor.LIGHT_GRAY.darker(), // nameColor
			createRectangle(4, UColor.GRAY), // icon
			false, // isWayIcon
			UColor.GRAY, // wayColor
			0.5f, // wayWidth
			false, // wayDashed
			LIGHT_GRAY_TRANS, // wayFillColor
			true); // fillAreasOnly

	/** Changes the default values. */
	public static void setDefaults(DefaultEntityViewInfo info) {
		DEFAULT_INFO = info;
	}

	/**
	 * Creates an entity classifier, which maps entities on
	 * {@link aimax.osm.viewer.DefaultEntityViewInfo} instances and can be used
	 * to draw general purpose maps for traveling. Default rules are applied to
	 * be able to visualize every kind of entity, even completely unexpected
	 * ones.
	 */
	public EntityClassifier<EntityViewInfo> createDefaultClassifier() {
		EntityClassifier<EntityViewInfo> result = new EntityClassifier<EntityViewInfo>();
		EntityClassifier<EntityViewInfo> sc;
		result.setDefaultEntityClass(createDefaultInfo());
		result.addRule(
				"highway",
				"motorway",
				createWayInfo(1f / 2000000, 1f / 20000, 50, UColor.BLUE,
						3));
		result.addRule(
				"highway",
				"motorway_link",
				createWayInfo(1f / 200000, 1f / 20000, 51, UColor.BLUE, 1));
		result.addRule(
				"highway",
				"trunk",
				createWayInfo(1f / 2000000, 1f / 6000, 52, UColor.BLUE, 2));
		result.addRule("highway", "trunk_link",
				createWayInfo(1f / 200000, 1f / 6000, 53, UColor.BLUE, 1));
		result.addRule(
				"highway",
				"primary",
				createWayInfo(1f / 1000000, 1f / 6000, 54, UColor.PINK, 4));
		result.addRule("highway", "primary_link",
				createWayInfo(1f / 100000, 1f / 6000, 55, UColor.PINK, 4));
		result.addRule("highway", "secondary",
				createWayInfo(1f / 500000, 1f / 6000, 56, UColor.PINK, 3));
		result.addRule("highway", "tertiary",
				createWayInfo(1f / 250000, 1f / 6000, 57, UColor.PINK, 2));
		result.addRule("highway", "road",
				createWayInfo(1f / 190000, 1f / 6000, 58, UColor.PINK, 1));
		result.addRule(
				"highway",
				"residential",
				createWayInfo(1f / 100000, 1f / 3500, 65,
						UColor.LIGHT_GRAY, 1));
		result.addRule(
				"highway",
				"living_street",
				createWayInfo(1f / 100000, 1f / 3500, 65,
						UColor.LIGHT_GRAY, 1));
		result.addRule(
				"highway",
				"pedestrian",
				createWayInfo(1f / 190000, 1f / 3500, 65, UColor.ORANGE,
						2, UColor.ORANGE, true));
		result.addRule(
				"highway",
				"cycleway",
				createWayInfo(1f / 100000, 1f / 3500, 65,
						UColor.GREEN.darker(), 1));
		result.addRule(
				"highway",
				"service",
				createWayInfo(1f / 100000, 1f / 3500, 65, UColor.LIGHT_GRAY, 1,
						UColor.LIGHT_GRAY, true));
		result.addRule("highway", "path",
				createWayInfo(1f / 100000, 1f / 3500, 65, UColor.YELLOW, 1));
		result.addRule("highway", "track",
				createWayInfo(1f / 60000, 1f / 3500, 65, UColor.YELLOW, 1));
		result.addRule("highway", "unclassified",
				createWayInfo(1f / 35000, 1f / 2500, 65, UColor.LIGHT_GRAY, 1));
		result.addRule(
				"highway",
				"footway",
				createWayInfo(1f / 60000, 1f / 2500, 68, UColor.ORANGE, 1,
						UColor.ORANGE, true));
		result.addRule(
				"highway",
				"steps",
				createWayInfo(1f / 35000, 1f / 2500, 68, UColor.ORANGE, 1,
						UColor.ORANGE, true));
		result.addRule(
				"highway",
				"speed_camera",
				createPoiInfo(1f / 60000, 1f / 1800, 68, UColor.GRAY,
						createTriangle(6, UColor.RED, UColor.WHITE), false));

		result.addRule(
				"natural",
				"land",
				createWayInfo(1f / 2000000, 1f / 35000, 40, LIGHT_BLUE, 1,
						UColor.WHITE, false));
		result.addRule("natural", "island",
				createWayInfo(1f / 2000000, 1f / 100000, 40, LIGHT_BLUE, 2));
		result.addRule("natural", "coastline",
				createWayInfo(1e-9f, 1f / 5000000, 40, LIGHT_BLUE, 2));
		result.addRule("natural", "cliff",
				createWayInfo(1f / 750000, 1f / 12000, 41, UColor.GRAY, 1));
		result.addRule(
				"natural",
				"water",
				createInfo(1f / 2000000, 1f / 35000, 39, UColor.GRAY, null,
						false, LIGHT_BLUE, 1, false, LIGHT_BLUE, false));
		result.addRule(
				"natural",
				"glacier",
				createWayInfo(1f / 750000, 1f / 35000, 40, LIGHT_BLUE, 1,
						LIGHT_BLUE.brighter(), false));
		result.addRule(
				"natural",
				"beach",
				createWayInfo(1f / 100000, 1f / 12000, 80, UColor.YELLOW, 1,
						UColor.YELLOW, false));
		result.addRule(
				"natural",
				"wood",
				createWayInfo(1f / 750000, 1f / 12000, 80, GREEN, 1, GREEN,
						false));
		result.addRule(
				"natural",
				"scrub",
				createWayInfo(1f / 500000, 1f / 12000, 80, LIGHT_GREEN, 1,
						LIGHT_GREEN, false));
		result.addRule(
				"natural",
				"heath",
				createWayInfo(1f / 500000, 1f / 12000, 80, LIGHT_GREEN, 1,
						LIGHT_GREEN, false));
		result.addRule(
				"natural",
				"fell",
				createWayInfo(1f / 500000, 1f / 12000, 80, LIGHT_GREEN, 1,
						UColor.LIGHT_GRAY, false));
		result.addRule(
				"natural",
				"peak",
				createPoiInfo(1f / 350000, 1f / 35000, 38, UColor.DARK_GRAY,
						createTriangle(10, UColor.ORANGE), false));
		result.addRule(
				"natural",
				null,
				createInfo(1f / 350000, 1f / 12000, 80, UColor.DARK_GRAY,
						createTriangle(8, UColor.GREEN.darker()), false,
						VERY_LIGHT_GREEN, 1, false, LIGHT_GREEN_TRANS, false));
		result.addRule(
				"leisure",
				"park",
				createWayInfo(1f / 350000, 1f / 6000, 80, VERY_LIGHT_GREEN, 1,
						VERY_LIGHT_GREEN, false));
		result.addRule(
				"leisure",
				"garden",
				createWayInfo(1f / 60000, 1f / 3500, 80, VERY_LIGHT_GREEN, 1,
						VERY_LIGHT_GREEN, false));
		result.addRule(
				"landuse",
				"forest",
				createWayInfo(1f / 750000, 1f / 12000, 80, GREEN, 1, GREEN,
						false));
		result.addRule(
				"landuse",
				"reservoir",
				createWayInfo(1f / 750000, 1f / 12000, 80, LIGHT_BLUE, 1,
						LIGHT_BLUE, false));
		result.addRule(
				"landuse",
				"farm",
				createWayInfo(1f / 750000, 1f / 12000, 80, LIGHT_YELLOW, 1,
						LIGHT_YELLOW, false));
		result.addRule(
				"landuse",
				"farmland",
				createWayInfo(1f / 750000, 1f / 12000, 80, LIGHT_YELLOW, 1,
						LIGHT_YELLOW, false));
		result.addRule(
				"landuse",
				"residential",
				createWayInfo(1f / 750000, 1f / 12000, 80, LIGHT_RED_TRANS, 1,
						LIGHT_RED_TRANS, false));
		result.addRule(
				"landuse",
				"village_green",
				createWayInfo(1f / 60000, 1f / 3500, 80, VERY_LIGHT_GREEN, 1,
						VERY_LIGHT_GREEN, false));
		result.addRule(
				"landuse",
				"recreation_ground",
				createWayInfo(1f / 60000, 1f / 3500, 80, VERY_LIGHT_GREEN, 1,
						VERY_LIGHT_GREEN, false));
		result.addRule(
				"landuse",
				"grass",
				createWayInfo(1f / 350000, 1f / 3500, 80, VERY_LIGHT_GREEN, 1,
						VERY_LIGHT_GREEN, false));
		result.addRule(
				"landuse",
				null,
				createInfo(1f / 350000, 1f / 1800, 80, UColor.GRAY, null, false,
						LIGHT_GRAY_TRANS, 1, false, LIGHT_GRAY_TRANS, false));
		addBuildingRules(
				result,
				"leisure",
				null,
				createInfo(1f / 60000, 1f / 3500, 80, UColor.GRAY, null, false,
						LIGHT_ORANGE, 1, false, LIGHT_ORANGE, false));

		sc = result.addRule(
				"boundary",
				null,
				createInfo(1f / 60000, 1f / 3500, 90, UColor.GRAY, null, false,
						UColor.GRAY, 1, true, null, false));
		sc.addRule(
				"admin_level",
				"1",
				createInfo(1e-9f, 1f / 12000, 86, UColor.GRAY, null, false,
						UColor.GRAY, 2, true, null, false));
		sc.addRule(
				"admin_level",
				"2",
				createInfo(1e-9f, 1f / 12000, 87, UColor.GRAY, null, false,
						UColor.GRAY, 2, true, null, false));
		sc.addRule(
				"admin_level",
				"3",
				createInfo(1f / 750000, 1f / 3500, 88, UColor.GRAY, null, false,
						UColor.GRAY, 1, true, null, false));
		sc.addRule(
				"admin_level",
				"4",
				createInfo(1f / 750000, 1f / 3500, 89, UColor.GRAY, null, false,
						UColor.GRAY, 1, true, null, false));

		result.addRule(
				"waterway",
				"riverbank",
				createWayInfo(1f / 200000, 1f / 12000, 80, LIGHT_BLUE, 1,
						LIGHT_BLUE, false));
		result.addRule(
				"waterway",
				null,
				createWayInfo(1f / 100000, 1f / 12000, 76, LIGHT_BLUE, 1,
						LIGHT_BLUE, true));
		result.addRule(
				"route",
				"ferry",
				createInfo(1f / 100000, 1f / 12000, 70, UColor.GRAY, null,
						false, UColor.BLUE, 1, true, null, false));
		result.addRule("railway", "rail",
				createWayInfo(1f / 100000, 1f / 3500, 95, UColor.GRAY, 1));
		addBuildingRules(
				result,
				"railway",
				"station",
				createPoiInfo(1f / 35000, 1f / 6000, 60, UColor.DARK_GRAY,
						createRectangle(4, UColor.DARK_GRAY), false));
		result.addRule("aeroway", null,
				createWayInfo(1f / 350000, 1f / 12000, 60, UColor.LIGHT_GRAY, 1));
		result.addRule("aerialway", null,
				createWayInfo(1f / 350000, 1f / 6000, 60, UColor.GRAY, 1));
		result.addRule(
				"mountain_pass",
				null,
				createPoiInfo(1e-9f, 1f / 350000, 10, UColor.DARK_GRAY, null,
						false));

		result.addRule("place", "city",
				createPoiInfo(0, 0, 1, UColor.BLACK, null, false));
		result.addRule(
				"place",
				"town",
				createPoiInfo(1f / 750000, 1f / 750000, 2, UColor.BLACK, null,
						false));
		result.addRule(
				"place",
				"village",
				createPoiInfo(1f / 200000, 1f / 200000, 3, UColor.DARK_GRAY,
						null, false));
		result.addRule(
				"place",
				null,
				createPoiInfo(1f / 50000, 1f / 50000, 30, UColor.DARK_GRAY,
						null, false));

		result.addRule(
				"tourism",
				"caravan_site",
				createPoiInfo(1f / 350000, 1f / 6000, 75, UColor.GRAY,
						createRectangle(8, "P", UColor.BLUE, UColor.RED), true));
		result.addRule(
				"tourism",
				"camp_site",
				createPoiInfo(1f / 350000, 1f / 6000, 73, UColor.GRAY,
						new TentIcon(8, UColor.DARK_GRAY, UColor.GREEN.darker()),
						true));
		addBuildingRules(
				result,
				"tourism",
				"alpine_hut",
				createPoiInfo(
						1f / 350000,
						1f / 35000,
						74,
						UColor.GRAY,
						createRectangle(8, "H", UColor.GREEN.darker(), UColor.RED),
						true));
		addBuildingRules(
				result,
				"tourism",
				"attraction",
				createInfo(
						1f / 60000,
						1f / 6000,
						74,
						UColor.GRAY,
						createCircle(11, "A", UColor.GREEN.darker(), UColor.WHITE),
						true, UColor.GRAY, 1, false, null, false));
		result.addRule(
				"tourism",
				"viewpoint",
				createPoiInfo(
						1f / 60000,
						1f / 1800,
						75,
						UColor.GRAY,
						createCircle(11, "V", UColor.GREEN.darker(), UColor.WHITE),
						true));
		addBuildingRules(
				result,
				"tourism",
				"museum",
				createPoiInfo(
						1f / 60000,
						1f / 2500,
						75,
						UColor.GRAY,
						createCircle(11, "M", UColor.GREEN.darker(), UColor.WHITE),
						true));
		addBuildingRules(
				result,
				"tourism",
				"hotel",
				createPoiInfo(
						1f / 12000,
						1f / 1800,
						76,
						UColor.GRAY,
						createRectangle(8, "H", UColor.GREEN.darker(),
								UColor.WHITE), true));
		addBuildingRules(
				result,
				"tourism",
				null,
				createPoiInfo(1f / 12000, 1f / 3500, 77, UColor.GREEN.darker(),
						createRectangle(4, UColor.GREEN.darker()), false));
		addBuildingRules(
				result,
				"historic",
				"castle",
				createPoiInfo(1f / 60000, 1f / 6000, 40, UColor.GRAY,
						new EntityIcon.CastleIcon(8, UColor.DARK_GRAY,
								UColor.ORANGE), true));
		result.addRule(
				"historic",
				"memorial",
				createPoiInfo(1f / 6000, 1f / 1800, 81, UColor.GRAY,
						createCircle(11, "M", UColor.ORANGE, UColor.WHITE), true));
		addBuildingRules(
				result,
				"historic",
				null,
				createPoiInfo(1f / 60000, 1f / 1800, 75, UColor.GRAY,
						createCircle(11, "H", UColor.ORANGE, UColor.WHITE), true));
		result.addRule(
				"amenity",
				"parking",
				createInfo(1f / 12000, 1f / 1800, 85, UColor.GRAY,
						createRectangle(8, "P", UColor.BLUE, UColor.WHITE), true,
						UColor.LIGHT_GRAY, 1, false, VERY_LIGHT_BLUE, false));
		addBuildingRules(
				result,
				"amenity",
				"place_of_worship",
				createPoiInfo(1f / 20000, 1f / 1800, 85, UColor.GRAY,
						new EntityIcon.ChurchIcon(8, UColor.DARK_GRAY,
								UColor.BLUE), true));
		addBuildingRules(
				result,
				"amenity",
				null,
				createPoiInfo(1f / 12000, 1f / 1800, 89, UColor.BLUE,
						createRectangle(4, UColor.BLUE), true));
		addBuildingRules(
				result,
				"shop",
				null,
				createPoiInfo(40000, 1f / 1800, 89, UColor.CYAN,
						createRectangle(4, UColor.CYAN), true));
		result.addRule(
				"building",
				"yes",
				createWayInfo(1f / 6000, 1f / 1800, 89, LIGHT_RED, 1,
						LIGHT_RED, false));
		result.addRule(
				"addr:housenumber",
				null,
				createPoiInfo(1f / 1800, 1f / 1800, 90, UColor.GRAY,
						createRectangle(4, UColor.LIGHT_GRAY), true));

		result.addRule(
				"marker",
				"yes",
				createPoiInfo(1e-9f, 1e-9f, 0, UColor.RED, new PinIcon(12,
						UColor.RED, UColor.RED), false));
		result.addRule("track_type", "GPS", createTrackInfo(UColor.GREEN));
		result.addRule("track_type", null, createTrackInfo(UColor.RED));

		return result;
	}

	/**
	 * Demonstrates, how an entity classifier for view information objects can
	 * be customized.
	 */
	public EntityClassifier<EntityViewInfo> createNightViewClassifier() {
		EntityClassifier<EntityViewInfo> result = createDefaultClassifier();

		result.replaceRule(
				"highway",
				"path",
				createWayInfo(1f / 100000, 1f / 3500, 38,
						UColor.YELLOW.darker(), 1));
		result.replaceRule(
				"highway",
				"track",
				createWayInfo(1f / 60000, 1f / 3500, 37, UColor.YELLOW.darker(),
						1));

		result.replaceRule(
				"place",
				"city",
				createPoiInfo(1e-9f, 1f / 3500000, 30, UColor.WHITE, null, false));
		result.replaceRule("place", "town",
				createPoiInfo(1e-9f, 1f / 350000, 29, UColor.WHITE, null, false));
		result.replaceRule("place", "village",
				createPoiInfo(1e-9f, 1f / 100000, 29, UColor.GRAY, null, false));
		result.replaceRule("place", null,
				createPoiInfo(1e-9f, 1f / 35000, 28, UColor.GRAY, null, false));

		result.replaceRule(
				"marker",
				"yes",
				createPoiInfo(1e-9f, 1e-9f, 0, UColor.YELLOW, new PinIcon(12,
						UColor.YELLOW, UColor.YELLOW), false));
		result.replaceRule("track_type", null, createTrackInfo(UColor.WHITE));

		return result;
	}

	public void addBuildingRules(EntityClassifier<EntityViewInfo> ec,
			String attName, String attValue, DefaultEntityViewInfo eclass) {
		ec = ec.addRule(attName, attValue, eclass);
		try {
			DefaultEntityViewInfo bclass = (DefaultEntityViewInfo) eclass
					.clone();
			bclass.isWayIcon = true;
			bclass.wayFillColor = LIGHT_RED;
			bclass.fillAreasOnly = false;
			ec.addRule("building", "yes", bclass);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	/** Creates an entity view info with default values. */
	public DefaultEntityViewInfo createDefaultInfo() {
		DefaultEntityViewInfo result = null;
		try {
			result = (DefaultEntityViewInfo) DEFAULT_INFO.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return result;
	}

	/** Creates an entity view info for points of interest (simple version). */
	public DefaultEntityViewInfo createPoiInfo(float minScale, int printOrder,
			EntityIcon icon) {
		DefaultEntityViewInfo result = createDefaultInfo();
		result.minVisibleScale = minScale;
		result.printOrder = printOrder;
		result.icon = icon;
		return result;
	}

	/** Creates an entity view info for points of interest. */
	public DefaultEntityViewInfo createPoiInfo(float minScale,
			float minNameScale, int printOrder, UColor nameColor,
			EntityIcon icon, boolean isWayIcon) {
		DefaultEntityViewInfo result = createDefaultInfo();
		result.minVisibleScale = minScale;
		result.minNameScale = minNameScale;
		result.printOrder = printOrder;
		result.nameColor = nameColor;
		result.icon = icon;
		result.isWayIcon = isWayIcon;
		return result;
	}

	/** Creates an entity view info for ways (simple version). */
	public DefaultEntityViewInfo createWayInfo(float minScale,
			float minNameScale, int printOrder, UColor wayColor,
			float wayWidth) {
		DefaultEntityViewInfo result = createDefaultInfo();
		result.minVisibleScale = minScale;
		result.minNameScale = minNameScale;
		result.printOrder = printOrder;
		result.wayColor = wayColor;
		result.wayWidth = wayWidth;
		return result;
	}

	/** Creates an entity view info for ways. */
	public DefaultEntityViewInfo createWayInfo(float minScale,
			float minNameScale, int printOrder, UColor wayColor, float wayWidth,
			UColor wayFillColor, boolean fillAreasOnly) {
		DefaultEntityViewInfo result = createDefaultInfo();
		result.minVisibleScale = minScale;
		result.minNameScale = minNameScale;
		result.printOrder = printOrder;
		result.wayColor = wayColor;
		result.wayWidth = wayWidth;
		result.wayFillColor = wayFillColor;
		result.fillAreasOnly = fillAreasOnly;
		return result;
	}

	/** Creates an entity view info for tracks. */
	public DefaultEntityViewInfo createTrackInfo(UColor color) {
		return new DefaultEntityViewInfo(1e-9f, 0, 0, color, createCircle(12,
				color, GRAY_TRANS), true, color, 2f, true, null, false);
	}

	/** Creates an entity view info (equivalent to general constructor call). */
	public DefaultEntityViewInfo createInfo(float minScale, float minNameScale,
			int printOrder, UColor nameColor, EntityIcon icon,
			boolean isWayIcon, UColor wayColor, float wayWidth,
			boolean wayDashed, UColor wayFillColor, boolean fillAreasOnly) {
		return new DefaultEntityViewInfo(minScale, minNameScale, printOrder,
				nameColor, icon, isWayIcon, wayColor, wayWidth, wayDashed,
				wayFillColor, fillAreasOnly);
	}

	// ///////////////////////////////////////////////////////////////
	// useful icon creators

	public static EntityIcon createCircle(float size, UColor color) {
		return new SimpleIcon(SimpleIcon.Shape.CIRCLE, size, null, color,
				color, null);
	}

	public static EntityIcon createCircle(float size, UColor line,
			UColor fill) {
		return new SimpleIcon(SimpleIcon.Shape.CIRCLE, size, null, line, fill,
				null);
	}

	public static EntityIcon createCircle(float size, String symbol,
			UColor color, UColor sym) {
		return new SimpleIcon(SimpleIcon.Shape.CIRCLE, size, symbol, color,
				color, sym);
	}

	public static EntityIcon createRectangle(float size, UColor color) {
		return new SimpleIcon(SimpleIcon.Shape.RECTANGLE, size, null, color,
				color, null);
	}

	public static EntityIcon createRectangle(float size, UColor line,
			UColor fill) {
		return new SimpleIcon(SimpleIcon.Shape.RECTANGLE, size, null, line,
				fill, null);
	}

	public static EntityIcon createRectangle(float size, String symbol,
			UColor color, UColor sym) {
		return new SimpleIcon(SimpleIcon.Shape.RECTANGLE, size, symbol, color,
				color, sym);
	}

	public static EntityIcon createTriangle(float size, UColor color) {
		return new SimpleIcon(SimpleIcon.Shape.TRIANGLE, size, null, color,
				color, null);
	}

	public static EntityIcon createTriangle(float size, UColor line,
			UColor fill) {
		return new SimpleIcon(SimpleIcon.Shape.TRIANGLE, size, null, line,
				fill, null);
	}

	public static EntityIcon createTriangle(float size, String symbol,
			UColor color, UColor sym) {
		return new SimpleIcon(SimpleIcon.Shape.TRIANGLE, size, symbol, color,
				color, sym);
	}
}
