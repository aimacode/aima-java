package aima.core.probability.example;

import java.util.HashMap;
import java.util.Map;

import aima.core.util.math.Matrix;

public class HMMExampleFactory {
	public static Matrix getUmbrellaWorldTransitionModel() {
		return new Matrix(new double[][] { { 0.7, 0.3 }, { 0.3, 0.7 } });
	}

	public static Map<Object, Matrix> getUmbrellaWorldSensorModel() {
		Map<Object, Matrix> sensorModel = new HashMap<Object, Matrix>();
		sensorModel.put(Boolean.TRUE, new Matrix(new double[][] { { 0.9, 0.0 },
				{ 0.0, 0.2 } }));
		sensorModel.put(Boolean.FALSE, new Matrix(new double[][] {
				{ 0.1, 0.0 }, { 0.0, 0.8 } }));
		return sensorModel;
	}
}
