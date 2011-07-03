package aima.core.probability.bayes.approx;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesInference;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.proposition.AssignmentProposition;

/**
 * An Adapter class to let BayesSampleInference implementations to be used in
 * places where calls are being made to the BayesInference API.
 * 
 * @author Ciaran O'Reilly
 */
public class BayesInferenceApproxAdapter implements BayesInference {
	private int N = 1000;
	private BayesSampleInference adaptee = null;

	public BayesInferenceApproxAdapter(BayesSampleInference adaptee) {
		this.adaptee = adaptee;
	}

	public BayesInferenceApproxAdapter(BayesSampleInference adaptee, int N) {
		this.adaptee = adaptee;
		this.N = N;
	}

	/**
	 * 
	 * @return the number of Samples when calling the BayesSampleInference
	 *         adaptee.
	 */
	public int getN() {
		return N;
	}

	/**
	 * 
	 * @param n
	 *            the numver of samples to be generated when calling the
	 *            BayesSampleInference adaptee.
	 */
	public void setN(int n) {
		N = n;
	}

	/**
	 * 
	 * @return The BayesSampleInference implementation to be adapted to the
	 *         BayesInference API.
	 */
	public BayesSampleInference getAdaptee() {
		return adaptee;
	}

	/**
	 * 
	 * @param adaptee
	 *            the BayesSampleInference implementation be be apated to the
	 *            BayesInference API.
	 */
	public void setAdaptee(BayesSampleInference adaptee) {
		this.adaptee = adaptee;
	}

	//
	// START-BayesInference
	@Override
	public CategoricalDistribution ask(final RandomVariable[] X,
			final AssignmentProposition[] observedEvidence,
			final BayesianNetwork bn) {
		return adaptee.ask(X, observedEvidence, bn, N);
	}

	// END-BayesInference
	//
}
