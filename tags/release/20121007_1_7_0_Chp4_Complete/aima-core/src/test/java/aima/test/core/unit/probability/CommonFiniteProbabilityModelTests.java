package aima.test.core.unit.probability;

import org.junit.Assert;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.FiniteProbabilityModel;
import aima.core.probability.domain.FiniteIntegerDomain;
import aima.core.probability.example.ExampleRV;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.proposition.ConjunctiveProposition;
import aima.core.probability.proposition.EquivalentProposition;
import aima.core.probability.proposition.IntegerSumProposition;

public abstract class CommonFiniteProbabilityModelTests extends
		CommonProbabilityModelTests {

	//
	// PROTECTED
	//
	protected void test_RollingPairFairDiceModel_Distributions(
			FiniteProbabilityModel model) {

		AssignmentProposition ad1_1 = new AssignmentProposition(
				ExampleRV.DICE_1_RV, 1);
		CategoricalDistribution dD1_1 = model.priorDistribution(ad1_1);
		Assert.assertArrayEquals(new double[] { 1.0 / 6.0 }, dD1_1.getValues(),
				DELTA_THRESHOLD);

		CategoricalDistribution dPriorDice1 = model
				.priorDistribution(ExampleRV.DICE_1_RV);
		Assert.assertArrayEquals(new double[] { 1.0 / 6.0, 1.0 / 6.0,
				1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0 },
				dPriorDice1.getValues(), DELTA_THRESHOLD);

		CategoricalDistribution dPriorDice2 = model
				.priorDistribution(ExampleRV.DICE_2_RV);
		Assert.assertArrayEquals(new double[] { 1.0 / 6.0, 1.0 / 6.0,
				1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0 },
				dPriorDice2.getValues(), DELTA_THRESHOLD);

		CategoricalDistribution dJointDice1Dice2 = model.jointDistribution(
				ExampleRV.DICE_1_RV, ExampleRV.DICE_2_RV);
		Assert.assertEquals(36, dJointDice1Dice2.getValues().length);
		for (int i = 0; i < dJointDice1Dice2.getValues().length; i++) {
			Assert.assertEquals(1.0 / 36.0, dJointDice1Dice2.getValues()[i],
					DELTA_THRESHOLD);
		}

		CategoricalDistribution dJointDice2Dice1 = model.jointDistribution(
				ExampleRV.DICE_2_RV, ExampleRV.DICE_1_RV);
		Assert.assertEquals(36, dJointDice2Dice1.getValues().length);
		for (int i = 0; i < dJointDice2Dice1.getValues().length; i++) {
			Assert.assertEquals(1.0 / 36.0, dJointDice2Dice1.getValues()[i],
					DELTA_THRESHOLD);
		}

		//
		// Test Sets of events
		IntegerSumProposition total11 = new IntegerSumProposition("Total",
				new FiniteIntegerDomain(11), ExampleRV.DICE_1_RV,
				ExampleRV.DICE_2_RV);
		// P<>(Total = 11) = <2.0/36.0>
		Assert.assertArrayEquals(new double[] { 2.0 / 36.0 }, model
				.priorDistribution(total11).getValues(), DELTA_THRESHOLD);

		// P<>(Dice1, Total = 11)
		// = <0.0, 0.0, 0.0, 0.0, 1.0/36.0, 1.0/36.0>
		Assert.assertArrayEquals(new double[] { 0, 0, 0, 0, 1.0 / 36.0,
				1.0 / 36.0 },
				model.priorDistribution(ExampleRV.DICE_1_RV, total11)
						.getValues(), DELTA_THRESHOLD);

		EquivalentProposition doubles = new EquivalentProposition("Doubles",
				ExampleRV.DICE_1_RV, ExampleRV.DICE_2_RV);
		// P(Doubles) = <1.0/6.0>
		Assert.assertArrayEquals(new double[] { 1.0 / 6.0 }, model
				.priorDistribution(doubles).getValues(), DELTA_THRESHOLD);

		//
		// Test posterior
		//
		// P<>(Dice1, Total = 11)
		// = <0.0, 0.0, 0.0, 0.0, 0.5, 0.5>
		Assert.assertArrayEquals(new double[] { 0, 0, 0, 0, 0.5, 0.5 }, model
				.posteriorDistribution(ExampleRV.DICE_1_RV, total11)
				.getValues(), DELTA_THRESHOLD);

		// P<>(Dice1 | Doubles) = <1/6, 1/6, 1/6, 1/6, 1/6, 1/6>
		Assert.assertArrayEquals(new double[] { 1.0 / 6.0, 1.0 / 6.0,
				1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0 }, model
				.posteriorDistribution(ExampleRV.DICE_1_RV, doubles)
				.getValues(), DELTA_THRESHOLD);

		CategoricalDistribution dPosteriorDice1GivenDice2 = model
				.posteriorDistribution(ExampleRV.DICE_1_RV, ExampleRV.DICE_2_RV);
		Assert.assertEquals(36, dPosteriorDice1GivenDice2.getValues().length);
		for (int i = 0; i < dPosteriorDice1GivenDice2.getValues().length; i++) {
			Assert.assertEquals(1.0 / 6.0,
					dPosteriorDice1GivenDice2.getValues()[i], DELTA_THRESHOLD);
		}

		CategoricalDistribution dPosteriorDice2GivenDice1 = model
				.posteriorDistribution(ExampleRV.DICE_2_RV, ExampleRV.DICE_1_RV);
		Assert.assertEquals(36, dPosteriorDice2GivenDice1.getValues().length);
		for (int i = 0; i < dPosteriorDice2GivenDice1.getValues().length; i++) {
			Assert.assertEquals(1.0 / 6.0,
					dPosteriorDice2GivenDice1.getValues()[i], DELTA_THRESHOLD);
		}
	}

	protected void test_ToothacheCavityCatchModel_Distributions(
			FiniteProbabilityModel model) {

		AssignmentProposition atoothache = new AssignmentProposition(
				ExampleRV.TOOTHACHE_RV, Boolean.TRUE);
		AssignmentProposition anottoothache = new AssignmentProposition(
				ExampleRV.TOOTHACHE_RV, Boolean.FALSE);
		AssignmentProposition acatch = new AssignmentProposition(
				ExampleRV.CATCH_RV, Boolean.TRUE);
		AssignmentProposition anotcatch = new AssignmentProposition(
				ExampleRV.CATCH_RV, Boolean.FALSE);

		// AIMA3e pg. 493
		// P<>(Cavity | toothache) = <0.6, 0.4>
		Assert.assertArrayEquals(new double[] { 0.6, 0.4 }, model
				.posteriorDistribution(ExampleRV.CAVITY_RV, atoothache)
				.getValues(), DELTA_THRESHOLD);

		// AIMA3e pg. 497
		// P<>(Cavity | toothache AND catch) = <0.871, 0.129>
		Assert.assertArrayEquals(
				new double[] { 0.8709677419354839, 0.12903225806451615 },
				model.posteriorDistribution(ExampleRV.CAVITY_RV, atoothache,
						acatch).getValues(), DELTA_THRESHOLD);

		// AIMA3e pg. 498
		// (13.17)
		// P<>(toothache AND catch | Cavity)
		// = P<>(toothache | Cavity)P<>(catch | Cavity)
		ConjunctiveProposition toothacheAndCatch = new ConjunctiveProposition(
				atoothache, acatch);
		Assert.assertArrayEquals(
				model.posteriorDistribution(toothacheAndCatch,
						ExampleRV.CAVITY_RV).getValues(),
				model.posteriorDistribution(atoothache, ExampleRV.CAVITY_RV)
						.multiplyBy(
								model.posteriorDistribution(acatch,
										ExampleRV.CAVITY_RV)).getValues(),
				DELTA_THRESHOLD);

		// (13.18)
		// P<>(Cavity | toothache AND catch)
		// = &alpha;P<>(toothache | Cavity)P<>(catch | Cavity)P(Cavity)
		Assert.assertArrayEquals(
				model.posteriorDistribution(ExampleRV.CAVITY_RV,
						toothacheAndCatch).getValues(),
				model.posteriorDistribution(atoothache, ExampleRV.CAVITY_RV)
						.multiplyBy(
								model.posteriorDistribution(acatch,
										ExampleRV.CAVITY_RV))
						.multiplyBy(
								model.priorDistribution(ExampleRV.CAVITY_RV))
						.normalize().getValues(), DELTA_THRESHOLD);

		// (13.19)
		// P<>(Toothache, Catch | Cavity)
		// = P<>(Toothache | Cavity)P<>(Catch | Cavity)
		ConjunctiveProposition toothacheAndCatchRV = new ConjunctiveProposition(
				ExampleRV.TOOTHACHE_RV, ExampleRV.CATCH_RV);
		Assert.assertArrayEquals(
				model.posteriorDistribution(toothacheAndCatchRV,
						ExampleRV.CAVITY_RV).getValues(),
				model.posteriorDistribution(ExampleRV.TOOTHACHE_RV,
						ExampleRV.CAVITY_RV)
						.multiplyByPOS(
								model.posteriorDistribution(ExampleRV.CATCH_RV,
										ExampleRV.CAVITY_RV),
								ExampleRV.TOOTHACHE_RV, ExampleRV.CATCH_RV,
								ExampleRV.CAVITY_RV).getValues(),
				DELTA_THRESHOLD);

		// (product rule)
		// P<>(Toothache, Catch, Cavity)
		// = P<>(Toothache, Catch | Cavity)P<>(Cavity)
		Assert.assertArrayEquals(
				model.priorDistribution(ExampleRV.TOOTHACHE_RV,
						ExampleRV.CATCH_RV, ExampleRV.CAVITY_RV).getValues(),
				model.posteriorDistribution(toothacheAndCatchRV,
						ExampleRV.CAVITY_RV)
						.multiplyBy(
								model.priorDistribution(ExampleRV.CAVITY_RV))
						.getValues(), DELTA_THRESHOLD);

		// (using 13.19)
		// P<>(Toothache, Catch | Cavity)P<>(Cavity)
		// = P<>(Toothache | Cavity)P<>(Catch | Cavity)P<>(Cavity)
		Assert.assertArrayEquals(
				model.posteriorDistribution(toothacheAndCatchRV,
						ExampleRV.CAVITY_RV)
						.multiplyBy(
								model.priorDistribution(ExampleRV.CAVITY_RV))
						.getValues(),
				model.posteriorDistribution(ExampleRV.TOOTHACHE_RV,
						ExampleRV.CAVITY_RV)
						.multiplyByPOS(
								model.posteriorDistribution(ExampleRV.CATCH_RV,
										ExampleRV.CAVITY_RV)
										.multiplyBy(
												model.priorDistribution(ExampleRV.CAVITY_RV)),
								ExampleRV.TOOTHACHE_RV, ExampleRV.CATCH_RV,
								ExampleRV.CAVITY_RV).getValues(),
				DELTA_THRESHOLD);
		//
		// P<>(Toothache, Catch, Cavity)
		// = P<>(Toothache | Cavity)P<>(Catch | Cavity)P<>(Cavity)
		Assert.assertArrayEquals(
				model.priorDistribution(ExampleRV.TOOTHACHE_RV,
						ExampleRV.CATCH_RV, ExampleRV.CAVITY_RV).getValues(),
				model.posteriorDistribution(ExampleRV.TOOTHACHE_RV,
						ExampleRV.CAVITY_RV)
						.multiplyByPOS(
								model.posteriorDistribution(ExampleRV.CATCH_RV,
										ExampleRV.CAVITY_RV),
								ExampleRV.TOOTHACHE_RV, ExampleRV.CATCH_RV,
								ExampleRV.CAVITY_RV)
						.multiplyBy(
								model.priorDistribution(ExampleRV.CAVITY_RV))
						.getValues(), DELTA_THRESHOLD);

		// AIMA3e pg. 496
		// General case of Bayes' Rule
		// P<>(Y | X) = P<>(X | Y)P<>(Y)/P<>(X)
		// Note: Performing in this order -
		// P<>(Y | X) = (P<>(Y)P<>(X | Y))/P<>(X)
		// as default multiplication of distributions are not commutative (could
		// also use pointwiseProductPOS() to specify the order).
		Assert.assertArrayEquals(
				model.posteriorDistribution(ExampleRV.CAVITY_RV,
						ExampleRV.TOOTHACHE_RV).getValues(),
				model.priorDistribution(ExampleRV.CAVITY_RV)
						.multiplyBy(
								model.posteriorDistribution(
										ExampleRV.TOOTHACHE_RV,
										ExampleRV.CAVITY_RV))
						.divideBy(
								model.priorDistribution(ExampleRV.TOOTHACHE_RV))
						.getValues(), DELTA_THRESHOLD);

		Assert.assertArrayEquals(
				model.posteriorDistribution(ExampleRV.CAVITY_RV,
						ExampleRV.CATCH_RV).getValues(),
				model.priorDistribution(ExampleRV.CAVITY_RV)
						.multiplyBy(
								model.posteriorDistribution(ExampleRV.CATCH_RV,
										ExampleRV.CAVITY_RV))
						.divideBy(model.priorDistribution(ExampleRV.CATCH_RV))
						.getValues(), DELTA_THRESHOLD);

		// General Bayes' Rule conditionalized on background evidence e (13.3)
		// P<>(Y | X, e) = P<>(X | Y, e)P<>(Y|e)/P<>(X | e)
		// Note: Performing in this order -
		// P<>(Y | X, e) = (P<>(Y|e)P<>(X | Y, e)))/P<>(X | e)
		// as default multiplication of distributions are not commutative (could
		// also use pointwiseProductPOS() to specify the order).
		Assert.assertArrayEquals(
				model.posteriorDistribution(ExampleRV.CAVITY_RV,
						ExampleRV.TOOTHACHE_RV, acatch).getValues(),
				model.posteriorDistribution(ExampleRV.CAVITY_RV, acatch)
						.multiplyBy(
								model.posteriorDistribution(
										ExampleRV.TOOTHACHE_RV,
										ExampleRV.CAVITY_RV, acatch))
						.divideBy(
								model.posteriorDistribution(
										ExampleRV.TOOTHACHE_RV, acatch))
						.getValues(), DELTA_THRESHOLD);
		//
		Assert.assertArrayEquals(
				model.posteriorDistribution(ExampleRV.CAVITY_RV,
						ExampleRV.TOOTHACHE_RV, anotcatch).getValues(),
				model.posteriorDistribution(ExampleRV.CAVITY_RV, anotcatch)
						.multiplyBy(
								model.posteriorDistribution(
										ExampleRV.TOOTHACHE_RV,
										ExampleRV.CAVITY_RV, anotcatch))
						.divideBy(
								model.posteriorDistribution(
										ExampleRV.TOOTHACHE_RV, anotcatch))
						.getValues(), DELTA_THRESHOLD);
		//
		Assert.assertArrayEquals(
				model.posteriorDistribution(ExampleRV.CAVITY_RV,
						ExampleRV.CATCH_RV, atoothache).getValues(),
				model.posteriorDistribution(ExampleRV.CAVITY_RV, atoothache)
						.multiplyBy(
								model.posteriorDistribution(ExampleRV.CATCH_RV,
										ExampleRV.CAVITY_RV, atoothache))
						.divideBy(
								model.posteriorDistribution(ExampleRV.CATCH_RV,
										atoothache)).getValues(),
				DELTA_THRESHOLD);

		Assert.assertArrayEquals(
				model.posteriorDistribution(ExampleRV.CAVITY_RV,
						ExampleRV.CATCH_RV, anottoothache).getValues(),
				model.posteriorDistribution(ExampleRV.CAVITY_RV, anottoothache)
						.multiplyBy(
								model.posteriorDistribution(ExampleRV.CATCH_RV,
										ExampleRV.CAVITY_RV, anottoothache))
						.divideBy(
								model.posteriorDistribution(ExampleRV.CATCH_RV,
										anottoothache)).getValues(),
				DELTA_THRESHOLD);
	}

	protected void test_ToothacheCavityCatchWeatherModel_Distributions(
			FiniteProbabilityModel model) {

		AssignmentProposition asunny = new AssignmentProposition(
				ExampleRV.WEATHER_RV, "sunny");
		AssignmentProposition acavity = new AssignmentProposition(
				ExampleRV.CAVITY_RV, Boolean.TRUE);

		// Should be able to run all the same queries for this independent
		// sub model.
		test_ToothacheCavityCatchModel_Distributions(model);

		// AIMA3e pg. 487
		// P(sunny, Cavity)
		// Would be a two-element vector giving the probabilities of a sunny day
		// with a cavity and a sunny day with no cavity.
		Assert.assertArrayEquals(new double[] { 0.12, 0.48 }, model
				.priorDistribution(asunny, ExampleRV.CAVITY_RV).getValues(),
				DELTA_THRESHOLD);

		// AIMA3e pg. 488 (i.e. one element Vector returned)
		// P(sunny, cavity)
		Assert.assertArrayEquals(new double[] { 0.12 }, model
				.priorDistribution(asunny, acavity).getValues(),
				DELTA_THRESHOLD);
		// P(sunny AND cavity)
		Assert.assertArrayEquals(new double[] { 0.12 }, model
				.priorDistribution(new ConjunctiveProposition(asunny, acavity))
				.getValues(), DELTA_THRESHOLD);
		// P(sunny) = <0.6>
		Assert.assertArrayEquals(new double[] { 0.6 },
				model.priorDistribution(asunny).getValues(), DELTA_THRESHOLD);
	}

	// AIMA3e pg. 496
	protected void test_MeningitisStiffNeckModel_Distributions(
			FiniteProbabilityModel model) {

		AssignmentProposition astiffNeck = new AssignmentProposition(
				ExampleRV.STIFF_NECK_RV, true);

		// AIMA3e pg. 497
		// P<>(Mengingitis | stiffneck) = &alpha;<P(s | m)P(m), P(s | ~m)P(~m)>
		CategoricalDistribution dMeningitisGivenStiffNeck = model
				.posteriorDistribution(ExampleRV.MENINGITIS_RV, astiffNeck);
		Assert.assertEquals(2, dMeningitisGivenStiffNeck.getValues().length);
		Assert.assertEquals(0.0014, dMeningitisGivenStiffNeck.getValues()[0],
				DELTA_THRESHOLD);
		Assert.assertEquals(0.9986, dMeningitisGivenStiffNeck.getValues()[1],
				DELTA_THRESHOLD);
	}

	protected void test_BurglaryAlarmModel_Distributions(
			FiniteProbabilityModel model) {

		AssignmentProposition aburglary = new AssignmentProposition(
				ExampleRV.BURGLARY_RV, Boolean.TRUE);
		AssignmentProposition anotburglary = new AssignmentProposition(
				ExampleRV.BURGLARY_RV, Boolean.FALSE);
		AssignmentProposition anotearthquake = new AssignmentProposition(
				ExampleRV.EARTHQUAKE_RV, Boolean.FALSE);
		AssignmentProposition ajohnCalls = new AssignmentProposition(
				ExampleRV.JOHN_CALLS_RV, Boolean.TRUE);
		AssignmentProposition amaryCalls = new AssignmentProposition(
				ExampleRV.MARY_CALLS_RV, Boolean.TRUE);

		// AIMA3e. pg. 514
		// P<>(Alarm | JohnCalls = true, MaryCalls = true, Burglary = false,
		// Earthquake = false)
		// = <0.558, 0.442>
		Assert.assertArrayEquals(
				new double[] { 0.5577689243027888, 0.44223107569721115 },
				model.posteriorDistribution(ExampleRV.ALARM_RV, ajohnCalls,
						amaryCalls, anotburglary, anotearthquake).getValues(),
				DELTA_THRESHOLD);

		// AIMA3e pg. 523
		// P<>(Burglary | JohnCalls = true, MaryCalls = true) = <0.284, 0.716>
		Assert.assertArrayEquals(
				new double[] { 0.2841718353643929, 0.7158281646356071 },
				model.posteriorDistribution(ExampleRV.BURGLARY_RV, ajohnCalls,
						amaryCalls).getValues(), DELTA_THRESHOLD);

		// AIMA3e pg. 528
		// P<>(JohnCalls | Burglary = true)
		Assert.assertArrayEquals(new double[] { 0.8490169999999999,
				0.15098299999999998 },
				model.posteriorDistribution(ExampleRV.JOHN_CALLS_RV, aburglary)
						.getValues(), DELTA_THRESHOLD);
	}
}
