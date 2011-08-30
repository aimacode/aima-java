package aima.core.logic.common;

/**
 * @author Ravi Mohan
 * 
 */
public interface LogicTokenTypes {
	static final int SYMBOL = 1;

	static final int LPAREN = 2;

	static final int RPAREN = 3;

	static final int COMMA = 4;

	static final int CONNECTOR = 5;

	static final int QUANTIFIER = 6;

	static final int PREDICATE = 7;

	static final int FUNCTION = 8;

	static final int VARIABLE = 9;

	static final int CONSTANT = 10;

	static final int TRUE = 11;

	static final int FALSE = 12;

	static final int EQUALS = 13;

	static final int WHITESPACE = 1000;

	static final int EOI = 9999;
}