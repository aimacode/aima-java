package aima.core.search.basic.support;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import aima.core.search.api.Constraint;
import aima.core.search.api.Domain;

/**
 * Basic implementation of the Constraint interface.
 * 
 * @author Ciaran O'Reilly
 */
public class BasicConstraint implements Constraint {
	private List<String> scope;
	private Relation relation;

	public BasicConstraint(String[] scope, Predicate<Object[]> memberTest) {
		this.scope = Collections.unmodifiableList(Arrays.asList(scope));
		this.relation = new BasicRelation(memberTest);
	}

	@Override
	public List<String> getScope() {
		return scope;
	}

	@Override
	public Relation getRelation() {
		return relation;
	}

	public static Constraint newTabularConstraint(String[] scope, Object[][] table) {
		final Set<List<Object>> lookup = new HashSet<>();
		for (Object[] row : table) {
			lookup.add(Arrays.asList(row));
		}
		return new BasicConstraint(scope, values -> lookup.contains(Arrays.asList(values)));
	}
	
	public static Constraint newEqualConstraint(String var1, String var2) {
		return new BasicConstraint(new String[] { var1, var2 },
				values -> values.length == 2 && values[0].equals(values[1]));
	}

	public static Constraint newNotEqualConstraint(String var1, String var2) {
		return new BasicConstraint(new String[] { var1, var2 },
				values -> values.length == 2 && !values[0].equals(values[1]));
	}

	//
	//
	protected class BasicRelation implements Relation {
		private Predicate<Object[]> memberTest;

		public BasicRelation(Predicate<Object[]> memberTest) {
			this.memberTest = memberTest;
		}

		@Override
		public boolean isMember(Object[] values) {
			return memberTest.test(values);
		}

		@Override
		public Iterator<List<Object>> iterator(List<Domain> domainsOfScope) {
			throw new UnsupportedOperationException("TODO - add support for.");
		}
	}
}
