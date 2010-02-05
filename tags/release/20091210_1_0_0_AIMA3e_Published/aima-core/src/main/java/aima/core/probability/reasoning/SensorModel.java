package aima.core.probability.reasoning;

import java.util.ArrayList;
import java.util.List;

import aima.core.util.datastructure.Table;
import aima.core.util.math.Matrix;

/**
 * @author Ravi Mohan
 * 
 */
public class SensorModel {
	private Table<String, String, Double> table;

	private List<String> states;

	public SensorModel(List<String> states, List<String> perceptions) {
		this.states = states;
		table = new Table<String, String, Double>(states, perceptions);
	}

	public void setSensingProbability(String state, String perception,
			double probability) {
		table.set(state, perception, probability);
	}

	public Double get(String state, String perception) {
		return table.get(state, perception);
	}

	public Matrix asMatrix(String perception) {
		List<Double> values = new ArrayList<Double>();
		// for (String state : aBelief.states()) {
		for (String state : states) {
			values.add(get(state, perception));
		}
		Matrix OMatrix = Matrix.createDiagonalMatrix(values);
		return OMatrix;
	}
}
