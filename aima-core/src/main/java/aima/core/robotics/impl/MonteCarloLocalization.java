package aima.core.robotics.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import aima.core.probability.bayes.approx.ParticleFiltering;
import aima.core.robotics.IMcl;
import aima.core.robotics.IMclMap;
import aima.core.robotics.IMclRobot;
import aima.core.robotics.datatypes.IMclMove;
import aima.core.robotics.datatypes.IMclPose;
import aima.core.robotics.datatypes.IMclRangeReading;
import aima.core.robotics.datatypes.IMclVector;
import aima.core.robotics.datatypes.RobotException;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page TODO.<br>
 * <br>
 * 
 * As stated on page TODO a Monte-Carlo-Localization is an extension of a {@link ParticleFiltering}.
 * This is true for the functionality but this implementation can not extend the implementation of the ParticleFiltering
 * as both implementations only contain the actual algorithm as a single method. (Thus it is divided into 4 additonal methods in this class.)
 * 
 * 
 * The interface {@link IMcl} that defines the functionality for the Monte-Carlo-Localization is implemented with this class.
 * It is possible to reduce the steps needed for the localization by tweaking the parameters {@code initialParticleCount}, {@code weightCutOff} and {@code maxDistance}.
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 * 
 * @param <P> a pose implementing {@link IMclPose}.
 * @param <V> an n-1-dimensional vector implementing {@link IMclVector}, where n is the dimensionality of the environment. This vector describes the angle between two rays in the environment.
 * @param <M> a movement (or sequence of movements) of the robot implementing {@link IMclMove}. 
 * @param <R> a range measurement implementing {@link IMclRangeReading}.
 */
public final class MonteCarloLocalization<P extends IMclPose<P,V,M>, V extends IMclVector, M extends IMclMove<M>, R extends IMclRangeReading<R,V>> implements IMcl<P,V,M,R> {
	
	private final IMclMap<P,V,M,R> map;
	private final IMclRobot<V,M,R> robot;
	
	private int particleCount;
	private double rememberFactor;
	private double weightCutOff;
	private double maxDistance;
	
	private boolean isSorted = true;
	private ArrayList<Particle<P,V,M>> particleCloud;
	private Random randomGenerator;
	
	/**
	 * @param map an instance of a class implementing {@link IMclMap}.
	 * @param robot an instance of a class implementing {@link IMclRobot}.
	 */
	public MonteCarloLocalization(IMclMap<P,V,M,R> map, IMclRobot<V,M,R> robot) {
		this.map = map;
		this.robot = robot;
		this.randomGenerator = new Random();
	}
	
	/**
	 * Sets the initial size of the particle cloud.
	 * @param particleCount the number of elements in the particle cloud.
	 */
	public void setParticleCount(int particleCount) {
		this.particleCount = particleCount;
	}
	
	/**
	 * Sets the factor by which the cloud shrinks each time.
	 * @param rememberFactor the factor that is used to calculate the new size of the particle cloud.
	 */
	public void setRememberFactor(double rememberFactor) {
		this.rememberFactor = rememberFactor; 
	}
	
	/**
	 * Sets the minimum weight of the particles.
	 * @param cutOff the minimum weight below which the corresponding particle gets removed during the {@code resample()} step. Set to zero when in doubt.
	 */
	public void setWeightCutOff(double cutOff) {
		this.weightCutOff = cutOff;
	}
	
	/**
	 * Sets the maximum distance between the particles to localize successfully.
	 * @param maxDistance the distance between the particle with the highest weight and the particle with the lowest weight has to fall below for the algorithm to finish.
	 */ 
	public void setMaxDistance(double maxDistance) {
		this.maxDistance = maxDistance;
	}
	
	/**
	 * Used for displaying the particles.
	 * @return the particle cloud including all particles.
	 */
	public final ArrayList<Particle<P, V, M>> getParticles() {
		return particleCloud;
	}
	
	@Override
	public void generateCloud() {
		this.particleCloud = new ArrayList<Particle<P,V,M>>(particleCount);
		for(int i=0;i<particleCount;i++) {
			this.particleCloud.add(new Particle<P,V,M>(map.randomPose()));
		}
		isSorted = true;
	}
	
	@Override
	public void applyMove(M move) {
		for(Particle<P,V,M> particle: particleCloud) {
			particle.setPose(particle.getPose().applyMovement(move.generateNoise()));
		}
	}
	
	@Override
	public void weightParticles(R[] rangeReadings) {
		isSorted = false;
		float totalWeight = 0.0f;
		for(Particle<P,V,M> particle: particleCloud) {
			if(map.isPoseValid(particle.getPose())) {
				particle.setWeight(1.0f);
				for(int i=0;i<rangeReadings.length;i++) {
					particle.setWeight(particle.getWeight() * robot.calculateWeight(rangeReadings[i].addRangeNoise(), map.rayCast(particle.getPose().addAngle(rangeReadings[i].getAngle()))));
				}
				if(particle.getWeight() > weightCutOff) totalWeight += particle.getWeight();
			} else {
				particle.setWeight(0.0f);
			}
		}
		//Sum-Normalize values:
		float previousWeight = 0.0f;
		for(Particle<P,V,M> particle: particleCloud) {
			if(particle.getWeight() > weightCutOff) {
				previousWeight += (particle.getWeight() / totalWeight);
				particle.setWeight(previousWeight);
			}
		}
	}
	
	@Override
	public void resampleParticles() {
		if(!isSorted) Collections.sort(particleCloud);
		final int newParticleCount = (int) (particleCloud.size() * rememberFactor);
		while(!particleCloud.isEmpty()) {
			Particle<P, V, M> particle = particleCloud.get(0);
			if(particle.getWeight() <= weightCutOff) particleCloud.remove(0);
			else break;
		}
		if(particleCloud.isEmpty() || newParticleCount == 0) generateCloud(); //If all particleCloud are below weightCutOff, generate a new set of Particles, as we are lost.
		else {
			List<Particle<P, V, M>> oldParticles = particleCloud;
			//WEIGHTED SAMPLE WITH REPLACEMENT:
			particleCloud = new ArrayList<Particle<P,V,M>>(newParticleCount);
			for(int i=0; i < newParticleCount; i++) {
				final float rand = randomGenerator.nextFloat();
				int j = 0;
				while(j < oldParticles.size() && rand > oldParticles.get(j).getWeight()) j++;
				if(j < oldParticles.size()) particleCloud.add(oldParticles.get(j).clone());
				else particleCloud.add(oldParticles.get(oldParticles.size() - 1).clone());
			}
			isSorted = false;
		}
	}
	
	@Override
	public P getPose() {
		if(!isSorted) Collections.sort(particleCloud);
		isSorted = true;
		Particle<P,V,M> first = particleCloud.get(0);
		double maxDistance = 0.0d;
		for(Particle<P, V, M> p:particleCloud) {
			double distance = first.getPose().distanceTo(p.getPose());
			maxDistance = distance > maxDistance ? distance : maxDistance;
		}
		if(maxDistance <= this.maxDistance) return map.getAverage(new PoseIterator(), particleCloud.size());
		return null;
	}

	@Override
	public P localize() {
		P result = null;
		try {
			while(true) {
				applyMove(robot.performMove());
				weightParticles(robot.getRangeReadings());
				result = getPose();
				if(result != null) break;
				resampleParticles();
			}
		} catch (RobotException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * A simple implementation of the {@link Iterator} interface over the poses in the particles in the cloud.
	 * The remove operation is not permitted nor implemented on this iterator.
	 * 
	 * @author Arno von Borries
	 * @author Jan Phillip Kretzschmar
	 * @author Andreas Walscheid
	 *
	 */
	private class PoseIterator implements Iterator<P> {

		private int index = 0;
		
		@Override
		public boolean hasNext() {
			return index < particleCloud.size();
		}

		@Override
		public P next() {
			return hasNext() ? particleCloud.get(index++).getPose() : null;
		}

		/**
		 * The remove operation is not permitted on the pose cloud as it would make the correspondent particle useless.
		 */
		@Override
		public void remove() { }
	}
}
