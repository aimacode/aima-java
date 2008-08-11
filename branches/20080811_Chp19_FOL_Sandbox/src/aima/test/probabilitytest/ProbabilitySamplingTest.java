package aima.test.probabilitytest;

import java.util.Hashtable;

import junit.framework.TestCase;
import aima.probability.BayesNet;
import aima.probability.BayesNetNode;
import aima.probability.EnumerationAsk;
import aima.probability.Query;

/**
 * @author Ravi Mohan
 * 
 */

public class ProbabilitySamplingTest extends TestCase {

	public void testPriorSample() {
		BayesNet net = createWetGrassNetwork();
		MockRandomizer r = new MockRandomizer(
				new double[] { 0.5, 0.5, 0.5, 0.5 });
		Hashtable table = net.getPriorSample(r);
		assertEquals(4, table.keySet().size());
		assertEquals(Boolean.TRUE, table.get("Cloudy"));
		assertEquals(Boolean.FALSE, table.get("Sprinkler"));
		assertEquals(Boolean.TRUE, table.get("Rain"));
		assertEquals(Boolean.TRUE, table.get("WetGrass"));
	}

	public void testRejectionSample() {
		BayesNet net = createWetGrassNetwork();
		MockRandomizer r = new MockRandomizer(new double[] { 0.1 });
		Hashtable<String, Boolean> evidence = new Hashtable<String, Boolean>();
		evidence.put("Sprinkler", Boolean.TRUE);
		double[] results = net.rejectionSample("Rain", evidence, 100, r);
		assertEquals(1.0, results[0], 0.001);
		assertEquals(0.0, results[1], 0.001);

	}

	public void testLikelihoodWeighting() {
		MockRandomizer r = new MockRandomizer(
				new double[] { 0.5, 0.5, 0.5, 0.5 });
		BayesNet net = createWetGrassNetwork();
		Hashtable<String, Boolean> evidence = new Hashtable<String, Boolean>();
		evidence.put("Sprinkler", Boolean.TRUE);
		double[] results = net.likelihoodWeighting("Rain", evidence, 1000, r);
		// System.out.println(results[0] + " " + results[1]);
		assertEquals(1.0, results[0], 0.001);
		assertEquals(0.0, results[1], 0.001);
	}

	public void testMCMCask() {
		BayesNet net = createWetGrassNetwork();
		MockRandomizer r = new MockRandomizer(
				new double[] { 0.5, 0.5, 0.5, 0.5 });

		Hashtable<String, Boolean> evidence = new Hashtable<String, Boolean>();
		evidence.put("Sprinkler", Boolean.TRUE);
		double[] results = net.mcmcAsk("Rain", evidence, 1, r);
		// System.out.println(results[0] + " " + results[1]);
		assertEquals(0.333, results[0], 0.001);
		assertEquals(0.666, results[1], 0.001);

	}

	public void testMCMCask2() {
		BayesNet net = createWetGrassNetwork();
		MockRandomizer r = new MockRandomizer(
				new double[] { 0.5, 0.5, 0.5, 0.5 });

		Hashtable<String, Boolean> evidence = new Hashtable<String, Boolean>();
		evidence.put("Sprinkler", Boolean.TRUE);
		double[] results = net.mcmcAsk("Rain", evidence, 1, r);
		// System.out.println(results[0] + " " + results[1]);
		assertEquals(0.333, results[0], 0.001);
		assertEquals(0.666, results[1], 0.001);

	}

	public void testEnumerationAskinMCMC() {
		BayesNet net = createWetGrassNetwork();
		MockRandomizer r = new MockRandomizer(
				new double[] { 0.5, 0.5, 0.5, 0.5 });
		Hashtable<String, Boolean> evidence = new Hashtable<String, Boolean>();
		evidence.put("Rain", Boolean.TRUE);
		evidence.put("Sprinkler", Boolean.TRUE);
		Query q = new Query("Cloudy", new String[] { "Sprinkler", "Rain" },
				new boolean[] { true, true });
		double[] results = EnumerationAsk.ask(q, net);
		double[] results2 = net.mcmcAsk("Cloudy", evidence, 1000);
		// System.out.println(results[0] + " " + results[1]);
		// System.out.println(results2[0] + " " + results2[1]);

	}

	private BayesNet createWetGrassNetwork() {
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
}