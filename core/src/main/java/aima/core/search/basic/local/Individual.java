package aima.core.search.basic.local;

import java.util.ArrayList;
import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): page ??.<br>
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
	private List<A> representation = new ArrayList<>();

	public Individual(List<A> representation) {
		this.representation.addAll(representation);
	}

	public Individual(List<A> representationFromParentX, List<A> representationFromParentY) {
		this.representation.addAll(representationFromParentX);
		this.representation.addAll(representationFromParentY);
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
	 * @param fromIndex
	 *            low endpoint (inclusive) of the individual's representation
	 * @param toIndex
	 *            high endpoint (exclusive) of the individual's representation
	 * @return a 'substring' of the individual's representation.
	 */
	public List<A> substring(int fromIndex, int toIndex) {
		return representation.subList(fromIndex, toIndex);
	}

	/**
	 * 
	 * @return the length of the individual's representation.
	 */
	public int length() {
		return representation.size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return super.equals(o);
		}
		return this.representation.equals(((Individual<A>) o).representation);
	}

	@Override
	public int hashCode() {
		return representation.hashCode();
	}

	@Override
	public String toString() {
		return representation.toString();
	}
}
