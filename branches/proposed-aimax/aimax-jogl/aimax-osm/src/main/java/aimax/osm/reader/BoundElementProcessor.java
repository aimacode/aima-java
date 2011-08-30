package aimax.osm.reader;

import org.xml.sax.Attributes;

import aimax.osm.data.BoundingBox;
import aimax.osm.data.MapBuilder;

public class BoundElementProcessor extends ElementProcessor {
	private static final String ATTRIBUTE_NAME_BOX = "box";
	private static final String ATTRIBUTE_NAME_ORIGIN = "origin";
	
	private BoundingBox bb;
	
	public BoundElementProcessor(ElementProcessor parentProcessor,
			MapBuilder mdConsumer) {
		super(parentProcessor, mdConsumer);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void begin(Attributes attributes) {
		String boxString;
		String origin;
		String[] boundStrings;
		float right;
		float left;
		float top;
		float bottom;
		
		boxString = attributes.getValue(ATTRIBUTE_NAME_BOX);
		
		if (boxString == null) {
			throw new OsmRuntimeException("Missing required box attribute of bound element");
		}
		boundStrings = boxString.split(",");
		if (boundStrings.length != 4) {
			throw new OsmRuntimeException("Badly formed box attribute of bound element");
		}
		try {
			bottom = Float.parseFloat(boundStrings[0]);
			left = Float.parseFloat(boundStrings[1]);
			top = Float.parseFloat(boundStrings[2]);
			right = Float.parseFloat(boundStrings[3]);
		} catch (NumberFormatException e) {
			throw new OsmRuntimeException("Can't parse box attribute of bound element", e);
		}
		origin = attributes.getValue(ATTRIBUTE_NAME_ORIGIN);
		if (origin == null || origin.equals("")) {
			throw new OsmRuntimeException("Origin attribute of bound element is empty or missing.");
		}
		bb = new BoundingBox(bottom, left, top, right);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void end() {
		getMapBuilder().setBoundingBox(bb);
		bb = null;
	}

}

