package aima.core.environment.support;

import aima.core.search.api.CSP;
import aima.core.search.basic.support.BasicCSP;
import aima.core.search.basic.support.BasicConstraint;

public class CSPFactory {
	public static CSP mapColoringTerritoriesOfAustraliaCSP() {
		return mapColoringTerritoriesOfAustraliaCSP(new String[] {"red", "green", "blue"});
	}
	
	public static CSP mapColoringTerritoriesOfAustraliaCSP(String[] allowedColors) {
		return new BasicCSP(new String[] {"WA", "NT","Q", "NSW", "V", "SA", "T"}, 
				new Object[][] {
					allowedColors, // WA
					allowedColors, // NT
					allowedColors, // Q
					allowedColors, // NSW
					allowedColors, // V
					allowedColors, // SA
					allowedColors  // T
				}, 
				BasicConstraint.newNotEqualConstraint("SA", "WA"),
				BasicConstraint.newNotEqualConstraint("SA", "NT"),
				BasicConstraint.newNotEqualConstraint("SA", "Q"),
				BasicConstraint.newNotEqualConstraint("SA", "NSW"),
				BasicConstraint.newNotEqualConstraint("SA", "V"),
				BasicConstraint.newNotEqualConstraint("WA", "NT"),
				BasicConstraint.newNotEqualConstraint("NT", "Q"),
				BasicConstraint.newNotEqualConstraint("Q", "NSW"),
				BasicConstraint.newNotEqualConstraint("NSW", "V"));
	}
}
