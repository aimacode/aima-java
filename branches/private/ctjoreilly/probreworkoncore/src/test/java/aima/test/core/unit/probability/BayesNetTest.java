package aima.test.core.unit.probability;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.probability.BayesNet;
import aima.core.probability.BayesNetNode;

/**
 * @author Ravi Mohan
 * 
 */
public class BayesNetTest {
	BayesNet net;

	@Before
	public void setUp() {
		net = createBurglaryNetwork();
	}

	@Test
	public void testVariablesAreCorrectlyObtainedFromBayesNetwork() {
		List<String> variables = net.getVariables();
		Assert.assertEquals(5, variables.size());
		Assert.assertEquals("Burglary", (String) variables.get(0));
		Assert.assertEquals("EarthQuake", (String) variables.get(1));
		Assert.assertEquals("Alarm", (String) variables.get(2));
		Assert.assertEquals("JohnCalls", (String) variables.get(3));
		Assert.assertEquals("MaryCalls", (String) variables.get(4));
	}

	//
	// PRIVATE METHODS
	//
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
