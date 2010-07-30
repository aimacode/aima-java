package aima.gui.demo.search;

import aima.core.search.csp.BacktrackingStrategy;
import aima.core.search.csp.CSP;
import aima.core.search.csp.MapCSP;
import aima.core.search.csp.MinConflictsStrategy;

/**
 * @author Ravi Mohan
 * @author Ruediger Lunde
 */

public class CSPDemo {
	public static void main(String[] args) {
		CSP csp = new MapCSP();
		System.out.println("Map Coloring - Backtracking ");
		System.out.println(new BacktrackingStrategy().solve(csp));
		System.out.println("Map Coloring - Minimum Conflicts ");
		System.out.println(new MinConflictsStrategy(100).solve(csp));
	}
}