package aimax.osm.viewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import aimax.osm.data.Position;
import aimax.osm.data.entities.MapEntity;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.data.entities.Track;

/**
 * Provides a rather general map entity renderer implementation. It assumes
 * that all entities provided to the renderer have an attached
 * {@link aimax.osm.viewer.DefaultEntityViewInfo} object. The visual appearance
 * of the drawn map strongly depends on those objects.
 * @author R. Lunde
 */
public class DefaultEntityRenderer extends AbstractEntityRenderer {
	
	/**
	 * Size used for fonts. It specifies the number of pixel if
	 * <code>displayFactor</code> is 1.
	 */
	protected float fontSize = 12f;
	protected int wayGeneralizationValue;
	protected Color backgroundColor;
	protected List<MapWay> areaBuffer;
	protected List<MapWay> wayBuffer;
	protected List<MapEntity> nodeBuffer;
	protected List<Track> trackBuffer;
	protected List<NameInfo> nameInfoBuffer;
	private List<MapNode> tmpNodeBuffer; // to improve thread-safety
	private BasicStroke standardStroke;
	
	/** Standard constructor. */
	public DefaultEntityRenderer() {
		areaBuffer = new ArrayList<MapWay>();
		wayBuffer = new ArrayList<MapWay>();
		nodeBuffer = new ArrayList<MapEntity>();
		trackBuffer = new ArrayList<Track>();
		nameInfoBuffer = new ArrayList<NameInfo>();
		tmpNodeBuffer = new ArrayList<MapNode>();
		setBackgroundColor(Color.WHITE);
	}
	
	/** Clears all buffers and prepares rendering. */
	public void initForRendering(Graphics2D g2, CoordTransformer transformer) {
		super.initForRendering(g2, transformer);
		g2.setFont(g2.getFont().deriveFont(fontSize*displayFactor));
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
	
	/** Adds a way to a buffer for printing. */ 
	@Override
	public void visitMapWay(MapWay way) {
		DefaultEntityViewInfo pInfo = (DefaultEntityViewInfo) way.getViewInfo();
		List<MapNode> nodes = way.getNodes();
		if (pInfo.wayColor != null) {
			if (pInfo.wayFillColor != null
					&& nodes.get(0) == nodes.get(nodes.size()-1)
					&& (way.isArea() || !pInfo.fillAreasOnly))
//				    alternative solution:
//					&& (way.isArea() || 
//							(nodes.get(0) == nodes.get(nodes.size()-1)
//							&& !pInfo.fillAreasOnly)))
				areaBuffer.add(way);
			else
				wayBuffer.add(way);
		}
		if (pInfo.isWayIcon && pInfo.icon != null)
			nodeBuffer.add(way);
	}
	
	/** Adds the entity to a buffer for printing. */ 
	@Override
	public void visitMapNode(MapNode node) {
		nodeBuffer.add(node);
	}
	
	/** Classifies the entity and possibly adds it to a buffer for printing. */ 
	@Override
	public void visitTrack(Track track) {
		DefaultEntityViewInfo vInfo = (DefaultEntityViewInfo) track.getViewInfo();
		if (vInfo != null && transformer.getScale() >= vInfo.minVisibleScale * displayFactor) {
			trackBuffer.add(track);
		}
	}
	
	/** Prints all buffered entities according to their rendering informations. */
	public void printBufferedObjects() {
		Comparator<MapEntity> comp = new MapEntityComparator();
		Collections.sort(areaBuffer, comp);
		if (wayBuffer.size() < 10000)
			Collections.sort(wayBuffer, comp);
		if (nodeBuffer.size() < 10000)
			Collections.sort(nodeBuffer, comp);
		for (MapWay area: areaBuffer)
			printWay(area, (DefaultEntityViewInfo) area.getViewInfo(), true);
		for (MapWay way: wayBuffer)
			printWay(way, (DefaultEntityViewInfo) way.getViewInfo(), false);
		for (MapEntity node: nodeBuffer) {
			MapNode n;
			if (node instanceof MapWay)
				n = ((MapWay) node).getNodes().get(0);
			else
				n = (MapNode) node;
			printNode(n, (DefaultEntityViewInfo) node.getViewInfo());
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
					< 4*fontSize*displayFactor)
				nameInfoBuffer.remove(i);
		}
		for (NameInfo textInfo : nameInfoBuffer) {
			g2.setColor(textInfo.color);
			g2.drawString(textInfo.name, textInfo.x, textInfo.y);
		}
	}
	
	/** Prints a way entity. */
	protected void printWay(MapWay way, DefaultEntityViewInfo pInfo, boolean asArea) {
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
	protected void printNode(MapNode node, DefaultEntityViewInfo pInfo) {
		int x = transformer.x(node.getLon());
		int y = transformer.y(node.getLat());
		int width = 0;
		
		if (pInfo.icon != null) {
			width = Math.round(pInfo.icon.size * displayFactor);
			pInfo.icon.draw(g2, x, y, displayFactor);
		}
		
		if (transformer.getScale() >= pInfo.minNameScale * displayFactor) {
			String name = node.getName();
			if (name != null && pInfo.nameColor != null) {
				NameInfo info = new NameInfo(name, pInfo.nameColor);
				info.x = x + width;
				info.y = y + width/4;
				nameInfoBuffer.add(info);
			}
		}
	}
	
	/** Prints a track entity. */
	protected void printTrack(Track track) {
		DefaultEntityViewInfo vInfo = (DefaultEntityViewInfo) track.getViewInfo();
		tmpNodeBuffer.clear();
		tmpNodeBuffer.addAll(track.getTrkPts());
		if (!tmpNodeBuffer.isEmpty()) {
			printLine(g2, tmpNodeBuffer, vInfo, false, null);
			printPoint(g2, tmpNodeBuffer.get(tmpNodeBuffer.size()-1), vInfo, null);
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
			DefaultEntityViewInfo pInfo, boolean asArea, NameInfo textInfo) {
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
			info.y+=displayFactor*fontSize;
	}
	
	/** Prints a point of interest. */
	protected void printPoint(Graphics2D g2, MapNode node, DefaultEntityViewInfo pInfo, Color nameColor) {
		int x = transformer.x(node.getLon());
		int y = transformer.y(node.getLat());
		int width = 0;
		
		if (pInfo.icon != null) {
			width = Math.round(pInfo.icon.size * displayFactor);
			pInfo.icon.draw(g2, x, y, displayFactor);
		}
		
		if (nameColor != null) {
			String name = (debugMode)
			? "P"+Long.toString(node.getId()) : node.getName();
			if (name != null) {
				NameInfo info = new NameInfo(name, nameColor);
				info.x = x + width;
				info.y = y + width/4;
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
	
	/** Compares entity print informations with respect to print order. */
	protected static class MapEntityComparator implements Comparator<MapEntity> {
		@Override
		public int compare(MapEntity arg0, MapEntity arg1) {
			DefaultEntityViewInfo info0 = (DefaultEntityViewInfo) arg0.getViewInfo();
			DefaultEntityViewInfo info1 = (DefaultEntityViewInfo) arg1.getViewInfo();
			if (info0.printOrder < info1.printOrder)
				return -1;
			else if (info0.printOrder > info1.printOrder)
				return 1;
			else
				return 0;
		}
	}
}
