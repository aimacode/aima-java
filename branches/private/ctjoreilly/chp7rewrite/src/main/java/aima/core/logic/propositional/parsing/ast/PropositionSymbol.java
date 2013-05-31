package aima.core.logic.propositional.parsing.ast;

import aima.core.logic.propositional.parsing.PLVisitor;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 244.<br>
 * <br>
 * <b>Proposition Symbol:</b> Each such symbol stands for a proposition that can
 * be true or false. There are two proposition symbols with fixed meanings:
 * <i>True</i> the always-true proposition and <i>False</i> the always-false
 * proposition.
 * 
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * 
 */
public class PropositionSymbol extends AtomicSentence {
	//
	public static final String TRUE = "True";
	public static final String FALSE = "False";
	//
	private String symbol;

	/**
	 * Constructor.
	 * 
	 * @param symbol
	 *            the symbol uniquely identifying the proposition.
	 */
	public PropositionSymbol(String symbol) {
		// Ensure differing cases for the 'True' and 'False'
		// constants are represented in a canonical form.
		if (TRUE.equalsIgnoreCase(symbol)) {
			this.symbol = TRUE;
		} else if (FALSE.equalsIgnoreCase(symbol)) {
			this.symbol = FALSE;
		} else {
			this.symbol = symbol;
		}
	}

	/**
	 * 
	 * @return true if this is the always 'True' proposition symbol, false
	 *         otherwise.
	 */
	public boolean isAlwaysTrue() {
		return TRUE.equals(symbol);
	}

	/**
	 * 
	 * @return true if the symbol passed in is the always 'True' proposition
	 *         symbol, false otherwise.
	 */
	public static boolean isAlwaysTrueSymbol(String symbol) {
		return TRUE.equalsIgnoreCase(symbol);
	}

	/**
	 * 
	 * @return true if this is the always 'False' proposition symbol, false
	 *         other.
	 */
	public boolean isAlwaysFalse() {
		return FALSE.equals(symbol);
	}

	/**
	 * 
	 * @return true if the symbol passed in is the always 'False' proposition
	 *         symbol, false other.
	 */
	public static boolean isAlwaysFalseSymbol(String symbol) {
		return FALSE.equalsIgnoreCase(symbol);
	}

	/**
	 * 
	 * @return the symbol uniquely identifying the proposition.
	 */
	public String getSymbol() {
		return symbol;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		PropositionSymbol sym = (PropositionSymbol) o;
		return (sym.getSymbol().equals(getSymbol()));

	}

	@Override
	public int hashCode() {
		return symbol.hashCode();
	}

	@Override
	public String toString() {
		return getSymbol();
	}

	@Override
	public Object accept(PLVisitor plv, Object arg) {
		Object result = plv.visitPropositionSymbol(this, arg);
		return result;
	}
}