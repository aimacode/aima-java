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
	 * 
	 * @return a set of domains (returned in a list for access and matching to
	 *         variables convenience, implementation must guarantee Set
	 *         semantics)
	 */
	List<Domain> getDomains();

	/**
	 * 
	 * @return a set of constraints that specify allowable combinations of
	 *         values (returned in a list for access convenience, implementation
	 *         must guarantee Set semantics).
	 */
	List<Constraint> getConstraints();
}
