/*
 * Created on Aug 6, 2005
 *
 */
package aima.learning.statistics;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import aima.util.Util;

public class PerceptronLearning implements NeuralNetworkTrainingScheme {
	
	private Hashtable<Neuron, Double> neuronDeltaMap;
	private Hashtable<Neuron, Double> neuronBiasMap;
	private Hashtable<Link, Double> linkWeightMap;
	private double learningRate;

	public PerceptronLearning() {
		neuronDeltaMap = new Hashtable<Neuron, Double>();
		neuronBiasMap = new Hashtable<Neuron, Double>();
		linkWeightMap = new Hashtable<Link, Double>();
		learningRate = 0.1;
	}

	public void backPropogate(FeedForwardNetwork network, List<Double> input,
			List<Double> correctOutput) {
		if (network.layerCount()!=2){
			throw new RuntimeException("Perceptron larning can be used only with 2 layer networks. This one has "+network.layerCount());
		}
		network.propogateInput(input);
		calculateDelta(network,correctOutput);
	}

	

	public void updateWeightsAndBiases(FeedForwardNetwork network) {
		Layer last = network.getOutputLayer();
		for (Neuron n :last.getNeurons()){
			double delta =  neuronDeltaMap.get(n);
			neuronBiasMap.put(n,n.bias());
			n.setBias(n.bias() - learningRate* delta) ;
			for (Link link : n.inLinks()){
				linkWeightMap.put(link,link.weight());
				 double weightChange = (learningRate * delta * link.source().activation());
				link.setWeight(link.weight() - weightChange);
			}
		}
		
	}
	
	private void calculateDelta(FeedForwardNetwork network, List<Double> correctOutput) {
		Layer outputLayer = network.getOutputLayer();
		Iterator<Neuron> neuronIter = outputLayer.iterator();
		Iterator<Double> errorIter = outputLayer.getError(correctOutput)
				.iterator();

		while (neuronIter.hasNext() && errorIter.hasNext()) {
			// multiplied by -1 because the error calculationis inverted from the book
			neuronDeltaMap.put(neuronIter.next(), -1 *  errorIter.next());
		}
	}

	public double error(List<Double> expectedOutput, FeedForwardNetwork network) {
		return Util.sumOfSquares(network.error(expectedOutput));
	}

}
