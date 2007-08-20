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
	private List<Percept> percepts = new ArrayList<Percept>();

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

	public boolean equals(Object o) {
		if (o == null || !(o instanceof PerceptSequence)) {
			return super.equals(o);
		}
		return (toString().equals(((PerceptSequence) o).toString()));
	}

	public int hashCode() {
		return toString().hashCode();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		Iterator<Percept> pers = percepts.iterator();
		while (pers.hasNext()) {
			Percept p = pers.next();

			sb.append("[");
			Iterator<Object> keys = p.getSortedAttributeKeys();
			while (keys.hasNext()) {
				Object key = keys.next();

				sb.append(key);
				sb.append("==");
				sb.append(p.getAttribute(key));
				if (keys.hasNext()) {
					sb.append(", ");
				} else {
					sb.append("]");
				}

			}
			if (pers.hasNext()) {
				sb.append(", ");
			}
		}

		return sb.toString();
	}
}
