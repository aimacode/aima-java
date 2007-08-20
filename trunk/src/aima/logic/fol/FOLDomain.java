/*
 * Created on Sep 18, 2004
 *
 */
package aima.logic.fol;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Ravi Mohan
 * 
 */

public class FOLDomain {
	private Set<String> constants, functions, predicates;

	public FOLDomain(Set<String> constants, Set<String> functions,
			Set<String> predicates) {
		this.constants = constants;
		this.functions = functions;
		this.predicates = predicates;
	}

	public FOLDomain() {
		this.constants = new HashSet<String>();
		this.functions = new HashSet<String>();
		this.predicates = new HashSet<String>();
	}

	public Set<String> getConstants() {
		return constants;
	}

	public Set<String> getFunctions() {
		return functions;
	}

	public Set<String> getPredicates() {
		return predicates;
	}

	public void addConstant(String constant) {
		constants.add(constant);
	}

	public void addFunction(String function) {
		functions.add(function);
	}

	public void addPredicate(String predicate) {
		predicates.add(predicate);
	}
}