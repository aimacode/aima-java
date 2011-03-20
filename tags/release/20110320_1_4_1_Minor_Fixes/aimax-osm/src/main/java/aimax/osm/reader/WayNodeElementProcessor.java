// License: GPL. Copyright 2007-2008 by Brett Henderson and other contributors.
// Modified by Ruediger Lunde, 2009
package aimax.osm.reader;

import org.xml.sax.Attributes;

/**
 * Provides an element processor implementation for a way node.
 */
public class WayNodeElementProcessor extends ElementProcessor {
	private static final String ATTRIBUTE_NAME_ID = "ref";
	
	private WayElementProcessor wayProcessor;
	long id;
	
	/**
	 * Creates a new instance.
	 * 
	 * @param parentProcessor
	 *            The parent element processor.
	 * @param wayProcessor
	 *            The way node listener for receiving created tags.
	 */
	public WayNodeElementProcessor(ElementProcessor parentProcessor, WayElementProcessor wayProcessor) {
		super(parentProcessor);
		this.wayProcessor = wayProcessor;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void begin(Attributes attributes) {
		id = Long.parseLong(attributes.getValue(ATTRIBUTE_NAME_ID));
	}
		
	/**
	 * {@inheritDoc}
	 */
	public void end() {
		wayProcessor.addWayNode(id);
		id = -1;
	}
}
