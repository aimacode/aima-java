package aimax.osm.viewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import aimax.osm.data.Position;
import aimax.osm.data.WayNodeProvider;
import aimax.osm.data.entities.MapEntity;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.data.entities.Track;

/**
 * Provides a rather general map entity renderer implementation. It assumes that
 * all entities provided to the renderer have an attached
 * {@link aimax.osm.viewer.DefaultEntityViewInfo} object. The visual appearance
 * of the drawn map strongly depends on those objects.
 * 
 * @author Ruediger Lunde
 */
public class DefaultEntityRenderer extends AbstractEntityRenderer {

	private Hashtable<Long, List<MapNode>> wayNodeHash;

	/**
	 * Default size used for fonts (in logical units).
	 */
	protected float defaultFontSize = 12f;

	protected float scale;
	protected float displayFactorSym;

	protected List<MapWay> areaBuffer;
	protected List<MapWay> wayBuffer;
	protected List<MapEntity> nodeBuffer;
	protected List<Track> trackBuffer;
	protected List<NameInfo> nameInfoBuffer;
	private List<MapNode> tmpNodeBuffer; // to improve thread-safety

	/** Standard constructor. */
	public DefaultEntityRenderer() {
		wayNodeHash = new Hashtable<Long, List<MapNode>>();
		setBackgroundColor(UColor.WHITE);
		areaBuffer = new ArrayList<MapWay>();
		wayBuffer = new ArrayList<MapWay>();
		nodeBuffer = new ArrayList<MapEntity>();
		trackBuffer = new ArrayList<Track>();
		nameInfoBuffer = new ArrayList<NameInfo>();
		tmpNodeBuffer = new ArrayList<MapNode>();
	}

	/** Clears all buffers and prepares rendering. */
	@Override
	public void initForRendering(UnifiedImageBuilder<?> imageBdr,
			CoordTransformer transformer, WayNodeProvider wnProvider) {
		super.initForRendering(imageBdr, transformer, wnProvider);
		wayNodeHash.clear();

		scale = transformer.computeScale();
		displayFactorSym = displayFactor * transformer.getDotsPerUnit();
		imageBdr.setFontSize(defaultFontSize * displayFactorSym);
		areaBuffer.clear();
		wayBuffer.clear();
		nodeBuffer.clear();
		trackBuffer.clear();
		nameInfoBuffer.clear();
		// count = 0;
	}

	protected List<MapNode> getWayNodes(MapWay way) {
		List<MapNode> result = wayNodeHash.get(way.getId());
		if (result == null) {
			result = wnProvider.getWayNodes(way, scale);
			wayNodeHash.put(way.getId(), result);
		}
		return result;
	}

	/**
	 * Returns the map node which is the nearest with respect to the specified
	 * view coordinates among the currently displayed nodes.
	 */
	public MapNode getNextNode(int x, int y) {
		Position pos = new Position(transformer.lat(y), transformer.lon(x));
		MapNode nextNode = null;
		MapNode tmp = null;
		for (int i = 0; i < 2; i++) {
			List<MapWay> ways = (i == 0) ? areaBuffer : wayBuffer;
			for (MapWay way : ways) {
				tmp = pos.selectNearest(way.getNodes(), null);
				if (nextNode == null
						|| pos.getDistKM(tmp) < pos.getDistKM(nextNode)) {
					nextNode = tmp;
				}
			}
		}
		for (MapEntity node : nodeBuffer) {
			if (node instanceof MapNode) {
				tmp = (MapNode) node;
				if (tmp != null
						&& tmp.getAttributeValue("marker") == null
						&& (nextNode == null || pos.getDistKM(tmp) < pos
								.getDistKM(nextNode))) {
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
		List<MapNode> nodes = getWayNodes(way);
		if (!nodes.isEmpty() && pInfo.wayColor != null) {
			if (pInfo.wayFillColor != null
					&& nodes.get(0) == nodes.get(nodes.size() - 1)
					&& (way.isArea() || !pInfo.fillAreasOnly))
				// alternative solution:
				// && (way.isArea() ||
				// (nodes.get(0) == nodes.get(nodes.size()-1)
				// && !pInfo.fillAreasOnly)))
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
		DefaultEntityViewInfo vInfo = (DefaultEntityViewInfo) track
				.getViewInfo();
		if (vInfo != null && scale >= vInfo.minVisibleScale * displayFactor) {
			trackBuffer.add(track);
		}
	}

	// int awnodes = 0;
	/** Prints all buffered entities according to their rendering informations. */
	public void printBufferedObjects() {
		Collections.sort(areaBuffer, new MapAreaComparator());
		Comparator<MapEntity> comp = new MapEntityComparator();
		if (wayBuffer.size() < 10000)
			Collections.sort(wayBuffer, comp);
		if (nodeBuffer.size() < 10000)
			Collections.sort(nodeBuffer, comp);
		for (MapWay area : areaBuffer)
			printWay(area, (DefaultEntityViewInfo) area.getViewInfo(), true);
		for (MapWay way : wayBuffer)
			printWay(way, (DefaultEntityViewInfo) way.getViewInfo(), false);
		for (MapEntity node : nodeBuffer) {
			MapNode n;
			if (node instanceof MapWay) {
				List<MapNode> wayNodes = getWayNodes((MapWay) node);
				// needed to show icons for ways, whose abstraction is empty.
				if (wayNodes.isEmpty())
					wayNodes = ((MapWay) node).getNodes();
				n = wayNodes.get(0);
			} else
				n = (MapNode) node;
			printNode(n, (DefaultEntityViewInfo) node.getViewInfo());
		}
		for (Track track : trackBuffer)
			printTrack(track);
		// System.out.print("NamesOrg: " + nameInfoBuffer.size() + "\n");
		Collections.sort(nameInfoBuffer);
		// remove names whose positions are to close to each other
		int charSize = (int) (defaultFontSize * displayFactorSym);
		for (int i = 0; i < nameInfoBuffer.size(); ++i) {
			NameInfo info = nameInfoBuffer.get(i);
			for (int j = 0; j < i; ++j) {
				NameInfo info1 = nameInfoBuffer.get(j);
				int fac = (info.name.equals(info1.name)) ? 3 : 2;
				if (Math.abs(info.y - info1.y) < charSize * fac) {
					fac = (info.x < info1.x) ? info.name.length() : info1.name
							.length();
					if (Math.abs(info.x - info1.x) < charSize * fac) {
						nameInfoBuffer.remove(i);
						--i;
						j = i;
					}
				}
			}
		}
		for (NameInfo textInfo : nameInfoBuffer) {
			imageBdr.setColor(textInfo.color);
			imageBdr.drawString(textInfo.name, textInfo.x, textInfo.y);
		}
		// System.out.print("Areas: " + areaBuffer.size() + "  ");
		// System.out.print("Ways: " + wayBuffer.size() + "  ");
		// System.out.print("Nodes: " + nodeBuffer.size() + "  ");
		// System.out.print("Names: " + nameInfoBuffer.size() + "\n");
	}

	/** Prints a way entity. */
	protected void printWay(MapWay way, DefaultEntityViewInfo pInfo,
			boolean asArea) {
		List<MapNode> nodes = getWayNodes(way);
		if (nodes != null) {
			// awnodes+=nodes.size();
			boolean asOneway = false;
			NameInfo textInfo = null;
			if (scale >= pInfo.minNameScale * displayFactor) {
				asOneway = way.isOneway();
				if (way.getName() != null && pInfo.nameColor != null) {
					textInfo = new NameInfo(way.getName(), pInfo.nameColor,
							pInfo.printOrder);
				}
			}
			printLine(imageBdr, nodes, pInfo, asArea, asOneway, textInfo);
		}
	}

	/** Prints a node entity. */
	protected void printNode(MapNode node, DefaultEntityViewInfo pInfo) {
		int x = transformer.x(node.getLon());
		int y = transformer.y(node.getLat());
		int width = 0;

		if (pInfo.icon != null) {
			width = Math.round(pInfo.icon.size * displayFactorSym);
			pInfo.icon.draw(imageBdr, x, y, displayFactorSym);
		}

		if (scale >= pInfo.minNameScale * displayFactor) {
			String name = node.getName();
			if (name != null && pInfo.nameColor != null) {
				NameInfo info = new NameInfo(name, pInfo.nameColor,
						pInfo.printOrder);
				info.x = x + width;
				info.y = y + width / 4;
				nameInfoBuffer.add(info);
			}
		}
	}

	/** Prints a track entity. */
	protected void printTrack(Track track) {
		DefaultEntityViewInfo vInfo = (DefaultEntityViewInfo) track
				.getViewInfo();
		tmpNodeBuffer.clear();
		tmpNodeBuffer.addAll(track.getNodes());
		if (!tmpNodeBuffer.isEmpty()) {
			printLine(imageBdr, tmpNodeBuffer, vInfo, false, false, null);
			printPoint(imageBdr, tmpNodeBuffer.get(tmpNodeBuffer.size() - 1),
					vInfo, null);
		}
	}

	/** Prints a line or fills an area. */
	protected void printLine(UnifiedImageBuilder<?> imageBdr, List<MapNode> nodes,
			DefaultEntityViewInfo pInfo, boolean asArea, boolean asOneway,
			NameInfo textInfo) {
		// count++;
		int[] xPoints = new int[nodes.size()];
		int[] yPoints = new int[nodes.size()];

		int viewWidth = !asArea ? imageBdr.getWidth() : -1;
		int viewHeight = !asArea ? imageBdr.getHeight() : -1;
		boolean visible = getViewCoords(nodes, viewWidth, viewHeight, xPoints,
				yPoints);

		if (visible) {
			boolean filled = false;
			if (asArea) {
				imageBdr.setColor(pInfo.wayFillColor != null ? pInfo.wayFillColor
						: pInfo.wayColor);
				imageBdr.setLineStyle(false, displayFactor);
				imageBdr.setAreaFilled(true);
				imageBdr.drawPolygon(xPoints, yPoints, nodes.size());
				filled = true;
			}
			if (!filled || pInfo.wayFillColor != null
					&& !pInfo.wayFillColor.equals(pInfo.wayColor)) {
				imageBdr.setColor(pInfo.wayColor);
				imageBdr.setLineStyle(pInfo.wayDashed, pInfo.wayWidth
						* displayFactorSym);
				imageBdr.setAreaFilled(false);
				imageBdr.drawPolyline(xPoints, yPoints, nodes.size());
			}
			if (asOneway) {
				float x = xPoints[xPoints.length - 1];
				float y = yPoints[yPoints.length - 1];
				double angle = Math.atan2(x - xPoints[xPoints.length - 2],
						-(y - yPoints[yPoints.length - 2]));
				printOnewayArrow(x, y, angle);
			}
			if (textInfo != null) {
				setWayNamePosition(textInfo, xPoints, yPoints, filled);
				nameInfoBuffer.add(textInfo);
			}
			if (debugMode && scale >= 2 * pInfo.minNameScale * displayFactor) {
				int i = 0;
				for (MapNode node : nodes) {
					textInfo = new NameInfo(Long.toString(node.getId()),
							pInfo.nameColor, pInfo.printOrder);
					textInfo.x = xPoints[i];
					textInfo.y = yPoints[i];
					nameInfoBuffer.add(textInfo);
					++i;
				}
			}
		}
	}

	/**
	 * Computes the view coordinates for a list of way nodes and checks
	 * visibility with respect to a clipping rectangle. The check improves the
	 * viewing performance in large scales in which long invisible ways (e.g.
	 * coast lines) often pass the bounding box test.
	 * 
	 * @param nodes
	 *            List of way nodes.
	 * @param viewWidth
	 *            Used for clipping if > 0.
	 * @param viewHeight
	 *            Used for clipping if > 0.
	 * @param xView
	 *            Array of coordinates for the result.
	 * @param yView
	 *            Array of coordinates for the result.
	 * @return true if at least a part of the line is visible.
	 */
	protected boolean getViewCoords(List<MapNode> nodes, int viewWidth,
			int viewHeight, int[] xView, int[] yView) {
		boolean visible = (viewWidth <= 0 || viewHeight <= 0);
		int xv;
		int yv;
		int xClipPos;
		int yClipPos;
		int xClipPosLast = 0;
		int yClipPosLast = 0;
		int i = 0;
		for (MapNode node : nodes) {
			xv = transformer.x(node.getLon());
			yv = transformer.y(node.getLat());
			// bounding box test not sufficient for large scales...
			xView[i] = xv;
			yView[i] = yv;
			if (!visible) {
				xClipPos = 0;
				if (xv < 0)
					xClipPos = 1;
				else if (xv > viewWidth)
					xClipPos = 2;
				yClipPos = 0;
				if (yv < 0)
					yClipPos = 1;
				else if (yv > viewHeight)
					yClipPos = 2;
				visible = (xClipPos == 0 || xClipPos != xClipPosLast && i > 0)
						&& (yClipPos == 0 || yClipPos != yClipPosLast && i > 0);
				xClipPosLast = xClipPos;
				yClipPosLast = yClipPos;
			}
			++i;
		}
		return visible;
	}

	/**
	 * Marks the end of a one-way street with an arrow. The angle specifies the
	 * direction, zero means north.
	 */
	protected void printOnewayArrow(float x, float y, double angle) {
		imageBdr.setColor(UColor.GRAY);
		imageBdr.setLineStyle(false, displayFactorSym);
		imageBdr.setAreaFilled(false);
		drawArrowLine(x, y, displayFactorSym * 10f, angle);
		drawArrowLine(x, y, displayFactorSym * 7f, angle - Math.PI/4);
		drawArrowLine(x, y, displayFactorSym * 7f, angle + Math.PI/4);
	}

	private void drawArrowLine(float x, float y, float length, double angle) {
		imageBdr.drawLine(Math.round(x), Math.round(y),
				(int) Math.round(-length * Math.sin(angle) + x),
				(int) Math.round(length * Math.cos(angle) + y));
	}

	/** Finds a good place for printing the name of a way entity. */
	protected void setWayNamePosition(NameInfo info, int[] xPoints,
			int[] yPoints, boolean area) {
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
			info.y += defaultFontSize * displayFactorSym;
	}

	/** Prints a point of interest. */
	protected void printPoint(UnifiedImageBuilder<?> imageBdr, MapNode node,
			DefaultEntityViewInfo pInfo, UColor nameColor) {
		int x = transformer.x(node.getLon());
		int y = transformer.y(node.getLat());
		int width = 0;

		if (pInfo.icon != null) {
			width = Math.round(pInfo.icon.size * displayFactorSym);
			pInfo.icon.draw(imageBdr, x, y, displayFactor);
		}

		if (nameColor != null) {
			String name = (debugMode) ? "P" + Long.toString(node.getId())
					: node.getName();
			if (name != null) {
				NameInfo info = new NameInfo(name, nameColor, pInfo.printOrder);
				info.x = x + width;
				info.y = y + width / 4;
				nameInfoBuffer.add(info);
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////
	// some inner classes...

	/**
	 * Stores color and position information for a name to be printed out.
	 */
	protected static class NameInfo implements Comparable<NameInfo> {
		public String name;
		public UColor color;
		public int x;
		public int y;
		/** Print order value of the corresponding entity. */
		public int printOrder;

		protected NameInfo(String name, UColor color, int printOrder) {
			this.name = name;
			this.color = color;
			this.printOrder = printOrder;
		}

		@Override
		public int compareTo(NameInfo arg0) {
			if (printOrder < arg0.printOrder)
				return -1;
			else if (printOrder > arg0.printOrder)
				return 1;
			else
				return 0;
		}
	}

	/** Compares entity print informations with respect to print order. */
	protected static class MapEntityComparator implements Comparator<MapEntity> {
		@Override
		public int compare(MapEntity arg0, MapEntity arg1) {
			DefaultEntityViewInfo info0 = (DefaultEntityViewInfo) arg0
					.getViewInfo();
			DefaultEntityViewInfo info1 = (DefaultEntityViewInfo) arg1
					.getViewInfo();
			if (info0.printOrder < info1.printOrder)
				return 1;
			else if (info0.printOrder > info1.printOrder)
				return -1;
			else
				return 0;
		}
	}

	/** Compares ways with respect to the size of their bounding box. */
	protected static class MapAreaComparator implements Comparator<MapWay> {
		@Override
		public int compare(MapWay arg0, MapWay arg1) {
			return -Float.compare(arg0.getBoundingBoxSize(),
					arg1.getBoundingBoxSize());
		}
	}
}
