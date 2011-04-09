package aima.test.core.unit.probability.proposed.model;

import org.junit.Assert;

import aima.core.probability.proposed.model.ProbabilityModel;
import aima.core.probability.proposed.model.RandomVariable;
import aima.core.probability.proposed.model.domain.FiniteIntegerDomain;
import aima.core.probability.proposed.model.proposition.ConjunctiveProposition;
import aima.core.probability.proposed.model.proposition.DisjunctiveProposition;
import aima.core.probability.proposed.model.proposition.EquivalentProposition;
import aima.core.probability.proposed.model.proposition.IntegerSumProposition;
import aima.core.probability.proposed.model.proposition.AssignmentProposition;
import aima.core.probability.proposed.model.proposition.SubsetProposition;

/**
 * @author Ciaran O'Reilly
 * 
 */
public abstract class CommonProbabilityModelTests {
	public static final double DELTA_THRESHOLD = ProbabilityModel.DEFAULT_ROUNDING_THRESHOLD;

	//
	// PROTECTED METHODS
	//
	protected void test_RollingPairFairDiceModel(ProbabilityModel model,
			RandomVariable dice1RV, RandomVariable dice2RV) {
		Assert.assertTrue(model.isValid());

		// Ensure each dice has 1/6 probability
		for (int d = 1; d <= 6; d++) {
			AssignmentProposition ad1 = new AssignmentProposition(dice1RV, d);
			AssignmentProposition ad2 = new AssignmentProposition(dice2RV, d);

			Assert.assertEquals(1.0 / 6.0, model.prior(ad1), DELTA_THRESHOLD);
			Assert.assertEquals(1.0 / 6.0, model.prior(ad2), DELTA_THRESHOLD);
		}

		// Ensure each combination is 1/36
		for (int d1 = 1; d1 <= 6; d1++) {
			for (int d2 = 1; d2 <= 6; d2++) {
				AssignmentProposition ad1 = new AssignmentProposition(dice1RV,
						d1);
				AssignmentProposition ad2 = new AssignmentProposition(dice2RV,
						d2);
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
				new FiniteIntegerDomain(11), dice1RV, dice2RV);
		Assert.assertEquals(2.0 / 36.0, model.prior(total11), DELTA_THRESHOLD);
		EquivalentProposition doubles = new EquivalentProposition("Doubles",
				dice1RV, dice2RV);
		Assert.assertEquals(1.0 / 6.0, model.prior(doubles), DELTA_THRESHOLD);
		SubsetProposition evenDice1 = new SubsetProposition("EvenDice1",
				new FiniteIntegerDomain(2, 4, 6), dice1RV);
		Assert.assertEquals(0.5, model.prior(evenDice1), DELTA_THRESHOLD);
		SubsetProposition oddDice2 = new SubsetProposition("OddDice2",
				new FiniteIntegerDomain(1, 3, 5), dice2RV);
		Assert.assertEquals(0.5, model.prior(oddDice2), DELTA_THRESHOLD);

		// pg. 485 AIMA3e
		AssignmentProposition dice1Is5 = new AssignmentProposition(dice1RV, 5);
		Assert.assertEquals(1.0 / 6.0, model.posterior(doubles, dice1Is5),
				DELTA_THRESHOLD);

		Assert.assertEquals(1.0, model.prior(dice1RV), DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.prior(dice2RV), DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.posterior(dice1RV, dice2RV),
				DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.posterior(dice2RV, dice1RV),
				DELTA_THRESHOLD);

		// Test a disjunctive proposition pg.489
		// P(a OR b) = P(a) + P(b) - P(a AND b)
		// = 1/6 + 1/6 - 1/36
		AssignmentProposition dice2Is5 = new AssignmentProposition(dice2RV, 5);
		DisjunctiveProposition dice1Is5OrDice2Is5 = new DisjunctiveProposition(
				dice1Is5, dice2Is5);
		Assert.assertEquals(1.0 / 6.0 + 1.0 / 6.0 - 1.0 / 36.0,
				model.prior(dice1Is5OrDice2Is5), DELTA_THRESHOLD);
	}

	protected void test_ToothacheCavityCatchModel(ProbabilityModel model,
			RandomVariable toothacheRV, RandomVariable cavityRV,
			RandomVariable catchRV) {
		Assert.assertTrue(model.isValid());

		AssignmentProposition atoothache = new AssignmentProposition(
				toothacheRV, Boolean.TRUE);
		AssignmentProposition anottoothache = new AssignmentProposition(
				toothacheRV, Boolean.FALSE);
		AssignmentProposition acavity = new AssignmentProposition(cavityRV,
				Boolean.TRUE);
		AssignmentProposition anotcavity = new AssignmentProposition(cavityRV,
				Boolean.FALSE);
		AssignmentProposition acatch = new AssignmentProposition(catchRV,
				Boolean.TRUE);
		AssignmentProposition anotcatch = new AssignmentProposition(catchRV,
				Boolean.FALSE);

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

		Assert.assertEquals(1.0, model.prior(toothacheRV), DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.prior(cavityRV), DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.prior(catchRV), DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.posterior(toothacheRV, cavityRV),
				DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.posterior(toothacheRV, catchRV),
				DELTA_THRESHOLD);
		Assert.assertEquals(1.0,
				model.posterior(toothacheRV, cavityRV, catchRV),
				DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.posterior(cavityRV, toothacheRV),
				DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.posterior(cavityRV, catchRV),
				DELTA_THRESHOLD);
		Assert.assertEquals(1.0,
				model.posterior(cavityRV, toothacheRV, catchRV),
				DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.posterior(catchRV, cavityRV),
				DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.posterior(catchRV, toothacheRV),
				DELTA_THRESHOLD);
		Assert.assertEquals(1.0,
				model.posterior(catchRV, cavityRV, toothacheRV),
				DELTA_THRESHOLD);

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
	protected void test_ToothacheCavityCatchWeatherModel(
			ProbabilityModel model, RandomVariable toothacheRV,
			RandomVariable cavityRV, RandomVariable catchRV,
			RandomVariable weatherRV) {

		// Should be able to run all the same queries for this independent
		// sub model.
		test_ToothacheCavityCatchModel(model, toothacheRV, cavityRV, catchRV);

		// AIMA3e pg. 486
		AssignmentProposition asunny = new AssignmentProposition(weatherRV,
				"sunny");
		AssignmentProposition arain = new AssignmentProposition(weatherRV,
				"rain");
		AssignmentProposition acloudy = new AssignmentProposition(weatherRV,
				"cloudy");
		AssignmentProposition asnow = new AssignmentProposition(weatherRV,
				"snow");

		Assert.assertEquals(0.6, model.prior(asunny), DELTA_THRESHOLD);
		Assert.assertEquals(0.1, model.prior(arain), DELTA_THRESHOLD);
		Assert.assertEquals(0.29, model.prior(acloudy), DELTA_THRESHOLD);
		Assert.assertEquals(0.01, model.prior(asnow), DELTA_THRESHOLD);

		// AIMA3e pg. 488
		// P(sunny, cavity)
		// P(sunny AND cavity)
		AssignmentProposition atoothache = new AssignmentProposition(
				toothacheRV, Boolean.TRUE);
		AssignmentProposition acatch = new AssignmentProposition(catchRV,
				Boolean.TRUE);
		AssignmentProposition acavity = new AssignmentProposition(cavityRV,
				Boolean.TRUE);
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
	protected void test_MeningitisStiffNeckModel(ProbabilityModel model,
			RandomVariable meningitisRV, RandomVariable stiffNeckRV) {

		Assert.assertTrue(model.isValid());

		AssignmentProposition ameningitis = new AssignmentProposition(
				meningitisRV, true);
		AssignmentProposition anotmeningitis = new AssignmentProposition(
				meningitisRV, false);
		AssignmentProposition astiffNeck = new AssignmentProposition(
				stiffNeckRV, true);
		AssignmentProposition anotstiffNeck = new AssignmentProposition(
				stiffNeckRV, false);

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
	protected void test_BurglaryAlarmModel(ProbabilityModel model,
			RandomVariable burglaryRV, RandomVariable earthQuakeRV,
			RandomVariable alarmRV, RandomVariable johnCallsRV,
			RandomVariable maryCallsRV) {
		Assert.assertTrue(model.isValid());
		Assert.fail("TODO");
	}
}
