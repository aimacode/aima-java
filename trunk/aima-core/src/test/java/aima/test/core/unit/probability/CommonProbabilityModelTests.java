package aima.test.core.unit.probability;

import org.junit.Assert;

import aima.core.probability.ProbabilityModel;
import aima.core.probability.domain.FiniteIntegerDomain;
import aima.core.probability.example.ExampleRV;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.proposition.ConjunctiveProposition;
import aima.core.probability.proposition.DisjunctiveProposition;
import aima.core.probability.proposition.EquivalentProposition;
import aima.core.probability.proposition.IntegerSumProposition;
import aima.core.probability.proposition.SubsetProposition;

/**
 * @author Ciaran O'Reilly
 * 
 */
public abstract class CommonProbabilityModelTests {
	public static final double DELTA_THRESHOLD = ProbabilityModel.DEFAULT_ROUNDING_THRESHOLD;

	//
	// PROTECTED METHODS
	//
	protected void test_RollingPairFairDiceModel(ProbabilityModel model) {
		Assert.assertTrue(model.isValid());

		// Ensure each dice has 1/6 probability
		for (int d = 1; d <= 6; d++) {
			AssignmentProposition ad1 = new AssignmentProposition(
					ExampleRV.DICE_1_RV, d);
			AssignmentProposition ad2 = new AssignmentProposition(
					ExampleRV.DICE_2_RV, d);

			Assert.assertEquals(1.0 / 6.0, model.prior(ad1), DELTA_THRESHOLD);
			Assert.assertEquals(1.0 / 6.0, model.prior(ad2), DELTA_THRESHOLD);
		}

		// Ensure each combination is 1/36
		for (int d1 = 1; d1 <= 6; d1++) {
			for (int d2 = 1; d2 <= 6; d2++) {
				AssignmentProposition ad1 = new AssignmentProposition(
						ExampleRV.DICE_1_RV, d1);
				AssignmentProposition ad2 = new AssignmentProposition(
						ExampleRV.DICE_2_RV, d2);
				ConjunctiveProposition d1AndD2 = new ConjunctiveProposition(
						ad1, ad2);

				Assert.assertEquals(1.0 / 6.0, model.prior(ad1),
						DELTA_THRESHOLD);
				Assert.assertEquals(1.0 / 6.0, model.prior(ad2),
						DELTA_THRESHOLD);

				// pg. 485 AIMA3e
				Assert.assertEquals(1.0 / 36.0, model.prior(ad1, ad2),
						DELTA_THRESHOLD);
				Assert.assertEquals(1.0 / 36.0, model.prior(d1AndD2),
						DELTA_THRESHOLD);

				Assert.assertEquals(1.0 / 6.0, model.posterior(ad1, ad2),
						DELTA_THRESHOLD);
				Assert.assertEquals(1.0 / 6.0, model.posterior(ad2, ad1),
						DELTA_THRESHOLD);
			}
		}

		// Test Sets of events defined via constraint propositions
		IntegerSumProposition total11 = new IntegerSumProposition("Total11",
				new FiniteIntegerDomain(11), ExampleRV.DICE_1_RV,
				ExampleRV.DICE_2_RV);
		Assert.assertEquals(2.0 / 36.0, model.prior(total11), DELTA_THRESHOLD);
		EquivalentProposition doubles = new EquivalentProposition("Doubles",
				ExampleRV.DICE_1_RV, ExampleRV.DICE_2_RV);
		Assert.assertEquals(1.0 / 6.0, model.prior(doubles), DELTA_THRESHOLD);
		SubsetProposition evenDice1 = new SubsetProposition("EvenDice1",
				new FiniteIntegerDomain(2, 4, 6), ExampleRV.DICE_1_RV);
		Assert.assertEquals(0.5, model.prior(evenDice1), DELTA_THRESHOLD);
		SubsetProposition oddDice2 = new SubsetProposition("OddDice2",
				new FiniteIntegerDomain(1, 3, 5), ExampleRV.DICE_2_RV);
		Assert.assertEquals(0.5, model.prior(oddDice2), DELTA_THRESHOLD);

		// pg. 485 AIMA3e
		AssignmentProposition dice1Is5 = new AssignmentProposition(
				ExampleRV.DICE_1_RV, 5);
		Assert.assertEquals(1.0 / 6.0, model.posterior(doubles, dice1Is5),
				DELTA_THRESHOLD);

		Assert.assertEquals(1.0, model.prior(ExampleRV.DICE_1_RV),
				DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.prior(ExampleRV.DICE_2_RV),
				DELTA_THRESHOLD);
		Assert.assertEquals(1.0,
				model.posterior(ExampleRV.DICE_1_RV, ExampleRV.DICE_2_RV),
				DELTA_THRESHOLD);
		Assert.assertEquals(1.0,
				model.posterior(ExampleRV.DICE_2_RV, ExampleRV.DICE_1_RV),
				DELTA_THRESHOLD);

		// Test a disjunctive proposition pg.489
		// P(a OR b) = P(a) + P(b) - P(a AND b)
		// = 1/6 + 1/6 - 1/36
		AssignmentProposition dice2Is5 = new AssignmentProposition(
				ExampleRV.DICE_2_RV, 5);
		DisjunctiveProposition dice1Is5OrDice2Is5 = new DisjunctiveProposition(
				dice1Is5, dice2Is5);
		Assert.assertEquals(1.0 / 6.0 + 1.0 / 6.0 - 1.0 / 36.0,
				model.prior(dice1Is5OrDice2Is5), DELTA_THRESHOLD);
	}

	protected void test_ToothacheCavityCatchModel(ProbabilityModel model) {
		Assert.assertTrue(model.isValid());

		AssignmentProposition atoothache = new AssignmentProposition(
				ExampleRV.TOOTHACHE_RV, Boolean.TRUE);
		AssignmentProposition anottoothache = new AssignmentProposition(
				ExampleRV.TOOTHACHE_RV, Boolean.FALSE);
		AssignmentProposition acavity = new AssignmentProposition(
				ExampleRV.CAVITY_RV, Boolean.TRUE);
		AssignmentProposition anotcavity = new AssignmentProposition(
				ExampleRV.CAVITY_RV, Boolean.FALSE);
		AssignmentProposition acatch = new AssignmentProposition(
				ExampleRV.CATCH_RV, Boolean.TRUE);
		AssignmentProposition anotcatch = new AssignmentProposition(
				ExampleRV.CATCH_RV, Boolean.FALSE);

		// AIMA3e pg. 485
		Assert.assertEquals(0.2, model.prior(acavity), DELTA_THRESHOLD);
		Assert.assertEquals(0.6, model.posterior(acavity, atoothache),
				DELTA_THRESHOLD);
		ConjunctiveProposition toothacheAndNotCavity = new ConjunctiveProposition(
				atoothache, anotcavity);
		Assert.assertEquals(0.0,
				model.posterior(acavity, toothacheAndNotCavity),
				DELTA_THRESHOLD);
		Assert.assertEquals(0.0,
				model.posterior(acavity, atoothache, anotcavity),
				DELTA_THRESHOLD);

		// AIMA3e pg. 492
		DisjunctiveProposition cavityOrToothache = new DisjunctiveProposition(
				acavity, atoothache);
		Assert.assertEquals(0.28, model.prior(cavityOrToothache),
				DELTA_THRESHOLD);

		// AIMA3e pg. 493
		Assert.assertEquals(0.4, model.posterior(anotcavity, atoothache),
				DELTA_THRESHOLD);

		Assert.assertEquals(1.0, model.prior(ExampleRV.TOOTHACHE_RV),
				DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.prior(ExampleRV.CAVITY_RV),
				DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.prior(ExampleRV.CATCH_RV),
				DELTA_THRESHOLD);
		Assert.assertEquals(1.0,
				model.posterior(ExampleRV.TOOTHACHE_RV, ExampleRV.CAVITY_RV),
				DELTA_THRESHOLD);
		Assert.assertEquals(1.0,
				model.posterior(ExampleRV.TOOTHACHE_RV, ExampleRV.CATCH_RV),
				DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.posterior(ExampleRV.TOOTHACHE_RV,
				ExampleRV.CAVITY_RV, ExampleRV.CATCH_RV), DELTA_THRESHOLD);
		Assert.assertEquals(1.0,
				model.posterior(ExampleRV.CAVITY_RV, ExampleRV.TOOTHACHE_RV),
				DELTA_THRESHOLD);
		Assert.assertEquals(1.0,
				model.posterior(ExampleRV.CAVITY_RV, ExampleRV.CATCH_RV),
				DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.posterior(ExampleRV.CAVITY_RV,
				ExampleRV.TOOTHACHE_RV, ExampleRV.CATCH_RV), DELTA_THRESHOLD);
		Assert.assertEquals(1.0,
				model.posterior(ExampleRV.CATCH_RV, ExampleRV.CAVITY_RV),
				DELTA_THRESHOLD);
		Assert.assertEquals(1.0,
				model.posterior(ExampleRV.CATCH_RV, ExampleRV.TOOTHACHE_RV),
				DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.posterior(ExampleRV.CATCH_RV,
				ExampleRV.CAVITY_RV, ExampleRV.TOOTHACHE_RV), DELTA_THRESHOLD);

		// AIMA3e pg. 495 - Bayes' Rule
		// P(b|a) = P(a|b)P(b)/P(a)
		Assert.assertEquals(model.posterior(acavity, atoothache),
				(model.posterior(atoothache, acavity) * model.prior(acavity))
						/ model.prior(atoothache), DELTA_THRESHOLD);
		Assert.assertEquals(
				model.posterior(acavity, anottoothache),
				(model.posterior(anottoothache, acavity) * model.prior(acavity))
						/ model.prior(anottoothache), DELTA_THRESHOLD);
		Assert.assertEquals(
				model.posterior(anotcavity, atoothache),
				(model.posterior(atoothache, anotcavity) * model
						.prior(anotcavity)) / model.prior(atoothache),
				DELTA_THRESHOLD);
		Assert.assertEquals(
				model.posterior(anotcavity, anottoothache),
				(model.posterior(anottoothache, anotcavity) * model
						.prior(anotcavity)) / model.prior(anottoothache),
				DELTA_THRESHOLD);
		//
		Assert.assertEquals(model.posterior(acavity, acatch),
				(model.posterior(acatch, acavity) * model.prior(acavity))
						/ model.prior(acatch), DELTA_THRESHOLD);
		Assert.assertEquals(model.posterior(acavity, anotcatch),
				(model.posterior(anotcatch, acavity) * model.prior(acavity))
						/ model.prior(anotcatch), DELTA_THRESHOLD);
		Assert.assertEquals(model.posterior(anotcavity, acatch),
				(model.posterior(acatch, anotcavity) * model.prior(anotcavity))
						/ model.prior(acatch), DELTA_THRESHOLD);
		Assert.assertEquals(
				model.posterior(anotcavity, anotcatch),
				(model.posterior(anotcatch, anotcavity) * model
						.prior(anotcavity)) / model.prior(anotcatch),
				DELTA_THRESHOLD);
	}

	// AIMA3e pg. 488, 494
	protected void test_ToothacheCavityCatchWeatherModel(ProbabilityModel model) {

		// Should be able to run all the same queries for this independent
		// sub model.
		test_ToothacheCavityCatchModel(model);

		// AIMA3e pg. 486
		AssignmentProposition asunny = new AssignmentProposition(
				ExampleRV.WEATHER_RV, "sunny");
		AssignmentProposition arain = new AssignmentProposition(
				ExampleRV.WEATHER_RV, "rain");
		AssignmentProposition acloudy = new AssignmentProposition(
				ExampleRV.WEATHER_RV, "cloudy");
		AssignmentProposition asnow = new AssignmentProposition(
				ExampleRV.WEATHER_RV, "snow");

		Assert.assertEquals(0.6, model.prior(asunny), DELTA_THRESHOLD);
		Assert.assertEquals(0.1, model.prior(arain), DELTA_THRESHOLD);
		Assert.assertEquals(0.29, model.prior(acloudy), DELTA_THRESHOLD);
		Assert.assertEquals(0.01, model.prior(asnow), DELTA_THRESHOLD);

		// AIMA3e pg. 488
		// P(sunny, cavity)
		// P(sunny AND cavity)
		AssignmentProposition atoothache = new AssignmentProposition(
				ExampleRV.TOOTHACHE_RV, Boolean.TRUE);
		AssignmentProposition acatch = new AssignmentProposition(
				ExampleRV.CATCH_RV, Boolean.TRUE);
		AssignmentProposition acavity = new AssignmentProposition(
				ExampleRV.CAVITY_RV, Boolean.TRUE);
		ConjunctiveProposition sunnyAndCavity = new ConjunctiveProposition(
				asunny, acavity);

		// 0.6 (sunny) * 0.2 (cavity) = 0.12
		Assert.assertEquals(0.12, model.prior(asunny, acavity), DELTA_THRESHOLD);
		Assert.assertEquals(0.12, model.prior(sunnyAndCavity), DELTA_THRESHOLD);

		// AIMA3e pg. 494
		// P(toothache, catch, cavity, cloudy) =
		// P(cloudy | toothache, catch, cavity)P(toothache, catch, cavity)
		Assert.assertEquals(
				model.prior(atoothache, acatch, acavity, acloudy),
				model.posterior(acloudy, atoothache, acatch, acavity)
						* model.prior(atoothache, acatch, acavity),
				DELTA_THRESHOLD);
		ConjunctiveProposition toothacheAndCatchAndCavityAndCloudy = new ConjunctiveProposition(
				new ConjunctiveProposition(atoothache, acatch),
				new ConjunctiveProposition(acavity, acloudy));
		ConjunctiveProposition toothacheAndCatchAndCavity = new ConjunctiveProposition(
				new ConjunctiveProposition(atoothache, acatch), acavity);
		Assert.assertEquals(
				model.prior(toothacheAndCatchAndCavityAndCloudy),
				model.posterior(acloudy, atoothache, acatch, acavity)
						* model.prior(toothacheAndCatchAndCavity),
				DELTA_THRESHOLD);

		// P(cloudy | toothache, catch, cavity) = P(cloudy)
		// (13.10)
		Assert.assertEquals(
				model.posterior(acloudy, atoothache, acatch, acavity),
				model.prior(acloudy), DELTA_THRESHOLD);

		// P(toothache, catch, cavity, cloudy) =
		// P(cloudy)P(tootache, catch, cavity)
		Assert.assertEquals(
				model.prior(atoothache, acatch, acavity, acloudy),
				model.prior(acloudy) * model.prior(atoothache, acatch, acavity),
				DELTA_THRESHOLD);

		// P(a | b) = P(a)
		Assert.assertEquals(model.posterior(acavity, acloudy),
				model.prior(acavity), DELTA_THRESHOLD);
		// P(b | a) = P(b)
		Assert.assertEquals(model.posterior(acloudy, acavity),
				model.prior(acloudy), DELTA_THRESHOLD);
		// P(a AND b) = P(a)P(b)
		Assert.assertEquals(model.prior(acavity, acloudy), model.prior(acavity)
				* model.prior(acloudy), DELTA_THRESHOLD);
		ConjunctiveProposition acavityAndacloudy = new ConjunctiveProposition(
				acavity, acloudy);
		Assert.assertEquals(model.prior(acavityAndacloudy),
				model.prior(acavity) * model.prior(acloudy), DELTA_THRESHOLD);
	}

	// AIMA3e pg. 496
	protected void test_MeningitisStiffNeckModel(ProbabilityModel model) {

		Assert.assertTrue(model.isValid());

		AssignmentProposition ameningitis = new AssignmentProposition(
				ExampleRV.MENINGITIS_RV, true);
		AssignmentProposition anotmeningitis = new AssignmentProposition(
				ExampleRV.MENINGITIS_RV, false);
		AssignmentProposition astiffNeck = new AssignmentProposition(
				ExampleRV.STIFF_NECK_RV, true);
		AssignmentProposition anotstiffNeck = new AssignmentProposition(
				ExampleRV.STIFF_NECK_RV, false);

		// P(stiffNeck | meningitis) = 0.7
		Assert.assertEquals(0.7, model.posterior(astiffNeck, ameningitis),
				DELTA_THRESHOLD);
		// P(meningitis) = 1/50000
		Assert.assertEquals(0.00002, model.prior(ameningitis), DELTA_THRESHOLD);
		// P(~meningitis) = 1-1/50000
		Assert.assertEquals(0.99998, model.prior(anotmeningitis),
				DELTA_THRESHOLD);
		// P(stiffNeck) = 0.01
		Assert.assertEquals(0.01, model.prior(astiffNeck), DELTA_THRESHOLD);
		// P(~stiffNeck) = 0.99
		Assert.assertEquals(0.99, model.prior(anotstiffNeck), DELTA_THRESHOLD);
		// P(meningitis | stiffneck)
		// = P(stiffneck | meningitis)P(meningitis)/P(stiffneck)
		// = (0.7 * 0.00002)/0.01
		// = 0.0014 (13.4)
		Assert.assertEquals(0.0014, model.posterior(ameningitis, astiffNeck),
				DELTA_THRESHOLD);

		// Assuming P(~stiffneck | meningitis) = 0.3 (pg. 497), i.e. CPT (row
		// must = 1)
		//
		// P(meningitis | ~stiffneck)
		// = P(~stiffneck | meningitis)P(meningitis)/P(~stiffneck)
		// = (0.3 * 0.00002)/0.99
		// = 0.000006060606
		Assert.assertEquals(0.000006060606,
				model.posterior(ameningitis, anotstiffNeck), DELTA_THRESHOLD);
	}

	// AIMA3e pg. 512
	protected void test_BurglaryAlarmModel(ProbabilityModel model) {
		Assert.assertTrue(model.isValid());

		AssignmentProposition aburglary = new AssignmentProposition(
				ExampleRV.BURGLARY_RV, Boolean.TRUE);
		AssignmentProposition anotburglary = new AssignmentProposition(
				ExampleRV.BURGLARY_RV, Boolean.FALSE);
		AssignmentProposition anotearthquake = new AssignmentProposition(
				ExampleRV.EARTHQUAKE_RV, Boolean.FALSE);
		AssignmentProposition aalarm = new AssignmentProposition(
				ExampleRV.ALARM_RV, Boolean.TRUE);
		AssignmentProposition anotalarm = new AssignmentProposition(
				ExampleRV.ALARM_RV, Boolean.FALSE);
		AssignmentProposition ajohnCalls = new AssignmentProposition(
				ExampleRV.JOHN_CALLS_RV, Boolean.TRUE);
		AssignmentProposition amaryCalls = new AssignmentProposition(
				ExampleRV.MARY_CALLS_RV, Boolean.TRUE);

		// AIMA3e pg. 514
		Assert.assertEquals(0.00062811126, model.prior(ajohnCalls, amaryCalls,
				aalarm, anotburglary, anotearthquake), DELTA_THRESHOLD);
		Assert.assertEquals(0.00049800249, model.prior(ajohnCalls, amaryCalls,
				anotalarm, anotburglary, anotearthquake), DELTA_THRESHOLD);

		// AIMA3e pg. 524
		// P(Burglary = true | JohnCalls = true, MaryCalls = true) = 0.00059224
		Assert.assertEquals(0.00059224,
				model.prior(aburglary, ajohnCalls, amaryCalls), DELTA_THRESHOLD);
		// P(Burglary = false | JohnCalls = true, MaryCalls = true) = 0.0014919
		Assert.assertEquals(0.00149185764899,
				model.prior(anotburglary, ajohnCalls, amaryCalls),
				DELTA_THRESHOLD);
	}
}
