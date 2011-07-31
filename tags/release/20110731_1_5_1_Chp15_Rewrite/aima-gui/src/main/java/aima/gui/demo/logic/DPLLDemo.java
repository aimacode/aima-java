package aima.gui.demo.logic;

import aima.core.logic.propositional.algorithms.DPLL;

/**
 * @author Ravi Mohan
 * 
 */
public class DPLLDemo {
	private static DPLL dpll = new DPLL();

	public static void main(String[] args) {
		displayDPLLSatisfiableStatus("( A AND B )");
		displayDPLLSatisfiableStatus("( A AND (NOT A) )");
		// displayDPLLSatisfiableStatus("((A OR (NOT A)) AND (A OR B))");
	}

	public static void displayDPLLSatisfiableStatus(String query) {
		if (dpll.dpllSatisfiable(query)) {
			System.out.println(query + " is  (DPLL) satisfiable");
		} else {
			System.out.println(query + " is NOT (DPLL)  satisfiable");
		}
	}
}
