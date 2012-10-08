package aima.core.probability.example;

import aima.core.probability.full.FullJointDistributionModel;

/**
 * 
 * @author Ciaran O'Reilly
 *
 */
public class FullJointDistributionPairFairDiceModel extends
		FullJointDistributionModel {

	public FullJointDistributionPairFairDiceModel() {
		super(new double[] {
				// Dice1 * Dice 2 = 36 possible worlds
				1.0 / 36.0,
				1.0 / 36.0,
				1.0 / 36.0,
				1.0 / 36.0,
				1.0 / 36.0,
				1.0 / 36.0,
				//
				1.0 / 36.0, 1.0 / 36.0,
				1.0 / 36.0,
				1.0 / 36.0,
				1.0 / 36.0,
				1.0 / 36.0,
				//
				1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0,
				1.0 / 36.0,
				1.0 / 36.0,
				1.0 / 36.0,
				//
				1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0,
				1.0 / 36.0,
				1.0 / 36.0,
				//
				1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0,
				1.0 / 36.0,
				//
				1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0, 1.0 / 36.0,
				1.0 / 36.0 }, ExampleRV.DICE_1_RV, ExampleRV.DICE_2_RV);
	}
}
