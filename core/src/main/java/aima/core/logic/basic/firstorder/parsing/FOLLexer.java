package aima.core.logic.basic.firstorder.parsing;

import java.util.HashSet;
import java.util.Set;

import aima.core.logic.basic.firstorder.Connectors;
import aima.core.logic.basic.firstorder.Quantifiers;
import aima.core.logic.basic.firstorder.domain.FOLDomain;
import aima.core.logic.basic.common.Lexer;
import aima.core.logic.basic.common.LexerException;
import aima.core.logic.basic.common.LogicTokenTypes;
import aima.core.logic.basic.common.Token;

/**
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Anurag Rai
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
		int startPosition = getCurrentPositionInInput();
		if (lookAhead(1) == '(') {
			consume();
			return new Token(LogicTokenTypes.LPAREN, "(", startPosition);

		} else if (lookAhead(1) == ')') {
			consume();
			return new Token(LogicTokenTypes.RPAREN, ")", startPosition);

		} else if (lookAhead(1) == ',') {
			consume();
			return new Token(LogicTokenTypes.COMMA, ",", startPosition);

		} else if (identifierDetected()) {
			// System.out.println("identifier detected");
			return identifier();
		} else if (Character.isWhitespace(lookAhead(1))) {
			consume();
			return nextToken();
		} else if (lookAhead(1) == (char) -1) {
			return new Token(LogicTokenTypes.EOI, "EOI", startPosition);
		} else {
			throw new LexerException(
					"Lexing error on character " + lookAhead(1) + " at position " + getCurrentPositionInInput(),
					getCurrentPositionInInput());
		}
	}

	private Token identifier() {
		int startPosition = getCurrentPositionInInput();
		StringBuffer sbuf = new StringBuffer();
		while ((Character.isJavaIdentifierPart(lookAhead(1))) || partOfConnector()) {
			sbuf.append(lookAhead(1));
			consume();
		}
		String readString = new String(sbuf);
		// System.out.println(readString);
		if (connectors.contains(readString)) {
			return new Token(LogicTokenTypes.CONNECTIVE, readString, startPosition);
		} else if (quantifiers.contains(readString)) {
			return new Token(LogicTokenTypes.QUANTIFIER, readString, startPosition);
		} else if (domain.getPredicates().contains(readString)) {
			return new Token(LogicTokenTypes.PREDICATE, readString, startPosition);
		} else if (domain.getFunctions().contains(readString)) {
			return new Token(LogicTokenTypes.FUNCTION, readString, startPosition);
		} else if (domain.getConstants().contains(readString)) {
			return new Token(LogicTokenTypes.CONSTANT, readString, startPosition);
		} else if (isVariable(readString)) {
			return new Token(LogicTokenTypes.VARIABLE, readString, startPosition);
		} else if (readString.equals("=")) {
			return new Token(LogicTokenTypes.EQUALS, readString, startPosition);
		} else {
			throw new LexerException(
					"Lexing error on character " + lookAhead(1) + " at position " + getCurrentPositionInInput(),
					getCurrentPositionInInput());
		}
	}

	private boolean isVariable(String s) {
		return (Character.isLowerCase(s.charAt(0)));
	}

	private boolean identifierDetected() {
		return (Character.isJavaIdentifierStart(lookAhead(1))) || partOfConnector();
	}

	private boolean partOfConnector() {
		return (lookAhead(1) == '=') || (lookAhead(1) == '<')
				|| (lookAhead(1) == '>' || (lookAhead(1) == '&') || (lookAhead(1) == '|') || (lookAhead(1) == '~'));
	}
}