package aimax.osm.viewer;

import aimax.osm.data.entities.EntityViewInfo;

/**
 * Simple container which maintains informations for rendering a certain map
 * entity type. It is used by {@link aimax.osm.viewer.DefaultEntityRenderer}.
 * @author Ruediger Lunde
 */
public class DefaultEntityViewInfo implements EntityViewInfo, Cloneable {
	/**
	 * Specifies the minimal scale, in which the entity should
	 * appear on the screen.
	 */
	public float minVisibleScale;
	/** Controls when the name of the entity appears on the screen. */
	public float minNameScale;
	/**
	 * Controls the order in which entities and names of entities are printed
	 * out. Small numbers are printed on top.
	 */
	public int printOrder;
	/** The color to print the entity name, null indicates no name display. */
	public UColor nameColor;
	/** The icon to be used to represent the entity or null (no icon). */
	public EntityIcon icon;
	/**
	 * Controls, whether the icon is only used for nodes (false)
	 * or also for ways (true).
	 */
	public boolean isWayIcon;
	/** Polygon color, used to display way entities (not used for nodes). */
	public UColor wayColor;
	/** Specifies the width of ways (not used for nodes). */
	public float wayWidth;
	/** Specifies the line type of ways (not used for nodes). */
	public boolean wayDashed;
	/**
	 * Used for ways to indicate, whether the area within a circular way
	 * should be filled, and if, in which color.
	 */
	public UColor wayFillColor;
	/**
	 * Restricts polygon filling to those way entities which are explicitly
	 * marked as areas by attribute.
	 */
	public boolean fillAreasOnly;
	
	
	/** Constructor for general use. */
	public DefaultEntityViewInfo(float minScale, float minNameScale, int printOrder,
			UColor nameColor, EntityIcon icon, boolean isWayIcon,
			UColor wayColor, float wayWidth, boolean wayDashed,
			UColor wayFillColor, boolean fillAreasOnly) {
		this.printOrder = printOrder;
		this.minVisibleScale = minScale;
		this.minNameScale = minNameScale;
		this.nameColor = nameColor;
		this.icon = icon;
		this.isWayIcon = isWayIcon;
		this.wayColor = wayColor;
		this.wayWidth = wayWidth;
		this.wayDashed = wayDashed;
		this.wayFillColor = wayFillColor;
		this.fillAreasOnly = fillAreasOnly;
	}
	
	public float getMinVisibleScale() {
		return minVisibleScale;
	}
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
