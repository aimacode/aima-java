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
		FiniteBayesModel model = new FiniteBayesModel(BayesNetExampleFactory
				.construct2FairDiceNetwor());

		test_RollingPairFairDiceModel(model, BayesNetExampleFactory.DICE_1_RV,
				BayesNetExampleFactory.DICE_2_RV);
	}

	@Test
	public void test_ToothacheCavityCatchModel() {
		FiniteBayesModel model = new FiniteBayesModel(BayesNetExampleFactory
				.constructToothacheCavityCatchNetwork());

		test_ToothacheCavityCatchModel(model,
				BayesNetExampleFactory.TOOTHACHE_RV,
				BayesNetExampleFactory.CAVITY_RV,
				BayesNetExampleFactory.CATCH_RV);
	}

	@Test
	public void test_ToothacheCavityCatchWeatherModel() {
		FiniteBayesModel model = new FiniteBayesModel(BayesNetExampleFactory
				.constructToothacheCavityCatchWeatherNetwork());

		test_ToothacheCavityCatchWeatherModel(model,
				BayesNetExampleFactory.TOOTHACHE_RV,
				BayesNetExampleFactory.CAVITY_RV,
				BayesNetExampleFactory.CATCH_RV,
				BayesNetExampleFactory.WEATHER_RV);
	}

	@Test
	public void test_MeningitisStiffNeckModel() {
		FiniteBayesModel model = new FiniteBayesModel(BayesNetExampleFactory
				.constructMeningitisStiffNeckNetwork());

		test_MeningitisStiffNeckModel(model,
				BayesNetExampleFactory.MENINGITIS_RV,
				BayesNetExampleFactory.STIFF_NECK_RV);
	}

	@Test
	public void test_BurglaryAlarmModel() {
		FiniteBayesModel model = new FiniteBayesModel(BayesNetExampleFactory
				.constructBurglaryAlarmNetwork());

		test_BurglaryAlarmModel(model, BayesNetExampleFactory.BURGLARY_RV,
				BayesNetExampleFactory.EARTHQUAKE_RV,
				BayesNetExampleFactory.ALARM_RV,
				BayesNetExampleFactory.JOHN_CALLS_RV,
				BayesNetExampleFactory.MARY_CALLS_RV);
	}

	//
	// FiniteProbabilityModel Tests
	@Test
	public void test_RollingPairFairDiceModel_Distributions() {
		FiniteBayesModel model = new FiniteBayesModel(BayesNetExampleFactory
				.construct2FairDiceNetwor());

		test_RollingPairFairDiceModel_Distributions(model,
				BayesNetExampleFactory.DICE_1_RV,
				BayesNetExampleFactory.DICE_2_RV);
	}

	@Test
	public void test_ToothacheCavityCatchModel_Distributions() {
		FiniteBayesModel model = new FiniteBayesModel(BayesNetExampleFactory
				.constructToothacheCavityCatchNetwork());

		test_ToothacheCavityCatchModel_Distributions(model,
				BayesNetExampleFactory.TOOTHACHE_RV,
				BayesNetExampleFactory.CAVITY_RV,
				BayesNetExampleFactory.CATCH_RV);
	}

	@Test
	public void test_ToothacheCavityCatchWeatherModel_Distributions() {
		FiniteBayesModel model = new FiniteBayesModel(BayesNetExampleFactory
				.constructToothacheCavityCatchWeatherNetwork());

		test_ToothacheCavityCatchWeatherModel_Distributions(model,
				BayesNetExampleFactory.TOOTHACHE_RV,
				BayesNetExampleFactory.CAVITY_RV,
				BayesNetExampleFactory.CATCH_RV,
				BayesNetExampleFactory.WEATHER_RV);
	}

	@Test
	public void test_MeningitisStiffNeckModel_Distributions() {
		FiniteBayesModel model = new FiniteBayesModel(BayesNetExampleFactory
				.constructMeningitisStiffNeckNetwork());

		test_MeningitisStiffNeckModel_Distributions(model,
				BayesNetExampleFactory.MENINGITIS_RV,
				BayesNetExampleFactory.STIFF_NECK_RV);
	}

	@Test
	public void test_BurglaryAlarmModel_Distributions() {
		FiniteBayesModel model = new FiniteBayesModel(BayesNetExampleFactory
				.constructBurglaryAlarmNetwork());

		test_BurglaryAlarmModel_Distributions(model, BayesNetExampleFactory.BURGLARY_RV,
				BayesNetExampleFactory.EARTHQUAKE_RV,
				BayesNetExampleFactory.ALARM_RV,
				BayesNetExampleFactory.JOHN_CALLS_RV,
				BayesNetExampleFactory.MARY_CALLS_RV);
	}
}
