package aima.core.search.csp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import aima.core.util.datastructure.Pair;

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
		if (!affectedVariables.contains(var))
			savedDomains.add(new Pair<Variable, Domain>(var, domain));
	}

	public void setEmptyDomainFound(boolean b) {
		emptyDomainObserved = b;
	}

	/**
	 * Can be called after all domain information has been collected to reduce
	 * storage consumption.
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
}
