// License: GPL. Copyright 2007-2008 by Brett Henderson and other contributors.
// Modified by Ruediger Lunde, 2009
package aimax.osm.reader;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import aimax.osm.data.EntityAttributeManager;
import aimax.osm.data.MapBuilder;
import aimax.osm.data.entities.EntityAttribute;

/**
 * Provides an element processor implementation for a way.
 * @author Ruediger Lunde
 */
public class WayElementProcessor extends ElementProcessor implements TagListener {
	private static final String ELEMENT_NAME_TAG = "tag";
	private static final String ELEMENT_NAME_NODE = "nd";
	private static final String ATTRIBUTE_NAME_ID = "id";
	// private static final String ATTRIBUTE_NAME_TIMESTAMP = "timestamp";
	// private static final String ATTRIBUTE_NAME_USER = "user";
	// private static final String ATTRIBUTE_NAME_USERID = "uid";
	// private static final String ATTRIBUTE_NAME_CHANGESET_ID = "changeset";
	// private static final String ATTRIBUTE_NAME_VERSION = "version";
	
	private TagElementProcessor tagElementProcessor;
	private WayNodeElementProcessor wayNodeElementProcessor;
	private long wayId;
	private String wayName;
	private List<EntityAttribute> wayAttributes;
	private List<Long> wayNodeIds;
	private boolean skipElement;
		
	/**
	 * Creates a new instance.
	 * 
	 * @param parentProcessor
	 *            The parent of this element processor.
	 * @param mdConsumer
	 *            The sink for receiving processed data.
	 */
	public WayElementProcessor(ElementProcessor parentProcessor,
			MapBuilder mdConsumer) {
		super(parentProcessor, mdConsumer);
		
		tagElementProcessor = new TagElementProcessor(this, this);
		wayNodeElementProcessor = new WayNodeElementProcessor(this, this);
		wayAttributes = new ArrayList<EntityAttribute>();
		wayNodeIds = new ArrayList<Long>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void begin(Attributes attributes) {
		wayId = Long.parseLong(attributes.getValue(ATTRIBUTE_NAME_ID));
		skipElement = getMapBuilder().isWayDefined(wayId);
		if (!skipElement) {
			wayName = null;
			wayAttributes.clear();
			wayNodeIds.clear();
		}
	}
	
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
		if (!skipElement) {
			if (ELEMENT_NAME_NODE.equals(qName))
				return wayNodeElementProcessor;
			else if (ELEMENT_NAME_TAG.equals(qName))
				return tagElementProcessor;
		}
		return super.getChild(uri, localName, qName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void end() {
		if (!skipElement)
			getMapBuilder().addWay(wayId, wayName, wayAttributes, wayNodeIds);
	}
	
	/**
	 * This is called by child element processors when a tag object is
	 * encountered.
	 * 
	 * @param tag
	 *            The tag to be processed.
	 */
	public void processTag(Tag tag) {
		String key = tag.getKey();
		String value = tag.getValue();
		if (key.equals("name")) {
			wayName = value;
		} else {
			EntityAttribute att = EntityAttributeManager.instance().intern
			(new EntityAttribute(key, value));
			if (att != null)
				wayAttributes.add(att);
		}
	}
	
	/**
	 * This is called by child element processors when a way node object is
	 * encountered.
	 * 
	 * @param nodeId
	 *            The id of the way node to be processed.
	 */
	public void addWayNode(long nodeId) {
		wayNodeIds.add(nodeId);
	}
}