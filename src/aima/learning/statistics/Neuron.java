package aima.learning.statistics;

import java.util.ArrayList;
import java.util.List;

import aima.util.Util;

public class Neuron {

	private double bias;

	private ActivationFunction activationFuncton;

	private List<Link> inLinks, outLinks;

	private boolean inputAccepted;

	private double activation = 0;

	public Neuron() {
		this.activationFuncton = new SigmoidActivationFunction();
		this.bias = 1.0;
		inLinks = new ArrayList<Link>();
		outLinks = new ArrayList<Link>();
		inputAccepted = false;
	}

	public Neuron(double bias) {
		this();
		this.bias = bias;
	}

	public Neuron(double bias, ActivationFunction activationFunction) {
		this(bias);
		this.activationFuncton = activationFunction;
	}

	public double bias() {
		return bias;
	}

	public double error(double value) {
		if (Double.isNaN(value)) {
			// System.out.println("input value is NAN");
		}
		if (Double.isNaN(activation)) {
			// System.out.println("activation is NAN");
		}
		return value - activation;
	}

	public double netInput() {
		double sum = 0;
		for (Link link : inLinks) {

			sum += link.source().activation() * link.weight();

		}
		Util.checkForNanOrInfinity(sum);
		Util.checkForNanOrInfinity(bias);
		sum += bias;

		return sum;
	}

	public double activation() {
		return activation;
	}

	public void update() {
		if (!inputAccepted) {
			activation = activationFuncton.activation(netInput());
		}
	}

	public List<Link> inLinks() {
		return inLinks;
	}

	public List<Link> outLinks() {
		return outLinks;
	}

	public void connectTo(Neuron n, double d) {
		Link link = new Link(this, n, d);
		this.outLinks.add(link);
		n.inLinks.add(link);
	}

	public List<Double> weights() {
		List<Double> weights = new ArrayList<Double>();
		for (Link link : inLinks) {
			weights.add(link.weight());
		}
		return weights;
	}

	public void acceptAsInput(double d) {
		inputAccepted = true;
		activation = d;

	}

	/**
	 * @return Returns the activationFuncton.
	 */
	public ActivationFunction getActivationFuncton() {
		return activationFuncton;
	}

	public void setBias(double bias) {
		Util.checkForNanOrInfinity(bias);
		this.bias = bias;

	}

}
