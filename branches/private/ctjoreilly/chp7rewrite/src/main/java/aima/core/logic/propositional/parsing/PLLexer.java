package aima.core.logic.propositional.parsing;

import aima.core.logic.common.Lexer;
import aima.core.logic.common.LexerException;
import aima.core.logic.common.LogicTokenTypes;
import aima.core.logic.common.Token;
import aima.core.logic.propositional.parsing.ast.Connective;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;

/**
 * A concrete implementation of a lexical analyzer for the propositional language.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class PLLexer extends Lexer {
	
	/**
	 * Default Constructor.
	 */
	public PLLexer() {
	}

	/**
	 * Constructs a propositional expression lexer with the specified character
	 * stream.
	 * 
	 * @param inputString
	 *            a sequence of characters to be converted into a sequence of
	 *            tokens.
	 */
	public PLLexer(String inputString) {
		setInput(inputString);
	}

	/**
	 * Returns the next propositional token from the character stream.
	 * 
	 * @return the next propositional token from the character stream.
	 */
	@Override
	public Token nextToken() {
		int startPosition = getCurrentPositionInInput();
		if (lookAhead(1) == '(') {
			consume();
			return new Token(LogicTokenTypes.LPAREN, "(", startPosition);
		} else if (lookAhead(1) == '[') {
				consume();
				return new Token(LogicTokenTypes.LSQRBRACKET, "[", startPosition);
		} else if (lookAhead(1) == ')') {
			consume();
			return new Token(LogicTokenTypes.RPAREN, ")", startPosition);
		} else if (lookAhead(1) == ']') {
			consume();
			return new Token(LogicTokenTypes.RSQRBRACKET, "]", startPosition);
		} else if (Character.isWhitespace(lookAhead(1))) {
			consume();
			return nextToken();
		} else if (connectiveDetected(lookAhead(1))) {
			return connective();
		} else if (symbolDetected(lookAhead(1))) {
			return symbol();
		} else if (lookAhead(1) == (char) -1) {
			return new Token(LogicTokenTypes.EOI, "EOI", startPosition);
		} else {
			throw new LexerException("Lexing error on character " + lookAhead(1) + " at position " + getCurrentPositionInInput(), getCurrentPositionInInput());
		}
	}

	private boolean connectiveDetected(char leadingChar) {
		return Connective.isConnectiveIdentifierStart(leadingChar);
	}
	
	private boolean symbolDetected(char leadingChar) {
		return PropositionSymbol.isPropositionSymbolIdentifierStart(leadingChar);
	}
	
	private Token connective() {
		int startPosition = getCurrentPositionInInput();
		StringBuffer sbuf = new StringBuffer();
		// Ensure pull out just one connective at a time, the isConnective(...)
		// test ensures we handle chained expressions like the following:
		// ~~P
		while (Connective.isConnectiveIdentifierPart(lookAhead(1)) && !isConnective(sbuf.toString())) {
			sbuf.append(lookAhead(1));
			consume();
		}
		
		String symbol = sbuf.toString();
		if (isConnective(symbol)) {
			return new Token(LogicTokenTypes.CONNECTIVE, sbuf.toString(), startPosition);
		}
		
		throw new LexerException("Lexing error on connective "+symbol + " at position " + getCurrentPositionInInput(), getCurrentPositionInInput());
	}

	private Token symbol() {
		int startPosition = getCurrentPositionInInput();
		StringBuffer sbuf = new StringBuffer();
		while (PropositionSymbol.isPropositionSymbolIdentifierPart(lookAhead(1))) {
			sbuf.append(lookAhead(1));
			consume();
		}
		String symbol = sbuf.toString();
		if (PropositionSymbol.isAlwaysTrueSymbol(symbol)) {
			return new Token(LogicTokenTypes.TRUE, PropositionSymbol.TRUE_SYMBOL, startPosition);
		} else if (PropositionSymbol.isAlwaysFalseSymbol(symbol)) {
			return new Token(LogicTokenTypes.FALSE, PropositionSymbol.FALSE_SYMBOL, startPosition);
		} else if (PropositionSymbol.isPropositionSymbol(symbol)){
			return new Token(LogicTokenTypes.SYMBOL, sbuf.toString(), startPosition);
		}
		
		throw new LexerException("Lexing error on symbol "+symbol+ " at position "+ getCurrentPositionInInput(), getCurrentPositionInInput());
	}

	private boolean isConnective(String aSymbol) {
		return Connective.isConnective(aSymbol);
	}
}