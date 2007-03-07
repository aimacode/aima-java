package aima.probability.reasoning;

import java.util.ArrayList;
import java.util.List;

import aima.probability.RandomVariable;
import aima.util.Matrix;
import aima.util.Table;

public class SensorModel {
    private Table<String, String, Double> table;

    public SensorModel(List<String> states, List<String> perceptions) {
	table = new Table<String, String, Double>(states, perceptions);
    }

    public void set(String state, String perception, double probability) {
	table.set(state, perception, probability);

    }

    public Double get(String state, String perception) {

	return table.get(state, perception);
    }

    public Matrix asMatrix(RandomVariable aBelief, String perception) {
	List<Double> values = new ArrayList<Double>();
	for (String state : aBelief.states()) {
	    values.add(get(state, perception));
	}
	Matrix OMatrix = Matrix.createDiagonalMatrix(values);
	return OMatrix;
    }

}
