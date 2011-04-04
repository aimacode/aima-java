package aima.core.probability.proposed.model.full.example;

import java.util.ArrayList;
import java.util.List;

import aima.core.probability.proposed.model.RandomVariable;
import aima.core.probability.proposed.model.domain.BooleanDomain;
import aima.core.probability.proposed.model.full.FullJointDistributionModel;

public class FullJointDistributionMeningitisStiffNeckModel extends
		FullJointDistributionModel {

	private RandomVariable meningitisRV = null;
	private RandomVariable stiffNeckRV = null;

	public FullJointDistributionMeningitisStiffNeckModel() {
		super(new double[] { 
				// Meningitis * StiffNeck = 4 possible worlds
				// Meningitis = true, StiffNeck = true
				0.000014, // i.e 1/50000 * 0.7
				// Meningitis = true, StiffNeck = false
				0.000006, // i.e. (1/50000) * 0.3
				// Meningitis = false, StiffNeck = true
				0.009986, // i.e. 0.01 - 0.000014
				// Meningitis = false, StiffNeck = false
				0.989994 // i.e. 0.99 - 0.000006
				}, new RandomVariable("Meningitis",
				new BooleanDomain()), new RandomVariable(
				"StiffNeck", new BooleanDomain()));
		
		List<RandomVariable> vars = new ArrayList<RandomVariable>(getRepresentation());
		meningitisRV = vars.get(0);
		stiffNeckRV = vars.get(1);
	}
	
	public RandomVariable getMeningitis() {
		return meningitisRV;
	}
	
	public RandomVariable getStiffNeck() {
		return stiffNeckRV;
	}
}
