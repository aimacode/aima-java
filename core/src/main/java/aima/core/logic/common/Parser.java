package aima.core.logic.common;

import java.io.Reader;
import java.io.StringReader;

/**
 * An abstract base class for constructing parsers for knowledge representation
 * languages. It provides a mechanism for converting a sequence of tokens
 * (derived from an appropriate lexer) into a syntactically correct abstract
 * syntax tree of the representation language.
 * 
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * 
 * @param <S> the root type of the abstract syntax tree being parsed.
 */
public abstract class Parser<S> {

	protected int lookAheadBufferSize = 1;
	//
	private Token[] lookAheadBuffer = null;

	/**
	 * 
	 * @return an instance of the Lexer to be used by a concrete implementation
	 *         of this class.
	 */
	public abstract Lexer getLexer();

	/**
	 * Parse the input concrete syntax into an abstract syntax tree.
	 * 
	 * @param input
	 *            a string representation of the concrete syntax to be parsed.
	 * @return the root node of an abstract syntax tree representation of the
	 *         the concrete input syntax that was parsed.
	 */
	public S parse(String input) {
		return parse(new StringReader(input));
	}

	/**
	 * Parse the input concrete syntax into an abstract syntax tree.
	 * 
	 * @param inputReader
	 *            a Reader of the concrete syntax to be parsed.
	 * @return the root node of an abstract syntax tree representation of the
	 *         the concrete input syntax that was parsed.
	 */
	public S parse(Reader inputReader) {
		S result = null;

		try {
			getLexer().setInput(inputReader);
			initializeLookAheadBuffer();

			result = parse();
		} catch (LexerException le) {
			throw new ParserException("Lexer Exception thrown during parsing at position "+le.getCurrentPositionInInputExceptionThrown(), le);
		}

		return result;
	}

	//
	// PROTECTED
	//

	/**
	 * To be implemented by concrete implementations of this class.
	 * 
	 * @return the root node of an abstract syntax tree representation of the
	 *         the concrete input syntax that was parsed.
	 */
	protected abstract S parse();

	/**
	 * @return the token at the specified position in the lookahead buffer.
	 */
	protected Token lookAhead(int i) {
		return lookAheadBuffer[i - 1];
	}

	/**
	 * Consume 1 token from the input.
	 */
	protected void consume() {
		loadNextTokenFromInput();
	}

	/**
	 * Consume the given match symbol if it matches the current input token. If
	 * it does not match throws a ParserException detailing the match error.
	 * 
	 * @param toMatchSymbol
	 *            the symbol to match before consuming it.
	 */
	protected void match(String toMatchSymbol) {
		if (lookAhead(1).getText().equals(toMatchSymbol)) {
			consume();
		} else {
			throw new ParserException(
					"Parser: Syntax error detected at match. Expected "
							+ toMatchSymbol + " but got "
							+ lookAhead(1).getText(), lookAhead(1));
		}

	}

	//
	// PRIVATE
	//
	private void initializeLookAheadBuffer() {
		lookAheadBuffer = new Token[lookAheadBufferSize];
		for (int i = 0; i < lookAheadBufferSize; i++) {
			// Now fill the buffer (if possible) from the input.
			lookAheadBuffer[i] = getLexer().nextToken();
			if (isEndOfInput(lookAheadBuffer[i])) {
				// The input is smaller than the buffer size
				break;
			}
		}
	}

	/*
	 * Loads the next token into the lookahead buffer if the end of the stream
	 * has not already been reached.
	 */
	private void loadNextTokenFromInput() {
		boolean eoiEncountered = false;
		for (int i = 0; i < lookAheadBufferSize - 1; i++) {
			lookAheadBuffer[i] = lookAheadBuffer[i + 1];
			if (isEndOfInput(lookAheadBuffer[i])) {
				eoiEncountered = true;
				break;
			}
		}
		if (!eoiEncountered) {
			lookAheadBuffer[lookAheadBufferSize - 1] = getLexer().nextToken();
		}
	}

	/*
	 * Returns true if the end of the stream has been reached.
	 */
	private boolean isEndOfInput(Token t) {
		return (t == null || t.getType() == LogicTokenTypes.EOI);
	}
}