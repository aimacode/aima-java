package aima.core.search.csp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Ed.): Section 6.1, Page 202. A
 * constraint satisfaction problem or CSP consists of three components, X, D,
 * and C:
 * <ul>
 * <li>X is a set of variables, {X1, ... ,Xn}.</li>
 * <li>D is a set of domains, {D1, ... ,Dn}, one for each variable.</li>
 * <li>C is a set of constraints that specify allowable combinations of values.</li>
 * </ul>
 * 
 * @author Ruediger Lunde
 */
public class CSP {

	private List<Variable> variables;
	private List<Domain> domains;
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

	/** Creates a new CSP for a fixed set of variables. */
	public CSP(List<Variable> vars) {
		variables = new ArrayList<Variable>(vars.size());
		domains = new ArrayList<Domain>(vars.size());
		constraints = new ArrayList<Constraint>();
		varIndexHash = new Hashtable<Variable, Integer>();
		cnet = new Hashtable<Variable, List<Constraint>>();
		Domain emptyDomain = new Domain(new ArrayList<Object>(0));
		int index = 0;
		for (Variable var : vars) {
			variables.add(var);
			domains.add(emptyDomain);
			varIndexHash.put(var, index++);
			cnet.put(var, new ArrayList<Constraint>());
		}
	}

	public List<Variable> getVariables() {
		return Collections.unmodifiableList(variables);
	}

	public int indexOf(Variable var) {
		return varIndexHash.get(var);
	}

	public Domain getDomain(Variable var) {
		return domains.get(varIndexHash.get(var));
	}

	public void setDomain(Variable var, Domain domain) {
		domains.set(indexOf(var), domain);
	}

	/**
	 * Replaces the domain of the specified variable by new domain,
	 * which contains all values of the old domain except the specified
	 * value.
	 */
	public void removeValueFromDomain(Variable var, Object value) {
		Domain currDomain = getDomain(var);
		List<Object> values = new ArrayList<Object>(currDomain.size());
		for (Object v : currDomain)
			if (!v.equals(value))
				values.add(v);
		setDomain(var, new Domain(values));
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

	/**
	 * Returns for binary constraints the other variable from the scope.
	 * 
	 * @return a variable or null for non-binary constraints.
	 */
	public Variable getNeighbor(Variable var, Constraint constraint) {
		List<Variable> scope = constraint.getScope();
		if (scope.size() == 2) {
			if (var == scope.get(0))
				return scope.get(1);
			else if (var == scope.get(1))
				return scope.get(0);
		}
		return null;
	}

	/**
	 * Returns a copy which contains a copy of the domains list and is in all
	 * other aspects a flat copy of this.
	 */
	public CSP copyDomains() {
		CSP result = new CSP();
		result.variables = variables;
		result.domains = new ArrayList<Domain>(domains.size());
		result.domains.addAll(domains);
		result.constraints = constraints;
		result.varIndexHash = varIndexHash;
		result.cnet = cnet;
		return result;
	}
}