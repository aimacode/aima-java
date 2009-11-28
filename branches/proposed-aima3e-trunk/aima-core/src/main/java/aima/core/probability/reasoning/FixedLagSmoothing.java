package aima.core.probability.reasoning;

import java.util.ArrayList;

import aima.core.probability.RandomVariable;
import aima.core.util.math.Matrix;

/**
 * @author Ravi Mohan
 * 
 */
public class FixedLagSmoothing {

	// This implementation is almost certainly wrong (see comments below).
	// This is faithful to the algorithm in the book but I think there are
	// some errors in the algorithmas published. Need to clarify with Dr N.

	private HiddenMarkovModel hmm;

	private int timelag;

	private ArrayList<String> evidenceFromSmoothedStepToPresent;

	private int time;

	private RandomVariable forwardMessage;

	private Matrix B;

	public FixedLagSmoothing(HiddenMarkovModel hmm, int timelag) {
		this.hmm = hmm;
		this.timelag = timelag;
		this.evidenceFromSmoothedStepToPresent = new ArrayList<String>();
		this.time = 1;
		this.forwardMessage = hmm.prior();
		this.B = hmm.transitionModel().unitMatrix();
	}

	public RandomVariable smooth(String perception) {

		evidenceFromSmoothedStepToPresent.add(perception);
		Matrix O_t = hmm.sensorModel().asMatrix(perception);
		Matrix transitionMatrix = hmm.transitionModel().asMatrix();
		if (time > timelag) {

			forwardMessage = hmm.forward(forwardMessage, perception); // This
			// seems
			// WRONG
			// I think this should be
			// forwardMessage = hmm.forward(forwardMessage,
			// evidenceFromSmoothedStepToPresent.get(0));
			// this the perception at t-d. the book's algorithm
			// uses the latest perception.
			evidenceFromSmoothedStepToPresent.remove(0);
			Matrix O_t_minus_d = hmm.sensorModel().asMatrix(
					evidenceFromSmoothedStepToPresent.get(0));

			B = O_t_minus_d.inverse().times(
					transitionMatrix.inverse().times(
							B.times(transitionMatrix.times(O_t))));

		} else {

			B = B.times(transitionMatrix.times(O_t));

		}
		time += 1;
		if (time > timelag) {

			Matrix one = hmm.prior().createUnitBelief().asMatrix();
			Matrix forwardMatrix = forwardMessage.asMatrix();
			RandomVariable result = hmm.prior().duplicate();
			Matrix backwardMessage = (B.times(one));

			result.updateFrom(forwardMatrix.arrayTimes(backwardMessage));

			result.normalize();
			return result;
		} else {
			return null;
		}
	}
}
