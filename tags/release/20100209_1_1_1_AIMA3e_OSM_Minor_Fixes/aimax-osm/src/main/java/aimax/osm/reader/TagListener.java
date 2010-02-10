// License: GPL. Copyright 2007-2008 by Brett Henderson and other contributors.
// Modified by Ruediger Lunde, 2009
package aimax.osm.reader;

/**
 * Provides the definition of a class receiving tags.
 */
public interface TagListener {
	
	/**
	 * Processes the tag.
	 * 
	 * @param tag
	 *            The tag.
	 */
	void processTag(Tag tag);
}

