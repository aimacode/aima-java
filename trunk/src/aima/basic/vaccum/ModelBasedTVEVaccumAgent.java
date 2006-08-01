package aima.basic.vaccum;

import aima.basic.Agent;

public class ModelBasedTVEVaccumAgent extends Agent {

	public ModelBasedTVEVaccumAgent() {
		super(new ModelBasedTVEVaccumAgentProgram(new VaccumEnvironmentModel()));

	}

}