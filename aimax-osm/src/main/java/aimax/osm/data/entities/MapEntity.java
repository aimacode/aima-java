package aimax.osm.data.entities;

import java.util.List;

import aimax.osm.data.EntityVisitor;

/**
 * Common interface for all map entities. Map entities have id, name, and
 * attributes, must accept a visitor (e.g. for rendering). The attached
 * view information tells the map data storage for which scales the entity
 * is relevant. It can also contain further data for rendering, but those
 * aspects are not visible on application layer.
 * @author Ruediger Lunde
 */
public interface MapEntity {
	
	/** Returns a unique ID for the entity. */
	long getId();
	
	/** Assigns a unique ID to the entity. */
	void setId(int id);
	
	/** Returns the name of the entity or null. */
	String getName();

	/** Assigns a name to the entity. */
	void setName(String name);
	
	/** Returns the attributes of the entity. */
	EntityAttribute[] getAttributes();
	
	/** Assigns attributes to the entity. */
	void setAttributes(List<EntityAttribute> atts);
	
	/** Provides maximal scale information. */
	EntityViewInfo getViewInfo();

	/** Assigns maximal scale information to an entity. */
	void setViewInfo(EntityViewInfo renderData);

	/**
	 * Typically applies binary search to find the specified attribute
	 * value and returns null if not found.
	 */
	String getAttributeValue(String attName);
	
	/** 
	 * Subclasses must call back the entity class specific method
	 * of the visitor.
	 */
	void accept(EntityVisitor visitor);
}
