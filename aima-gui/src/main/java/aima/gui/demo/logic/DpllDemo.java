package aima.gui.demo.logic;

import aima.core.logic.propositional.inference.DPLL;
import aima.core.logic.propositional.parsing.PLParser;

/**
 * @author Ravi Mohan
 * 
 */
public class DpllDemo {
	private static DPLL dpll = new DPLL();

	public static void main(String[] args) {
		displayDPLLSatisfiableStatus("A & B");
		displayDPLLSatisfiableStatus("A & ~A");
		displayDPLLSatisfiableStatus("(A | ~A) & (A | B)");
	}

	public static void displayDPLLSatisfiableStatus(String query) {
		PLParser parser = new PLParser();
		if (dpll.isSatisfiable(parser.parse(query))) {
			System.out.println(query + " is  (DPLL) satisfiable");
		} else {
			System.out.println(query + " is NOT (DPLL)  satisfiable");
		}
	}
}
