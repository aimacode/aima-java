package aima.core.search.local;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 127.<br>
 * <br>
 * A state in a genetic algorithm is represented as an individual from the
 * population.
 * 
 * @author Ciaran O'Reilly
 * 
 * @param <A>
 *            the type of the alphabet used in the representation of the
 *            individuals in the population (this is to provide flexibility in
 *            terms of how a problem can be encoded).
 */
public class Individual<A> {
	private List<A> representation = new ArrayList<A>();
	private int descendants; // for debugging!

	/**
	 * Construct an individual using the provided representation.
	 * 
	 * @param representation
	 *            the individual's representation.
	 */
	public Individual(List<A> representation) {
		this.representation = Collections.unmodifiableList(representation);
	}

	/**
	 * 
	 * @return the individual's representation.
	 */
	public List<A> getRepresentation() {
		return representation;
	}

	/**
	 * 
	 * @return the length of the individual's representation.
	 */
	public int length() {
		return representation.size();
	}

	/**
	 * Should be called by the genetic algorithm whenever the individual is
	 * selected to produce a descendant.
	 */
	public void incDescendants() {
		descendants++;
	}

	/** Returns the number of descendants for this individual. */
	public int getDescendants() {
		return descendants;
	}

	@Override
	public String toString() {
		return representation.toString() + descendants;
	}
}