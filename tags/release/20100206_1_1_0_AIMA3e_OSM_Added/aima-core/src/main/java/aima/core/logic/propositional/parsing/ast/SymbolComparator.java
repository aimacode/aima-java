package aima.core.logic.propositional.parsing.ast;

import java.util.Comparator;

/**
 * @author Ravi Mohan
 * 
 */
public class SymbolComparator implements Comparator {

	public int compare(Object symbol1, Object symbol2) {
		Symbol one = (Symbol) symbol1;
		Symbol two = (Symbol) symbol2;
		return one.getValue().compareTo(two.getValue());
	}
}
