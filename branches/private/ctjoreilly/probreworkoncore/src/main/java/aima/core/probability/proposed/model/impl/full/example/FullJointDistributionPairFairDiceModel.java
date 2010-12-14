package aima.core.probability.proposed.model.impl.full.example;

import java.util.ArrayList;
import java.util.List;

import aima.core.probability.proposed.model.RandomVariable;
import aima.core.probability.proposed.model.domain.FiniteIntegerDomain;
import aima.core.probability.proposed.model.impl.full.FullJointDistribution;

public class FullJointDistributionPairFairDiceModel extends
		FullJointDistribution {

	private RandomVariable dice1RV = null;
	private RandomVariable dice2RV = null;

	public FullJointDistributionPairFairDiceModel() {
		super(new double[] { 
				// Dice1 * Dice 2 = 36 possible worlds
				1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 
				//
				1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 
				//
				1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 
				//
				1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 
				//
				1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 
				//
				1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0
				}, new RandomVariable("Dice1",
				new FiniteIntegerDomain(1, 2, 3, 4, 5, 6)), new RandomVariable(
				"Dice2", new FiniteIntegerDomain(1, 2, 3, 4, 5, 6)));
		
		List<RandomVariable> vars = new ArrayList<RandomVariable>(getRepresentation());
		dice1RV = vars.get(0);
		dice2RV = vars.get(1);
	}
	
	public RandomVariable getDice1() {
		return dice1RV;
	}
	
	public RandomVariable getDice2() {
		return dice2RV;
	}
}
