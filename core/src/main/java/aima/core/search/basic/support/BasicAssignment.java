package aima.core.search.basic.support;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import aima.core.search.api.Assignment;

/**
 * Basic implementation of the Assignment interface.
 * 
 * @author Ciaran O'Reilly
 *
 */
public class BasicAssignment implements Assignment {
	private Map<String, Object> assignments = new LinkedHashMap<String, Object>();
	private Map<String, List<Object>> domainsReducedBy = new LinkedHashMap<>();
	
	public BasicAssignment() {
	}
	
	public BasicAssignment(Assignment toCopyAssignment) {
		add(toCopyAssignment);
	}
	
	//
	// Assignment tracking
	@Override
	public Map<String, Object> getAssignments() {
		return assignments;
	}

	@Override
	public Object add(String var, Object value) {
		return assignments.put(var, value);
	}
	
	@Override
	public boolean remove(String var, Object value) {
		return assignments.remove(var, value);
	}
	
	//
	// Domain tracking
	@Override
	public boolean reducedDomain(String var, Object value) {
		boolean reduced = false;
		
		List<Object> varReducedBy = domainsReducedBy.get(var);
		if (varReducedBy == null) {
			varReducedBy = new ArrayList<>();
			domainsReducedBy.put(var, varReducedBy);
		}
		if (!varReducedBy.contains(value)) {
			reduced = varReducedBy.add(value);
		}
		
		return reduced;
	}

	@Override
	public boolean restoredDomain(String var, Object value) {
		boolean restored = false;
		List<Object> varReducedBy = domainsReducedBy.get(var);
		if (varReducedBy != null) {
			restored = varReducedBy.remove(value);
		}
		return restored;
	}

	@Override
	public Map<String, List<Object>> getDomainsReducedBy() {
		return domainsReducedBy;
	}
}