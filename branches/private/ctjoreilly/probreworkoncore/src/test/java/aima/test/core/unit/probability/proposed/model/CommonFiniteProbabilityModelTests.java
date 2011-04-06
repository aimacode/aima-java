package aima.test.core.unit.probability.proposed.model;

import junit.framework.Assert;
import aima.core.probability.proposed.model.Distribution;
import aima.core.probability.proposed.model.FiniteProbabilityModel;
import aima.core.probability.proposed.model.RandomVariable;
import aima.core.probability.proposed.model.domain.FiniteIntegerDomain;
import aima.core.probability.proposed.model.proposition.AssignmentProposition;
import aima.core.probability.proposed.model.proposition.EquivalentProposition;
import aima.core.probability.proposed.model.proposition.IntegerSumProposition;

public abstract class CommonFiniteProbabilityModelTests extends
		CommonProbabilityModelTests {

	//
	// PROTECTED
	//
	protected void test_RollingPairFairDiceModel_Distributions(
			FiniteProbabilityModel model, RandomVariable dice1RV,
			RandomVariable dice2RV) {

		AssignmentProposition ad1_1 = new AssignmentProposition(dice1RV, 1);
		Distribution dD1_1 = model.priorDistribution(ad1_1);
		assertEqualDistributions(new double[] { 1.0 / 6.0 }, dD1_1.getValues());

		Distribution dPriorDice1 = model.priorDistribution(dice1RV);
		assertEqualDistributions(new double[] { 1.0 / 6.0, 1.0 / 6.0,
				1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0 }, dPriorDice1
				.getValues());

		Distribution dPriorDice2 = model.priorDistribution(dice2RV);
		assertEqualDistributions(new double[] { 1.0 / 6.0, 1.0 / 6.0,
				1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0 }, dPriorDice2
				.getValues());

		Distribution dJointDice1Dice2 = model.jointDistribution(dice1RV,
				dice2RV);
		Assert.assertEquals(36, dJointDice1Dice2.getValues().length);
		for (int i = 0; i < dJointDice1Dice2.getValues().length; i++) {
			Assert.assertEquals(1.0 / 36.0, dJointDice1Dice2.getValues()[i],
					DELTA_THRESHOLD);
		}

		Distribution dJointDice2Dice1 = model.jointDistribution(dice2RV,
				dice1RV);
		Assert.assertEquals(36, dJointDice2Dice1.getValues().length);
		for (int i = 0; i < dJointDice2Dice1.getValues().length; i++) {
			Assert.assertEquals(1.0 / 36.0, dJointDice2Dice1.getValues()[i],
					DELTA_THRESHOLD);
		}

		//
		// Test Sets of events
		IntegerSumProposition total11 = new IntegerSumProposition("Total",
				new FiniteIntegerDomain(11), dice1RV, dice2RV);
		Distribution dPriorTotal11 = model.priorDistribution(total11);
		Assert.assertEquals(1, dPriorTotal11.getValues().length);
		Assert.assertEquals(2.0 / 36.0, dPriorTotal11.getValues()[0],
				DELTA_THRESHOLD);

		Distribution dPriorDice1Total11 = model.priorDistribution(dice1RV,
				total11);
		// TODO - check if correct
		// <0.0, 0.0, 0.0, 0.0, 0.027777777777777776, 0.027777777777777776>
		System.out.println("dPriorDice1Total11=" + dPriorDice1Total11);

		EquivalentProposition doubles = new EquivalentProposition("Doubles",
				dice1RV, dice2RV);
		// TODO

		//
		// Test posterior
		Distribution dPosteriorDice1GivenTotal11 = model.posteriorDistribution(
				dice1RV, total11);
		System.out.println("dPosteriorDice1GivenTotal11="
				+ dPosteriorDice1GivenTotal11);

		Distribution dPosteriorDice1GivenDice2 = model.posteriorDistribution(
				dice1RV, dice2RV);
		assertEqualDistributions(new double[] { 1.0 / 6.0, 1.0 / 6.0,
				1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0 },
				dPosteriorDice1GivenDice2.getValues());

		Distribution dPosteriorDice2GivenDice1 = model.posteriorDistribution(
				dice2RV, dice1RV);
		assertEqualDistributions(new double[] { 1.0 / 6.0, 1.0 / 6.0,
				1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0 },
				dPosteriorDice2GivenDice1.getValues());
	}

	protected void test_ToothacheCavityCatchModel_Distributions(
			FiniteProbabilityModel model, RandomVariable toothacheRV,
			RandomVariable cavityRV, RandomVariable catchRV) {
		Assert.fail("TODO");

		// AIMA3e pg. 493
		// P(Cavity | toothache) = <0.6, 0.4>

		// AIMA3e pg. 497
		// P(Cavity | toothache AND catch) = <0.871, 0.129>

		// AIMA3e pg. 498
		// (13.17)
		// P(toothache AND catch | Cavity)
		// = P(toothache | Cavity)P(catch | Cavity)

		// (13.18)
		// P(Cavity | toothache AND catch)
		// = &alpha;P(toothache | Cavity)P(catch | Cavity)P(Cavity)

		// (13.19)
		// P(Toothache, Catch | Cavity)
		// = P(Toothache | Cavity)P(Catch | Cavity)

		// (product rule)
		// P(Toothache, Catch, Cavity)
		// = P(Toothache, Catch | Cavity)P(Cavity)

		// (using 13.19)
		// P(Toothache, Catch | Cavity)P(Cavity)
		// = P(Toothache | Cavity)P(Catch | Cavity)P(Cavity)
		//
		// P(Toothache, Catch, Cavity)
		// = P(Toothache | Cavity)P(Catch | Cavity)P(Cavity)
	}

	protected void test_ToothacheCavityCatchWeatherModel_Distributions(
			FiniteProbabilityModel model, RandomVariable toothacheRV,
			RandomVariable cavityRV, RandomVariable catchRV,
			RandomVariable weatherRV) {
		Assert.fail("TODO");

		// TODO - call sub-model tests
		// test_ToothacheCavityCatch

		// AIMA3e pg. 487
		// P(sunny, Cavity)
		// Would be a two-element vector giving the probabilities of a sunny day
		// with a cavity and a sunny day with no cavity.

		// AIMA3e pg. 488 (i.e. one element Vector returned)
		// P(sunny, cavity)
		// P(sunny AND cavity)
		// P(sunny) = <0.6>

		// AIMA3e pg. 496
		// General case of Bayes' Rule
		// P(Y | X) = P(X | Y)P(Y)/P(X)

		// General Bayes' Rule conditionalized on background evidence e (13.3)
		// P(Y | X, e) = P(X | Y, e)P(Y|e)/P(X | e)
	}

	// AIMA3e pg. 496
	protected void test_MeningitisStiffNeckModel_Distributions(
			FiniteProbabilityModel model, RandomVariable meningitisRV,
			RandomVariable stiffNeckRV) {

		AssignmentProposition astiffNeck = new AssignmentProposition(
				stiffNeckRV, true);

		// AIMA3e pg. 497
		// P(Mengingitis | stiffneck) = &alpha;<P(s | m)P(m), P(s | ~m)P(~m)>
		Distribution dMeningitisGivenStiffNeck = model.posteriorDistribution(
				meningitisRV, astiffNeck);
		Assert.assertEquals(2, dMeningitisGivenStiffNeck.getValues().length);
		Assert.assertEquals(0.0014, dMeningitisGivenStiffNeck.getValues()[0],
				DELTA_THRESHOLD);
		Assert.assertEquals(0.9986, dMeningitisGivenStiffNeck.getValues()[1],
				DELTA_THRESHOLD);
	}

	protected void test_BurglaryAlarmModel_Distributions(
			FiniteProbabilityModel model, RandomVariable burglaryRV,
			RandomVariable earthQuakeRV, RandomVariable alarmRV,
			RandomVariable johnCallsRV, RandomVariable maryCallsRV) {
		Assert.fail("TODO");

		// AIMA3e pg. 523
		// P(Burglary | JohnCalls = true, MaryCalls = true) = <0.284, 0.716>
	}

	//
	// PRIVATE METHODS
	//
	private void assertEqualDistributions(double[] expected, double[] actual) {
		Assert.assertEquals(expected.length, actual.length);
		for (int i = 0; i < expected.length; i++) {
			Assert.assertEquals(expected[i], actual[i], DELTA_THRESHOLD);
		}
	}
}
