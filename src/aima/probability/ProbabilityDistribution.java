/*
 * Created on Dec 28, 2004
 *
 */
package aima.probability;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import aima.logic.propositional.algorithms.Model;

/**
 * @author Ravi Mohan
 * 
 */

public class ProbabilityDistribution {
	private List<Row> rows;

	String[] variableNames;

	public ProbabilityDistribution(String variableNameOne) {
		this(new String[] { variableNameOne });
	}

	public ProbabilityDistribution(String variableNameOne,
			String variableNameTwo) {
		this(new String[] { variableNameOne, variableNameTwo });
	}

	public ProbabilityDistribution(String variableNameOne,
			String variableNameTwo, String variableNameThree) {
		this(
				new String[] { variableNameOne, variableNameTwo,
						variableNameThree });
	}

	public ProbabilityDistribution(String variableNameOne,
			String variableNameTwo, String variableNameThree,
			String variableNameFour) {
		this(new String[] { variableNameOne, variableNameTwo,
				variableNameThree, variableNameFour });
	}

	public ProbabilityDistribution(String[] variableNames) {
		this.variableNames = variableNames;
		rows = new ArrayList<Row>();
	}

	public void set(boolean[] values, double probability) {
		Model m = new Model();
		for (int i = 0; i < variableNames.length; i++) {
			m = m.extend(variableNames[i], values[i]);
		}
		rows.add(new Row(m, probability));
	}

	public void set(boolean value1, double probability) {
		set(new boolean[] { value1 }, probability);
	}

	public void set(boolean value1, boolean value2, double probability) {
		set(new boolean[] { value1, value2 }, probability);
	}

	public void set(boolean value1, boolean value2, boolean value3,
			double probability) {
		set(new boolean[] { value1, value2, value3 }, probability);
	}

	public void set(boolean value1, boolean value2, boolean value3,
			boolean value4, double probability) {
		set(new boolean[] { value1, value2, value3, value4 }, probability);
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (Row row : rows) {
			buf.append(row.toString() + "\n");
		}
		return buf.toString();

	}

	public double probabilityOf(Hashtable conditions) {
		double prob = 0.0;
		for (Row row : rows) {
			Iterator iter = conditions.keySet().iterator();
			boolean rowMeetsAllConditions = true;
			while (iter.hasNext()) {
				String variable = (String) iter.next();
				boolean value = ((Boolean) conditions.get(variable))
						.booleanValue();
				if (!(row.model.matches(variable, value))) {
					rowMeetsAllConditions = false;
					break;
					// return false;
				}
			}
			if (rowMeetsAllConditions) {
				prob += row.probability;
			}
		}

		return prob;
	}

	class Row {
		Model model;

		double probability;

		Row(Model m, double probability) {
			model = m;
			this.probability = probability;
		}

		@Override
		public String toString() {
			return model.toString() + " => " + probability;
		}
	}

	public double probabilityOf(String variableName, boolean b) {
		Hashtable<String, Boolean> h = new Hashtable<String, Boolean>();
		h.put(variableName, new Boolean(b));
		return probabilityOf(h);
	}

}