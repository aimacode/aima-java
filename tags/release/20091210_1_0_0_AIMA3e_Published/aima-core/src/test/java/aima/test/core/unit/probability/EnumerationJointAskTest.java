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
		Assert.assertEquals(0.6, probs[0], 0.001);
		Assert.assertEquals(0.4, probs[1], 0.001);
	}
}