package aima.core.logic.propositional.parsing;

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.SourceVersion;

import aima.core.logic.common.Lexer;
import aima.core.logic.common.LogicTokenTypes;
import aima.core.logic.common.Token;
import aima.core.logic.propositional.parsing.ast.Connective;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class PELexer extends Lexer {

	private Set<Character> connectiveLeadingChars = new HashSet<Character>();
	private Set<Character> connectiveChars        = new HashSet<Character>();
	
	public PELexer() {
		for (Connective connective : Connective.values()) {
			char[] chars = new char[connective.getSymbol().length()];
			connective.getSymbol().getChars(0, connective.getSymbol().length(), chars, 0);
			for (int i = 0; i < chars.length; i++) {
				if (i == 0) {
					connectiveLeadingChars.add(chars[i]);
				}
				connectiveChars.add(chars[i]);
			}
		}
	}

	/**
	 * Constructs a propositional expression lexer with the specified character
	 * stream.
	 * 
	 * @param inputString
	 *            a sequence of characters to be converted into a sequence of
	 *            tokens.
	 */
	public PELexer(String inputString) {
		this();
		setInput(inputString);
	}

	/**
	 * Returns the next token from the character stream.
	 * 
	 * @return the next token from the character stream.
	 */
	@Override
	public Token nextToken() {
		if (lookAhead(1) == '(') {
			consume();
			return new Token(LogicTokenTypes.LPAREN, "(");
		} else if (lookAhead(1) == '[') {
				consume();
				return new Token(LogicTokenTypes.LSQRBRACKET, "[");
		} else if (lookAhead(1) == ')') {
			consume();
			return new Token(LogicTokenTypes.RPAREN, ")");
		} else if (lookAhead(1) == ']') {
			consume();
			return new Token(LogicTokenTypes.RSQRBRACKET, "]");
		} else if (Character.isWhitespace(lookAhead(1))) {
			consume();
			return nextToken();
		} else if (connectiveDetected(lookAhead(1))) {
			return connective();
		} else if (symbolDetected(lookAhead(1))) {
			return symbol();
		} else if (lookAhead(1) == (char) -1) {
			return new Token(LogicTokenTypes.EOI, "EOI");
		} else {
			throw new RuntimeException("Lexing error on character "
					+ lookAhead(1));
		}
	}

	private boolean connectiveDetected(char leadingChar) {
		return connectiveLeadingChars.contains(leadingChar);
	}
	
	private boolean symbolDetected(char leadingChar) {
		return Character.isJavaIdentifierStart(leadingChar);
	}
	
	private Token connective() {
		StringBuffer sbuf = new StringBuffer();
		// Ensure pull out just one connective at a time, the isConnective(...)
		// test ensures we handle chained expressions like the following:
		// ~~P
		while (connectiveChars.contains(lookAhead(1)) && !isConnective(sbuf.toString())) {
			sbuf.append(lookAhead(1));
			consume();
		}
		
		String symbol = sbuf.toString();
		if (isConnective(symbol)) {
			return new Token(LogicTokenTypes.CONNECTIVE, sbuf.toString());
		}
		
		throw new RuntimeException("Lexing error on connective "+symbol);
	}

	private Token symbol() {
		StringBuffer sbuf = new StringBuffer();
		while (Character.isJavaIdentifierStart(lookAhead(1)) || Character.isJavaIdentifierPart(lookAhead(1))) {
			sbuf.append(lookAhead(1));
			consume();
		}
		String symbol = sbuf.toString();
		if (symbol.equalsIgnoreCase("true")) {
			return new Token(LogicTokenTypes.TRUE, PropositionSymbol.TRUE);
		} else if (symbol.equalsIgnoreCase("false")) {
			return new Token(LogicTokenTypes.FALSE, PropositionSymbol.FALSE);
		} else if (SourceVersion.isIdentifier(symbol)){
			return new Token(LogicTokenTypes.SYMBOL, sbuf.toString());
		}
		
		throw new RuntimeException("Lexing error on symbol "+symbol);
	}

	private boolean isConnective(String aSymbol) {
		return Connective.isConnective(aSymbol);
	}
}