package aimax.osm.gps;

import aimax.osm.data.Position;

/**
 * Maintains measurement quality and position informations.
 * @author Ruediger Lunde
 */
public class GpsFix extends Position {
	private boolean posOk;
	
	public GpsFix(boolean posOK, float lat, float lon) {
		super(lat, lon);
		this.posOk = posOK;
	}

	public boolean isPosOk() {
		return posOk;
	}

	public float getLat() {
		return lat;
	}

	public float getLon() {
		return lon;
	}
	
	public String toString() {
		return "Lat: " + lat + " Lon: " + lon + (posOk ? "" : " ?");
	}
}
