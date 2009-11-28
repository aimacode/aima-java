package aima.core.probability;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import aima.core.util.Util;

/**
 * @author Ravi Mohan
 * 
 */
public class EnumerationAsk {

	public static double[] ask(Query q, BayesNet net) {
		Hashtable<String, Boolean> evidenceVariables = q.getEvidenceVariables();

		double[] probDist = new double[2];
		// true probability
		evidenceVariables.put(q.getQueryVariable(), new Boolean(true));
		probDist[0] = enumerateAll(net, net.getVariables(), evidenceVariables);
		// false probability
		evidenceVariables.put(q.getQueryVariable(), new Boolean(false));
		probDist[1] = enumerateAll(net, net.getVariables(), evidenceVariables);
		// System.out.println( probDist[0] + " " + probDist[1]);
		// return probDist;
		double[] normalized = Util.normalize(probDist);
		// System.out.println( normalized[0] + " " + normalized[1]);
		return normalized;
	}

	private static double enumerateAll(BayesNet net, List unprocessedVariables,
			Hashtable<String, Boolean> evidenceVariables) {
		if (unprocessedVariables.size() == 0) {

			return 1.0;
		} else {
			String Y = (String) unprocessedVariables.get(0);

			if (evidenceVariables.keySet().contains(Y)) {

				double probYGivenParents = net.probabilityOf(Y,
						evidenceVariables.get(Y), evidenceVariables);

				double secondTerm = enumerateAll(net, Util
						.rest(unprocessedVariables), evidenceVariables);

				return probYGivenParents * secondTerm;
			} else {
				double sigma = 0.0;
				Hashtable<String, Boolean> clone1 = cloneEvidenceVariables(evidenceVariables);
				clone1.put(Y, Boolean.TRUE);
				double probYTrueGivenParents = net.probabilityOf(Y,
						Boolean.TRUE, clone1);

				double secondTerm = enumerateAll(net, Util
						.rest(unprocessedVariables), clone1);

				double trueProbabilityY = probYTrueGivenParents * secondTerm;

				Hashtable<String, Boolean> clone2 = cloneEvidenceVariables(evidenceVariables);
				clone2.put(Y, Boolean.FALSE);
				double probYFalseGivenParents = net.probabilityOf(Y,
						Boolean.FALSE, clone2);

				secondTerm = enumerateAll(net, Util.rest(unprocessedVariables),
						clone2);
				double falseProbabilityY = probYFalseGivenParents * secondTerm;
				// System.out.print(secondTerm + " ) )");
				sigma = trueProbabilityY + falseProbabilityY;
				return sigma;

			}
		}
	}

	private static Hashtable<String, Boolean> cloneEvidenceVariables(
			Hashtable<String, Boolean> evidence) {
		Hashtable<String, Boolean> cloned = new Hashtable<String, Boolean>();
		Iterator<String> iter = evidence.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			Boolean bool = evidence.get(key);
			if (bool.equals(Boolean.TRUE)) {
				cloned.put(key, Boolean.TRUE);
			} else if ((evidence.get(key)).equals(Boolean.FALSE)) {
				cloned.put(key, Boolean.FALSE);
			}
		}
		return cloned;
	}
}