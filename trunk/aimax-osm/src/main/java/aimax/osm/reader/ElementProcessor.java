// License: GPL. Copyright 2007-2008 by Brett Henderson and other contributors.
// Modified by Ruediger Lunde, 2009
package aimax.osm.reader;

import org.xml.sax.Attributes;

import aimax.osm.data.MapBuilder;

/**
 * Provides common functionality shared by element processor implementations.
 */
public abstract class ElementProcessor {
	private ElementProcessor parentProcessor;
	
	private ElementProcessor dummyChildProcessor;
	private MapBuilder mapBuilder;
	
	/**
	 * Creates a new instance.
	 * 
	 * @param parentProcessor
	 *            The parent of this element processor.
	 */
	protected ElementProcessor(ElementProcessor parentProcessor) {
		this.parentProcessor = parentProcessor;
	}
	
	public ElementProcessor(ElementProcessor parentProcessor, MapBuilder builder) {
		this(parentProcessor);
		mapBuilder = builder;
	}
	
	/**
	 * This implementation returns a dummy element processor as the child which
	 * ignores all nested xml elements. Sub-classes wishing to handle child
	 * elements must override this method and delegate to this method for xml
	 * elements they don't care about.
	 * 
	 * @param uri
	 *            The element uri.
	 * @param localName
	 *            The element localName.
	 * @param qName
	 *            The element qName.
	 * @return A dummy element processor.
	 */
	public ElementProcessor getChild(String uri, String localName, String qName) {
		if (dummyChildProcessor == null) {
			dummyChildProcessor = new DummyElementProcessor(this);
		}
		return dummyChildProcessor;
	}
	
	/**
	 * Returns the parent of the processor.
	 */
	public ElementProcessor getParent() {
		return parentProcessor;
	}
	
	/**
	 * @return The sink.
	 */
	protected MapBuilder getMapBuilder() {
		return mapBuilder;
	}
	
	
	//////////////////////////////////////////////////////////////////////
	// abstract methods
	
	/**
	 * Initialises the element processor with attributes for a new element to be
	 * processed.
	 * 
	 * @param attributes
	 *            The attributes of the new element.
	 */
	abstract void begin(Attributes attributes);
	
	/**
	 * Finalises processing for the element processor, this is called when the
	 * end of an element is reached.
	 */
	abstract void end();
}
