package aima.basic.vaccum;

import aima.basic.BasicEnvironmentView;

public class TrivialVaccumDemo {
	public static void main(String[] args) {
		TrivialVaccumEnvironment tve = new TrivialVaccumEnvironment("Dirty",
				"Dirty");
		ModelBasedTVEVaccumAgent a = new ModelBasedTVEVaccumAgent();
		tve.addAgent(a, "A");
		BasicEnvironmentView view = new BasicEnvironmentView();
		tve.registerView(view);
		tve.step(5);
	}
}