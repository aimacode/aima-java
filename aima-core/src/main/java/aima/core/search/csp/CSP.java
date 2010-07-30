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
	private Domains domains;
	private List<Constraint> constraints;
	/**
	 * Constraint network. Maps variables to those constraints in which they
	 * participate.
	 */
	private Hashtable<Variable, List<Constraint>> cnet;

	public CSP(List<Variable> vars) {
		variables = new ArrayList<Variable>();
		domains = new Domains(vars);
		constraints = new ArrayList<Constraint>();
		cnet = new Hashtable<Variable, List<Constraint>>();
		for (Variable var : vars) {
			variables.add(var);
			cnet.put(var, new ArrayList<Constraint>());
		}
	}

	public List<Variable> getVariables() {
		return variables;
	}

	public List<?> getDomain(Variable var) {
		return domains.getDomain(var);
	}

	public void setDomain(Variable var, List<?> values) {
		domains.setDomain(var, values);
	}

	public List<Constraint> getConstraints() {
		return constraints;
	}

	/**
	 * Returns all constraints in which the specified variable occurs.
	 */
	public List<Constraint> getConstraints(Variable var) {
		return cnet.get(var);
	}

	public void addConstraint(Constraint constraint) {
		constraints.add(constraint);
		for (Variable var : constraint.getScope())
			cnet.get(var).add(constraint);
	}
}