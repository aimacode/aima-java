package aima.core.probability.proposed.model.bayes.example;

import aima.core.probability.proposed.model.RandomVariable;
import aima.core.probability.proposed.model.bayes.BayesianNetwork;
import aima.core.probability.proposed.model.bayes.FiniteNode;
import aima.core.probability.proposed.model.domain.ArbitraryTokenDomain;
import aima.core.probability.proposed.model.domain.BooleanDomain;
import aima.core.probability.proposed.model.domain.FiniteIntegerDomain;

public class BayesNetExampleFactory {
	//
	public static final RandomVariable DICE_1_RV = new RandomVariable("Dice1",
			new FiniteIntegerDomain(1, 2, 3, 4, 5, 6));
	public static final RandomVariable DICE_2_RV = new RandomVariable("Dice2",
			new FiniteIntegerDomain(1, 2, 3, 4, 5, 6));
	//
	public static final RandomVariable TOOTHACHE_RV = new RandomVariable(
			"Toothache", new BooleanDomain());
	public static final RandomVariable CAVITY_RV = new RandomVariable("Cavity",
			new BooleanDomain());
	public static final RandomVariable CATCH_RV = new RandomVariable("Catch",
			new BooleanDomain());
	//
	public static final RandomVariable WEATHER_RV = new RandomVariable(
			"Weather", new ArbitraryTokenDomain("sunny", "rain", "cloudy",
					"snow"));
	//
	public static final RandomVariable MENINGITIS_RV = new RandomVariable(
			"Meningitis", new BooleanDomain());
	public static final RandomVariable STIFF_NECK_RV = new RandomVariable(
			"StiffNeck", new BooleanDomain());
	//
	public static final RandomVariable BURGLARY_RV = new RandomVariable(
			"Burglary", new BooleanDomain());
	public static final RandomVariable EARTHQUAKE_RV = new RandomVariable(
			"Earthquake", new BooleanDomain());
	public static final RandomVariable ALARM_RV = new RandomVariable("Alarm",
			new BooleanDomain());
	public static final RandomVariable JOHN_CALLS_RV = new RandomVariable(
			"JohnCalls", new BooleanDomain());
	public static final RandomVariable MARY_CALLS_RV = new RandomVariable(
			"MaryCalls", new BooleanDomain());

	public static BayesianNetwork construct2FairDiceNetwor() {
		FiniteNode dice1 = new FiniteNode(DICE_1_RV, new double[] { 1.0 / 6.0,
				1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0 });
		FiniteNode dice2 = new FiniteNode(DICE_2_RV, new double[] { 1.0 / 6.0,
				1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0 });

		return new BayesianNetwork(dice1, dice2);
	}

	public static BayesianNetwork constructToothacheCavityCatchNetwork() {
		FiniteNode cavity = new FiniteNode(CAVITY_RV, new double[] { 0.2, 0.8 });
		FiniteNode toothache = new FiniteNode(TOOTHACHE_RV, new double[] {
		// C=true, T=true
				0.6,
				// C=true, T=false
				0.4,
				// C=false, T=true
				0.1,
				// C=false, T=false
				0.9

		}, cavity);
		FiniteNode catchN = new FiniteNode(CATCH_RV, new double[] {
		// C=true, Catch=true
				0.9,
				// C=true, Catch=false
				0.1,
				// C=false, Catch=true
				0.2,
				// C=false, Catch=false
				0.8 }, cavity);

		return new BayesianNetwork(cavity);
	}

	public static BayesianNetwork constructToothacheCavityCatchWeatherNetwork() {
		FiniteNode cavity = new FiniteNode(CAVITY_RV, new double[] { 0.2, 0.8 });
		FiniteNode toothache = new FiniteNode(TOOTHACHE_RV, new double[] {
		// C=true, T=true
				0.6,
				// C=true, T=false
				0.4,
				// C=false, T=true
				0.1,
				// C=false, T=false
				0.9

		}, cavity);
		FiniteNode catchN = new FiniteNode(CATCH_RV, new double[] {
		// C=true, Catch=true
				0.9,
				// C=true, Catch=false
				0.1,
				// C=false, Catch=true
				0.2,
				// C=false, Catch=false
				0.8 }, cavity);
		FiniteNode weather = new FiniteNode(WEATHER_RV, new double[] {
		// sunny
				0.6,
				// rain
				0.1,
				// cloudy
				0.29,
				// snow
				0.01 });

		return new BayesianNetwork(cavity, weather);
	}

	public static BayesianNetwork constructMeningitisStiffNeckNetwork() {
		FiniteNode meningitis = new FiniteNode(MENINGITIS_RV, new double[] {
				1.0 / 50000.0, 1.0 - (1.0 / 50000.0) });
		FiniteNode stiffneck = new FiniteNode(STIFF_NECK_RV, new double[] {
		// M=true, S=true
				0.7,
				// M=true, S=false
				0.3,
				// M=false, S=true
				0.009986199723994478,
				// M=false, S=false
				0.9900138002760055

		}, meningitis);
		return new BayesianNetwork(meningitis);
	}

	public static BayesianNetwork constructBurglaryAlarmNetwork() {
		FiniteNode burglary = new FiniteNode(BURGLARY_RV, new double[] { 0.001,
				0.999 });
		FiniteNode earthquake = new FiniteNode(EARTHQUAKE_RV, new double[] {
				0.002, 0.998 });
		FiniteNode alarm = new FiniteNode(ALARM_RV, new double[] {
		// B=true, E=true, A=true
				0.95,
				// B=true, E=true, A=false
				0.05,
				// B=true, E=false, A=true
				0.94,
				// B=true, E=false, A=false
				0.06,
				// B=false, E=true, A=true
				0.29,
				// B=false, E=true, A=false
				0.71,
				// B=false, E=false, A=true
				0.001,
				// B=false, E=false, A=false
				0.999 }, burglary, earthquake);
		FiniteNode johnCalls = new FiniteNode(JOHN_CALLS_RV, new double[] {
		// A=true, J=true
				0.90,
				// A=true, J=false
				0.10,
				// A=false, J=true
				0.05,
				// A=false, J=false
				0.95 }, alarm);
		FiniteNode maryCalls = new FiniteNode(MARY_CALLS_RV, new double[] {
		// A=true, M=true
				0.70,
				// A=true, M=false
				0.30,
				// A=false, M=true
				0.01,
				// A=false, M=false
				0.99 }, alarm);

		return new BayesianNetwork(burglary, earthquake);
	}
}
