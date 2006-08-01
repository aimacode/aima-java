/*
 * Created on Sep 21, 2004
 *
 */
package aima.search.csp;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * @author Ravi Mohan
 *  
 */
public class Assignment {
	//TODO check if object necessary or can constrict to boolean
	Hashtable<String,Object> hash;

	List<String> variables;

	public Assignment() {
		this(new ArrayList<String>());
	}

	public Assignment(List<String> variables) {
		hash = new Hashtable<String,Object> ();
		this.variables = variables;
	}

	public void setAssignment(String variable, Object value) {
		hash.put(variable, value);
	}

	public Object getAssignment(String variable) {
		return hash.get(variable);
	}

	public boolean isComplete() {

		return everyVariableIsAKeyAndHasAValue();
	}

	private boolean everyVariableIsAKeyAndHasAValue() {

		Iterator iter = variables.iterator();
		while (iter.hasNext()) {
			String variable = (String) iter.next();
			if (!hash.keySet().contains(variable)) {
				return false;
			} else {
				if (hash.get(variable) == null) {
					return false;
				}
			}
		}
		return true;
	}

	public void remove(String variable) {
		if (hash.keySet().contains(variable)) {
			hash.remove(variable);
		}
	}

	public String selectFirstUnassignedVariable() {
		Iterator iter = variables.iterator();
		while (iter.hasNext()) {
			String variable = (String) iter.next();
			if (!(hash.keySet().contains(variable))) {
				return variable;
			}
		}
		return null;

	}

	public boolean hasAssignmentFor(String variable) {
		return hash.keySet().contains(variable);
	}

	public String toString() {

		return hash.toString();
	}

	public Assignment copy() {
		Assignment copy = new Assignment();
		for (int i = 0; i < variables.size(); i++) {
			copy.variables.add(variables.get(i));
		}
		Iterator<String> iter = hash.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			copy.hash.put(key, hash.get(key));
		}
		return copy;
	}

	public int getNumberOfConflictsFor(String conflictedVariable, Object value,
			Constraint constraint) {
		Assignment duplicate = copy();
		duplicate.setAssignment(conflictedVariable, value);
		return duplicate.getConflictedVariables(constraint).size();
	}

	public List getConflictedVariables(Constraint constraint) {
		List<String> conflictedVariables = new ArrayList<String>();
		List<String> variables =getVariables();
		for (String variable : variables) {
			Object value = getAssignment(variable);
			if (!(constraint.isSatisfiedWith(this, variable, value))) {
				conflictedVariables.add(variable);
			}
		}
		return conflictedVariables;
	}

	public Object getMinimumConflictingValueFor(String conflictedVariable,
			List domain, Constraint constraint) {
		int minConflict = Integer.MAX_VALUE;
		Object minConflictValue = null;

		for (int i = 0; i < domain.size(); i++) {
			Object value = domain.get(i);
			if (getNumberOfConflictsFor(conflictedVariable, value, constraint) < minConflict) {
				minConflict = getNumberOfConflictsFor(conflictedVariable,
						value, constraint);
				minConflictValue = value;
			}
		}
		return minConflictValue;
	}

	public boolean satisfies(Constraint constraint) {
		if (isComplete()) {
			for (int j = 0; j < getVariables().size(); j++) {
				String variable = (String) getVariables().get(j);
				Object value = getAssignment(variable);
				if (!(constraint.isSatisfiedWith(this, variable, value))) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public List<String> getVariables() {
		return variables;
	}
}