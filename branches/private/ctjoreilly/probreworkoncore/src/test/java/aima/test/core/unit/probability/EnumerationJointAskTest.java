package aima.test.core.unit.probability;

import org.junit.Assert;
import org.junit.Test;

import aima.core.probability.EnumerateJointAsk;
import aima.core.probability.ProbabilityDistribution;
import aima.core.probability.Query;

/**
 * @author Ravi Mohan
 * 
 */
public class EnumerationJointAskTest {

	@Test
	public void testBasicUsage() {
		// >>> P[T, T, T] = 0.108; P[T, T, F] = 0.012; P[F, T, T] = 0.072; P[F,
		// T, F] = 0.008
		// >>> P[T, F, T] = 0.016; P[T, F, F] = 0.064; P[F, F, T] = 0.144; P[F,
		// F, F] = 0.576

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
		Assert.assertEquals(0.6, probs[0], 0.001);
		Assert.assertEquals(0.4, probs[1], 0.001);
	}
}