package aima.test.core.unit.probability.proposed.model;

import junit.framework.Assert;
import aima.core.probability.proposed.model.Distribution;
import aima.core.probability.proposed.model.FiniteProbabilityModel;
import aima.core.probability.proposed.model.RandomVariable;
import aima.core.probability.proposed.model.domain.FiniteIntegerDomain;
import aima.core.probability.proposed.model.proposition.AssignmentProposition;
import aima.core.probability.proposed.model.proposition.ConjunctiveProposition;
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
		// P<>(Total = 11) = <2.0/36.0>
		assertEqualDistributions(new double[] { 2.0 / 36.0 }, model
				.priorDistribution(total11).getValues());

		// P<>(Dice1, Total = 11)
		// = <0.0, 0.0, 0.0, 0.0, 1.0/36.0, 1.0/36.0>
		assertEqualDistributions(new double[] { 0, 0, 0, 0, 1.0 / 36.0,
				1.0 / 36.0 }, model.priorDistribution(dice1RV, total11)
				.getValues());

		EquivalentProposition doubles = new EquivalentProposition("Doubles",
				dice1RV, dice2RV);
		// P(Doubles) = <1.0/6.0>
		assertEqualDistributions(new double[] { 1.0 / 6.0 }, model
				.priorDistribution(doubles).getValues());

		//
		// Test posterior
		//
		// P<>(Dice1, Total = 11)
		// = <0.0, 0.0, 0.0, 0.0, 0.5, 0.5>
		assertEqualDistributions(new double[] { 0, 0, 0, 0, 0.5, 0.5 }, model
				.posteriorDistribution(dice1RV, total11).getValues());

		// P<>(Dice1 | Doubles) = <1/6, 1/6, 1/6, 1/6, 1/6, 1/6>
		assertEqualDistributions(new double[] { 1.0 / 6.0, 1.0 / 6.0,
				1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0 }, model
				.posteriorDistribution(dice1RV, doubles).getValues());

		Distribution dPosteriorDice1GivenDice2 = model.posteriorDistribution(
				dice1RV, dice2RV);
		Assert.assertEquals(36, dPosteriorDice1GivenDice2.getValues().length);
		for (int i = 0; i < dPosteriorDice1GivenDice2.getValues().length; i++) {
			Assert.assertEquals(1.0 / 6.0, dPosteriorDice1GivenDice2
					.getValues()[i], DELTA_THRESHOLD);
		}

		Distribution dPosteriorDice2GivenDice1 = model.posteriorDistribution(
				dice2RV, dice1RV);
		Assert.assertEquals(36, dPosteriorDice2GivenDice1.getValues().length);
		for (int i = 0; i < dPosteriorDice2GivenDice1.getValues().length; i++) {
			Assert.assertEquals(1.0 / 6.0, dPosteriorDice2GivenDice1
					.getValues()[i], DELTA_THRESHOLD);
		}
	}

	protected void test_ToothacheCavityCatchModel_Distributions(
			FiniteProbabilityModel model, RandomVariable toothacheRV,
			RandomVariable cavityRV, RandomVariable catchRV) {

		AssignmentProposition atoothache = new AssignmentProposition(
				toothacheRV, Boolean.TRUE);
		AssignmentProposition acatch = new AssignmentProposition(catchRV,
				Boolean.TRUE);

		// AIMA3e pg. 493
		// P<>(Cavity | toothache) = <0.6, 0.4>
		assertEqualDistributions(new double[] { 0.6, 0.4 }, model
				.posteriorDistribution(cavityRV, atoothache).getValues());

		// AIMA3e pg. 497
		// P<>(Cavity | toothache AND catch) = <0.871, 0.129>
		assertEqualDistributions(new double[] { 0.8709677419354839,
				0.12903225806451615 }, model.posteriorDistribution(cavityRV,
				atoothache, acatch).getValues());

		// AIMA3e pg. 498
		// (13.17)
		// P<>(toothache AND catch | Cavity)
		// = P<>(toothache | Cavity)P<>(catch | Cavity)
		ConjunctiveProposition toothacheAndCatch = new ConjunctiveProposition(
				atoothache, acatch);
		assertEqualDistributions(model.posteriorDistribution(toothacheAndCatch,
				cavityRV).getValues(), model.posteriorDistribution(atoothache,
				cavityRV).multiplyBy(
				model.posteriorDistribution(acatch, cavityRV)).getValues());

		// (13.18)
		// P<>(Cavity | toothache AND catch)
		// = &alpha;P<>(toothache | Cavity)P<>(catch | Cavity)P(Cavity)
		assertEqualDistributions(model.posteriorDistribution(cavityRV,
				toothacheAndCatch).getValues(), model.posteriorDistribution(
				atoothache, cavityRV).multiplyBy(
				model.posteriorDistribution(acatch, cavityRV)).multiplyBy(
				model.priorDistribution(cavityRV)).normalize().getValues());

		// (13.19)
		// P<>(Toothache, Catch | Cavity)
		// = P<>(Toothache | Cavity)P<>(Catch | Cavity)
		ConjunctiveProposition toothacheAndCatchRV = new ConjunctiveProposition(
				toothacheRV, catchRV);
		assertEqualDistributions(model.posteriorDistribution(
				toothacheAndCatchRV, cavityRV).getValues(), model
				.posteriorDistribution(toothacheRV, cavityRV).multiplyBy(
						model.posteriorDistribution(catchRV, cavityRV))
				.getValues());

		// (product rule)
		// P<>(Toothache, Catch, Cavity)
		// = P<>(Toothache, Catch | Cavity)P<>(Cavity)
		assertEqualDistributions(model.priorDistribution(toothacheRV, catchRV,
				cavityRV).getValues(), model.posteriorDistribution(
				toothacheAndCatchRV, cavityRV).multiplyBy(
				model.priorDistribution(cavityRV)).getValues());

		// (using 13.19)
		// P<>(Toothache, Catch | Cavity)P<>(Cavity)
		// = P<>(Toothache | Cavity)P<>(Catch | Cavity)P<>(Cavity)
		assertEqualDistributions(model.posteriorDistribution(
				toothacheAndCatchRV, cavityRV).multiplyBy(
				model.priorDistribution(cavityRV)).getValues(), model
				.posteriorDistribution(toothacheRV, cavityRV).multiplyBy(
						model.posteriorDistribution(catchRV, cavityRV)
								.multiplyBy(model.priorDistribution(cavityRV)))
				.getValues());
		//
		// P<>(Toothache, Catch, Cavity)
		// = P<>(Toothache | Cavity)P<>(Catch | Cavity)P<>(Cavity)
		assertEqualDistributions(model.priorDistribution(toothacheRV, catchRV,
				cavityRV).getValues(), model.posteriorDistribution(toothacheRV,
				cavityRV).multiplyBy(
				model.posteriorDistribution(catchRV, cavityRV)).multiplyBy(
				model.priorDistribution(cavityRV)).getValues());
		
		System.out.println("X:"+model.posteriorDistribution(cavityRV, catchRV));	
		System.out.println("X:"+model.posteriorDistribution(catchRV, cavityRV)
				.multiplyBy(model.priorDistribution(cavityRV)).divideBy(
						model.priorDistribution(catchRV)));
	}

	protected void test_ToothacheCavityCatchWeatherModel_Distributions(
			FiniteProbabilityModel model, RandomVariable toothacheRV,
			RandomVariable cavityRV, RandomVariable catchRV,
			RandomVariable weatherRV) {

		AssignmentProposition asunny = new AssignmentProposition(weatherRV,
				"sunny");
		AssignmentProposition acavity = new AssignmentProposition(cavityRV,
				Boolean.TRUE);

		// Should be able to run all the same queries for this independent
		// sub model.
		test_ToothacheCavityCatchModel_Distributions(model, toothacheRV,
				cavityRV, catchRV);

		// AIMA3e pg. 487
		// P(sunny, Cavity)
		// Would be a two-element vector giving the probabilities of a sunny day
		// with a cavity and a sunny day with no cavity.
		assertEqualDistributions(new double[] { 0.12, 0.48 }, model
				.priorDistribution(asunny, cavityRV).getValues());

		// AIMA3e pg. 488 (i.e. one element Vector returned)
		// P(sunny, cavity)
		assertEqualDistributions(new double[] { 0.12 }, model
				.priorDistribution(asunny, acavity).getValues());
		// P(sunny AND cavity)
		assertEqualDistributions(new double[] { 0.12 }, model
				.priorDistribution(new ConjunctiveProposition(asunny, acavity))
				.getValues());
		// P(sunny) = <0.6>
		assertEqualDistributions(new double[] { 0.6 }, model.priorDistribution(
				asunny).getValues());

		// AIMA3e pg. 496
		// General case of Bayes' Rule
		// P(Y | X) = P(X | Y)P(Y)/P(X)
		System.out.println("Y:"+model.posteriorDistribution(cavityRV, toothacheRV));
		
		System.out.println(model.posteriorDistribution(catchRV, cavityRV));
		System.out.println(model.priorDistribution(cavityRV));
		System.out.println(model.posteriorDistribution(catchRV, cavityRV)
				.multiplyBy(model.priorDistribution(cavityRV)));
		System.out.println(model.priorDistribution(catchRV));
		
		System.out.println("Y:"+model.posteriorDistribution(toothacheRV, cavityRV)
				.multiplyBy(model.priorDistribution(cavityRV)).divideBy(
						model.priorDistribution(toothacheRV)));

		// General Bayes' Rule conditionalized on background evidence e (13.3)
		// P(Y | X, e) = P(X | Y, e)P(Y|e)/P(X | e)

		Assert.fail("TODO");
	}

	// AIMA3e pg. 496
	protected void test_MeningitisStiffNeckModel_Distributions(
			FiniteProbabilityModel model, RandomVariable meningitisRV,
			RandomVariable stiffNeckRV) {

		AssignmentProposition astiffNeck = new AssignmentProposition(
				stiffNeckRV, true);

		// AIMA3e pg. 497
		// P<>(Mengingitis | stiffneck) = &alpha;<P(s | m)P(m), P(s | ~m)P(~m)>
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

		// AIMA3e pg. 523
		// P(Burglary | JohnCalls = true, MaryCalls = true) = <0.284, 0.716>

		Assert.fail("TODO");
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
