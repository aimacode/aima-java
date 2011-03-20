package aima.core.search.csp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import aima.core.util.datastructure.Pair;

/**
 * Provides informations which might be useful for a caller of a
 * constraint propagation algorithm. It maintains old domains for
 * variables and provides means to restore the initial state of the
 * CSP (before domain reduction started). Additionally, a flag indicates
 * whether an empty domain has been found during propagation. 
 * @author Ruediger Lunde
 *
 */
public class DomainRestoreInfo {
	private List<Pair<Variable, Domain>> savedDomains;
	private HashSet<Variable> affectedVariables;
	private boolean emptyDomainObserved;

	public DomainRestoreInfo() {
		savedDomains = new ArrayList<Pair<Variable, Domain>>();
		affectedVariables = new HashSet<Variable>();
	}

	public void clear() {
		savedDomains.clear();
		affectedVariables.clear();
	}

	public boolean isEmpty() {
		return savedDomains.isEmpty();
	}

	/**
	 * Stores the specified domain for the specified variable if a domain has
	 * not yet been stored for the variable.
	 */
	public void storeDomainFor(Variable var, Domain domain) {
		if (!affectedVariables.contains(var)) {
			savedDomains.add(new Pair<Variable, Domain>(var, domain));
			affectedVariables.add(var);
		}
	}

	public void setEmptyDomainFound(boolean b) {
		emptyDomainObserved = b;
	}

	/**
	 * Can be called after all domain information has been collected to reduce
	 * storage consumption.
	 * 
	 * @return this object, after removing one hashtable.
	 */
	public DomainRestoreInfo compactify() {
		affectedVariables = null;
		return this;
	}

	public boolean isEmptyDomainFound() {
		return emptyDomainObserved;
	}

	public List<Pair<Variable, Domain>> getSavedDomains() {
		return savedDomains;
	}
	
	public void restoreDomains(CSP csp) {
		for (Pair<Variable, Domain> pair : getSavedDomains())
			csp.setDomain(pair.getFirst(), pair.getSecond());
	}
	
	public String toString() {
		StringBuffer result = new StringBuffer();
		for (Pair<Variable, Domain> pair : savedDomains)
			result.append(pair.getFirst() + "=" + pair.getSecond() + " ");
		if (emptyDomainObserved)
			result.append("!");
		return result.toString();
	}
}
