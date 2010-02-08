package aima.core.logic.propositional.parsing;

import java.util.HashSet;
import java.util.Set;

import aima.core.logic.common.Lexer;
import aima.core.logic.common.LogicTokenTypes;
import aima.core.logic.common.Token;

/**
 * @author Ravi Mohan
 * 
 */
public class PELexer extends Lexer {

	Set<String> connectors;

	public PELexer() {
		connectors = new HashSet<String>();
		connectors.add("NOT");
		connectors.add("AND");
		connectors.add("OR");
		connectors.add("=>");
		connectors.add("<=>");
	}

	public PELexer(String inputString) {
		this();
		setInput(inputString);
	}

	@Override
	public Token nextToken() {
		Token result = null;
		int tokenType;
		String tokenContent;

		if (lookAhead(1) == '(') {
			consume();
			return new Token(LogicTokenTypes.LPAREN, "(");

		} else if (lookAhead(1) == ')') {
			consume();
			return new Token(LogicTokenTypes.RPAREN, ")");
		} else if (identifierDetected()) {
			return symbol();

		} else if (Character.isWhitespace(lookAhead(1))) {
			consume();
			return nextToken();
			// return whiteSpace();
		} else if (lookAhead(1) == (char) -1) {
			return new Token(LogicTokenTypes.EOI, "EOI");
		} else {
			throw new RuntimeException("Lexing error on character "
					+ lookAhead(1));
		}

	}

	private boolean identifierDetected() {
		return (Character.isJavaIdentifierStart((char) lookAheadBuffer[0]))
				|| partOfConnector();
	}

	private boolean partOfConnector() {
		return (lookAhead(1) == '=') || (lookAhead(1) == '<')
				|| (lookAhead(1) == '>');
	}

	private Token symbol() {
		StringBuffer sbuf = new StringBuffer();
		while ((Character.isLetterOrDigit(lookAhead(1)))
				|| (lookAhead(1) == '=') || (lookAhead(1) == '<')
				|| (lookAhead(1) == '>')) {
			sbuf.append(lookAhead(1));
			consume();
		}
		String symbol = sbuf.toString();
		if (isConnector(symbol)) {
			return new Token(LogicTokenTypes.CONNECTOR, sbuf.toString());
		} else if (symbol.equalsIgnoreCase("true")) {
			return new Token(LogicTokenTypes.TRUE, "TRUE");
		} else if (symbol.equalsIgnoreCase("false")) {
			return new Token(LogicTokenTypes.FALSE, "FALSE");
		} else {
			return new Token(LogicTokenTypes.SYMBOL, sbuf.toString());
		}

	}

	private Token connector() {
		StringBuffer sbuf = new StringBuffer();
		while (Character.isLetterOrDigit(lookAhead(1))) {
			sbuf.append(lookAhead(1));
			consume();
		}
		return new Token(LogicTokenTypes.CONNECTOR, sbuf.toString());
	}

	private Token whiteSpace() {
		StringBuffer sbuf = new StringBuffer();
		while (Character.isWhitespace(lookAhead(1))) {
			sbuf.append(lookAhead(1));
			consume();
		}
		return new Token(LogicTokenTypes.WHITESPACE, sbuf.toString());

	}

	private boolean isConnector(String aSymbol) {
		return (connectors.contains(aSymbol));
	}
}