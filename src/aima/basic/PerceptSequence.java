package aima.basic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple object for maintaining a list of Percepts and performing equality based on the
 * value of the Percepts it contains.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class PerceptSequence {
	private final List<Percept> percepts = new ArrayList<Percept>();

	public PerceptSequence() {
	}

	public PerceptSequence(Percept p1) {
		append(p1);
	}

	public PerceptSequence(Percept p1, Percept p2) {
		append(p1);
		append(p2);
	}

	public PerceptSequence(Percept p1, Percept p2, Percept p3) {
		append(p1);
		append(p2);
		append(p3);
	}

	public void append(Percept percept) {
		assert (null != percept);

		percepts.add(percept);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof PerceptSequence)) {
			return super.equals(o);
		}
		return (toString().equals(((PerceptSequence) o).toString()));
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		Iterator<Percept> pers = percepts.iterator();
		while (pers.hasNext()) {
			Percept p = pers.next();

			sb.append(p);

			if (pers.hasNext()) {
				sb.append(", ");
			}
		}

		return sb.toString();
	}
}
