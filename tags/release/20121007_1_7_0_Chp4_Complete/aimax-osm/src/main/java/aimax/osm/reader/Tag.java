// License: GPL. Copyright 2007-2008 by Brett Henderson and other contributors.
// Modified by Ruediger Lunde, 2009
package aimax.osm.reader;

/**
 * A data class representing a single OSM tag.
 */
public class Tag implements Comparable<Tag> {

    /**
     * The key identifying the tag.
     */
	private String key;
	/**
	 * The value associated with the tag.
	 */
	private String value;
	
	
	/**
	 * Creates a new instance.
	 * 
	 * @param key
	 *            The key identifying the tag.
	 * @param value
	 *            The value associated with the tag.
	 */
	public Tag(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	/**
	 * Compares this tag to the specified tag. The tag comparison is based on
	 * a comparison of key and value in that order.
	 * 
	 * @param tag
	 *            The tag to compare to.
	 * @return 0 if equal, < 0 if considered "smaller", and > 0 if considered
	 *         "bigger".
	 */
	public int compareTo(Tag tag) {
		int keyResult;
		
		keyResult = this.key.compareTo(tag.key);
		
		if (keyResult != 0) {
			return keyResult;
		}
		
		return this.value.compareTo(tag.value);
	}
	
	
	/**
	 * @return The key.
	 */
	public String getKey() {
		return key;
	}
	
	
	/**
	 * @return The value.
	 */
	public String getValue() {
		return value;
	}
  
    /** 
     * ${@inheritDoc}.
     */
    @Override
    public String toString() {
        return "Tag('" + getKey() + "'='" + getValue() + "')";
    }
}
