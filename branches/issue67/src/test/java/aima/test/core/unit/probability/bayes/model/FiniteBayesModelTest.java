package aima.test.core.unit.probability.bayes.model;

import org.junit.Test;

import aima.core.probability.bayes.BayesInference;
import aima.core.probability.bayes.exact.EliminationAsk;
import aima.core.probability.bayes.exact.EnumerationAsk;
import aima.core.probability.bayes.model.FiniteBayesModel;
import aima.core.probability.example.BayesNetExampleFactory;
import aima.test.core.unit.probability.CommonFiniteProbabilityModelTests;

public class FiniteBayesModelTest extends CommonFiniteProbabilityModelTests {

	//
	// ProbabilityModel Tests
	@Test
	public void test_RollingPairFairDiceModel() {
		for (BayesInference bi : getBayesInferenceImplementations()) {
			test_RollingPairFairDiceModel(new FiniteBayesModel(
					BayesNetExampleFactory.construct2FairDiceNetwor(), bi));
		}
	}

	@Test
	public void test_ToothacheCavityCatchModel() {
		for (BayesInference bi : getBayesInferenceImplementations()) {
			test_ToothacheCavityCatchModel(new FiniteBayesModel(
					BayesNetExampleFactory
							.constructToothacheCavityCatchNetwork(),
					bi));
		}
	}

	@Test
	public void test_ToothacheCavityCatchWeatherModel() {
		for (BayesInference bi : getBayesInferenceImplementations()) {
			test_ToothacheCavityCatchWeatherModel(new FiniteBayesModel(
					BayesNetExampleFactory
							.constructToothacheCavityCatchWeatherNetwork(),
					bi));
		}
	}

	@Test
	public void test_MeningitisStiffNeckModel() {
		for (BayesInference bi : getBayesInferenceImplementations()) {
			test_MeningitisStiffNeckModel(new FiniteBayesModel(
					BayesNetExampleFactory
							.constructMeningitisStiffNeckNetwork(),
					bi));
		}
	}

	@Test
	public void test_BurglaryAlarmModel() {
		for (BayesInference bi : getBayesInferenceImplementations()) {
			test_BurglaryAlarmModel(new FiniteBayesModel(
					BayesNetExampleFactory.constructBurglaryAlarmNetwork(), bi));
		}
	}

	//
	// FiniteProbabilityModel Tests
	@Test
	public void test_RollingPairFairDiceModel_Distributions() {
		for (BayesInference bi : getBayesInferenceImplementations()) {
			test_RollingPairFairDiceModel_Distributions(new FiniteBayesModel(
					BayesNetExampleFactory.construct2FairDiceNetwor(), bi));
		}
	}

	@Test
	public void test_ToothacheCavityCatchModel_Distributions() {
		for (BayesInference bi : getBayesInferenceImplementations()) {
			test_ToothacheCavityCatchModel_Distributions(new FiniteBayesModel(
					BayesNetExampleFactory
							.constructToothacheCavityCatchNetwork(),
					bi));
		}
	}

	@Test
	public void test_ToothacheCavityCatchWeatherModel_Distributions() {
		for (BayesInference bi : getBayesInferenceImplementations()) {
			test_ToothacheCavityCatchWeatherModel_Distributions(new FiniteBayesModel(
					BayesNetExampleFactory
							.constructToothacheCavityCatchWeatherNetwork(),
					bi));
		}
	}

	@Test
	public void test_MeningitisStiffNeckModel_Distributions() {
		for (BayesInference bi : getBayesInferenceImplementations()) {
			test_MeningitisStiffNeckModel_Distributions(new FiniteBayesModel(
					BayesNetExampleFactory
							.constructMeningitisStiffNeckNetwork(),
					bi));
		}
	}

	@Test
	public void test_BurglaryAlarmModel_Distributions() {
		for (BayesInference bi : getBayesInferenceImplementations()) {
			test_BurglaryAlarmModel_Distributions(new FiniteBayesModel(
					BayesNetExampleFactory.constructBurglaryAlarmNetwork(), bi));
		}
	}

	//
	// PRIVATE METHODS
	//
	private BayesInference[] getBayesInferenceImplementations() {
		return new BayesInference[] { new EnumerationAsk(),
				new EliminationAsk() };
	}
}
