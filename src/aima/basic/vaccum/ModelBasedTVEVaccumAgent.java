package aima.basic.vaccum;

import aima.basic.Agent;

/**
 * @author Ravi Mohan
 * 
 */
public class ModelBasedTVEVaccumAgent extends Agent {

	public ModelBasedTVEVaccumAgent() {
		super(new ModelBasedTVEVaccumAgentProgram(new VaccumEnvironmentModel()));

	}

}