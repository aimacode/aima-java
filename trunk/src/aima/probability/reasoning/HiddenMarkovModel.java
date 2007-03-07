package aima.probability.reasoning;

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
	Matrix transitionMatrix = transitionModel.asMatrix(
		aBelief, action);
	Matrix predicted = transitionMatrix.transpose().times(beliefMatrix);
	newBelief.updateFrom(predicted);
	return newBelief;
    }

    public RandomVariable perceptionUpdate(RandomVariable aBelief,
	    String perception) {
	RandomVariable newBelief = aBelief.duplicate();

	//one way - use matrices
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

}
