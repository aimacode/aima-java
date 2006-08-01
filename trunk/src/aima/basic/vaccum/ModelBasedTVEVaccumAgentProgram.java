package aima.basic.vaccum;

import aima.basic.AgentProgram;
import aima.basic.Percept;

public class ModelBasedTVEVaccumAgentProgram extends AgentProgram {
	VaccumEnvironmentModel myModel;

	ModelBasedTVEVaccumAgentProgram(VaccumEnvironmentModel model) {
		myModel = model;
	}

	public String execute(Percept percept) {
		String location = (String) percept.getAttribute("location");
		String locationStatus = (String) percept.getAttribute("status");
		myModel.setLocationStatus(location, locationStatus);

		if (myModel.bothLocationsClean()) {
			return "NoOp";
		} else if (locationStatus.equals("Dirty")) {
			return "Suck";
		} else if (location.equals("A")) {
			return "Right";
		} else if (location.equals("B")) {
			return "Left";
		} else
			return "None";

	}

}