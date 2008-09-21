/*
 * Created on Sep 18, 2004
 *
 */
package aima.logic.fol;

import java.util.HashSet;
import java.util.Set;

import aima.logic.fol.parsing.ast.Constant;
import aima.logic.fol.parsing.ast.Function;

/**
 * @author Ravi Mohan
 * 
 */

public class FOLDomain {
	private Set<String> constants, functions, predicates;
	private int skolemConstantIndexical = 0;
	private int skolemFunctionIndexical = 0;

	public FOLDomain(Set<String> constants, Set<String> functions,
			Set<String> predicates) {
		this.constants = new HashSet<String>(constants);
		this.functions = new HashSet<String>(functions);
		this.predicates = new HashSet<String>(predicates);
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
	
	public String addSkolemConstant() {
		
		String sc = null;
		do {
			sc = "SC" + (skolemConstantIndexical++);			
		} while (constants.contains(sc) || functions.contains(sc) || predicates.contains(sc));
		
		addConstant(sc);
		
		return sc;
	}

	public void addFunction(String function) {
		functions.add(function);
	}
	
	public String addSkolemFunction () {
		String sf = null;
		do {
			sf = "SF" + (skolemFunctionIndexical++);			
		} while (constants.contains(sf) || functions.contains(sf) || predicates.contains(sf));
		
		addFunction(sf);
		
		return sf;
	}

	public void addPredicate(String predicate) {
		predicates.add(predicate);
	}
}