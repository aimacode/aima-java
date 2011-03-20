package aima.gui.demo.probability;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import aima.core.environment.cellworld.CellWorld;
import aima.core.environment.cellworld.CellWorldPosition;
import aima.core.learning.reinforcement.PassiveADPAgent;
import aima.core.learning.reinforcement.PassiveTDAgent;
import aima.core.learning.reinforcement.QLearningAgent;
import aima.core.learning.reinforcement.QTable;
import aima.core.probability.BayesNet;
import aima.core.probability.BayesNetNode;
import aima.core.probability.EnumerateJointAsk;
import aima.core.probability.EnumerationAsk;
import aima.core.probability.JavaRandomizer;
import aima.core.probability.ProbabilityDistribution;
import aima.core.probability.Query;
import aima.core.probability.RandomVariable;
import aima.core.probability.Randomizer;
import aima.core.probability.decision.MDP;
import aima.core.probability.decision.MDPFactory;
import aima.core.probability.decision.MDPPolicy;
import aima.core.probability.decision.MDPUtilityFunction;
import aima.core.probability.reasoning.HMMFactory;
import aima.core.probability.reasoning.HiddenMarkovModel;
import aima.core.probability.reasoning.HmmConstants;
import aima.core.probability.reasoning.ParticleSet;

/**
 * @author Ravi Mohan
 * 
 */

public class ProbabilityDemo {

	public static void main(String[] args) {
		enumerationJointAskDemo();
		enumerationAskDemo();
		priorSampleDemo();
		rejectionSamplingDemo();
		likelihoodWeightingDemo();
		mcmcAskDemo();

		forwardBackWardDemo();
		particleFilterinfDemo();

		valueIterationDemo();
		policyIterationDemo();

		passiveADPgentDemo();
		passiveTDAgentDemo();
		qLearningAgentDemo();
	}

	private static void forwardBackWardDemo() {

		System.out.println("\nForward BackWard Demo\n");

		HiddenMarkovModel rainmanHmm = HMMFactory.createRainmanHMM();
		System.out
				.println("Creating a Hdden Markov Model to represent the model in Fig 15.5 ");
		List<String> perceptions = new ArrayList<String>();
		perceptions.add(HmmConstants.SEE_UMBRELLA);
		perceptions.add(HmmConstants.SEE_UMBRELLA);

		List<RandomVariable> results = rainmanHmm.forward_backward(perceptions);

		RandomVariable smoothedDayOne = results.get(1);
		System.out.println("Smoothed Probability Of Raining on Day One = "
				+ smoothedDayOne.getProbabilityOf(HmmConstants.RAINING));
		System.out.println("Smoothed Probability Of NOT Raining on Day One ="
				+ smoothedDayOne.getProbabilityOf(HmmConstants.NOT_RAINING));

		RandomVariable smoothedDayTwo = results.get(2);
		System.out.println("Smoothed Probability Of Raining on Day Two = "
				+ smoothedDayTwo.getProbabilityOf(HmmConstants.RAINING));
		System.out.println("Smoothed Probability Of NOT Raining on Day Two = "
				+ smoothedDayTwo.getProbabilityOf(HmmConstants.NOT_RAINING));

	}

	private static void particleFilterinfDemo() {
		System.out.println("\nParticle Filtering Demo\n");
		HiddenMarkovModel rainman = HMMFactory.createRainmanHMM();
		Randomizer r = new JavaRandomizer();
		ParticleSet starting = rainman.prior().toParticleSet(rainman, r, 1000);
		System.out.println("at the beginning, "
				+ starting.numberOfParticlesWithState(HmmConstants.RAINING)
				+ " particles 0f 1000 indicate status == 'raining' ");
		System.out.println("at the beginning, "
				+ starting.numberOfParticlesWithState(HmmConstants.NOT_RAINING)
				+ " particles of 1000 indicate status == 'NOT raining' ");

		System.out
				.println("\n Filtering Particle Set.On perception == 'SEE_UMBRELLA' ..\n");
		ParticleSet afterSeeingUmbrella = starting.filter(
				HmmConstants.SEE_UMBRELLA, r);
		System.out.println("after filtering "
				+ afterSeeingUmbrella
						.numberOfParticlesWithState(HmmConstants.RAINING)
				+ " particles of 1000 indicate status == 'raining' ");
		System.out.println("after filtering "
				+ afterSeeingUmbrella
						.numberOfParticlesWithState(HmmConstants.NOT_RAINING)
				+ " particles of 1000 indicate status == 'NOT raining' ");

	}

	private static void valueIterationDemo() {

		System.out.println("\nValue Iteration Demo\n");
		System.out.println("creating an MDP to represent the 4 X 3 world");
		MDP<CellWorldPosition, String> fourByThreeMDP = MDPFactory
				.createFourByThreeMDP();

		System.out.println("Beginning Value Iteration");
		MDPUtilityFunction<CellWorldPosition> uf = fourByThreeMDP
				.valueIterationTillMAximumUtilityGrowthFallsBelowErrorMargin(1,
						0.00001);
		for (int i = 1; i <= 3; i++) {
			for (int j = 1; j <= 4; j++) {
				if (!((i == 2) && (j == 2))) {
					printUtility(uf, i, j);
				}

			}
		}

	}

	private static void printUtility(MDPUtilityFunction<CellWorldPosition> uf,
			int i, int j) {
		System.out.println("Utility of (" + i + " , " + j + " ) "
				+ uf.getUtility(new CellWorldPosition(i, j)));

	}

	private static void policyIterationDemo() {

		System.out.println("\nPolicy Iteration Demo\n");
		System.out.println("\nValue Iteration Demo\n");
		System.out.println("creating an MDP to represent the 4 X 3 world");
		MDP<CellWorldPosition, String> fourByThreeMDP = MDPFactory
				.createFourByThreeMDP();
		MDPPolicy<CellWorldPosition, String> policy = fourByThreeMDP
				.policyIteration(1);
		for (int i = 1; i <= 3; i++) {
			for (int j = 1; j <= 4; j++) {
				if (!((i == 2) && (j == 2))) {
					printPolicy(i, j, policy);
				}
			}

		}
	}

	private static void printPolicy(int i, int j,
			MDPPolicy<CellWorldPosition, String> policy) {
		System.out.println("Reccomended Action for (" + i + " , " + j
				+ " )  =  " + policy.getAction(new CellWorldPosition(i, j)));

	}

	private static void passiveADPgentDemo() {
		System.out.println("\nPassive ADP Agent Demo\n");
		System.out.println("creating an MDP to represent the 4 X 3 world");
		MDP<CellWorldPosition, String> fourByThree = MDPFactory
				.createFourByThreeMDP();
		;

		MDPPolicy<CellWorldPosition, String> policy = new MDPPolicy<CellWorldPosition, String>();
		System.out
				.println("Creating a policy to reflect the policy in Fig 17.3");
		policy.setAction(new CellWorldPosition(1, 1), CellWorld.UP);
		policy.setAction(new CellWorldPosition(1, 2), CellWorld.LEFT);
		policy.setAction(new CellWorldPosition(1, 3), CellWorld.LEFT);
		policy.setAction(new CellWorldPosition(1, 4), CellWorld.LEFT);

		policy.setAction(new CellWorldPosition(2, 1), CellWorld.UP);
		policy.setAction(new CellWorldPosition(2, 3), CellWorld.UP);

		policy.setAction(new CellWorldPosition(3, 1), CellWorld.RIGHT);
		policy.setAction(new CellWorldPosition(3, 2), CellWorld.RIGHT);
		policy.setAction(new CellWorldPosition(3, 3), CellWorld.RIGHT);

		PassiveADPAgent<CellWorldPosition, String> agent = new PassiveADPAgent<CellWorldPosition, String>(
				fourByThree, policy);

		Randomizer r = new JavaRandomizer();
		System.out
				.println("Deriving Utility Function using the Passive ADP Agent  From 100 trials in the 4 by 3 world");
		MDPUtilityFunction<CellWorldPosition> uf = null;
		for (int i = 0; i < 100; i++) {
			agent.executeTrial(r);
			uf = agent.getUtilityFunction();

		}

		for (int i = 1; i <= 3; i++) {
			for (int j = 1; j <= 4; j++) {
				if (!((i == 2) && (j == 2))) {
					printUtility(uf, i, j);
				}

			}
		}

	}

	private static void passiveTDAgentDemo() {
		System.out.println("\nPassive TD Agent Demo\n");
		System.out.println("creating an MDP to represent the 4 X 3 world");
		MDP<CellWorldPosition, String> fourByThree = MDPFactory
				.createFourByThreeMDP();
		;

		MDPPolicy<CellWorldPosition, String> policy = new MDPPolicy<CellWorldPosition, String>();
		System.out
				.println("Creating a policy to reflect the policy in Fig 17.3");
		policy.setAction(new CellWorldPosition(1, 1), CellWorld.UP);
		policy.setAction(new CellWorldPosition(1, 2), CellWorld.LEFT);
		policy.setAction(new CellWorldPosition(1, 3), CellWorld.LEFT);
		policy.setAction(new CellWorldPosition(1, 4), CellWorld.LEFT);

		policy.setAction(new CellWorldPosition(2, 1), CellWorld.UP);
		policy.setAction(new CellWorldPosition(2, 3), CellWorld.UP);

		policy.setAction(new CellWorldPosition(3, 1), CellWorld.RIGHT);
		policy.setAction(new CellWorldPosition(3, 2), CellWorld.RIGHT);
		policy.setAction(new CellWorldPosition(3, 3), CellWorld.RIGHT);
		PassiveTDAgent<CellWorldPosition, String> agent = new PassiveTDAgent<CellWorldPosition, String>(
				fourByThree, policy);
		Randomizer r = new JavaRandomizer();
		System.out
				.println("Deriving Utility Function in the Passive ADP Agent  From 200 trials in the 4 by 3 world");
		MDPUtilityFunction<CellWorldPosition> uf = null;
		for (int i = 0; i < 200; i++) {
			agent.executeTrial(r);
			uf = agent.getUtilityFunction();
			// System.out.println(uf);

		}
		for (int i = 1; i <= 3; i++) {
			for (int j = 1; j <= 4; j++) {
				if (!((i == 2) && (j == 2))) {
					printUtility(uf, i, j);
				}

			}
		}

	}

	private static void qLearningAgentDemo() {
		System.out.println("\nQ Learning Agent Demo Demo\n");
		System.out.println("creating an MDP to represent the 4 X 3 world");
		MDP<CellWorldPosition, String> fourByThree = MDPFactory
				.createFourByThreeMDP();
		;
		QLearningAgent<CellWorldPosition, String> qla = new QLearningAgent<CellWorldPosition, String>(
				fourByThree);
		Randomizer r = new JavaRandomizer();

		// Randomizer r = new JavaRandomizer();
		// Hashtable<Pair<CellWorldPosition, String>, Double> q = null;
		QTable<CellWorldPosition, String> qTable = null;
		System.out.println("After 100 trials in the 4 by 3 world");
		for (int i = 0; i < 100; i++) {
			qla.executeTrial(r);
			// q = qla.getQ();
			qTable = qla.getQTable();

		}
		System.out.println("Final Q table" + qTable);

	}

	public static void enumerationJointAskDemo() {
		System.out.println("\nEnumerationJointAsk Demo\n");
		ProbabilityDistribution jp = new ProbabilityDistribution("ToothAche",
				"Cavity", "Catch");
		jp.set(0.108, true, true, true);
		jp.set(0.012, true, true, false);
		jp.set(0.072, false, true, true);
		jp.set(0.008, false, true, false);
		jp.set(0.016, true, false, true);
		jp.set(0.064, true, false, false);
		jp.set(0.144, false, false, true);
		jp.set(0.008, false, false, false);

		Query q = new Query("Cavity", new String[] { "ToothAche" },
				new boolean[] { true });
		double[] probs = EnumerateJointAsk.ask(q, jp);
		System.out
				.println("Using the full joint distribution of page 475 of Aima 2nd Edition");
		System.out
				.println("Probability distribution of ToothAche using Enumeration joint ask is "
						+ string(probs));
	}

	private static void priorSampleDemo() {
		System.out.println("\nPriorSample Demo\n");
		BayesNet net = createWetGrassNetwork();
		System.out
				.println("Using the Bayesian Network from page 510 of AIMA 2nd Edition generates");
		Hashtable<?, ?> table = net.getPriorSample();
		System.out.println(table.toString());
	}

	private static void rejectionSamplingDemo() {
		BayesNet net = createWetGrassNetwork();
		Hashtable<String, Boolean> evidence = new Hashtable<String, Boolean>();
		evidence.put("Sprinkler", Boolean.TRUE);
		double[] results = net.rejectionSample("Rain", evidence, 100);
		System.out.println("\nRejectionSampling Demo\n");
		System.out
				.println("Using the Bayesian Network from page 510 of AIMA 2nd Edition ");
		System.out
				.println("and querying for P(Rain|Sprinkler=true) using 100 samples gives");
		System.out.println(string(results));

	}

	private static void likelihoodWeightingDemo() {
		BayesNet net = createWetGrassNetwork();
		Hashtable<String, Boolean> evidence = new Hashtable<String, Boolean>();
		evidence.put("Sprinkler", Boolean.TRUE);
		double[] results = net.likelihoodWeighting("Rain", evidence, 100);
		System.out.println("\nLikelihoodWeighting Demo\n");
		System.out
				.println("Using the Bayesian Network from page 510 of AIMA 2nd Edition ");
		System.out
				.println("and querying for P(Rain|Sprinkler=true) using 100 samples gives");
		System.out.println(string(results));

	}

	private static void mcmcAskDemo() {
		BayesNet net = createWetGrassNetwork();
		Hashtable<String, Boolean> evidence = new Hashtable<String, Boolean>();
		evidence.put("Sprinkler", Boolean.TRUE);
		double[] results = net.mcmcAsk("Rain", evidence, 100);
		System.out.println("\nMCMCAsk Demo\n");
		System.out
				.println("Using the Bayesian Network from page 510 of AIMA 2nd Edition ");
		System.out
				.println("and querying for P(Rain|Sprinkler=true) using 100 samples gives");
		System.out.println(string(results));

	}

	public static void enumerationAskDemo() {
		System.out.println("\nEnumerationAsk Demo\n");
		Query q = new Query("Burglary",
				new String[] { "JohnCalls", "MaryCalls" }, new boolean[] {
						true, true });
		double[] probs = EnumerationAsk.ask(q, createBurglaryNetwork());
		System.out
				.println("Using the Burglary BayesNet from page 494 of AIMA 2nd Edition");
		System.out
				.println("Querying the probability of Burglary|JohnCalls=true, MaryCalls=true gives "
						+ string(probs));

	}

	private static BayesNet createBurglaryNetwork() {
		BayesNetNode burglary = new BayesNetNode("Burglary");
		BayesNetNode earthquake = new BayesNetNode("EarthQuake");
		BayesNetNode alarm = new BayesNetNode("Alarm");
		BayesNetNode johnCalls = new BayesNetNode("JohnCalls");
		BayesNetNode maryCalls = new BayesNetNode("MaryCalls");

		alarm.influencedBy(burglary, earthquake);
		johnCalls.influencedBy(alarm);
		maryCalls.influencedBy(alarm);

		burglary.setProbability(true, 0.001);// TODO behaviour changes if
		// root node
		earthquake.setProbability(true, 0.002);

		alarm.setProbability(true, true, 0.95);
		alarm.setProbability(true, false, 0.94);
		alarm.setProbability(false, true, 0.29);
		alarm.setProbability(false, false, 0.001);

		johnCalls.setProbability(true, 0.90);
		johnCalls.setProbability(false, 0.05);

		maryCalls.setProbability(true, 0.70);
		maryCalls.setProbability(false, 0.01);

		BayesNet net = new BayesNet(burglary, earthquake);
		return net;
	}

	private static BayesNet createWetGrassNetwork() {
		BayesNetNode cloudy = new BayesNetNode("Cloudy");
		BayesNetNode sprinkler = new BayesNetNode("Sprinkler");
		BayesNetNode rain = new BayesNetNode("Rain");
		BayesNetNode wetGrass = new BayesNetNode("WetGrass");

		sprinkler.influencedBy(cloudy);
		rain.influencedBy(cloudy);
		wetGrass.influencedBy(rain, sprinkler);

		cloudy.setProbability(true, 0.5);
		sprinkler.setProbability(true, 0.10);
		sprinkler.setProbability(false, 0.50);

		rain.setProbability(true, 0.8);
		rain.setProbability(false, 0.2);

		wetGrass.setProbability(true, true, 0.99);
		wetGrass.setProbability(true, false, 0.90);
		wetGrass.setProbability(false, true, 0.90);
		wetGrass.setProbability(false, false, 0.00);

		BayesNet net = new BayesNet(cloudy);
		return net;
	}

	private static String string(double[] probs) {
		return " [ " + probs[0] + " , " + probs[1] + " ] ";
	}

}
