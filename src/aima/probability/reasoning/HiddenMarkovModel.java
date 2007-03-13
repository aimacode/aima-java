package aima.probability.reasoning;

import java.util.Arrays;
import java.util.List;

import aima.probability.RandomVariable;
import aima.util.Matrix;

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
	Matrix transitionMatrix = transitionModel.asMatrix(aBelief, action);
	Matrix predicted = transitionMatrix.transpose().times(beliefMatrix);
	newBelief.updateFrom(predicted);
	return newBelief;
    }

    public RandomVariable perceptionUpdate(RandomVariable aBelief,
	    String perception) {
	RandomVariable newBelief = aBelief.duplicate();

	// one way - use matrices
	Matrix beliefMatrix = aBelief.asMatrix();
	Matrix o_matrix = sensorModel.asMatrix(aBelief, perception);
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

    public RandomVariable calculate_next_backward_message(RandomVariable forwardBelief,
	    RandomVariable present_backward_message, String perception) {
	RandomVariable result = present_backward_message.duplicate();
	Matrix oMatrix = sensorModel.asMatrix(present_backward_message, perception);
	Matrix transitionMatrix = transitionModel.asMatrix(present_backward_message);// action
	// should
	// be
	// passed
	// in
	// here?
	Matrix backwardMatrix = transitionMatrix.times(oMatrix.times(present_backward_message
		.asMatrix()));
	Matrix resultMatrix = backwardMatrix.arrayTimes(forwardBelief
		.asMatrix());
	result.updateFrom(resultMatrix);
	result.normalize();
	return result;
    }

    public List<RandomVariable> forward_backward(List<String> perceptions) {
	RandomVariable forwardMessages[] = new RandomVariable[perceptions
		.size()+1];
	RandomVariable backwardMessage = priorDistribution.createUnitBelief();
	RandomVariable smoothedBeliefs[] = new RandomVariable[perceptions
		.size()];

	forwardMessages[0] = priorDistribution;
	for (int i = 1; i <= perceptions.size(); i++) { // N.B i starts at 1 ,
	    // not zero
	    forwardMessages[i] = forward(forwardMessages[i - 1],
		    HmmConstants.DO_NOTHING, perceptions.get(i - 1));

	}
	for (int j = perceptions.size(); j > 0; j--) {
	    RandomVariable smoothed =  priorDistribution.duplicate();
	    smoothed.updateFrom(forwardMessages[j].asMatrix().arrayTimes(backwardMessage.asMatrix()));
	    smoothed.normalize();
	    smoothedBeliefs[j-1] = smoothed;
	    backwardMessage = calculate_next_backward_message(forwardMessages[j],backwardMessage,perceptions.get(j-1));
	}
	
	return Arrays.asList(smoothedBeliefs);
    }

}
