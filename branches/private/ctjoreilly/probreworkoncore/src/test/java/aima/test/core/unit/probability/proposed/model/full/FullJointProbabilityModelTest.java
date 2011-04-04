package aima.test.core.unit.probability.proposed.model.full;

import junit.framework.Assert;

import org.junit.Test;

import aima.core.probability.proposed.model.full.example.FullJointDistributionMeningitisStiffNeckModel;
import aima.core.probability.proposed.model.full.example.FullJointDistributionPairFairDiceModel;
import aima.core.probability.proposed.model.full.example.FullJointDistributionToothacheCavityCatchModel;
import aima.core.probability.proposed.model.full.example.FullJointDistributionToothacheCavityCatchWeatherModel;
import aima.test.core.unit.probability.proposed.model.CommonFiniteProbabilityModelTests;

public class FullJointProbabilityModelTest extends
		CommonFiniteProbabilityModelTests {

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

		test_ToothacheCavityCatchModel(model, model.getToothache(),
				model.getCavity(), model.getCatch());
	}

	@Test
	public void test_ToothacheCavityCatchWeatherModel() {
		FullJointDistributionToothacheCavityCatchWeatherModel model = new FullJointDistributionToothacheCavityCatchWeatherModel();

		test_ToothacheCavityCatchWeatherModel(model, model.getToothache(),
				model.getCavity(), model.getCatch(), model.getWeather());
	}

	@Test
	public void test_MeningitisStiffNeckModel() {
		FullJointDistributionMeningitisStiffNeckModel model = new FullJointDistributionMeningitisStiffNeckModel();

		test_MeningitisStiffNeckModel(model, model.getMeningitis(),
				model.getStiffNeck());
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

		test_RollingPairFairDiceModel_Distributions(model, model.getDice1(),
				model.getDice2());
	}

	@Test
	public void test_ToothacheCavityCatchModel_Distributions() {
		FullJointDistributionToothacheCavityCatchModel model = new FullJointDistributionToothacheCavityCatchModel();

		test_ToothacheCavityCatchModel_Distributions(model,
				model.getToothache(), model.getCavity(), model.getCatch());
	}

	@Test
	public void test_ToothacheCavityCatchWeatherModel_Distributions() {
		Assert.fail("TODO");
	}

	@Test
	public void test_MeningitisStiffNeckModel_Distributions() {
		Assert.fail("TODO");
	}

	@Test
	public void test_BurglaryAlarmModel_Distributions() {
		Assert.fail("TODO");
	}
}
