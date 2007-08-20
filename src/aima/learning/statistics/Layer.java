/*
 * Created on Aug 3, 2005
 *
 */
package aima.learning.statistics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import aima.probability.Randomizer;

/**
 * @author Ravi Mohan
 * 
 */

public class Layer {
	private List<Neuron> neurons;

	public Layer(int n) {
		neurons = new ArrayList<Neuron>(n);
		for (int i = 0; i < n; i++) {
			neurons.add(new Neuron());
		}
	}

	public Layer(int n, double bias, ActivationFunction function) {
		neurons = new ArrayList<Neuron>(n);
		for (int i = 0; i < n; i++) {
			neurons.add(new Neuron(bias, function));
		}
	}

	public Layer(int n, List<Double> biases, ActivationFunction function) {
		neurons = new ArrayList<Neuron>(n);
		for (int i = 0; i < n; i++) {
			neurons.add(new Neuron(biases.get(i), function));
		}
	}

	public Neuron getNeuron(int zeroBasedIndex) {
		return neurons.get(zeroBasedIndex);
	}

	public void acceptInput(List<Double> input) {
		for (int i = 0; i < input.size(); i++) {
			neurons.get(i).acceptAsInput(input.get(i));
		}

	}

	public List<Double> getError(List<Double> values) {
		List<Double> error = new ArrayList<Double>();
		for (int i = 0; i < values.size(); i++) {
			error.add(neurons.get(i).error(values.get(i)));
		}
		return error;
	}

	public void connectTo(Layer layer, Randomizer weights) {
		for (Neuron source : neurons) {

			for (Neuron target : layer.neurons) {
				source.connectTo(target, weights.nextDouble());
			}
		}
	}

	public void update() {
		for (Neuron n : neurons) {
			n.update();
		}
	}

	public List<Double> activation() {
		List<Double> result = new ArrayList<Double>();
		for (Neuron n : neurons) {
			result.add(n.activation());
		}
		return result;
	}

	public Iterator<Neuron> iterator() {
		return neurons.iterator();
	}

	/**
	 * @return Returns the neurons.
	 */
	public List<Neuron> getNeurons() {
		return neurons;
	}

	public List<Double> weights() {
		List<Double> weights = new ArrayList<Double>();
		for (Neuron n : neurons) {
			for (Double weight : n.weights()) {
				weights.add(weight);
			}
		}
		return weights;
	}

}
