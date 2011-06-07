package aima.core.probability.proposed.example;

import aima.core.probability.proposed.bayes.BayesianNetwork;
import aima.core.probability.proposed.bayes.FiniteNode;
import aima.core.probability.proposed.bayes.impl.BayesNet;
import aima.core.probability.proposed.bayes.impl.FullCPTNode;

public class BayesNetExampleFactory {
	public static BayesianNetwork construct2FairDiceNetwor() {
		FiniteNode dice1 = new FullCPTNode(ExampleRV.DICE_1_RV, new double[] {
				1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0,
				1.0 / 6.0 });
		FiniteNode dice2 = new FullCPTNode(ExampleRV.DICE_2_RV, new double[] {
				1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 6.0,
				1.0 / 6.0 });

		return new BayesNet(dice1, dice2);
	}

	public static BayesianNetwork constructToothacheCavityCatchNetwork() {
		FiniteNode cavity = new FullCPTNode(ExampleRV.CAVITY_RV, new double[] {
				0.2, 0.8 });
		@SuppressWarnings("unused")
		FiniteNode toothache = new FullCPTNode(ExampleRV.TOOTHACHE_RV,
				new double[] {
				// C=true, T=true
						0.6,
						// C=true, T=false
						0.4,
						// C=false, T=true
						0.1,
						// C=false, T=false
						0.9

				}, cavity);
		@SuppressWarnings("unused")
		FiniteNode catchN = new FullCPTNode(ExampleRV.CATCH_RV, new double[] {
		// C=true, Catch=true
				0.9,
				// C=true, Catch=false
				0.1,
				// C=false, Catch=true
				0.2,
				// C=false, Catch=false
				0.8 }, cavity);

		return new BayesNet(cavity);
	}

	public static BayesianNetwork constructToothacheCavityCatchWeatherNetwork() {
		FiniteNode cavity = new FullCPTNode(ExampleRV.CAVITY_RV, new double[] {
				0.2, 0.8 });
		@SuppressWarnings("unused")
		FiniteNode toothache = new FullCPTNode(ExampleRV.TOOTHACHE_RV,
				new double[] {
				// C=true, T=true
						0.6,
						// C=true, T=false
						0.4,
						// C=false, T=true
						0.1,
						// C=false, T=false
						0.9

				}, cavity);
		@SuppressWarnings("unused")
		FiniteNode catchN = new FullCPTNode(ExampleRV.CATCH_RV, new double[] {
		// C=true, Catch=true
				0.9,
				// C=true, Catch=false
				0.1,
				// C=false, Catch=true
				0.2,
				// C=false, Catch=false
				0.8 }, cavity);
		FiniteNode weather = new FullCPTNode(ExampleRV.WEATHER_RV,
				new double[] {
				// sunny
						0.6,
						// rain
						0.1,
						// cloudy
						0.29,
						// snow
						0.01 });

		return new BayesNet(cavity, weather);
	}

	public static BayesianNetwork constructMeningitisStiffNeckNetwork() {
		FiniteNode meningitis = new FullCPTNode(ExampleRV.MENINGITIS_RV,
				new double[] { 1.0 / 50000.0, 1.0 - (1.0 / 50000.0) });
		@SuppressWarnings("unused")
		FiniteNode stiffneck = new FullCPTNode(ExampleRV.STIFF_NECK_RV,
				new double[] {
				// M=true, S=true
						0.7,
						// M=true, S=false
						0.3,
						// M=false, S=true
						0.009986199723994478,
						// M=false, S=false
						0.9900138002760055

				}, meningitis);
		return new BayesNet(meningitis);
	}

	public static BayesianNetwork constructBurglaryAlarmNetwork() {
		FiniteNode burglary = new FullCPTNode(ExampleRV.BURGLARY_RV,
				new double[] { 0.001, 0.999 });
		FiniteNode earthquake = new FullCPTNode(ExampleRV.EARTHQUAKE_RV,
				new double[] { 0.002, 0.998 });
		FiniteNode alarm = new FullCPTNode(ExampleRV.ALARM_RV, new double[] {
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
		@SuppressWarnings("unused")
		FiniteNode johnCalls = new FullCPTNode(ExampleRV.JOHN_CALLS_RV,
				new double[] {
				// A=true, J=true
						0.90,
						// A=true, J=false
						0.10,
						// A=false, J=true
						0.05,
						// A=false, J=false
						0.95 }, alarm);
		@SuppressWarnings("unused")
		FiniteNode maryCalls = new FullCPTNode(ExampleRV.MARY_CALLS_RV,
				new double[] {
				// A=true, M=true
						0.70,
						// A=true, M=false
						0.30,
						// A=false, M=true
						0.01,
						// A=false, M=false
						0.99 }, alarm);

		return new BayesNet(burglary, earthquake);
	}
}
