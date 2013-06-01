package aima.core.logic.common;

/**
 * @author Ravi Mohan
 * 
 */
public interface LogicTokenTypes {
	static final int SYMBOL = 1;

	static final int LPAREN = 2; // (

	static final int RPAREN = 3; // )
	
	static final int LSQRBRACKET = 4; // [
	
	static final int RSQRBRACKET = 5; // ]

	static final int COMMA = 6;

	static final int CONNECTIVE = 7;

	static final int QUANTIFIER = 8;

	static final int PREDICATE = 9;

	static final int FUNCTION = 10;

	static final int VARIABLE = 11;

	static final int CONSTANT = 12;

	static final int TRUE = 13;

	static final int FALSE = 14;

	static final int EQUALS = 15;

	static final int WHITESPACE = 1000;

	static final int EOI = 9999; // End of Input.
}