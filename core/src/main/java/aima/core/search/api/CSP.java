package aima.core.search.api;

import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (4th Ed.): Section ??, Page
 * ???.<br>
 * <br>
 * A constraint satisfaction problem or CSP consists of three components, X, D,
 * and C:
 * <ul>
 * <li>X is a set of variables, {X1, ... ,Xn}.</li>
 * <li>D is a set of domains, {D1, ... ,Dn}, one for each variable.</li>
 * <li>C is a set of constraints that specify allowable combinations of
 * values.</li>
 * </ul>
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public interface CSP {
	/**
	 * 
	 * @return a set of variable names (returned in a list for access and
	 *         matching to domains convenience, implementation must guarantee
	 *         Set semantics)
	 */
	List<String> getVariables();

	/**
	 * Lookup the index of a variable.
	 * 
	 * @param variable
	 *            the variable whose index is to be looked up.
	 * @return the index of the variable.
	 */
	default int indexOf(String variable) {
		return getVariables().indexOf(variable);
	}

	/**
	 * 
	 * @return a set of domains (returned in a list for access and matching to
	 *         variables convenience, implementation must guarantee Set
	 *         semantics)
	 */
	List<Domain> getDomains();
	
	default Domain getDomain(String variable) {
		return getDomains().get(indexOf(variable));
	}
	
	
	default List<Object> getDomainValues(String variable) {
		return getDomain(variable).getValues();
	}

	/**
	 * 
	 * @return a set of constraints that specify allowable combinations of
	 *         values (returned in a list for access convenience, implementation
	 *         must guarantee Set semantics).
	 */
	List<Constraint> getConstraints();
}
