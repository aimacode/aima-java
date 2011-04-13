package aima.test.core.unit.probability.proposed.model.bayes;

import org.junit.Test;

import aima.core.probability.proposed.example.BayesNetExampleFactory;
import aima.core.probability.proposed.model.bayes.FiniteBayesModel;
import aima.test.core.unit.probability.proposed.model.CommonFiniteProbabilityModelTests;

public class FiniteBayesModelTest extends CommonFiniteProbabilityModelTests {

	//
	// ProbabilityModel Tests
	@Test
	public void test_RollingPairFairDiceModel() {
		test_RollingPairFairDiceModel(new FiniteBayesModel(
				BayesNetExampleFactory.construct2FairDiceNetwor()));
	}

	@Test
	public void test_ToothacheCavityCatchModel() {
		test_ToothacheCavityCatchModel(new FiniteBayesModel(
				BayesNetExampleFactory.constructToothacheCavityCatchNetwork()));
	}

	@Test
	public void test_ToothacheCavityCatchWeatherModel() {
		test_ToothacheCavityCatchWeatherModel(new FiniteBayesModel(
				BayesNetExampleFactory
						.constructToothacheCavityCatchWeatherNetwork()));
	}

	@Test
	public void test_MeningitisStiffNeckModel() {
		test_MeningitisStiffNeckModel(new FiniteBayesModel(
				BayesNetExampleFactory.constructMeningitisStiffNeckNetwork()));
	}

	@Test
	public void test_BurglaryAlarmModel() {
		test_BurglaryAlarmModel(new FiniteBayesModel(BayesNetExampleFactory
				.constructBurglaryAlarmNetwork()));
	}

	//
	// FiniteProbabilityModel Tests
	@Test
	public void test_RollingPairFairDiceModel_Distributions() {
		test_RollingPairFairDiceModel_Distributions(new FiniteBayesModel(
				BayesNetExampleFactory.construct2FairDiceNetwor()));
	}

	@Test
	public void test_ToothacheCavityCatchModel_Distributions() {
		test_ToothacheCavityCatchModel_Distributions(new FiniteBayesModel(
				BayesNetExampleFactory.constructToothacheCavityCatchNetwork()));
	}

	@Test
	public void test_ToothacheCavityCatchWeatherModel_Distributions() {
		test_ToothacheCavityCatchWeatherModel_Distributions(new FiniteBayesModel(
				BayesNetExampleFactory
						.constructToothacheCavityCatchWeatherNetwork()));
	}

	@Test
	public void test_MeningitisStiffNeckModel_Distributions() {
		test_MeningitisStiffNeckModel_Distributions(new FiniteBayesModel(
				BayesNetExampleFactory.constructMeningitisStiffNeckNetwork()));
	}

	@Test
	public void test_BurglaryAlarmModel_Distributions() {
		test_BurglaryAlarmModel_Distributions(new FiniteBayesModel(
				BayesNetExampleFactory.constructBurglaryAlarmNetwork()));
	}
}
