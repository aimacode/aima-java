/*
 * Created on Feb 17, 2005
 *
 */
package aima.probability.demos;

import java.util.Hashtable;

import aima.probability.BayesNet;
import aima.probability.BayesNetNode;
import aima.probability.EnumerateJointAsk;
import aima.probability.EnumerationAsk;
import aima.probability.ProbabilityDistribution;
import aima.probability.Query;

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
	}

	public static void enumerationJointAskDemo() {
		System.out.println("\nEnumerationJointAsk Demo\n");
		ProbabilityDistribution jp = new ProbabilityDistribution("ToothAche",
				"Cavity", "Catch");
		jp.set(true, true, true, 0.108);
		jp.set(true, true, false, 0.012);
		jp.set(false, true, true, 0.072);
		jp.set(false, true, false, 0.008);
		jp.set(true, false, true, 0.016);
		jp.set(true, false, false, 0.064);
		jp.set(false, false, true, 0.144);
		jp.set(false, false, false, 0.008);

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
		Hashtable table = net.getPriorSample();
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
