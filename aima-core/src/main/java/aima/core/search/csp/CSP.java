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
	
	public CSP(List<Variable> vars) {
		variables = new ArrayList<Variable>();
		domains = new ArrayList<Domain>();
		constraints = new ArrayList<Constraint>();
		varIndexHash = new Hashtable<Variable, Integer>();
		cnet = new Hashtable<Variable, List<Constraint>>();
		int index = 0;
		for (Variable var : vars) {
			variables.add(var);
			domains.add(new Domain());
			varIndexHash.put(var, index++);
			cnet.put(var, new ArrayList<Constraint>());
		}
	}

	public List<Variable> getVariables() {
		return variables;
	}

	public Domain getDomain(Variable var) {
		return domains.get(varIndexHash.get(var));
	}

	public void setDomain(Variable var, List<?> values) {
		domains.add(varIndexHash.get(var), new Domain(values));
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
		result.domains = new ArrayList<Domain>(domains.size());
		for (Domain domain : domains)
			result.domains.add(new Domain(domain));
		result.constraints = constraints;
		result.varIndexHash = varIndexHash;
		result.cnet = cnet;
		return result;
	}
}