package aimax.osm.data.entities;

import java.util.List;

import aimax.osm.data.EntityVisitor;

/**
 * Common base class for all map entities. Map entities have id, name, and
 * attributes, must accept a visitor (e.g. for rendering), and
 * can be compared with respect to their position relative
 * to a split plane. The attached view information tells the map data store
 * for which scales the entity is relevant. It can also contain further
 * data for rendering, but those aspects are not visible on application
 * layer.
 * @author Ruediger Lunde
 */
public interface MapEntity {
	
	public long getId();
	
	public void setId(int id);
	
	/** Returns the name of the entity or null. */
	public String getName();

	public void setName(String name);
	
	/** Returns the attributes of the entity. */
	public EntityAttribute[] getAttributes();
	
	public void setAttributes(List<EntityAttribute> atts);
	
	public EntityViewInfo getViewInfo();

	public void setViewInfo(EntityViewInfo renderData);

	/**
	 * Applies binary search to find the specified attribute value and
	 * returns null if not found.
	 */
	public String getAttributeValue(String attName);
	
	/** 
	 * Subclasses must call back the entity class specific method
	 * of the visitor.
	 */
	public abstract void accept(EntityVisitor visitor);
}
