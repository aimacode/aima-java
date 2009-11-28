package aima.core.logic.fol.parsing;

import java.util.HashSet;
import java.util.Set;

import aima.core.logic.common.Lexer;
import aima.core.logic.common.LogicTokenTypes;
import aima.core.logic.common.Token;
import aima.core.logic.fol.Connectors;
import aima.core.logic.fol.Quantifiers;
import aima.core.logic.fol.domain.FOLDomain;

/**
 * @author Ravi Mohan
 * 
 */
public class FOLLexer extends Lexer {
	private FOLDomain domain;
	private Set<String> connectors, quantifiers;

	public FOLLexer(FOLDomain domain) {
		this.domain = domain;

		connectors = new HashSet<String>();
		connectors.add(Connectors.NOT);
		connectors.add(Connectors.AND);
		connectors.add(Connectors.OR);
		connectors.add(Connectors.IMPLIES);
		connectors.add(Connectors.BICOND);

		quantifiers = new HashSet<String>();
		quantifiers.add(Quantifiers.FORALL);
		quantifiers.add(Quantifiers.EXISTS);
	}

	public FOLDomain getFOLDomain() {
		return domain;
	}

	@Override
	public Token nextToken() {
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
		while ((Character.isJavaIdentifierPart(lookAhead(1)))
				|| partOfConnector()) {
			sbuf.append(lookAhead(1));
			consume();
		}
		String readString = new String(sbuf);
		// System.out.println(readString);
		if (connectors.contains(readString)) {
			return new Token(LogicTokenTypes.CONNECTOR, readString);
		} else if (quantifiers.contains(readString)) {
			return new Token(LogicTokenTypes.QUANTIFIER, readString);
		} else if (domain.getPredicates().contains(readString)) {
			return new Token(LogicTokenTypes.PREDICATE, readString);
		} else if (domain.getFunctions().contains(readString)) {
			return new Token(LogicTokenTypes.FUNCTION, readString);
		} else if (domain.getConstants().contains(readString)) {
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