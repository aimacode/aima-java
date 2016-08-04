package aima.core.environment.support;

import aima.core.search.api.CSP;
import aima.core.search.basic.support.BasicCSP;
import aima.core.search.basic.support.BasicConstraint;

public class CSPFactory {
	public static CSP mapColoringTerritoriesOfAustraliaCSP() {
		return mapColoringTerritoriesOfAustraliaCSP(new String[] { "red", "green", "blue" });
	}

	public static CSP mapColoringTerritoriesOfAustraliaCSP(String[] allowedColors) {
		return new BasicCSP(new String[] { "WA", "NT", "Q", "NSW", "V", "SA", "T" },
				new Object[][] { allowedColors, // WA
						allowedColors, // NT
						allowedColors, // Q
						allowedColors, // NSW
						allowedColors, // V
						allowedColors, // SA
						allowedColors // T
				}, BasicConstraint.newNotEqualConstraint("SA", "WA"), BasicConstraint.newNotEqualConstraint("SA", "NT"), BasicConstraint.newNotEqualConstraint("SA", "Q"), BasicConstraint.newNotEqualConstraint("SA", "NSW"), BasicConstraint.newNotEqualConstraint("SA", "V"), BasicConstraint.newNotEqualConstraint("WA", "NT"), BasicConstraint.newNotEqualConstraint("NT", "Q"), BasicConstraint.newNotEqualConstraint("Q", "NSW"), BasicConstraint.newNotEqualConstraint("NSW", "V"));
	}

	public static CSP aima3eFig6_10_treeCSP() {
		return aima3eFig6_10_treeCSP(new String[] { "red", "green", "blue" });
	}

	public static CSP aima3eFig6_10_treeCSP(String[] allowedColors) {
		return new BasicCSP(new String[] { "A", "B", "C", "D", "E", "F" },
				new Object[][] { allowedColors, // A
						allowedColors, // B
						allowedColors, // C
						allowedColors, // D
						allowedColors, // E
						allowedColors // F
				}, BasicConstraint.newNotEqualConstraint("A", "B"), BasicConstraint.newNotEqualConstraint("C", "B"), BasicConstraint.newNotEqualConstraint("B", "D"), BasicConstraint.newNotEqualConstraint("D", "E"), BasicConstraint.newNotEqualConstraint("D", "F"));
	}
}
