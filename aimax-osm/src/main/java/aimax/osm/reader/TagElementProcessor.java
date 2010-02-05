// License: GPL. Copyright 2007-2008 by Brett Henderson and other contributors.
// Modified by Ruediger Lunde, 2009
package aimax.osm.reader;

import org.xml.sax.Attributes;

/**
 * Provides an element processor implementation for a tag.
 */
public class TagElementProcessor extends ElementProcessor {
	private static final String ATTRIBUTE_NAME_KEY = "k";
	private static final String ATTRIBUTE_NAME_VALUE = "v";
	
	private TagListener tagListener;
	private Tag tag;
		
	/**
	 * Creates a new instance.
	 * 
	 * @param parentProcessor
	 *            The parent element processor.
	 * @param tagListener
	 *            The tag listener for receiving created tags.
	 */
	public TagElementProcessor(ElementProcessor parentProcessor, TagListener tagListener) {
		super(parentProcessor);
		
		this.tagListener = tagListener;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void begin(Attributes attributes) {
		String key;
		String value;
		
		key = attributes.getValue(ATTRIBUTE_NAME_KEY);
		value = attributes.getValue(ATTRIBUTE_NAME_VALUE);
		
		tag = new Tag(key, value);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void end() {
		tagListener.processTag(tag);
		tag = null;
	}
}
