package aimax.osm.viewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import aimax.osm.data.EntityClassifier;
import aimax.osm.data.Position;
import aimax.osm.data.entities.MapEntity;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.data.entities.Track;

/**
 * Provides a simple map entity renderer implementation which is highly
 * configurable. The visual appearance of the map is controlled by declarative
 * classification rules. This renderer uses a default set of rules
 * which is created in method <code>setDefaults</code>. It can be changed
 * by getting the entity classifier and replacing some of the rules or
 * by overriding the method completely. When changing rules after using
 * the renderer, it is necessary to remove all rendering data from the
 * map data store which maintains the map entities.
 * @author R. Lunde
 */
public class DefaultMapEntityRenderer extends AbstractMapEntityRenderer {

	public static final Color GRAY_TRANS = new Color(0, 0, 0, 40);
	public static final Color LIGHT_GRAY_TRANS = new Color(0, 0, 0, 20);
	public static final Color LIGHT_RED_TRANS = new Color(255, 0, 0, 30);
	public static final Color LIGHT_YELLOW_TRANS = new Color(230, 255, 0, 60);
	public static final Color VERY_LIGHT_GREEN = new Color(200, 254, 184);
	public static final Color LIGHT_GREEN = new Color(116, 252, 133);
	public static final Color GREEN = new Color(91, 210, 95);
	public static final Color LIGHT_BLUE = new Color(164, 164, 255);
	
	/**
	 * Size used for fonts, mark, and track symbols. Specifies the
	 * number of pixel if <code>displayFactor</code> is 1. It is used
	 * especially in sub-calls of method {@link #setDefaults()}.
	 */
	protected float defaultSize = 12f;
	protected int wayGeneralizationValue;
	protected Color backgroundColor;
	/**
	 * Classifies the map entities and maintains rendering information
	 * for each entity class.
	 */
	private EntityClassifier<EntityPrintInfo> entityClassifier;
	/**
	 * Lookup, maintains rendering information for different track names.
	 */
	private Hashtable<String, EntityPrintInfo> trackPrintInfo;
	protected List<MapWay> areaBuffer;
	protected List<MapWay> wayBuffer;
	protected List<MapEntity> nodeBuffer;
	protected List<Track> trackBuffer;
	protected List<NameInfo> nameInfoBuffer;
	private List<MapNode> tmpNodeBuffer; // to improve thread-safety
	private BasicStroke standardStroke;
	
	/** Standard constructor. */
	public DefaultMapEntityRenderer() {
		entityClassifier = new EntityClassifier<EntityPrintInfo>();
		trackPrintInfo = new Hashtable<String, EntityPrintInfo>();
		areaBuffer = new ArrayList<MapWay>();
		wayBuffer = new ArrayList<MapWay>();
		nodeBuffer = new ArrayList<MapEntity>();
		trackBuffer = new ArrayList<Track>();
		nameInfoBuffer = new ArrayList<NameInfo>();
		tmpNodeBuffer = new ArrayList<MapNode>();
		setDefaults();
	}
	
	/** Creates the rules which control the visual appearance of the map. */
	protected void setDefaults() {
		setBackgroundColor(Color.WHITE);
		setTrackInfo("default", 0f, Color.RED, GRAY_TRANS);
		entityClassifier.addRule("highway", "motorway", new EntityPrintInfo(0, 20000, Color.BLUE, 2, 50));
		entityClassifier.addRule("highway", "motorway_link", new EntityPrintInfo(0, 20000, Color.BLUE, 2, 50));
		entityClassifier.addRule("highway", "trunk", new EntityPrintInfo(0, 60000, Color.BLUE, 2, 50));
		entityClassifier.addRule("highway", "trunk_link", new EntityPrintInfo(0, 90000, Color.BLUE, 2, 50));
		entityClassifier.addRule("highway", "primary", new EntityPrintInfo(200, 90000, Color.PINK, 3, 49));
		entityClassifier.addRule("highway", "primary_link", new EntityPrintInfo(200, 90000, Color.PINK, 3, 49));
		entityClassifier.addRule("highway", "secondary", new EntityPrintInfo(500, 90000, Color.PINK, 2, 49));
		entityClassifier.addRule("highway", "tertiary", new EntityPrintInfo(500, 90000, Color.PINK, 1, 48));
		entityClassifier.addRule("highway", "road", new EntityPrintInfo(500, 90000, Color.PINK, 1, 48));
		entityClassifier.addRule("highway", "residential", new EntityPrintInfo(2000, 100000, Color.LIGHT_GRAY, 1, 45));
		entityClassifier.addRule("highway", "living_street", new EntityPrintInfo(2000, 100000, Color.LIGHT_GRAY, 1, 45));
		entityClassifier.addRule("highway", "pedestrian", new EntityPrintInfo(2000, 100000, Color.ORANGE, 2, Color.ORANGE, true, 43));
		entityClassifier.addRule("highway", "cycleway", new EntityPrintInfo(3000, 100000, Color.GREEN.darker(), 1, 40));
		entityClassifier.addRule("highway", "service", new EntityPrintInfo(3000, 100000, Color.LIGHT_GRAY, 1, Color.LIGHT_GRAY, true, 39));
		entityClassifier.addRule("highway", "path", new EntityPrintInfo(3000, 100000, Color.YELLOW, 1, 38));
		entityClassifier.addRule("highway", "track", new EntityPrintInfo(6000, 100000, Color.YELLOW, 1, 37));
		entityClassifier.addRule("highway", "footway", new EntityPrintInfo(10000, 200000, Color.ORANGE, 1, 36));
		entityClassifier.addRule("highway", "steps", new EntityPrintInfo(10000, 200000, Color.ORANGE, 1, 35));
		entityClassifier.addRule("highway", "unclassified", new EntityPrintInfo(10000, 200000, Color.LIGHT_GRAY, 1, 34));
		entityClassifier.addRule("highway", "speed_camera", new EntityPrintInfo(6000, 200000, Color.GRAY, Icon.createTriangle(6, Color.RED, Color.WHITE), 32));

		entityClassifier.addRule("natural", "land", new EntityPrintInfo(200, 30000, LIGHT_BLUE, 1, Color.WHITE, 29));
		entityClassifier.addRule("natural", "island", new EntityPrintInfo(200, 30000, LIGHT_BLUE, 2, 23));
		entityClassifier.addRule("natural", "coastline", new EntityPrintInfo(200, 30000, LIGHT_BLUE, 2, 22));
		entityClassifier.addRule("natural", "water", new EntityPrintInfo(200, 30000, Color.GRAY, null, false, LIGHT_BLUE, 1, false, LIGHT_BLUE, false, 20));
		entityClassifier.addRule("natural", "glacier", new EntityPrintInfo(200, 30000, LIGHT_BLUE.brighter(), 1, LIGHT_BLUE.brighter(), 19));
		entityClassifier.addRule("natural", "beach", new EntityPrintInfo(3000, 30000, Color.YELLOW, 1, Color.YELLOW, 18));
		entityClassifier.addRule("natural", "wood", new EntityPrintInfo(200, 30000, GREEN, 1, GREEN, 17));
		entityClassifier.addRule("natural", "scrub", new EntityPrintInfo(200, 30000, LIGHT_GREEN, 1, LIGHT_GREEN, 16));
		entityClassifier.addRule("natural", "heath", new EntityPrintInfo(500, 30000, LIGHT_GREEN, 1, LIGHT_GREEN, 16));
		entityClassifier.addRule("natural", "peak", new EntityPrintInfo(1000, 30000, Color.DARK_GRAY, Icon.createTriangle(10, Color.ORANGE), 15));
		entityClassifier.addRule("natural", null, new EntityPrintInfo(10000, 30000, Color.DARK_GRAY, Icon.createTriangle(10, Color.GREEN.darker()), 25));
		entityClassifier.addRule("leisure", "park", new EntityPrintInfo(500, 100000, VERY_LIGHT_GREEN, 1, VERY_LIGHT_GREEN, 14));
		entityClassifier.addRule("leisure", "garden", new EntityPrintInfo(500, 100000, VERY_LIGHT_GREEN, 1, VERY_LIGHT_GREEN, 14));
		entityClassifier.addRule("landuse", "forest",  new EntityPrintInfo(200, 30000, GREEN, 1, GREEN, 13));
		entityClassifier.addRule("landuse", "farm",  new EntityPrintInfo(500, 30000, LIGHT_YELLOW_TRANS, 1, LIGHT_YELLOW_TRANS, 13));
		entityClassifier.addRule("landuse", "farmland",  new EntityPrintInfo(500, 30000, LIGHT_YELLOW_TRANS, 1, LIGHT_YELLOW_TRANS, 13));
		entityClassifier.addRule("landuse", "residential",  new EntityPrintInfo(500, 30000, LIGHT_RED_TRANS, 1, LIGHT_RED_TRANS, 13));
		entityClassifier.addRule("landuse", null, new EntityPrintInfo(500, 200000, Color.GRAY, null, false, LIGHT_GRAY_TRANS, 1, false, LIGHT_GRAY_TRANS, false, 13));
		entityClassifier.addRule("boundary", null, new EntityPrintInfo(500, 200000, Color.GRAY, null, false, Color.GRAY, 1, true, null, false, 13));

		entityClassifier.addRule("waterway", "riverbank", new EntityPrintInfo(200, 30000, LIGHT_BLUE, 1, LIGHT_BLUE, 25));
		entityClassifier.addRule("waterway", null, new EntityPrintInfo(3000, 30000, LIGHT_BLUE, 1, LIGHT_BLUE, 24));
		entityClassifier.addRule("railway", "rail", new EntityPrintInfo(3000, 30000, Color.GRAY, 1, 30));
		entityClassifier.addRule("railway", "station", new EntityPrintInfo(10000, 60000, Color.DARK_GRAY, Icon.createRectangle(4, Color.DARK_GRAY), 29));
		entityClassifier.addRule("aerialway", null, new EntityPrintInfo(1000, 60000, LIGHT_BLUE.brighter(), 2, 28));

		entityClassifier.addRule("historic", null, new EntityPrintInfo(6000, 200000, Color.GRAY, Icon.createCircle(11, "H", Color.ORANGE, Color.WHITE), true, Color.GRAY, 1, false, null, false, 30));
		entityClassifier.addRule("tourism", "caravan_site", new EntityPrintInfo(1000, 60000, Color.GRAY, Icon.createRectangle(8, "P", Color.BLUE, Color.RED), true, Color.GRAY, 1, false, null, false, 28)); 
		entityClassifier.addRule("tourism", "camp_site", new EntityPrintInfo(1000, 60000, Color.GRAY, Icon.createRectangle(8, "C", Color.GREEN.darker(), Color.WHITE), 27));
		entityClassifier.addRule("tourism", "attraction", new EntityPrintInfo(6000, 60000, Color.GRAY, Icon.createCircle(11, "A", Color.GREEN.darker(), Color.WHITE), 26));
		entityClassifier.addRule("tourism", "viewpoint", new EntityPrintInfo(6000, 200000, Color.GRAY, Icon.createCircle(11, "V", Color.GREEN.darker(), Color.WHITE), 25));
		entityClassifier.addRule("tourism", "museum", new EntityPrintInfo(6000, 200000, Color.GRAY, Icon.createCircle(11, "M", Color.GREEN.darker(), Color.WHITE), 25));
		entityClassifier.addRule("tourism", "hotel", new EntityPrintInfo(30000, 200000, Color.GRAY, Icon.createRectangle(8, "H", Color.GREEN.darker(), Color.WHITE), 24));
		entityClassifier.addRule("tourism", null, new EntityPrintInfo(30000, 100000, Color.GREEN.darker(), Icon.createRectangle(4, Color.GREEN.darker()), 23));
		entityClassifier.addRule("amenity", "place_of_worship", new EntityPrintInfo(20000, 200000, Color.GRAY, Icon.createTriangle(11, "+", Color.BLUE, Color.WHITE), true, GRAY_TRANS, 1, false, Color.LIGHT_GRAY, true, 15));
		entityClassifier.addRule("amenity", "parking", new EntityPrintInfo(30000, 200000, Color.GRAY, Icon.createRectangle(8, "P", Color.BLUE, Color.WHITE), true, GRAY_TRANS, 1, false, Color.LIGHT_GRAY, true, 15));
		entityClassifier.addRule("amenity", null, new EntityPrintInfo(30000, 300000, Color.BLUE, Icon.createRectangle(4, Color.BLUE), 10));

		entityClassifier.addRule("place", "city", new EntityPrintInfo(0, 100, Color.BLACK, null, 40));
		entityClassifier.addRule("place", "town", new EntityPrintInfo(0, 1000, Color.BLACK, null, 39));
		entityClassifier.addRule("place", "village", new EntityPrintInfo(0, 3000, Color.DARK_GRAY, null, 38));
		entityClassifier.addRule("place", null, new EntityPrintInfo(0, 10000, Color.DARK_GRAY, null, 37));

		entityClassifier.addRule("mountain_pass", null, new EntityPrintInfo(0, 1000, Color.DARK_GRAY, null, 20));
		entityClassifier.addRule("shop", null, new EntityPrintInfo(40000, 300000, Color.CYAN, Icon.createRectangle(4, Color.CYAN), 10));
		entityClassifier.addRule("mark", "yes", new EntityPrintInfo(0, 0, Color.RED, Icon.createCircle(defaultSize, Color.RED), 100));

		entityClassifier.addRule(null, null, new EntityPrintInfo());
	} 
	
	/** Returns the classifier which maintains all rendering rules. */
	public EntityClassifier<EntityPrintInfo> getEntityClassifier() {
		return entityClassifier;
	}
	
	/**
	 * Provides rendering information for tracks. If no information
	 * is found for the specified track name, the information for the
	 * name default is returned.
	 */
	public EntityPrintInfo getTrackInfo(String trackName) {
		EntityPrintInfo result = trackPrintInfo.get(trackName);
		if (result == null)
			result = trackPrintInfo.get("default");
		return result;
	}
	
	/** Adds track rendering information to the lookup. */
	public void setTrackInfo(String trackName, float minScale, Color color, Color fill) {
		trackPrintInfo.put(trackName, new EntityPrintInfo
				(minScale, minScale, color, Icon.createCircle(defaultSize, color, fill), true,
						color, 2f, true, null, false, 0));
	}
	
	/** Clears all buffers and prepares rendering. */
	public void initForRendering(Graphics2D g2, CoordTransformer transformer) {
		super.initForRendering(g2, transformer);
		g2.setFont(g2.getFont().deriveFont(defaultSize*displayFactor));
		double scale = transformer.getScale();
		if (scale <= 500)
			wayGeneralizationValue = 8;
		else if (scale <= 1000)
			wayGeneralizationValue = 4;
		else if (scale <= 6000)
			wayGeneralizationValue = 2;
		else
			wayGeneralizationValue = 1;
		standardStroke = new BasicStroke(displayFactor);
		areaBuffer.clear();
		wayBuffer.clear();
		nodeBuffer.clear();
		trackBuffer.clear();
		nameInfoBuffer.clear();
		//count = 0;
	}

	/**
	 * Returns the map node which is the nearest with respect to
	 * the specified view coordinates among the currently displayed nodes.
	 */
	public MapNode getNextNode(int x, int y) {
		Position pos = new Position(transformer.lat(y), transformer.lon(x));
		MapNode nextNode = null;
		MapNode tmp = null;
		for (int i = 0; i < 2; i++) {
			List<MapWay> ways = (i == 0) ? areaBuffer : wayBuffer;
			for (MapWay way : ways) {
				tmp = pos.selectNearest(way.getNodes(), null);
				if (nextNode == null || pos.getDistKM(tmp) < pos.getDistKM(nextNode)) {
					nextNode = tmp;
				}
			}
		}
		for (MapEntity node : nodeBuffer) {
			if (node instanceof MapNode) {
				tmp = (MapNode) node;
				if (tmp != null && (nextNode == null
						|| pos.getDistKM(tmp) < pos.getDistKM(nextNode))) {
					nextNode = tmp;
				}
			}
		}
		return nextNode;
	}
	
	/** Classifies the entity and possibly adds it to a buffer for printing. */ 
	@Override
	public void visitMapWay(MapWay way) {
		double scale = transformer.getScale();
		
		EntityPrintInfo pInfo = (EntityPrintInfo) way.getRenderData();
		if (pInfo == null) {
			pInfo = entityClassifier.classify(way);
			way.setRenderData(pInfo);
		}
		if (scale >= pInfo.minScale * displayFactor) {
			List<MapNode> nodes = way.getNodes();
			if (pInfo.wayColor != null) {
				if (nodes.get(0) == nodes.get(nodes.size()-1)
					&& pInfo.wayFillColor != null
					&& (way.isArea() || !pInfo.fillAreasOnly))
					areaBuffer.add(way);
				else
					wayBuffer.add(way);
			}
			if (pInfo.isWayIcon && pInfo.icon != null)
				nodeBuffer.add(way);
		}
	}
	
	/** Classifies the entity and possibly adds it to a buffer for printing. */ 
	@Override
	public void visitMapNode(MapNode node) {
		double scale = transformer.getScale();
		EntityPrintInfo pInfo = (EntityPrintInfo) node.getRenderData();
		if (pInfo == null) {
			pInfo = entityClassifier.classify(node);
			node.setRenderData(pInfo);
		}
		if (scale >= pInfo.minScale * displayFactor)
			nodeBuffer.add(node);
	}
	
	/** Classifies the entity and possibly adds it to a buffer for printing. */ 
	@Override
	public void visitTrack(Track track) {
		EntityPrintInfo pInfo = getTrackInfo(track.getName());
		if (transformer.getScale() >= pInfo.minScale * displayFactor) {
			trackBuffer.add(track);
		}
	}
	
	/** Prints all buffered entities according to their rendering informations. */
	public void printBufferedObjects() {
		Comparator<MapEntity> comp = new Comparator<MapEntity>() {
			public int compare(MapEntity e1, MapEntity e2) {
				return e1.getRenderData().compareTo(e2.getRenderData());
			}
		};
		Collections.sort(areaBuffer, comp);
		if (wayBuffer.size() < 10000)
			Collections.sort(wayBuffer, comp);
		if (nodeBuffer.size() < 10000)
			Collections.sort(nodeBuffer, comp);
		for (MapWay area: areaBuffer)
			printWay(area, (EntityPrintInfo) area.getRenderData(), true);
		for (MapWay way: wayBuffer)
			printWay(way, (EntityPrintInfo) way.getRenderData(), false);
		for (MapEntity node: nodeBuffer) {
			MapNode n;
			if (node instanceof MapWay)
				n = ((MapWay) node).getNodes().get(0);
			else
				n = (MapNode) node;
			printNode(n, (EntityPrintInfo) node.getRenderData());
		}
		for (Track track: trackBuffer)
			printTrack(track);

		// remove identical names at the same place...
		Hashtable<String, NameInfo> hash = new Hashtable<String, NameInfo>();
		for (int i = nameInfoBuffer.size()-1; i>=0; i--) {
			NameInfo iNew = nameInfoBuffer.get(i);
			NameInfo iOld = hash.get(iNew.name);
			if (iOld == null)
				hash.put(iNew.name, iNew);
			else if (Math.abs(iNew.x-iOld.x)+Math.abs(iNew.y-iOld.y)
					< 4*defaultSize*displayFactor)
				nameInfoBuffer.remove(i);
		}
		for (NameInfo textInfo : nameInfoBuffer) {
			g2.setColor(textInfo.color);
			g2.drawString(textInfo.name, textInfo.x, textInfo.y);
		}
	}
	
	/** Prints a way entity. */
	protected void printWay(MapWay way, EntityPrintInfo pInfo, boolean asArea) {
		List<MapNode> nodes = generalizeWay(way.getNodes());
		if (nodes != null) {
			NameInfo textInfo = null;
			if (transformer.getScale() >= pInfo.minNameScale * displayFactor) {
				String name = way.getName();
				if (name != null && pInfo.nameColor != null) {
					if (way.isOneway())
						name = ">" + name;
					textInfo = new NameInfo(name, pInfo.nameColor);
				}
			}
			printLine(g2, nodes, pInfo, asArea, textInfo);
		}
	}
	
	/** Prints a node entity. */
	protected void printNode(MapNode node, EntityPrintInfo pInfo) {
		int x = transformer.x(node.getLon());
		int y = transformer.y(node.getLat());
		int width = 0;
		
		if (pInfo.icon != null) {
			width = Math.round(pInfo.icon.size * displayFactor);
			int offset = Math.round(pInfo.icon.size * displayFactor / 2f);
			pInfo.icon.draw(g2, x-offset, y-offset, displayFactor);
		}
		
		if (transformer.getScale() >= pInfo.minNameScale * displayFactor) {
			String name = node.getName();
			if (name != null && pInfo.nameColor != null) {
				NameInfo info = new NameInfo(name, pInfo.nameColor);
				info.x = x + width;
				info.y = y - width/4;
				nameInfoBuffer.add(info);
			}
		}
	}
	
	/** Prints a track entity. */
	protected void printTrack(Track track) {
		EntityPrintInfo pInfo = getTrackInfo(track.getName());
		tmpNodeBuffer.clear();
		tmpNodeBuffer.addAll(track.getTrkPts());
		if (!tmpNodeBuffer.isEmpty()) {
			printLine(g2, tmpNodeBuffer, pInfo, false, null);
			printPoint(g2, tmpNodeBuffer.get(tmpNodeBuffer.size()-1), pInfo, null);
		}
	}
	
	/** Reduces the level of detail by selecting some of the given nodes. */
	protected List<MapNode> generalizeWay(List<MapNode> wayNodes) {
		int size = wayNodes.size();
		List<MapNode> result = new ArrayList<MapNode>(size / wayGeneralizationValue + 2);
		int i;
		for (i = 0; i < size; i += wayGeneralizationValue)
			result.add(wayNodes.get(i));
		if (i < size - 1 + wayGeneralizationValue)
			result.add(wayNodes.get(size-1));
		return result;
	}
	
	/** Prints a line or fills an area. */
	protected void printLine(Graphics2D g2, List<MapNode> nodes,
			EntityPrintInfo pInfo, boolean asArea, NameInfo textInfo) {
		//count++;
		int [] xPoints = new int[nodes.size()];
		int [] yPoints = new int[nodes.size()];
		int i = 0;
		for (MapNode node : nodes) {
			xPoints[i]  = transformer.x(node.getLon());
			yPoints[i]  = transformer.y(node.getLat());
			++i;
		}
		boolean filled = false;
		if (asArea) {
			g2.setColor(pInfo.wayFillColor != null ? pInfo.wayFillColor : pInfo.wayColor);
			g2.setStroke(standardStroke);
			g2.fillPolygon(xPoints, yPoints, nodes.size());
			filled = true;
		}
		if (!filled || pInfo.wayFillColor != null && !pInfo.wayFillColor.equals(pInfo.wayColor)) {
			float dash[] =  null;
			if (pInfo.wayDashed) {
				dash = new float[] { pInfo.wayWidth * 2f * displayFactor };
			}
			g2.setColor(pInfo.wayColor);
			g2.setStroke(new BasicStroke(pInfo.wayWidth * displayFactor, BasicStroke.CAP_BUTT,
			        BasicStroke.JOIN_ROUND, 10.0f, dash, 0.0f));
			g2.drawPolyline(xPoints, yPoints, nodes.size());
		}
		if (textInfo != null) {
			setWayNamePosition(textInfo, xPoints, yPoints, filled);
			nameInfoBuffer.add(textInfo);
		}
		if (debugMode && transformer.getScale()
				>= 2 * pInfo.minNameScale * displayFactor) {
			i = 0;
			for (MapNode node : nodes) {
				textInfo = new NameInfo
				(Long.toString(node.getId()), pInfo.nameColor);
				textInfo.x = xPoints[i];
				textInfo.y = yPoints[i];
				nameInfoBuffer.add(textInfo);
				++i;
			}
		}
	}
	
	/** Finds a good place for printing the name of a way entity. */
	protected void setWayNamePosition(NameInfo info,
			int [] xPoints, int [] yPoints, boolean area) {
		int x = 0;
		int y = 0;
		int max = debugMode ? 1 : (area ? xPoints.length : 2);
		for (int i = 0; i < max; i++) {
			x += xPoints[i];
			y += yPoints[i];
		}
		info.x = x / max;
		info.y = y / max;
		if (debugMode)
			info.y+=displayFactor*defaultSize;
	}
	
	/** Prints a point of interest. */
	protected void printPoint(Graphics2D g2, MapNode node, EntityPrintInfo pInfo, Color nameColor) {
		int x = transformer.x(node.getLon());
		int y = transformer.y(node.getLat());
		int width = 0;
		
		if (pInfo.icon != null) {
			width = Math.round(pInfo.icon.size * displayFactor);
			int offset = Math.round(pInfo.icon.size * displayFactor / 2f);
			pInfo.icon.draw(g2, x-offset, y-offset, displayFactor);
		}
		
		if (nameColor != null) {
			String name = (debugMode)
			? "P"+Long.toString(node.getId()) : node.getName();
			if (name != null) {
				NameInfo info = new NameInfo(name, nameColor);
				info.x = x + width;
				info.y = y - width/4;
				nameInfoBuffer.add(info);
			}
		}
	}
	
	
	//////////////////////////////////////////////////////////////////////
	// some inner classes...
	
	/**
	 * Stores color and position information for a name to be printed out.
	 */
	protected static class NameInfo {
		public String name;
		public Color color;
		public int x;
		public int y;
		protected NameInfo(String name, Color color) {
			this.name = name;
			this.color = color;
		}
	}
}
