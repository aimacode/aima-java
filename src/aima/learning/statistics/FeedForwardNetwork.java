/*
 * Created on Aug 3, 2005
 *
 */
package aima.learning.statistics;

import java.util.ArrayList;
import java.util.List;

import aima.probability.Randomizer;

/**
 * @author Ravi Mohan
 * 
 */

public class FeedForwardNetwork {
	private List<Layer> layers;

	public FeedForwardNetwork() {
		layers = new ArrayList<Layer>();
	}

	public FeedForwardNetwork(int neuronsInInputLayer, int neuronsInOuterLayer,
			Randomizer random) {
		this();
		Layer inputLayer = new Layer(neuronsInInputLayer, 1.0,
				new SigmoidActivationFunction());
		Layer outputLayer = new Layer(neuronsInOuterLayer, 1.0,
				new IdentityActivationFunction());
		addLayer(inputLayer, random);
		addLayer(outputLayer, random);
	}

	public FeedForwardNetwork(int neuronsInInputLayer,
			int neuronsInHiddenLayer, int neuronsInOutoutLayer,
			Randomizer random) {
		this();
		Layer inputLayer = new Layer(neuronsInInputLayer, 1.0,
				new SigmoidActivationFunction());
		Layer hiddenLayer = new Layer(neuronsInHiddenLayer, 1.0,
				new SigmoidActivationFunction());
		Layer outputLayer = new Layer(neuronsInOutoutLayer, 1.0,
				new IdentityActivationFunction());

		addLayer(inputLayer, random);
		addLayer(hiddenLayer, random);
		addLayer(outputLayer, random);
	}

	public void addLayer(Layer layer, Randomizer weightSource) {
		if (atLeastOneLayerPresent()) {
			getLastLayer().connectTo(layer, weightSource);
		}
		layers.add(layer);

	}

	public void propogateInput(List<Double> input) {
		getInputLayer().acceptInput(input);
		if (layerCount() > 2) {
			for (Layer hiddenLayer : getHiddenLayers()) {
				hiddenLayer.update();
			}
		}
		getOutputLayer().update();
	}

	public List<Double> output() {
		return getOutputLayer().activation();
	}

	public List<Double> error(List<Double> expectedOutput) {
		return getOutputLayer().getError(expectedOutput);
	}

	private boolean atLeastOneLayerPresent() {
		return layers.size() > 0;
	}

	private Layer getLastLayer() {
		if (layers.size() == 0) {
			throw new RuntimeException(
					"cannot call this method on network with zero layers");
		}
		return layers.get(layers.size() - 1);
	}

	public Layer getOutputLayer() {
		return getLastLayer();
	}

	public Layer getInputLayer() {
		return layers.get(0);
	}

	public List<Layer> getHiddenLayers() {
		if (layers.size() < 3) {
			throw new RuntimeException(
					"cannot call this method on network with " + layers.size()
							+ " layers");
		}
		List<Layer> hidden = new ArrayList<Layer>();
		for (int i = 1; i < layers.size() - 1; i++) {
			hidden.add(layers.get(i));
		}

		return hidden;
	}

	public int layerCount() {

		return layers.size();
	}

}
