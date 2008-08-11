/*
 * Created on Sep 18, 2004
 *
 */
package aima.logic.fol.parsing;

import java.util.HashSet;
import java.util.Set;

import aima.logic.common.Lexer;
import aima.logic.common.LogicTokenTypes;
import aima.logic.common.Token;
import aima.logic.fol.FOLDomain;

/**
 * @author Ravi Mohan
 * 
 */
public class FOLLexer extends Lexer {
	private Set<String> constants, functions, predicates, connectors,
			quantifiers;

	public FOLLexer(FOLDomain domain) {
		this.constants = domain.getConstants();
		this.functions = domain.getFunctions();
		this.predicates = domain.getPredicates();

		connectors = new HashSet<String>();
		connectors.add("NOT");
		connectors.add("AND");
		connectors.add("OR");
		connectors.add("=>");
		connectors.add("<=>");

		quantifiers = new HashSet<String>();
		quantifiers.add("FORALL");
		quantifiers.add("EXISTS");
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

		} else if (lookAhead(1) == ',') {
			consume();
			return new Token(LogicTokenTypes.COMMA, ",");

		} else if (identifierDetected()) {
			// System.out.println("identifier detected");
			return identifier();
		} else if (Character.isWhitespace(lookAhead(1))) {
			consume();
			return nextToken();
		} else if (lookAhead(1) == (char) -1) {
			return new Token(LogicTokenTypes.EOI, "EOI");
		} else {
			throw new RuntimeException("Lexing error on character "
					+ lookAhead(1));
		}
	}

	private Token identifier() {
		StringBuffer sbuf = new StringBuffer();
		while ((Character.isLetter(lookAhead(1))) || partOfConnector()) {
			sbuf.append(lookAhead(1));
			consume();
		}
		String readString = new String(sbuf);
		// System.out.println(readString);
		if (connectors.contains(readString)) {
			return new Token(LogicTokenTypes.CONNECTOR, readString);
		} else if (quantifiers.contains(readString)) {
			return new Token(LogicTokenTypes.QUANTIFIER, readString);
		} else if (predicates.contains(readString)) {
			return new Token(LogicTokenTypes.PREDICATE, readString);
		} else if (functions.contains(readString)) {
			return new Token(LogicTokenTypes.FUNCTION, readString);
		} else if (constants.contains(readString)) {
			return new Token(LogicTokenTypes.CONSTANT, readString);
		} else if (isVariable(readString)) {
			return new Token(LogicTokenTypes.VARIABLE, readString);
		} else if (readString.equals("=")) {
			return new Token(LogicTokenTypes.EQUALS, readString);
		} else {
			throw new RuntimeException("Lexing error on character "
					+ lookAhead(1));
		}

	}

	private boolean isVariable(String s) {
		return (Character.isLowerCase(s.charAt(0)));
	}

	private boolean identifierDetected() {
		return (Character.isJavaIdentifierStart((char) lookAheadBuffer[0]))
				|| partOfConnector();
	}

	private boolean partOfConnector() {
		return (lookAhead(1) == '=') || (lookAhead(1) == '<')
				|| (lookAhead(1) == '>');
	}

}