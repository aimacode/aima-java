package aimax.osm.data;

import java.util.HashSet;

import aimax.osm.data.entities.MapWay;

/**
 * Way filter, which is based on attribute value checking. It uses
 * a <code>MapDataStore</code> for way data lookup.
 * @author Ruediger Lunde
 */
public class MapWayAttFilter implements MapWayFilter {
	OsmMap mapData;
	String attName;
	HashSet<String> attValues;
	
	public MapWayAttFilter(OsmMap mapData, String attName) {
		this.mapData = mapData;
		this.attName = attName;
	}
	
	public void setAccepted(String attValue) {
		if (attValues == null)
			attValues = new HashSet<String>();
		attValues.add(attValue);
	}
	
	public boolean isAccepted(long wayId) {
		MapWay way = mapData.getWay(wayId);
		String val = null;
		if (way != null) {
			val = way.getAttributeValue(attName);
		}
		return val != null && (attValues == null || attValues.contains(val));
	}
	
	
	/** Creates a way filter which passes any way except waterways. */
	public static MapWayAttFilter createAnyWayFilter(OsmMap mapData) {
		MapWayAttFilter filter = new MapWayAttFilter(mapData, "highway");
		filter.setAccepted("motorway");
		filter.setAccepted("motorway_link");
		filter.setAccepted("trunk");
		filter.setAccepted("trunk_link");
		filter.setAccepted("primary");
		filter.setAccepted("primary_link");
		filter.setAccepted("secondary");
		filter.setAccepted("tertiary");
		filter.setAccepted("road");
		filter.setAccepted("residential");
		filter.setAccepted("living_street");
		filter.setAccepted("pedestrian");
		filter.setAccepted("service");
		filter.setAccepted("track");
		filter.setAccepted("cycleway");
		filter.setAccepted("path");
		filter.setAccepted("footway");
		filter.setAccepted("steps");
		//filter.setAccepted("waterway");
		filter.setAccepted("unclassified");
		return filter;
	}
	
	/** Creates a way filter which passes any way suitable for traveling by car. */
	public static MapWayAttFilter createCarWayFilter(OsmMap mapData) {
		MapWayAttFilter filter = new MapWayAttFilter(mapData, "highway");
		filter.setAccepted("motorway");
		filter.setAccepted("motorway_link");
		filter.setAccepted("trunk");
		filter.setAccepted("trunk_link");
		filter.setAccepted("primary");
		filter.setAccepted("primary_link");
		filter.setAccepted("secondary");
		filter.setAccepted("tertiary");
		filter.setAccepted("road");
		filter.setAccepted("residential");
		filter.setAccepted("living_street");
//		filter.setAccepted("pedestrian");
//		filter.setAccepted("service");
//		filter.setAccepted("track");
//		filter.setAccepted("cycleway");
//		filter.setAccepted("path");
//		filter.setAccepted("footway");
//		filter.setAccepted("steps");
//		filter.setAccepted("waterway");
		filter.setAccepted("unclassified");
		return filter;
	}

	/** Creates a way filter which passes any way suitable for traveling by bike. */
	public static MapWayAttFilter createBicycleWayFilter(OsmMap mapData) {
		MapWayAttFilter filter = new MapWayAttFilter(mapData, "highway");
//		filter.setAccepted("motorway");
//		filter.setAccepted("motorway_link");
//		filter.setAccepted("trunk");
//		filter.setAccepted("trunk_link");
		filter.setAccepted("primary");
		filter.setAccepted("primary_link");
		filter.setAccepted("secondary");
		filter.setAccepted("tertiary");
		filter.setAccepted("road");
		filter.setAccepted("residential");
		filter.setAccepted("living_street");
		filter.setAccepted("pedestrian");
		filter.setAccepted("service");
		filter.setAccepted("track");
		filter.setAccepted("cycleway");
		filter.setAccepted("path");
		filter.setAccepted("footway");
//		filter.setAccepted("steps");
		//filter.setAccepted("waterway");
		filter.setAccepted("unclassified");
		return filter;
	}
}
