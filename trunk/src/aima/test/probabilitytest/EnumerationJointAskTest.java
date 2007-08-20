/*
 * Created on Dec 28, 2004
 *
 */
package aima.test.probabilitytest;

import junit.framework.TestCase;
import aima.probability.EnumerateJointAsk;
import aima.probability.ProbabilityDistribution;
import aima.probability.Query;

/**
 * @author Ravi Mohan
 * 
 */

public class EnumerationJointAskTest extends TestCase {

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
		assertEquals(0.6, probs[0], 0.001);
		assertEquals(0.4, probs[1], 0.001);

	}

}