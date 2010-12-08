package aima.core.learning.neural;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aima.core.learning.framework.Example;
import aima.core.util.datastructure.Pair;

/**
 * @author Ravi Mohan
 * 
 */
public class IrisDataSetNumerizer implements Numerizer {

	public Pair<List<Double>, List<Double>> numerize(Example e) {
		List<Double> input = new ArrayList<Double>();
		List<Double> desiredOutput = new ArrayList<Double>();

		double sepal_length = e.getAttributeValueAsDouble("sepal_length");
		double sepal_width = e.getAttributeValueAsDouble("sepal_width");
		double petal_length = e.getAttributeValueAsDouble("petal_length");
		double petal_width = e.getAttributeValueAsDouble("petal_width");

		input.add(sepal_length);
		input.add(sepal_width);
		input.add(petal_length);
		input.add(petal_width);

		String plant_category_string = e
				.getAttributeValueAsString("plant_category");

		desiredOutput = convertCategoryToListOfDoubles(plant_category_string);

		Pair<List<Double>, List<Double>> io = new Pair<List<Double>, List<Double>>(
				input, desiredOutput);

		return io;
	}

	public String denumerize(List<Double> outputValue) {
		List<Double> rounded = new ArrayList<Double>();
		for (Double d : outputValue) {
			rounded.add(round(d));
		}
		if (rounded.equals(Arrays.asList(0.0, 0.0, 1.0))) {
			return "setosa";
		} else if (rounded.equals(Arrays.asList(0.0, 1.0, 0.0))) {
			return "versicolor";
		} else if (rounded.equals(Arrays.asList(1.0, 0.0, 0.0))) {
			return "virginica";
		} else {
			return "unknown";
		}
	}

	//
	// PRIVATE METHODS
	//
	private double round(Double d) {
		if (d < 0) {
			return 0.0;
		}
		if (d > 1) {
			return 1.0;
		} else {
			return Math.round(d);
		}
	}

	private List<Double> convertCategoryToListOfDoubles(
			String plant_category_string) {
		if (plant_category_string.equals("setosa")) {
			return Arrays.asList(0.0, 0.0, 1.0);
		} else if (plant_category_string.equals("versicolor")) {
			return Arrays.asList(0.0, 1.0, 0.0);
		} else if (plant_category_string.equals("virginica")) {
			return Arrays.asList(1.0, 0.0, 0.0);
		} else {
			throw new RuntimeException("invalid plant category");
		}
	}
}
