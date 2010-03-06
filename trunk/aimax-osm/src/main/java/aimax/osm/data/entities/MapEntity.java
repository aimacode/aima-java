package aimax.osm.data.entities;

import java.util.Collections;
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
 * @author R. Lunde
 */
public abstract class MapEntity {
	private static final EntityAttribute[] EMPTY_ATT_LIST = new EntityAttribute[0];
	
	protected long id;
	protected String name;
	protected EntityAttribute[] attributes;
	protected EntityViewInfo viewInfo;
	
	protected MapEntity() {
		attributes = EMPTY_ATT_LIST;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	/** Returns the name of the entity or null. */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/** Returns the attributes of the entity. */
	public EntityAttribute[] getAttributes() {
		return attributes;
	}
	
	public void setAttributes(List<EntityAttribute> atts) {
		if (!atts.isEmpty() || attributes.length != 0) {
			attributes = new EntityAttribute[atts.size()];
			atts.toArray(attributes);
			Collections.sort(atts);
		}
	}
	
	public EntityViewInfo getViewInfo() {
		return viewInfo;
	}

	public void setViewInfo(EntityViewInfo renderData) {
		this.viewInfo = renderData;
	}

	/**
	 * Applies binary search to find the specified attribute value and
	 * returns null if not found.
	 */
	public String getAttributeValue(String attName) {
		int min = 0;
		int max = attributes.length-1;
		int curr;
		int cr;
		while (min <= max) {
			curr = (min+max)/2;
			cr = attName.compareTo(attributes[curr].getName());
			if (cr == 0)
				return attributes[curr].getValue();
			else if (cr < 0)
				max = curr-1;
			else
				min = curr+1;
		}
		return null;
	}
	
	/** 
	 * Subclasses must call back the entity class specific method
	 * of the visitor.
	 */
	public abstract void accept(EntityVisitor visitor);
	/**
	 * Returns -1, if all parts of the entity have
	 * a lower latitude than <code>lat</code>;
	 * 1 if no part has a higher latitude than <code>lat</code>;
	 * an 0 otherwise.
	 * @param lat Defines the split plane.
	 * @return -1, 0, or 1
	 */
	public abstract int compareLatitude(float lat);
	/**
	 * Returns -1, if all parts of the entity have
	 * a lower longitude than <code>lon</code>;
	 * 1 if no part has a higher longitude than <code>lon</code>;
	 * an 0 otherwise.
	 * @param lon Defines the split plane.
	 * @return -1, 0, or 1
	 */
	public abstract int compareLongitude(float lon);
}
