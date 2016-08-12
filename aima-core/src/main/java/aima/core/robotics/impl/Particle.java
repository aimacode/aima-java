package aima.core.robotics.impl;

import aima.core.robotics.datatypes.IMclMove;
import aima.core.robotics.datatypes.IMclPose;
import aima.core.robotics.datatypes.IMclVector;
import aima.core.util.Util;

/**
 * A particle consists of two elements:
 * <ol>
 * <li>A pose somewhere in the map.</li>
 * <li>A weight connected to that pose.</li>
 * </ol>
 * This class is used in the {@link MonteCarloLocalization} to form a cloud of particles.<br/>
 * The particle implements the two interfaces {@link Comparable} and {@link Cloneable} to support ordering and the {@code clone()} operation.
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 * 
 * @param <P> a pose implementing {@link IMclPose}.
 * @param <V> an n-1-dimensional vector implementing {@link IMclVector}, where n is the dimensionality of the environment. This vector describes the angle between two rays in the environment.
 * @param <M> a movement (or sequence of movements) of the robot, implementing {@link IMclMove}. 
 */
public final class Particle<P extends IMclPose<P,V,M>, V extends IMclVector, M extends IMclMove<M>> implements Comparable<Particle<P,V,M>>,Cloneable {
	
	private P pose;
	private float weight;
	
	/**
	 * Creates a particle with no pose and a zero weight.
	 */
	Particle() {
		this.pose = null;
		this.weight = 0.0f;
	}
	
	/**
	 * Creates a particle for the given pose and a weight of 1.0.
	 * @param pose the pose which the particle should be associated with.
	 */
	Particle(P pose) {
		this.pose = pose;
		weight = 1.0f;
	}
	
	/**
	 * Creates a particle for the given pose and weight.
	 * @param pose the pose which the particle should be associated with.
	 * @param weight the weight which the particle has.
	 */
	Particle(P pose, float weight) {
		this.pose = pose;
		this.weight = weight;
	}
	
	/**
	 * Returns the pose of this particle.
	 * @return the pose of this particle.
	 */
	public P getPose() { return this.pose; }
	
	/**
	 * Sets the pose of this particle.
	 * @param pose the pose to be set.
	 */
	void setPose(P pose) { this.pose = pose; }
	
	/**
	 * Returns the weight of this particle.
	 * @return the weight of this particle.
	 */
	public float getWeight() { return this.weight; }
	
	/**
	 * Sets the weight of this particle.
	 * @param weight the weight to be set.
	 */
	void setWeight(float weight) { this.weight = weight; }

	@Override
	public int compareTo(Particle<P,V,M> o) {
		if (Util.compareFloats(this.weight,o.weight)) return 0;
		if (this.weight < o.weight) return -1;
		return 1;
	}
	
	@Override
	public Particle<P,V,M> clone() {
		return new Particle<P,V,M>(this.pose.clone(),this.weight);
	}
}
