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

/**
 * Provides an element processor implementation for a node.
 */
public class NodeElementProcessor extends ElementProcessor implements TagListener {
	private static final String ELEMENT_NAME_TAG = "tag";
	private static final String ATTRIBUTE_NAME_ID = "id";
	private static final String ATTRIBUTE_NAME_LATITUDE = "lat";
	private static final String ATTRIBUTE_NAME_LONGITUDE = "lon";
	
	
	private TagElementProcessor tagElementProcessor;
	private MapNode node;
	private List<EntityAttribute> nodeAttributes;
	
	
	/**
	 * Creates a new instance.
	 * 
	 * @param parentProcessor
	 *            The parent of this element processor.
	 * @param mdConsumer
	 *            The sink for receiving processed data.
	 */
	public NodeElementProcessor(ElementProcessor parentProcessor,
			MapDataConsumer mdConsumer) {
		super(parentProcessor, mdConsumer);
		
		tagElementProcessor = new TagElementProcessor(this, this);
		nodeAttributes = new ArrayList<EntityAttribute>();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void begin(Attributes attributes) {
		long id;
		float latitude;
		float longitude;
		
		id = Long.parseLong(attributes.getValue(ATTRIBUTE_NAME_ID));
		latitude = Float.parseFloat(attributes.getValue(ATTRIBUTE_NAME_LATITUDE));
		longitude = Float.parseFloat(attributes.getValue(ATTRIBUTE_NAME_LONGITUDE));
		node = new MapNode(id, latitude, longitude);
		nodeAttributes.clear();
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
		if (ELEMENT_NAME_TAG.equals(qName)) {
			return tagElementProcessor;
		}
		
		return super.getChild(uri, localName, qName);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void end() {
		node.setAttributes(nodeAttributes);
		getConsumer().addNode(node);
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
			node.setName(value);
		} else {
			EntityAttribute att = EntityAttributeManager.instance().intern
			(new EntityAttribute(key, value));
			if (att != null)
				nodeAttributes.add(att);
		}
	}
}