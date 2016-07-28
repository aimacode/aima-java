package aima.test.unit.search.csp;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import aima.core.environment.support.CSPFactory;
import aima.core.search.api.Assignment;
import aima.core.search.api.CSP;
import aima.core.search.api.SearchForAssignmentFunction;
import aima.core.search.basic.csp.BacktrackingSearch;

@RunWith(Parameterized.class)
public class BacktrackingSearchTest {

	@Parameters(name = "{index}: {0}")
	public static Collection<Object[]> implementations() {
		return Arrays.asList(new Object[][] { { "BacktrackingSearch" } });
	}

	@Parameter
	public String searchFunctionName;

	public Assignment searchForAssignment(CSP csp) {
		SearchForAssignmentFunction searchFn;
		switch (searchFunctionName) {
		case "BacktrackingSearch":
			searchFn = new BacktrackingSearch();
			break;
		default:
			throw new IllegalArgumentException(searchFunctionName + " not handled properly.");
		}

		return searchFn.apply(csp);
	}

	@Test
	public void testMapColoringTerritoriesOfAustraliaCSP() {
		CSP csp = CSPFactory.mapColoringTerritoriesOfAustraliaCSP();
		Assignment ans = searchForAssignment(csp);

		Assert.assertTrue(ans.isComplete(csp));
		Assert.assertNotEquals(ans.getAssignment("SA"), ans.getAssignment("WA"));
		Assert.assertNotEquals(ans.getAssignment("SA"), ans.getAssignment("NT"));
		Assert.assertNotEquals(ans.getAssignment("SA"), ans.getAssignment("Q"));
		Assert.assertNotEquals(ans.getAssignment("SA"), ans.getAssignment("NSW"));
		Assert.assertNotEquals(ans.getAssignment("SA"), ans.getAssignment("V"));
		Assert.assertNotEquals(ans.getAssignment("WA"), ans.getAssignment("NT"));
		Assert.assertNotEquals(ans.getAssignment("NT"), ans.getAssignment("Q"));
		Assert.assertNotEquals(ans.getAssignment("Q"), ans.getAssignment("NSW"));
		Assert.assertNotEquals(ans.getAssignment("NSW"), ans.getAssignment("V"));

		// Now try a version of the problem we know can't be solved
		csp = CSPFactory.mapColoringTerritoriesOfAustraliaCSP(new String[] { "red", "green" });
		ans = searchForAssignment(csp);
		Assert.assertNull(ans); // i.e. null indicates failure.
	}
}