package aima.core.probability.reasoning;

import java.util.Arrays;
import java.util.List;

import aima.core.probability.RandomVariable;
import aima.core.util.math.Matrix;

/**
 * @author Ravi Mohan
 * 
 */
public class HiddenMarkovModel {

	SensorModel sensorModel;

	TransitionModel transitionModel;

	private RandomVariable priorDistribution;

	public HiddenMarkovModel(RandomVariable priorDistribution,
			TransitionModel tm, SensorModel sm) {
		this.priorDistribution = priorDistribution;
		this.transitionModel = tm;
		this.sensorModel = sm;
	}

	public RandomVariable prior() {
		return priorDistribution;
	}

	public RandomVariable predict(RandomVariable aBelief, String action) {
		RandomVariable newBelief = aBelief.duplicate();

		Matrix beliefMatrix = aBelief.asMatrix();
		Matrix transitionMatrix = transitionModel.asMatrix(action);
		Matrix predicted = transitionMatrix.transpose().times(beliefMatrix);
		newBelief.updateFrom(predicted);
		return newBelief;
	}

	public RandomVariable perceptionUpdate(RandomVariable aBelief,
			String perception) {
		RandomVariable newBelief = aBelief.duplicate();

		// one way - use matrices
		Matrix beliefMatrix = aBelief.asMatrix();
		Matrix o_matrix = sensorModel.asMatrix(perception);
		Matrix updated = o_matrix.times(beliefMatrix);
		newBelief.updateFrom(updated);
		newBelief.normalize();
		return newBelief;

		// alternate way of doing this. clearer in intent.
		// for (String state : aBelief.states()){
		// double probabilityOfPerception= sensorModel.get(state,perception);
		// newBelief.setProbabilityOf(state,probabilityOfPerception *
		// aBelief.getProbabilityOf(state));
		// }
	}

	public RandomVariable forward(RandomVariable aBelief, String action,
			String perception) {
		return perceptionUpdate(predict(aBelief, action), perception);
	}

	public RandomVariable forward(RandomVariable aBelief, String perception) {
		return forward(aBelief, HmmConstants.DO_NOTHING, perception);
	}

	public RandomVariable calculate_next_backward_message(
			RandomVariable forwardBelief,
			RandomVariable present_backward_message, String perception) {
		RandomVariable result = present_backward_message.duplicate();
		// System.out.println("fb :-calculating new backward message");
		// System.out.println("fb :-diagonal matrix from sens model = ");
		Matrix oMatrix = sensorModel.asMatrix(perception);
		// System.out.println(oMatrix);
		Matrix transitionMatrix = transitionModel.asMatrix();// action
		// should
		// be
		// passed
		// in
		// here?
		// System.out.println("fb :-present backward message = "
		// +present_backward_message);
		Matrix backwardMatrix = transitionMatrix.times(oMatrix
				.times(present_backward_message.asMatrix()));
		Matrix resultMatrix = backwardMatrix.arrayTimes(forwardBelief
				.asMatrix());
		result.updateFrom(resultMatrix);
		result.normalize();
		// System.out.println("fb :-normalized new backward message = "
		// +result);
		return result;
	}

	public List<RandomVariable> forward_backward(List<String> perceptions) {
		RandomVariable forwardMessages[] = new RandomVariable[perceptions
				.size() + 1];
		RandomVariable backwardMessage = priorDistribution.createUnitBelief();
		RandomVariable smoothedBeliefs[] = new RandomVariable[perceptions
				.size() + 1];

		forwardMessages[0] = priorDistribution;
		smoothedBeliefs[0] = null;

		// populate forward messages
		for (int i = 0; i < perceptions.size(); i++) { // N.B i starts at 1,
			// not zero
			forwardMessages[i + 1] = forward(forwardMessages[i], perceptions
					.get(i));
		}
		for (int i = perceptions.size(); i > 0; i--) {
			RandomVariable smoothed = priorDistribution.duplicate();
			smoothed.updateFrom(forwardMessages[i].asMatrix().arrayTimes(
					backwardMessage.asMatrix()));
			smoothed.normalize();
			smoothedBeliefs[i] = smoothed;
			backwardMessage = calculate_next_backward_message(
					forwardMessages[i], backwardMessage, perceptions.get(i - 1));
		}

		return Arrays.asList(smoothedBeliefs);
	}

	public SensorModel sensorModel() {
		return sensorModel;
	}

	public TransitionModel transitionModel() {
		return transitionModel;
	}

}
