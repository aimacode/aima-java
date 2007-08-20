/*
 * Created on Aug 2, 2005
 *
 */
package aima.test.learningtest;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import aima.learning.statistics.FeedForwardNetwork;
import aima.learning.statistics.IdentityActivationFunction;
import aima.learning.statistics.Layer;
import aima.learning.statistics.Link;
import aima.learning.statistics.LogSigActivationFunction;
import aima.learning.statistics.Neuron;
import aima.learning.statistics.SquareActivationFunction;
import aima.learning.statistics.StandardBackPropogation;
import aima.test.probabilitytest.MockRandomizer;

/**
 * @author Ravi Mohan
 * 
 */

public class NeuralNetworkTest extends TestCase {

	public double testFunction(double x) {
		return 1.0 + (Math.sin(Math.PI / 4) * x);
	}

	public void testDefaultValuesOfUnConnectedNeuron() {
		Neuron n = new Neuron();
		assertEquals(1.0, n.bias());
		assertEquals(0, n.outLinks().size());
		assertEquals(0, n.inLinks().size());
	}

	public void testBiasSettingOnNeuron() {
		Neuron n = new Neuron(3.0);
		assertEquals(3.0, n.bias());
	}

	public void testLinkCreation() {
		Neuron n1 = new Neuron();
		Neuron n2 = new Neuron();
		Link link = new Link(n1, n2, 4);
		assertEquals(n1, link.source());
		assertEquals(n2, link.target());
		assertEquals(4.0, link.weight());
	}

	public void testNeuronConnection() {
		Neuron n1 = new Neuron();
		Neuron n2 = new Neuron();
		n1.connectTo(n2, 5.0);
		assertEquals(1, n1.outLinks().size());
		assertEquals(1, n2.inLinks().size());
		assertEquals(5.0, n2.weights().get(0));
	}

	public void testActivationOfConnectedNeurons() {
		Neuron inputNeuron = new Neuron();
		Neuron hiddenNeuron = new Neuron(0.0, new SquareActivationFunction());
		Neuron outputNeuron = new Neuron(1.0, new IdentityActivationFunction());
		inputNeuron.connectTo(hiddenNeuron, 1.0);
		hiddenNeuron.connectTo(outputNeuron, 2.0);
		inputNeuron.acceptAsInput(2.0);
		hiddenNeuron.update();
		assertEquals(4.0, hiddenNeuron.activation());
		outputNeuron.update();
		assertEquals(9.0, outputNeuron.activation());
	}

	public void testLayerConstruction() {
		Layer l = new Layer(3);
		Neuron n0 = l.getNeuron(0);
		Neuron n1 = l.getNeuron(1);
		Neuron n2 = l.getNeuron(2);
		assertNotNull(n0);
		assertNotNull(n1);
		assertNotNull(n2);
	}

	public void testLayerAcceptsInputAndGeneratesErrorCorrectly() {
		Layer l = new Layer(3);
		Neuron n0 = l.getNeuron(0);
		Neuron n1 = l.getNeuron(1);
		Neuron n2 = l.getNeuron(2);
		List<Double> input = Arrays.asList(1.0, 2.0, 3.0);
		List<Double> values = Arrays.asList(2.0, 3.0, 4.0);
		List<Double> expectedError = Arrays.asList(1.0, 1.0, 1.0);

		l.acceptInput(input);
		assertEquals(expectedError, l.getError(values));
	}

	public void testLayerUpdatesActivationProperly() {
		Layer inputLayer = new Layer(1);
		Layer hiddenLayer = new Layer(1, 0.0, new SquareActivationFunction());
		Layer outputLayer = new Layer(1, 1.0, new IdentityActivationFunction());

		inputLayer.connectTo(hiddenLayer, new MockRandomizer(
				new double[] { 1.0 }));
		hiddenLayer.connectTo(outputLayer, new MockRandomizer(
				new double[] { 2.0 }));

		inputLayer.acceptInput(Arrays.asList(1.0));
		hiddenLayer.update();
		assertEquals(Arrays.asList(1.0), hiddenLayer.activation());
		outputLayer.update();
		assertEquals(Arrays.asList(3.0), outputLayer.activation());

		inputLayer.acceptInput(Arrays.asList(2.0));
		hiddenLayer.update();
		assertEquals(Arrays.asList(4.0), hiddenLayer.activation());
		outputLayer.update();
		assertEquals(Arrays.asList(9.0), outputLayer.activation());
	}

	public void testFeedForwardNeuralNetwork() {
		Layer inputLayer = new Layer(1);
		Layer hiddenLayer = new Layer(1, 0.0, new SquareActivationFunction());
		Layer outputLayer = new Layer(1, 1.0, new IdentityActivationFunction());

		FeedForwardNetwork network = new FeedForwardNetwork();
		network.addLayer(inputLayer, null);
		network.addLayer(hiddenLayer, new MockRandomizer(new double[] { 1.0 }));
		network.addLayer(outputLayer, new MockRandomizer(new double[] { 2.0 }));

		network.propogateInput(Arrays.asList(1.0));
		assertEquals(Arrays.asList(3.0), network.output());

		network.propogateInput(Arrays.asList(2.0));
		assertEquals(Arrays.asList(9.0), network.output());
	}

	public void testBackPropogation() {

		// Neural Network test data from "neural Network Design" by
		// Hagan,Demuth,Beale
		// section 11-15

		// create neural network
		Layer inputLayer = new Layer(1);
		Layer hiddenLayer = new Layer(2, Arrays.asList(-0.48, -0.13),
				new LogSigActivationFunction());
		Layer outputLayer = new Layer(1, 0.48, new IdentityActivationFunction());

		FeedForwardNetwork network = new FeedForwardNetwork();
		network.addLayer(inputLayer, null);
		network.addLayer(hiddenLayer, new MockRandomizer(new double[] { -0.27,
				-0.41 }));
		network.addLayer(outputLayer, new MockRandomizer(new double[] { 0.09,
				-0.17 }));

		StandardBackPropogation scheme = new StandardBackPropogation();
		scheme.backPropogate(network, Arrays.asList(1.0), Arrays
				.asList(testFunction(1.0)));

		assertEquals(0.321, hiddenLayer.activation().get(0), 0.001);
		assertEquals(0.368, hiddenLayer.activation().get(1), 0.001);
		assertEquals(0.446, outputLayer.activation().get(0), 0.001);

		assertEquals(-2.522, scheme.delta(outputLayer).get(0), 0.001);
		assertEquals(0.2979, scheme.delta(hiddenLayer).get(0), 0.001);
		assertEquals(-0.3564, scheme.delta(hiddenLayer).get(1), 0.001);

		scheme.updateWeightsAndBiases(network);
		assertEquals(0.1709, outputLayer.weights().get(0), 0.001);
		assertEquals(-0.0771, outputLayer.weights().get(1), 0.001);
		assertEquals(0.732, outputLayer.getNeurons().get(0).bias(), 0.001);

		assertEquals(-0.2997, hiddenLayer.weights().get(0), 0.001);
		assertEquals(-0.3743, hiddenLayer.weights().get(1), 0.001);

		assertEquals(-0.509, hiddenLayer.getNeuron(0).bias(), 0.001);
		assertEquals(-0.0943, hiddenLayer.getNeuron(1).bias(), 0.001);

		// System.out.println(hiddenLayer.getNeuron(1).bias());

	}

}
