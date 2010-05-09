// License: GPL. Copyright 2007-2008 by Brett Henderson and other contributors.
// Modified by Ruediger Lunde, 2009
package aimax.osm.reader;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import aimax.osm.data.EntityAttributeManager;
import aimax.osm.data.MapDataConsumer;
import aimax.osm.data.entities.EntityAttribute;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;

/**
 * Provides an element processor implementation for a way.
 * @author R. Lunde
 */
public class WayElementProcessor extends ElementProcessor implements TagListener {
	private static final String ELEMENT_NAME_TAG = "tag";
	private static final String ELEMENT_NAME_NODE = "nd";
	private static final String ATTRIBUTE_NAME_ID = "id";
	private static final String ATTRIBUTE_NAME_TIMESTAMP = "timestamp";
	private static final String ATTRIBUTE_NAME_USER = "user";
	private static final String ATTRIBUTE_NAME_USERID = "uid";
	private static final String ATTRIBUTE_NAME_CHANGESET_ID = "changeset";
	private static final String ATTRIBUTE_NAME_VERSION = "version";
	
	private TagElementProcessor tagElementProcessor;
	private WayNodeElementProcessor wayNodeElementProcessor;
	private List<EntityAttribute> wayAttributes;
	private MapWay way;
		
	/**
	 * Creates a new instance.
	 * 
	 * @param parentProcessor
	 *            The parent of this element processor.
	 * @param mdConsumer
	 *            The sink for receiving processed data.
	 */
	public WayElementProcessor(ElementProcessor parentProcessor,
			MapDataConsumer mdConsumer) {
		super(parentProcessor, mdConsumer);
		
		tagElementProcessor = new TagElementProcessor(this, this);
		wayNodeElementProcessor = new WayNodeElementProcessor(this, this);
		wayAttributes = new ArrayList<EntityAttribute>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void begin(Attributes attributes) {
		long id;
		id = Long.parseLong(attributes.getValue(ATTRIBUTE_NAME_ID));
		way = new MapWay(id); // unclassified!
		wayAttributes.clear();
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
		if (ELEMENT_NAME_NODE.equals(qName)) {
			return wayNodeElementProcessor;
		} else if (ELEMENT_NAME_TAG.equals(qName)) {
			return tagElementProcessor;
		}
		return super.getChild(uri, localName, qName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void end() {
		if (way.getNodes().size() < 2) {
			// way to short, remove it.
			for (MapNode node : way.getNodes())
				node.removeWayRef(way);
		} else {
			way.setAttributes(wayAttributes);
			getConsumer().addWay(way);
		}
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
			way.setName(value);
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
		MapNode node = getConsumer().getWayNode(nodeId);
		if (node != null) {
			node.addWayRef(way, way.getNodes().size());
			way.addNode(node);
		}
	}
}