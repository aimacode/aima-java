package aima.core.logic.common;

import java.io.Reader;
import java.io.StringReader;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public abstract class Lexer {
	protected abstract Token nextToken();

	protected Reader input;

	protected int lookAhead = 1;

	protected int[] lookAheadBuffer;

	/**
	 * Sets the character stream of the lexical analyzer.
	 * 
	 * @param inputString
	 *            a sequence of characters to be converted into a sequence of
	 *            tokens.
	 */
	public void setInput(String inputString) {
		lookAheadBuffer = new int[lookAhead];
		this.input = new StringReader(inputString);
		fillLookAheadBuffer();
	}

	/**
	 * Sets the character stream and look ahead buffer to <code>null</code>.
	 */
	public void clear() {
		this.input = null;
		lookAheadBuffer = null;
	}

	/*
	 * Stores the next character in the lookahead buffer to make parsing action
	 * decisions.
	 */
	protected void fillLookAheadBuffer() {
		try {
			lookAheadBuffer[0] = (char) input.read();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * Returns the character at the specified position in the lookahead buffer.
	 */
	protected char lookAhead(int position) {
		return (char) lookAheadBuffer[position - 1];
	}

	/*
	 * Returns true if the end of the stream has been reached.
	 */
	protected boolean isEndOfFile(int i) {
		return (-1 == i);
	}

	/*
	 * Loads the next character into the lookahead buffer if the end of the
	 * stream has not already been reached.
	 */
	protected void loadNextCharacterFromInput() {

		boolean eofEncountered = false;
		for (int i = 0; i < lookAhead - 1; i++) {

			lookAheadBuffer[i] = lookAheadBuffer[i + 1];
			if (isEndOfFile(lookAheadBuffer[i])) {
				eofEncountered = true;
				break;
			}
		}
		if (!eofEncountered) {
			try {
				lookAheadBuffer[lookAhead - 1] = input.read();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	protected void consume() {
		loadNextCharacterFromInput();
	}
}