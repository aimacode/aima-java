package aima.extra.probability;

/**
 * AbstractProbabilityNumber class is an implementation of ProbabilityNumber
 * interface.
 * 
 * @author Nagaraj Poti
 */
public abstract class AbstractProbabilityNumber implements ProbabilityNumber {

	/**
	 * @return String representation of value.
	 */
	@Override
	public String toString() {
		return this.getValue().toString();
	}
}