package aima.core.probability.reasoning;

/**
 * @author Ravi Mohan
 * 
 */
public class Particle {

	private String state;

	private double weight;

	public Particle(String state, double weight) {
		this.state = state;
		this.weight = weight;
	}

	public Particle(String state) {
		this(state, 0);
	}

	public boolean hasState(String aState) {
		return state.equals(aState);
	}

	public String getState() {
		return state;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double particleWeight) {
		weight = particleWeight;
	}
}
