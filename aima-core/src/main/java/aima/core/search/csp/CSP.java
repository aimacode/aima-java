package aima.core.search.csp;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Ed.): Section 6.1, Page 206. A
 * constraint satisfaction problem or CSP consists of three components, X,D, and
 * C:
 * <ul><li>X is a set of variables, {X1, ... ,Xn}.</li>
 *     <li>D is a set of domains, {D1, ... ,Dn}, one for each variable.</li>
 *     <li>C is a set of constraints that specify allowable combinations of
 *         values.</li></ul>
 * 
 * @author Ruediger Lunde
 */
public class CSP {

	private Variable[] variables;
	private Domain[] domains;
	private List<Constraint> constraints;
	
	/** Lookup, which maps a variable to its index in the list of variables. */
	private Hashtable<Variable, Integer> varIndexHash;
	/**
	 * Constraint network. Maps variables to those constraints in which they
	 * participate.
	 */
	private Hashtable<Variable, List<Constraint>> cnet;

	private CSP() {
	}
	
	public CSP(List<Variable> vars) {
		variables = new Variable[vars.size()];
		domains = new Domain[vars.size()];
		constraints = new ArrayList<Constraint>();
		varIndexHash = new Hashtable<Variable, Integer>();
		cnet = new Hashtable<Variable, List<Constraint>>();
		Domain emptyDomain = new Domain(new ArrayList<Object>(0));
		int index = 0;
		for (Variable var : vars) {
			variables[index] = var;
			domains[index] = emptyDomain;
			varIndexHash.put(var, index);
			cnet.put(var, new ArrayList<Constraint>());
			++index;
		}
	}

	public Variable[] getVariables() {
		return variables;
	}

	public int indexOf(Variable var) {
		return varIndexHash.get(var);
	}
	
	public Domain getDomain(Variable var) {
		return domains[varIndexHash.get(var)];
	}

	public void setDomain(Variable var, List<?> values) {
		domains[varIndexHash.get(var)] = new Domain(values);
	}
	
	public void removeValueFromDomain(Variable var, Object value) {
		Domain currDomain = getDomain(var);
		List<Object> values = new ArrayList<Object>(currDomain.size());
		for (Object v : currDomain)
			if (!v.equals(value))
				values.add(v);
		setDomain(var, values);
	}

	public List<Constraint> getConstraints() {
		return constraints;
	}

	/**
	 * Returns all constraints in which the specified variable participates.
	 */
	public List<Constraint> getConstraints(Variable var) {
		return cnet.get(var);
	}

	public void addConstraint(Constraint constraint) {
		constraints.add(constraint);
		for (Variable var : constraint.getScope())
			cnet.get(var).add(constraint);
	}
	
	public CSP copyForPropagation() {
		CSP result = new CSP();
		result.variables = variables;
		result.domains = new Domain[domains.length];
		for (int i = 0; i < domains.length; i++)
			result.domains[i] = domains[i];
		result.constraints = constraints;
		result.varIndexHash = varIndexHash;
		result.cnet = cnet;
		return result;
	}
}