/*
 * Created on Aug 3, 2005
 *
 */
package aima.learning.statistics;

import aima.util.Util;

public class Link {
	private Neuron source;
	private Neuron target;
	private double weight;

	public Link(Neuron source, Neuron target, double weight){
		this.source = source;
		this.target =target;
		Util.isNanOrInfinity(weight);
		this.weight = weight;
	}

	/**
	 * @return Returns the source.
	 */
	public Neuron source() {
		return source;
	}
	

	/**
	 * @return Returns the target.
	 */
	public Neuron target() {
		return target;
	}
	

	/**
	 * @return Returns the weight.
	 */
	public double weight() {
		return weight;
	}

	public void setWeight(double d) {
		Util.isNanOrInfinity(d);
		weight = d;
		
	}
	

}
