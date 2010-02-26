package aimax.osm.viewer;

import java.awt.Color;

/**
 * Maintains informations for rendering a certain map entity type.
 * @author R. Lunde
 */
public class EntityPrintInfo implements Comparable<Object> {
	
	/**
	 * Maintains default values. Used in constructors for all attributes which are
	 * not specified by parameter.
	 */
	private static EntityPrintInfo DEFAULT_INFO = new EntityPrintInfo
	(20000, 200000, Color.LIGHT_GRAY.darker(), EntityIcon.createRectangle(4, Color.GRAY), false, Color.GRAY, 1, false, null, false, 10);
	
	/** Changes the default values. */
	public static void setDefaults(EntityPrintInfo info) {
		DEFAULT_INFO = info;
	}
	
	/**
	 * Specifies the minimal scale, in which the entity should
	 * appear on the screen.
	 */
	public float minScale;
	/** Controls when the name of the entity appears on the screen. */
	public float minNameScale;
	/** The color to print the entity name, null indicates no name display. */
	public Color nameColor;
	/** The icon to be used to represent the entity or null (no icon). */
	public EntityIcon icon;
	/**
	 * Controls, whether the icon is only used for nodes (false)
	 * or also for ways (true).
	 */
	public boolean isWayIcon;
	/** Polygon color, used to display way entities (not used for nodes). */
	public Color wayColor;
	/** Specifies the width of ways (not used for nodes). */
	public float wayWidth;
	/** Specifies the line type of ways (not used for nodes). */
	public boolean wayDashed;
	/**
	 * Used for ways to indicate, whether the area within a circular way
	 * should be filled, and if, in which color.
	 */
	public Color wayFillColor;
	/**
	 * Restricts polygon filling to those way entities which are explicitly
	 * marked as areas by attribute.
	 */
	public boolean fillAreasOnly;
	/** Controls the order in which entities are printed out. */
	public int printOrder;
	
	/** Default constructor. */
	public EntityPrintInfo() {
		this.minScale = DEFAULT_INFO.minScale;
		this.minNameScale = DEFAULT_INFO.minNameScale;
		this.nameColor = DEFAULT_INFO.nameColor;
		this.icon = DEFAULT_INFO.icon;
		this.isWayIcon = DEFAULT_INFO.isWayIcon;
		this.wayColor = DEFAULT_INFO.wayColor;
		this.wayWidth = DEFAULT_INFO.wayWidth;
		this.wayDashed = DEFAULT_INFO.wayDashed;
		this.wayFillColor = DEFAULT_INFO.wayFillColor;
		this.printOrder = DEFAULT_INFO.printOrder;
	}
	
	/** Simple constructor for points of interest. */
	public EntityPrintInfo(float minScale, EntityIcon icon, int printOrder) {
		this();
		this.minScale = minScale;
		this.icon = icon;
		this.printOrder = printOrder;
	}
	
	/** Constructor for points of interest. */
	public EntityPrintInfo(float minScale, float minNameScale, Color nameColor,
			EntityIcon icon, int printOrder) {
		this();
		this.minScale = minScale;
		this.minNameScale = minNameScale;
		this.nameColor = nameColor;
		this.icon = icon;
		this.printOrder = printOrder;
	}
	
	/** Constructor for ways. */
	public EntityPrintInfo(float minScale, float minNameScale,
			Color wayColor, float wayWidth, int printOrder) {
		this();
		this.minScale = minScale;
		this.minNameScale = minNameScale;
		this.wayColor = wayColor;
		this.wayWidth = wayWidth;
		this.printOrder = printOrder;
	}
	
	/** Constructor for ways and regions. */
	public EntityPrintInfo(float minScale, float minNameScale,
			Color wayColor, float wayWidth,
			Color wayFillColor, int printOrder) {
		this();
		this.minScale = minScale;
		this.minNameScale = minNameScale;
		this.wayColor = wayColor;
		this.wayWidth = wayWidth;
		this.wayFillColor = wayFillColor;
		this.printOrder = printOrder;
	}
	
	/** Constructor for ways and regions. */
	public EntityPrintInfo(float minScale, float minNameScale,
			Color wayColor, float wayWidth,
			Color wayFillColor, boolean fillAreasOnly, int printOrder) {
		this();
		this.minScale = minScale;
		this.minNameScale = minNameScale;
		this.wayColor = wayColor;
		this.wayWidth = wayWidth;
		this.wayFillColor = wayFillColor;
		this.fillAreasOnly = fillAreasOnly;
		this.printOrder = printOrder;
	}
	
	/** Constructor for general use. */
	public EntityPrintInfo(float minScale, float minNameScale, Color nameColor,
			EntityIcon icon, boolean isWayIcon, Color wayColor, float wayWidth, boolean wayDashed,
			Color wayFillColor, boolean fillAreasOnly, int printOrder) {
		this.minScale = minScale;
		this.minNameScale = minNameScale;
		this.nameColor = nameColor;
		this.icon = icon;
		this.isWayIcon = isWayIcon;
		this.wayColor = wayColor;
		this.wayWidth = wayWidth;
		this.wayDashed = wayDashed;
		this.wayFillColor = wayFillColor;
		this.fillAreasOnly = fillAreasOnly;
		this.printOrder = printOrder;
	}

	/** Compares entity print informations with respect to print order. */
	@Override
	public int compareTo(Object arg0) {
		int pOrder = ((EntityPrintInfo) arg0).printOrder;
		if (printOrder < pOrder)
			return -1;
		else if (printOrder == pOrder)
			return 0;
		else
			return 1;
	}
}
