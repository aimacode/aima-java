package aima.test.core.unit.probability.proposed.model;

import org.junit.Assert;

import aima.core.probability.proposed.model.ProbabilityModel;
import aima.core.probability.proposed.model.RandomVariable;
import aima.core.probability.proposed.model.domain.FiniteIntegerDomain;
import aima.core.probability.proposed.model.proposition.ConjunctiveProposition;
import aima.core.probability.proposed.model.proposition.IntegerSumProposition;
import aima.core.probability.proposed.model.proposition.RandomVariableProposition;
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
	protected void test_RollingPairFairDiceModel(ProbabilityModel model, RandomVariable dice1RV, RandomVariable dice2RV) {
		Assert.assertTrue(model.isValid());
		
		// Ensure each dice has 1/6 probability
		for (int d = 1; d <= 6; d++) {
			AssignmentProposition ad1 = new AssignmentProposition(dice1RV, d);
			AssignmentProposition ad2 = new AssignmentProposition(dice2RV, d);
			
			Assert.assertEquals(1.0/6.0, model.prior(ad1), DELTA_THRESHOLD);
			Assert.assertEquals(1.0/6.0, model.prior(ad2), DELTA_THRESHOLD);
		}
		
		// Ensure each combination is 1/36
		for (int d1 = 1; d1 <= 6; d1++) {
			for (int d2 = 1; d2 <= 6; d2++) {
				AssignmentProposition ad1 = new AssignmentProposition(dice1RV, d1);
				AssignmentProposition ad2 = new AssignmentProposition(dice2RV, d2);
				ConjunctiveProposition d1AndD2 = new ConjunctiveProposition(ad1, ad2);
				
				Assert.assertEquals(1.0/6.0, model.prior(ad1), DELTA_THRESHOLD);
				Assert.assertEquals(1.0/6.0, model.prior(ad2), DELTA_THRESHOLD);
				
				// pg. 485 AIMA3e
				Assert.assertEquals(1.0/36.0, model.prior(d1AndD2), DELTA_THRESHOLD);
				
				Assert.assertEquals(1.0/6.0, model.posterior(ad1, ad2), DELTA_THRESHOLD);
				Assert.assertEquals(1.0/6.0, model.posterior(ad2, ad1), DELTA_THRESHOLD);
			}
		}
		
		// Test Sets of events defined via constraint propositions
		IntegerSumProposition total11 = new IntegerSumProposition("Total", new FiniteIntegerDomain(11), dice1RV, dice2RV);
		Assert.assertEquals(2.0/36.0, model.prior(total11), DELTA_THRESHOLD);
		IntegerSumProposition doubles = new IntegerSumProposition("Doubles", new FiniteIntegerDomain(2, 4, 6, 8, 10, 12), dice1RV, dice2RV);
		Assert.assertEquals(0.5, model.prior(doubles), DELTA_THRESHOLD);
		SubsetProposition evenDice1 = new SubsetProposition("EvenDice1", new FiniteIntegerDomain(2, 4, 6), dice1RV);
		Assert.assertEquals(0.5, model.prior(evenDice1), DELTA_THRESHOLD);
		SubsetProposition oddDice2 = new SubsetProposition("OddDice2", new FiniteIntegerDomain(1, 3, 5), dice2RV);
		Assert.assertEquals(0.5, model.prior(oddDice2), DELTA_THRESHOLD);
		
		// pg. 485 AIMA3e
		AssignmentProposition dice1Is5 = new AssignmentProposition(dice1RV, 5);
		Assert.assertEquals(0.5, model.posterior(doubles, dice1Is5), DELTA_THRESHOLD);
		
		RandomVariableProposition dice1 = new RandomVariableProposition(dice1RV);
		RandomVariableProposition dice2 = new RandomVariableProposition(dice2RV);
		Assert.assertEquals(1.0, model.prior(dice1), DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.prior(dice2), DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.posterior(dice1, dice2), DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.posterior(dice2, dice1), DELTA_THRESHOLD);
	}
	
	protected void test_ToothacheCavityCatchModel(ProbabilityModel model, RandomVariable toothacheRV, RandomVariable cavityRV, RandomVariable catchRV) {
		Assert.assertTrue(model.isValid());
		
		AssignmentProposition acavity = new AssignmentProposition(cavityRV, Boolean.TRUE);
		AssignmentProposition anotcavity = new AssignmentProposition(cavityRV, Boolean.FALSE);
		AssignmentProposition atoothache = new AssignmentProposition(toothacheRV, Boolean.TRUE);
		
		// AIMA3e pg. 485
		Assert.assertEquals(0.2, model.prior(acavity), DELTA_THRESHOLD);
		Assert.assertEquals(0.6, model.posterior(acavity, atoothache), DELTA_THRESHOLD);
		ConjunctiveProposition toothacheAndNotCavity = new ConjunctiveProposition(atoothache, anotcavity);
		Assert.assertEquals(0.0, model.posterior(acavity, toothacheAndNotCavity), DELTA_THRESHOLD);
		Assert.assertEquals(0.0, model.posterior(acavity, atoothache, anotcavity), DELTA_THRESHOLD);
		
		// AIMA3e pg. 493
		Assert.assertEquals(0.4, model.posterior(anotcavity, atoothache), DELTA_THRESHOLD);
		
		RandomVariableProposition ptoothache = new RandomVariableProposition(toothacheRV);
		RandomVariableProposition pcavity = new RandomVariableProposition(cavityRV);
		RandomVariableProposition pcatch = new RandomVariableProposition(catchRV);
		
		Assert.assertEquals(1.0, model.prior(ptoothache), DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.prior(pcavity), DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.prior(pcatch), DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.posterior(ptoothache, pcavity), DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.posterior(ptoothache, pcatch), DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.posterior(ptoothache, pcavity, pcatch), DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.posterior(pcavity, ptoothache), DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.posterior(pcavity, pcatch), DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.posterior(pcavity, ptoothache, pcatch), DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.posterior(pcatch, pcavity), DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.posterior(pcatch, ptoothache), DELTA_THRESHOLD);
		Assert.assertEquals(1.0, model.posterior(pcatch, pcavity, ptoothache), DELTA_THRESHOLD);
	}
	
	// AIMA3e pg. 488, 494
	protected void test_ToothacheCavityCatchWeatherModel(ProbabilityModel model, RandomVariable toothacheRV, RandomVariable cavityRV, RandomVariable catchRV, RandomVariable weatherRV) {
		Assert.fail("TODO");
	}
	
	// AIMA3e pg. 512
	protected void test_BurglaryAlarmModel(ProbabilityModel model, RandomVariable burglaryRV, RandomVariable earthQuakeRV, RandomVariable alarmRV, RandomVariable johnCallsRV, RandomVariable maryCallsRV) {
		Assert.fail("TODO");
	}
}
