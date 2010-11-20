package aimax.osm.data.impl;

import java.util.Arrays;
import java.util.List;

import aimax.osm.data.EntityVisitor;
import aimax.osm.data.entities.EntityAttribute;
import aimax.osm.data.entities.EntityViewInfo;
import aimax.osm.data.entities.MapEntity;

/**
 * Base class for all map entities in this default map representation
 * implementation.
 * @author Ruediger Lunde
 */
public abstract class DefaultMapEntity implements MapEntity {
private static final EntityAttribute[] EMPTY_ATT_LIST = new EntityAttribute[0];
	
	protected long id;
	protected String name;
	protected EntityAttribute[] attributes;
	volatile protected EntityViewInfo viewInfo;
	
	protected DefaultMapEntity() {
		attributes = EMPTY_ATT_LIST;
	}
	
	/** {@inheritDoc} */
	public long getId() {
		return id;
	}
	
	/** {@inheritDoc} */
	public void setId(int id) {
		this.id = id;
	}
	
	/** Returns the name of the entity or null. */
	public String getName() {
		return name;
	}

	/** {@inheritDoc} */
	public void setName(String name) {
		this.name = name;
	}
	
	/** {@inheritDoc} */
	public EntityAttribute[] getAttributes() {
		return attributes;
	}
	
	/** {@inheritDoc} */
	public void setAttributes(List<EntityAttribute> atts) {
		if (!atts.isEmpty() || attributes.length != 0) {
			attributes = new EntityAttribute[atts.size()];
			atts.toArray(attributes);
			Arrays.sort(attributes);
		}
	}
	
	/** {@inheritDoc} */
	public EntityViewInfo getViewInfo() {
		return viewInfo;
	}

	/** {@inheritDoc} */
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
			cr = attName.compareTo(attributes[curr].getKey());
			if (cr == 0)
				return attributes[curr].getValue();
			else if (cr < 0)
				max = curr-1;
			else
				min = curr+1;
		}
		return null;
	}
	
	/** {@inheritDoc} */
	public abstract void accept(EntityVisitor visitor);
	
	
	/////////////////////////////////////////////////////////////////
	// extension for kd-tree
	
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
