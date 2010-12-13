package aima.test.core.unit.probability.proposed.model;

import org.junit.Assert;

import aima.core.probability.proposed.model.ProbabilityModel;
import aima.core.probability.proposed.model.RandomVariable;
import aima.core.probability.proposed.model.domain.FiniteIntegerDomain;
import aima.core.probability.proposed.model.proposition.ConjunctiveProposition;
import aima.core.probability.proposed.model.proposition.IntegerSumProposition;
import aima.core.probability.proposed.model.proposition.SingleAssignmentProposition;

/**
 * @author Ciaran O'Reilly
 * 
 */
public abstract class CommonProbabilityModelTests {
	public static final double DELTA_THRESHOLD = 0.00000001;
	
	//
	// PROTECTED METHODS
	//
	protected void test_RollingPairFairDiceModel(ProbabilityModel model, RandomVariable dice1RV, RandomVariable dice2RV) {
		Assert.assertTrue(model.isValid());
		
		// Ensure each dice has 1/6 probability
		for (int d = 1; d <= 6; d++) {
			SingleAssignmentProposition ad1 = new SingleAssignmentProposition(dice1RV, d);
			SingleAssignmentProposition ad2 = new SingleAssignmentProposition(dice2RV, d);
			
			Assert.assertEquals(1.0/36.0, model.prior(ad1), DELTA_THRESHOLD);
			Assert.assertEquals(1.0/36.0, model.prior(ad2), DELTA_THRESHOLD);
		}
		
		// Ensure each combination is 1/36
		for (int d1 = 1; d1 <= 6; d1++) {
			for (int d2 = 1; d2 <= 6; d2++) {
				SingleAssignmentProposition ad1 = new SingleAssignmentProposition(dice1RV, d1);
				SingleAssignmentProposition ad2 = new SingleAssignmentProposition(dice2RV, d2);
				ConjunctiveProposition d1AndD2 = new ConjunctiveProposition(ad1, ad2);
				
				Assert.assertEquals(1.0/6.0, model.prior(ad1), DELTA_THRESHOLD);
				Assert.assertEquals(1.0/6.0, model.prior(ad2), DELTA_THRESHOLD);
				
				// pg. 485 AIMA3e
				Assert.assertEquals(1.0/36.0, model.prior(d1AndD2), DELTA_THRESHOLD);
				
				Assert.assertEquals(1.0/6.0, model.posterior(ad1, ad2), DELTA_THRESHOLD);
				Assert.assertEquals(1.0/6.0, model.posterior(ad2, ad1), DELTA_THRESHOLD);
			}
		}
		
		// Test Sets of events
		IntegerSumProposition total11 = new IntegerSumProposition("Total", new FiniteIntegerDomain(11), dice1RV, dice2RV);
		Assert.assertEquals(2.0/36.0, model.prior(total11), DELTA_THRESHOLD);
		IntegerSumProposition doubles = new IntegerSumProposition("Doubles", new FiniteIntegerDomain(2, 4, 6, 8, 10, 12), dice1RV, dice2RV);
		Assert.assertEquals(0.5, model.prior(doubles), DELTA_THRESHOLD);
		
		// pg. 485 AIMA3e
		SingleAssignmentProposition dice1Is5 = new SingleAssignmentProposition(dice1RV, 5);
		Assert.assertEquals(0.5, model.posterior(doubles, dice1Is5), DELTA_THRESHOLD);
	}
	
	protected void test_ToothacheCavityCatchModel(ProbabilityModel model, RandomVariable toothacheRV, RandomVariable cavityRV, RandomVariable catchRV) {
		Assert.assertTrue(model.isValid());
		
		SingleAssignmentProposition cavity = new SingleAssignmentProposition(cavityRV, Boolean.TRUE);
		SingleAssignmentProposition notcavity = new SingleAssignmentProposition(cavityRV, Boolean.FALSE);
		SingleAssignmentProposition toothache = new SingleAssignmentProposition(toothacheRV, Boolean.TRUE);
		
		// pg. 485
		Assert.assertEquals(0.2, model.prior(cavity), DELTA_THRESHOLD);
		Assert.assertEquals(0.6, model.posterior(cavity, toothache), DELTA_THRESHOLD);
		ConjunctiveProposition toothacheAndNotCavity = new ConjunctiveProposition(toothache, notcavity);
		Assert.assertEquals(0.0, model.posterior(cavity, toothacheAndNotCavity), DELTA_THRESHOLD);
		Assert.assertEquals(0.0, model.posterior(cavity, toothache, notcavity), DELTA_THRESHOLD);
		
	}
}
