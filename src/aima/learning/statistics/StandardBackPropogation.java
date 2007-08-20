/*
 * Created on Aug 5, 2005
 *
 */
package aima.learning.statistics;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import aima.util.Util;

public class StandardBackPropogation implements NeuralNetworkTrainingScheme {

	private Hashtable<Neuron, Double> neuronDeltaMap,neuronBiasMap;
	private Hashtable<Link, Double> linkWeightMap;
	private double learningRate;
	
	public StandardBackPropogation() {
		neuronDeltaMap = new Hashtable<Neuron, Double>();
		neuronBiasMap = new Hashtable<Neuron, Double>();
		linkWeightMap = new Hashtable<Link, Double>();
		learningRate = 0.1;
	}

	public void backPropogate(FeedForwardNetwork network, List<Double> input,
			List<Double> correctOutput) {
		network.propogateInput(input);
		calculateOutputLayerDelta(network, correctOutput);
		calculateHiddenLayersDelta(network);

	}

	private void calculateOutputLayerDelta(FeedForwardNetwork network,
			List<Double> correctOutput) {
		Layer outputLayer = network.getOutputLayer();
		Iterator<Neuron> neuronIter = outputLayer.iterator();
		Iterator<Double> errorIter = outputLayer.getError(correctOutput)
				.iterator();
		 

		while (neuronIter.hasNext() && errorIter.hasNext()) {
			Neuron n = neuronIter.next();
			Double error = errorIter.next();
			Util.checkForNanOrInfinity(n.netInput());
			Util.checkForNanOrInfinity(n.getActivationFuncton().deriv(n.netInput()));
		
			Util.checkForNanOrInfinity(error);
			
			double delta =  -2.0 * n.getActivationFuncton().deriv(n.netInput())*error;
			Util.checkForNanOrInfinity(delta);
			neuronDeltaMap.put(n, delta );
		}

	}



	private void calculateHiddenLayersDelta(FeedForwardNetwork network) {
		List<Layer>  hiddenLayers =  network.getHiddenLayers();
		for (int i = hiddenLayers.size() - 1;i> -1;i-- ){
			Layer layer = hiddenLayers.get(i);
			for (Neuron neuron :layer.getNeurons()){
				double weightsum =0.0;
				for (Link l :neuron.outLinks()){
					weightsum += (l.weight()*neuronDeltaMap.get(l.target()));
				}
				Util.checkForNanOrInfinity(weightsum);
				Util.checkForNanOrInfinity(neuron.netInput());
				double delta = weightsum * neuron.getActivationFuncton().deriv(neuron.netInput());
				Util.checkForNanOrInfinity(delta);
				
				neuronDeltaMap.put(neuron,delta);
			}
		}

	}
	
	public List<Double> delta(Layer l){
		List<Double> list = new ArrayList<Double>();
		for (Neuron n :l.getNeurons()){
			list.add(neuronDeltaMap.get(n));
		}
		return list;
	}

	public void updateWeightsAndBiases(FeedForwardNetwork network) {
		for (Neuron n :network.getOutputLayer().getNeurons()){
			updateWeightsAndBiases(n);
		}
		for (Layer hiddenLayer:network.getHiddenLayers()){
			for (Neuron n :hiddenLayer.getNeurons()){
				updateWeightsAndBiases(n);
			}
		}
		
	}

	private void updateWeightsAndBiases(Neuron n) {
		Double delta =  neuronDeltaMap.get(n);
		neuronBiasMap.put(n,n.bias());
		n.setBias(n.bias() - learningRate* delta) ;
		for (Link link : n.inLinks()){
			linkWeightMap.put(link,link.weight());
			 double weightChange = (learningRate * delta * link.source().activation());
			link.setWeight(link.weight() - weightChange);
		}
		
	}

	public double error(List<Double> expectedOutput,FeedForwardNetwork network) {
		return Util.sumOfSquares(network.error(expectedOutput));
	}
	
	
}
