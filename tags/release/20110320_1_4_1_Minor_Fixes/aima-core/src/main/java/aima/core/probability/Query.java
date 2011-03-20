package aima.core.probability;

import java.util.Hashtable;

/**
 * @author Ravi Mohan
 * 
 */
public class Query {

	private String queryVariable;

	private Hashtable<String, Boolean> evidenceVariables;

	public Query(String queryVariable, String[] evidenceVariables,
			boolean[] evidenceValues) {
		this.queryVariable = queryVariable;
		this.evidenceVariables = new Hashtable<String, Boolean>();
		for (int i = 0; i < evidenceVariables.length; i++) {
			this.evidenceVariables.put(evidenceVariables[i], new Boolean(
					evidenceValues[i]));
		}
	}

	public Hashtable<String, Boolean> getEvidenceVariables() {
		return evidenceVariables;
	}

	public String getQueryVariable() {
		return queryVariable;
	}
}
