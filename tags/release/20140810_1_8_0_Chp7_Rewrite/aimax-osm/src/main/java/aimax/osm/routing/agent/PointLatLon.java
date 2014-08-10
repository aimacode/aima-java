package aimax.osm.routing.agent;

import aima.core.util.datastructure.Point2D;
import aimax.osm.data.Position;

/**
 * Special <code>Point2D</code> implementation which provides correct
 * distance values in kilometer for geographical positions (x=lon and y=lat).
 * @author Ruediger Lunde
 */
public class PointLatLon extends Point2D {
	public PointLatLon(double lat, double lon) {
		super(lon, lat);
	}
	
	public double getLat() { return getY(); }
	
	public double getLon() { return getX(); }
	
	public double distance(Point2D pt) {
		return Position.getDistKM((float) getY(), (float) getX(),
				(float) pt.getY(), (float) pt.getX());
	}
}
