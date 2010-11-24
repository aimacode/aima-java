// License: GPL. Copyright 2007-2008 by Brett Henderson and other contributors.
// Modified by Ruediger Lunde, 2009
package aimax.osm.reader;

import org.xml.sax.Attributes;

import aimax.osm.data.MapBuilder;

/**
 * Provides an element processor implementation for an osm element.
 * @author Ruediger Lunde
 */
public class OsmElementProcessor extends ElementProcessor {
	
	// private static final Logger LOG = Logger.getLogger("rlu.osm");
	
	private static final String ELEMENT_NAME_BOUND = "bound";
	private static final String ELEMENT_NAME_NODE = "node";
	private static final String ELEMENT_NAME_WAY = "way";
	// private static final String ELEMENT_NAME_RELATION = "relation";
	// private static final String ATTRIBUTE_NAME_VERSION = "version";
	
	private BoundElementProcessor boundElementProcessor;
	private NodeElementProcessor nodeElementProcessor;
	private WayElementProcessor wayElementProcessor;
	//private RelationElementProcessor relationElementProcessor;
	
	// private boolean foundBound = false;
	// private boolean foundEntities = false;
	
	/**
	 * Creates a new instance.
	 * 
	 * @param parentProcessor
	 *            The parent of this element processor.
	 * @param mdConsumer
	 *            The sink for receiving processed data.
	 */
	public OsmElementProcessor(ElementProcessor parentProcessor,
			MapBuilder mdConsumer) {
		super(parentProcessor, mdConsumer);
		
		boundElementProcessor = new BoundElementProcessor(this, getMapBuilder());
		nodeElementProcessor = new NodeElementProcessor(this, getMapBuilder());
		wayElementProcessor = new WayElementProcessor(this, getMapBuilder());
		//relationElementProcessor = new RelationElementProcessor(this, getConsumer());
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void begin(Attributes attributes) {}
	
	
	/**
	 * Retrieves the appropriate child element processor for the newly
	 * encountered nested element.
	 * 
	 * @param uri
	 *            The element uri.
	 * @param localName
	 *            The element localName.
	 * @param qName
	 *            The element qName.
	 * @return The appropriate element processor for the nested element.
	 */
	@Override
	public ElementProcessor getChild(String uri, String localName, String qName) {
		if (ELEMENT_NAME_BOUND.equals(qName)) {
			return boundElementProcessor;
		} else if (ELEMENT_NAME_NODE.equals(qName)) {
			// foundEntities = true;
			return nodeElementProcessor;
		} else if (ELEMENT_NAME_WAY.equals(qName)) {
			// foundEntities = true;
			return wayElementProcessor;
//		} else if (ELEMENT_NAME_RELATION.equals(qName)) {
//			foundEntities = true;
//			return relationElementProcessor;
		}
		
		return super.getChild(uri, localName, qName);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void end() {}
}

