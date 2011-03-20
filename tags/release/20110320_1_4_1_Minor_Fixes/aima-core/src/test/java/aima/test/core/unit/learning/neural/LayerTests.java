package aima.test.core.unit.learning.neural;

import org.junit.Assert;
import org.junit.Test;

import aima.core.learning.neural.BackPropLearning;
import aima.core.learning.neural.Layer;
import aima.core.learning.neural.LayerSensitivity;
import aima.core.learning.neural.LogSigActivationFunction;
import aima.core.learning.neural.PureLinearActivationFunction;
import aima.core.util.math.Matrix;
import aima.core.util.math.Vector;

public class LayerTests {

	@Test
	public void testFeedForward() {
		// example 11.14 of Neural Network Design by Hagan, Demuth and Beale
		// lots of tedious tests necessary to ensure nn is fundamentally correct
		Matrix weightMatrix1 = new Matrix(2, 1);
		weightMatrix1.set(0, 0, -0.27);
		weightMatrix1.set(1, 0, -0.41);

		Vector biasVector1 = new Vector(2);
		biasVector1.setValue(0, -0.48);
		biasVector1.setValue(1, -0.13);

		Layer layer1 = new Layer(weightMatrix1, biasVector1,
				new LogSigActivationFunction());

		Vector inputVector1 = new Vector(1);
		inputVector1.setValue(0, 1);

		Vector expected = new Vector(2);
		expected.setValue(0, 0.321);
		expected.setValue(1, 0.368);

		Vector result1 = layer1.feedForward(inputVector1);
		Assert.assertEquals(expected.getValue(0), result1.getValue(0), 0.001);
		Assert.assertEquals(expected.getValue(1), result1.getValue(1), 0.001);

		Matrix weightMatrix2 = new Matrix(1, 2);
		weightMatrix2.set(0, 0, 0.09);
		weightMatrix2.set(0, 1, -0.17);

		Vector biasVector2 = new Vector(1);
		biasVector2.setValue(0, 0.48);

		Layer layer2 = new Layer(weightMatrix2, biasVector2,
				new PureLinearActivationFunction());
		Vector inputVector2 = layer1.getLastActivationValues();
		Vector result2 = layer2.feedForward(inputVector2);
		Assert.assertEquals(0.446, result2.getValue(0), 0.001);
	}

	@Test
	public void testSensitivityMatrixCalculationFromErrorVector() {
		Matrix weightMatrix1 = new Matrix(2, 1);
		weightMatrix1.set(0, 0, -0.27);
		weightMatrix1.set(1, 0, -0.41);

		Vector biasVector1 = new Vector(2);
		biasVector1.setValue(0, -0.48);
		biasVector1.setValue(1, -0.13);

		Layer layer1 = new Layer(weightMatrix1, biasVector1,
				new LogSigActivationFunction());

		Vector inputVector1 = new Vector(1);
		inputVector1.setValue(0, 1);

		layer1.feedForward(inputVector1);

		Matrix weightMatrix2 = new Matrix(1, 2);
		weightMatrix2.set(0, 0, 0.09);
		weightMatrix2.set(0, 1, -0.17);

		Vector biasVector2 = new Vector(1);
		biasVector2.setValue(0, 0.48);

		Layer layer2 = new Layer(weightMatrix2, biasVector2,
				new PureLinearActivationFunction());
		Vector inputVector2 = layer1.getLastActivationValues();
		layer2.feedForward(inputVector2);

		Vector errorVector = new Vector(1);
		errorVector.setValue(0, 1.261);
		LayerSensitivity layer2Sensitivity = new LayerSensitivity(layer2);
		layer2Sensitivity.sensitivityMatrixFromErrorMatrix(errorVector);

		Matrix sensitivityMatrix = layer2Sensitivity.getSensitivityMatrix();
		Assert.assertEquals(-2.522, sensitivityMatrix.get(0, 0), 0.0001);
	}

	@Test
	public void testSensitivityMatrixCalculationFromSucceedingLayer() {
		Matrix weightMatrix1 = new Matrix(2, 1);
		weightMatrix1.set(0, 0, -0.27);
		weightMatrix1.set(1, 0, -0.41);

		Vector biasVector1 = new Vector(2);
		biasVector1.setValue(0, -0.48);
		biasVector1.setValue(1, -0.13);

		Layer layer1 = new Layer(weightMatrix1, biasVector1,
				new LogSigActivationFunction());
		LayerSensitivity layer1Sensitivity = new LayerSensitivity(layer1);

		Vector inputVector1 = new Vector(1);
		inputVector1.setValue(0, 1);

		layer1.feedForward(inputVector1);

		Matrix weightMatrix2 = new Matrix(1, 2);
		weightMatrix2.set(0, 0, 0.09);
		weightMatrix2.set(0, 1, -0.17);

		Vector biasVector2 = new Vector(1);
		biasVector2.setValue(0, 0.48);

		Layer layer2 = new Layer(weightMatrix2, biasVector2,
				new PureLinearActivationFunction());
		Vector inputVector2 = layer1.getLastActivationValues();
		layer2.feedForward(inputVector2);

		Vector errorVector = new Vector(1);
		errorVector.setValue(0, 1.261);
		LayerSensitivity layer2Sensitivity = new LayerSensitivity(layer2);
		layer2Sensitivity.sensitivityMatrixFromErrorMatrix(errorVector);

		layer1Sensitivity
				.sensitivityMatrixFromSucceedingLayer(layer2Sensitivity);
		Matrix sensitivityMatrix = layer1Sensitivity.getSensitivityMatrix();

		Assert.assertEquals(2, sensitivityMatrix.getRowDimension());
		Assert.assertEquals(1, sensitivityMatrix.getColumnDimension());
		Assert.assertEquals(-0.0495, sensitivityMatrix.get(0, 0), 0.001);
		Assert.assertEquals(0.0997, sensitivityMatrix.get(1, 0), 0.001);
	}

	@Test
	public void testWeightUpdateMatrixesFormedCorrectly() {
		Matrix weightMatrix1 = new Matrix(2, 1);
		weightMatrix1.set(0, 0, -0.27);
		weightMatrix1.set(1, 0, -0.41);

		Vector biasVector1 = new Vector(2);
		biasVector1.setValue(0, -0.48);
		biasVector1.setValue(1, -0.13);

		Layer layer1 = new Layer(weightMatrix1, biasVector1,
				new LogSigActivationFunction());
		LayerSensitivity layer1Sensitivity = new LayerSensitivity(layer1);

		Vector inputVector1 = new Vector(1);
		inputVector1.setValue(0, 1);

		layer1.feedForward(inputVector1);

		Matrix weightMatrix2 = new Matrix(1, 2);
		weightMatrix2.set(0, 0, 0.09);
		weightMatrix2.set(0, 1, -0.17);

		Vector biasVector2 = new Vector(1);
		biasVector2.setValue(0, 0.48);

		Layer layer2 = new Layer(weightMatrix2, biasVector2,
				new PureLinearActivationFunction());
		Vector inputVector2 = layer1.getLastActivationValues();
		layer2.feedForward(inputVector2);

		Vector errorVector = new Vector(1);
		errorVector.setValue(0, 1.261);
		LayerSensitivity layer2Sensitivity = new LayerSensitivity(layer2);
		layer2Sensitivity.sensitivityMatrixFromErrorMatrix(errorVector);

		layer1Sensitivity
				.sensitivityMatrixFromSucceedingLayer(layer2Sensitivity);

		Matrix weightUpdateMatrix2 = BackPropLearning.calculateWeightUpdates(
				layer2Sensitivity, layer1.getLastActivationValues(), 0.1);
		Assert.assertEquals(0.0809, weightUpdateMatrix2.get(0, 0), 0.001);
		Assert.assertEquals(0.0928, weightUpdateMatrix2.get(0, 1), 0.001);

		Matrix lastWeightUpdateMatrix2 = layer2.getLastWeightUpdateMatrix();
		Assert.assertEquals(0.0809, lastWeightUpdateMatrix2.get(0, 0), 0.001);
		Assert.assertEquals(0.0928, lastWeightUpdateMatrix2.get(0, 1), 0.001);

		Matrix penultimateWeightUpdatematrix2 = layer2
				.getPenultimateWeightUpdateMatrix();
		Assert.assertEquals(0.0, penultimateWeightUpdatematrix2.get(0, 0),
				0.001);
		Assert.assertEquals(0.0, penultimateWeightUpdatematrix2.get(0, 1),
				0.001);

		Matrix weightUpdateMatrix1 = BackPropLearning.calculateWeightUpdates(
				layer1Sensitivity, inputVector1, 0.1);
		Assert.assertEquals(0.0049, weightUpdateMatrix1.get(0, 0), 0.001);
		Assert.assertEquals(-0.00997, weightUpdateMatrix1.get(1, 0), 0.001);

		Matrix lastWeightUpdateMatrix1 = layer1.getLastWeightUpdateMatrix();
		Assert.assertEquals(0.0049, lastWeightUpdateMatrix1.get(0, 0), 0.001);
		Assert.assertEquals(-0.00997, lastWeightUpdateMatrix1.get(1, 0), 0.001);
		Matrix penultimateWeightUpdatematrix1 = layer1
				.getPenultimateWeightUpdateMatrix();
		Assert.assertEquals(0.0, penultimateWeightUpdatematrix1.get(0, 0),
				0.001);
		Assert.assertEquals(0.0, penultimateWeightUpdatematrix1.get(1, 0),
				0.001);
	}

	@Test
	public void testBiasUpdateMatrixesFormedCorrectly() {
		Matrix weightMatrix1 = new Matrix(2, 1);
		weightMatrix1.set(0, 0, -0.27);
		weightMatrix1.set(1, 0, -0.41);

		Vector biasVector1 = new Vector(2);
		biasVector1.setValue(0, -0.48);
		biasVector1.setValue(1, -0.13);

		Layer layer1 = new Layer(weightMatrix1, biasVector1,
				new LogSigActivationFunction());
		LayerSensitivity layer1Sensitivity = new LayerSensitivity(layer1);

		Vector inputVector1 = new Vector(1);
		inputVector1.setValue(0, 1);

		layer1.feedForward(inputVector1);

		Matrix weightMatrix2 = new Matrix(1, 2);
		weightMatrix2.set(0, 0, 0.09);
		weightMatrix2.set(0, 1, -0.17);

		Vector biasVector2 = new Vector(1);
		biasVector2.setValue(0, 0.48);

		Layer layer2 = new Layer(weightMatrix2, biasVector2,
				new PureLinearActivationFunction());
		LayerSensitivity layer2Sensitivity = new LayerSensitivity(layer2);
		Vector inputVector2 = layer1.getLastActivationValues();
		layer2.feedForward(inputVector2);

		Vector errorVector = new Vector(1);
		errorVector.setValue(0, 1.261);
		layer2Sensitivity.sensitivityMatrixFromErrorMatrix(errorVector);

		layer1Sensitivity
				.sensitivityMatrixFromSucceedingLayer(layer2Sensitivity);

		Vector biasUpdateVector2 = BackPropLearning.calculateBiasUpdates(
				layer2Sensitivity, 0.1);
		Assert.assertEquals(0.2522, biasUpdateVector2.getValue(0), 0.001);

		Vector lastBiasUpdateVector2 = layer2.getLastBiasUpdateVector();
		Assert.assertEquals(0.2522, lastBiasUpdateVector2.getValue(0), 0.001);

		Vector penultimateBiasUpdateVector2 = layer2
				.getPenultimateBiasUpdateVector();
		Assert.assertEquals(0.0, penultimateBiasUpdateVector2.getValue(0),
				0.001);

		Vector biasUpdateVector1 = BackPropLearning.calculateBiasUpdates(
				layer1Sensitivity, 0.1);
		Assert.assertEquals(0.00495, biasUpdateVector1.getValue(0), 0.001);
		Assert.assertEquals(-0.00997, biasUpdateVector1.getValue(1), 0.001);

		Vector lastBiasUpdateVector1 = layer1.getLastBiasUpdateVector();

		Assert.assertEquals(0.00495, lastBiasUpdateVector1.getValue(0), 0.001);
		Assert.assertEquals(-0.00997, lastBiasUpdateVector1.getValue(1), 0.001);

		Vector penultimateBiasUpdateVector1 = layer1
				.getPenultimateBiasUpdateVector();
		Assert.assertEquals(0.0, penultimateBiasUpdateVector1.getValue(0),
				0.001);
		Assert.assertEquals(0.0, penultimateBiasUpdateVector1.getValue(1),
				0.001);
	}

	@Test
	public void testWeightsAndBiasesUpdatedCorrectly() {
		Matrix weightMatrix1 = new Matrix(2, 1);
		weightMatrix1.set(0, 0, -0.27);
		weightMatrix1.set(1, 0, -0.41);

		Vector biasVector1 = new Vector(2);
		biasVector1.setValue(0, -0.48);
		biasVector1.setValue(1, -0.13);

		Layer layer1 = new Layer(weightMatrix1, biasVector1,
				new LogSigActivationFunction());
		LayerSensitivity layer1Sensitivity = new LayerSensitivity(layer1);

		Vector inputVector1 = new Vector(1);
		inputVector1.setValue(0, 1);

		layer1.feedForward(inputVector1);

		Matrix weightMatrix2 = new Matrix(1, 2);
		weightMatrix2.set(0, 0, 0.09);
		weightMatrix2.set(0, 1, -0.17);

		Vector biasVector2 = new Vector(1);
		biasVector2.setValue(0, 0.48);

		Layer layer2 = new Layer(weightMatrix2, biasVector2,
				new PureLinearActivationFunction());
		Vector inputVector2 = layer1.getLastActivationValues();
		layer2.feedForward(inputVector2);

		Vector errorVector = new Vector(1);
		errorVector.setValue(0, 1.261);
		LayerSensitivity layer2Sensitivity = new LayerSensitivity(layer2);
		layer2Sensitivity.sensitivityMatrixFromErrorMatrix(errorVector);

		layer1Sensitivity
				.sensitivityMatrixFromSucceedingLayer(layer2Sensitivity);

		BackPropLearning.calculateWeightUpdates(layer2Sensitivity, layer1
				.getLastActivationValues(), 0.1);

		BackPropLearning.calculateBiasUpdates(layer2Sensitivity, 0.1);

		BackPropLearning.calculateWeightUpdates(layer1Sensitivity,
				inputVector1, 0.1);

		BackPropLearning.calculateBiasUpdates(layer1Sensitivity, 0.1);

		layer2.updateWeights();
		Matrix newWeightMatrix2 = layer2.getWeightMatrix();
		Assert.assertEquals(0.171, newWeightMatrix2.get(0, 0), 0.001);
		Assert.assertEquals(-0.0772, newWeightMatrix2.get(0, 1), 0.001);

		layer2.updateBiases();
		Vector newBiasVector2 = layer2.getBiasVector();
		Assert.assertEquals(0.7322, newBiasVector2.getValue(0), 0.00001);

		layer1.updateWeights();
		Matrix newWeightMatrix1 = layer1.getWeightMatrix();

		Assert.assertEquals(-0.265, newWeightMatrix1.get(0, 0), 0.001);
		Assert.assertEquals(-0.419, newWeightMatrix1.get(1, 0), 0.001);

		layer1.updateBiases();
		Vector newBiasVector1 = layer1.getBiasVector();

		Assert.assertEquals(-0.475, newBiasVector1.getValue(0), 0.001);
		Assert.assertEquals(-0.139, newBiasVector1.getValue(1), 0.001);
	}
}
