package aima.test.core.unit.probability.proposed.model.impl.full;

import junit.framework.Assert;

import org.junit.Test;

import aima.core.probability.proposed.model.impl.full.example.FullJointDistributionPairFairDiceModel;
import aima.core.probability.proposed.model.impl.full.example.FullJointDistributionToothacheCavityCatchModel;
import aima.test.core.unit.probability.proposed.model.CommonFiniteProbabilityModelTests;

public class FullJointProbabilityModelTest extends CommonFiniteProbabilityModelTests {

	//
	// ProbabilityModel Tests
	@Test
	public void test_RollingPairFairDiceModel() {
		FullJointDistributionPairFairDiceModel model = new FullJointDistributionPairFairDiceModel();
		
		test_RollingPairFairDiceModel(model, model.getDice1(), model.getDice2());
	}
	
	@Test
	public void test_ToothacheCavityCatchModel() {
		FullJointDistributionToothacheCavityCatchModel model = new FullJointDistributionToothacheCavityCatchModel();
		
		test_ToothacheCavityCatchModel(model, model.getToothache(), model.getCavity(), model.getCatch());
	}
	
	@Test
	public void test_ToothacheCavityCatchWeatherModel() {
		Assert.fail("TODO");
	}
	
	@Test
	public void test_BurglaryAlarmModel() {
		Assert.fail("TODO");
	}
	
	//
	// FiniteProbabilityModel Tests
	@Test
	public void test_RollingPairFairDiceModel_Distributions() {
		FullJointDistributionPairFairDiceModel model = new FullJointDistributionPairFairDiceModel();
		
		test_RollingPairFairDiceModel_Distributions(model, model.getDice1(), model.getDice2());
	}
	
	@Test
	public void test_ToothacheCavityCatchModel_Distributions() {
		FullJointDistributionToothacheCavityCatchModel model = new FullJointDistributionToothacheCavityCatchModel();
		
		test_ToothacheCavityCatchModel_Distributions(model, model.getToothache(), model.getCavity(), model.getCatch());
	}
	
	@Test
	public void test_BurglaryAlarmModel_Distributions() {
		Assert.fail("TODO");
	}
}
