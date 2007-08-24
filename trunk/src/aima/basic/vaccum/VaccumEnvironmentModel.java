package aima.basic.vaccum;

import java.util.Hashtable;

/**
 * @author Ravi Mohan
 * 
 */
public class VaccumEnvironmentModel {
	private Hashtable<String, String> model;

	public VaccumEnvironmentModel() {
		model = new Hashtable<String, String>();
	}

	public void setLocationStatus(String location, String locationStatus) {
		model.put(location, locationStatus);
	}

	public String getStatusOf(String location) {
		return model.get(location);
	}

	public boolean bothLocationsClean() {
		String loc1Status = getStatusOf("A");
		String loc2Status = getStatusOf("B");
		return ((loc1Status != null) && (loc1Status.equals("Clean")))
				&& ((loc2Status != null) && (loc2Status.equals("Clean")));
	}

}