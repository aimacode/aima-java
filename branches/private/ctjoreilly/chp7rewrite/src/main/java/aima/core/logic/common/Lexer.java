package aima.core.logic.common;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * An abstract base class for constructing lexical analyzers for knowledge
 * representation languages. It provides a mechanism for converting a sequence
 * of characters to a sequence of tokens that are meaningful in the
 * representation language of interest.
 * 
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public abstract class Lexer {
	protected int lookAheadBufferSize = 1;
	//
	private static final int END_OF_INPUT = -1;
	//
	private Reader input;
	private int[] lookAheadBuffer;
	private int currentPositionInInput;

	/**
	 * Sets the character stream of the lexical analyzer.
	 * 
	 * @param inputString
	 *            a sequence of characters to be converted into a sequence of
	 *            tokens.
	 */
	public void setInput(String inputString) {
		setInput(new StringReader(inputString));
	}

	/**
	 * Set the character stream reader of the lexical analyzer.
	 * 
	 * @param inputReader
	 *            a reader on a sequence of characters to be converted into a
	 *            sequence of tokens.
	 */
	public void setInput(Reader inputReader) {
		input = inputReader;
		lookAheadBuffer = new int[lookAheadBufferSize];
		currentPositionInInput = 0;
		initializeLookAheadBuffer();
	}

	/**
	 * To be implemented by concrete implementations
	 * 
	 * @return the next token from the input.
	 */
	public abstract Token nextToken();

	//
	// PROTECTED
	//
	protected int getCurrentPositionInInput() {
		return currentPositionInInput;
	}

	/*
	 * Returns the character at the specified position in the lookahead buffer.
	 */
	protected char lookAhead(int position) {
		return (char) lookAheadBuffer[position - 1];
	}

	/**
	 * Consume 1 character from the input.
	 */
	protected void consume() {
		currentPositionInInput++;
		loadNextCharacterFromInput();
	}

	//
	// PRIVATE
	//

	/**
	 * Returns true if the end of the stream has been reached.
	 */
	private boolean isEndOfInput(int i) {
		return (END_OF_INPUT == i);
	}

	/**
	 * Initialize the look ahead buffer from the input.
	 */
	private void initializeLookAheadBuffer() {
		for (int i = 0; i < lookAheadBufferSize; i++) {
			// Mark th entire buffer as being end of input.
			lookAheadBuffer[i] = END_OF_INPUT;
		}
		for (int i = 0; i < lookAheadBufferSize; i++) {
			// Now fill the buffer (if possible) from the input.
			lookAheadBuffer[i] = readInput();
			if (isEndOfInput(lookAheadBuffer[i])) {
				// The input is smaller than the buffer size
				break;
			}
		}
	}

	/**
	 * Loads the next character into the lookahead buffer if the end of the
	 * stream has not already been reached.
	 */
	private void loadNextCharacterFromInput() {
		boolean eoiEncountered = false;
		for (int i = 0; i < lookAheadBufferSize - 1; i++) {
			lookAheadBuffer[i] = lookAheadBuffer[i + 1];
			if (isEndOfInput(lookAheadBuffer[i])) {
				eoiEncountered = true;
				break;
			}
		}
		if (!eoiEncountered) {
			lookAheadBuffer[lookAheadBufferSize - 1] = readInput();
		}
	}

	private int readInput() {
		int read = -1;

		try {
			read = input.read();
		} catch (IOException ioe) {
			throw new LexerException("IOException thrown reading input.",
					currentPositionInInput, ioe);
		}

		return read;
	}
}