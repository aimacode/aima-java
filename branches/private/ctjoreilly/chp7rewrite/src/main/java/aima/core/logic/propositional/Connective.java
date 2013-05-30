package aima.core.logic.propositional;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 244.<br>
 * <br>
 * <pre>
 * There are five connectives in common use:
 * 1. ~   (not).
 * 2. &   (and).
 * 3. |   (or).
 * 4. =>  (implication).
 * 5. <=> (biconditional).
 * 
 * Note: We use ASCII characters that commonly have the same meaning to those 
 * symbols used in the book.
 * 
 * OPERATOR PRECEDENCE: ~, &, |, =>, <=>
 * </pre>
 *  
 * @author Ciaran O'Reilly
 * 
 */
public enum Connective {
	NOT          ("~",  10), // i.e. highest to lowest precedence.
	AND          ("&",   8),
	OR           ("|",   6),
	IMPLICATION  ("=>",  4),
	BICONDITIONAL("<=>", 2);
	
	public String getSymbol() {
		return symbol;
	}
	
	public int getPrecedence() {
		return precedence;
	}
	
	@Override
	public String toString() {
		return getSymbol();
	}
	
	public static boolean isConnective(String symbol) {
		if (NOT.getSymbol().equals(symbol)) {
			return true;
		}
		else if (AND.getSymbol().equals(symbol)) {
			return true;
		}
		else if (OR.getSymbol().equals(symbol)) {
			return true;
		}
		else if (IMPLICATION.getSymbol().equals(symbol)) {
			return true;
		}
		else if (BICONDITIONAL.getSymbol().equals(symbol)) {
			return true;
		}
		return false;
	}
	
	public static Connective get(String symbol) {
		if (NOT.getSymbol().equals(symbol)) {
			return NOT;
		}
		else if (AND.getSymbol().equals(symbol)) {
			return AND;
		}
		else if (OR.getSymbol().equals(symbol)) {
			return OR;
		}
		else if (IMPLICATION.getSymbol().equals(symbol)) {
			return IMPLICATION;
		}
		else if (BICONDITIONAL.getSymbol().equals(symbol)) {
			return BICONDITIONAL;
		}
		
		throw new IllegalArgumentException("Not a valid symbol for a connective: "+symbol);
	}
	
	//
	// PRIVATE
	//
	private final String symbol;
	private final int    precedence;
	
	private Connective(String symbol, int precedence) {
		this.symbol     = symbol;
		this.precedence = precedence;
	}
}
