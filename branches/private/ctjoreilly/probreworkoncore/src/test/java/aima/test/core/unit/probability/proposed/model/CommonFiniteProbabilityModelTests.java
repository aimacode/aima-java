package aima.test.core.unit.probability.proposed.model;

import junit.framework.Assert;
import aima.core.probability.proposed.model.Distribution;
import aima.core.probability.proposed.model.FiniteProbabilityModel;
import aima.core.probability.proposed.model.ProbabilityModel;
import aima.core.probability.proposed.model.RandomVariable;
import aima.core.probability.proposed.model.domain.FiniteIntegerDomain;
import aima.core.probability.proposed.model.proposition.IntegerSumProposition;
import aima.core.probability.proposed.model.proposition.RandomVariableProposition;

public abstract class CommonFiniteProbabilityModelTests extends
		CommonProbabilityModelTests {

	//
	// PROTECTED
	//
	protected void test_RollingPairFairDiceModel_Distributions(
			FiniteProbabilityModel model, RandomVariable dice1RV,
			RandomVariable dice2RV) {
		RandomVariableProposition dice1 = new RandomVariableProposition(dice1RV);
		RandomVariableProposition dice2 = new RandomVariableProposition(dice2RV);

		Distribution dPriorDice1 = model.priorDistribution(dice1);
		Assert.assertEquals(6, dPriorDice1.getValues().length);
		Assert.assertEquals(new double[] { 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0,
				1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0 }, dPriorDice1.getValues());

		Distribution dPriorDice2 = model.priorDistribution(dice2);
		Assert.assertEquals(6, dPriorDice2.getValues().length);
		Assert.assertEquals(new double[] { 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0,
				1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0 }, dPriorDice2.getValues());

		Distribution dPosteriorDice1GivenDice2 = model.posteriorDistribution(
				dice1, dice2);
		Assert.assertEquals(36, dPosteriorDice1GivenDice2.getValues().length);
		for (int i = 0; i < dPosteriorDice1GivenDice2.getValues().length; i++) {
			Assert.assertEquals(1.0 / 6.0,
					dPosteriorDice1GivenDice2.getValues()[i], DELTA_THRESHOLD);
		}

		Distribution dPosteriorDice2GivenDice1 = model.posteriorDistribution(
				dice2, dice1);
		Assert.assertEquals(36, dPosteriorDice2GivenDice1.getValues().length);
		for (int i = 0; i < dPosteriorDice2GivenDice1.getValues().length; i++) {
			Assert.assertEquals(1.0 / 6.0,
					dPosteriorDice2GivenDice1.getValues()[i], DELTA_THRESHOLD);
		}

		Distribution dJointDice1Dice2 = model.jointDistribution(dice1, dice2);
		Assert.assertEquals(36, dJointDice1Dice2.getValues().length);
		for (int i = 0; i < dJointDice1Dice2.getValues().length; i++) {
			Assert.assertEquals(1.0 / 36.0, dJointDice1Dice2.getValues()[i],
					DELTA_THRESHOLD);
		}

		Distribution dJointDice2Dice1 = model.jointDistribution(dice2, dice1);
		Assert.assertEquals(36, dJointDice2Dice1.getValues().length);
		for (int i = 0; i < dJointDice2Dice1.getValues().length; i++) {
			Assert.assertEquals(1.0 / 36.0, dJointDice2Dice1.getValues()[i],
					DELTA_THRESHOLD);
		}

		// TODO
		// Ensure distributions with Random Variables in them sum to 1.

		// Test Sets of events
		IntegerSumProposition total11 = new IntegerSumProposition("Total",
				new FiniteIntegerDomain(11), dice1RV, dice2RV);
		Distribution dPriorTotal11 = model.priorDistribution(total11);
		// TODO - Determine how constraints across multiple variables should be
		// represented in a
		// distribution. Currently going with {true, false} but it may make
		// sense
		// to be all permutations of the random variables from the model that
		// make up the constraint
		// - TBD.
		Assert.assertEquals(2, dPriorTotal11.getValues().length);
		Assert.assertEquals(2.0 / 36.0, dPriorTotal11.getValues()[0],
				DELTA_THRESHOLD);
		Assert.assertEquals(34.0 / 36.0, dPriorTotal11.getValues()[1],
				DELTA_THRESHOLD);

		IntegerSumProposition doubles = new IntegerSumProposition("Doubles",
				new FiniteIntegerDomain(2, 4, 6, 8, 10, 12), dice1RV, dice2RV);
		// TODO
	}

	protected void test_ToothacheCavityCatchModel_Distributions(
			FiniteProbabilityModel model, RandomVariable toothacheRV,
			RandomVariable cavityRV, RandomVariable catchRV) {
		Assert.fail("TODO");

		// AIMA3e pg. 493
		// P(Cavity | toothache) = <0.6, 0.4>

		// AIMA3e pg. 497
		// P(Cavity | toothache AND catch) = <0.871, 0.129>
	}

	protected void test_ToothacheCavityCatchWeatherModel_Distributions(
			FiniteProbabilityModel model, RandomVariable toothacheRV,
			RandomVariable cavityRV, RandomVariable catchRV) {
		Assert.fail("TODO");

		// AIMA3e pg. 487
		// P(sunny, Cavity)
		// Would be a two-element vector giving the probabilities of a sunny day
		// with a cavity and a sunny day with no cavity.
		
		// AIMA3e pg. 488 (i.e. one element Vector returned)
		// P(sunny, cavity)
		// P(sunny AND cavity)
		// P(sunny) = <0.6>
	}

	protected void test_BurglaryAlarmModel_Distributions(
			ProbabilityModel model, RandomVariable burglaryRV,
			RandomVariable earthQuakeRV, RandomVariable alarmRV,
			RandomVariable johnCallsRV, RandomVariable maryCallsRV) {
		Assert.fail("TODO");

		// AIMA3e pg. 523
		// P(Burglary | JohnCalls = true, MaryCalls = true) = <0.284, 0.716>
	}
}
