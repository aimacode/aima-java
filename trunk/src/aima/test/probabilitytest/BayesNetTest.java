package aima.test.probabilitytest;

import java.util.List;

import junit.framework.TestCase;
import aima.probability.BayesNet;
import aima.probability.BayesNetNode;

/**
 * @author Ravi Mohan
 * 
 */

public class BayesNetTest extends TestCase {
	BayesNet net;

	@Override
	public void setUp() {
		net = createBurglaryNetwork();
	}

	public void testVariablesAreCorrectlyObtainedFromBayesNetwork() {
		List variables = net.getVariables();
		assertEquals(5, variables.size());
		assertEquals("Burglary", (String) variables.get(0));
		assertEquals("EarthQuake", (String) variables.get(1));
		assertEquals("Alarm", (String) variables.get(2));
		assertEquals("JohnCalls", (String) variables.get(3));
		assertEquals("MaryCalls", (String) variables.get(4));
	}

	private BayesNet createBurglaryNetwork() {
		BayesNetNode burglary = new BayesNetNode("Burglary");
		BayesNetNode earthquake = new BayesNetNode("EarthQuake");
		BayesNetNode alarm = new BayesNetNode("Alarm");
		BayesNetNode johnCalls = new BayesNetNode("JohnCalls");
		BayesNetNode maryCalls = new BayesNetNode("MaryCalls");

		alarm.influencedBy(burglary, earthquake);
		johnCalls.influencedBy(alarm);
		maryCalls.influencedBy(alarm);

		burglary.setProbability(true, 0.001);
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
}
