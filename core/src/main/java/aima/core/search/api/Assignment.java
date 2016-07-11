package aima.core.search.api;

import java.util.Map;

/**
 * An assignment assigns values to some or all variables of a CSP.
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public interface Assignment {
	/**
	 * 
	 * @return a map of named variables and their corresponding assignments.
	 */
	Map<String, Object> getAssignments();
} 
