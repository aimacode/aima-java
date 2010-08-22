package aima.core.logic.common;

import java.io.Reader;
import java.io.StringReader;

/**
 * @author Ravi Mohan
 * 
 */
public abstract class Lexer {
	protected abstract Token nextToken();

	protected Reader input;

	protected int lookAhead = 1;

	protected int[] lookAheadBuffer;

	public void setInput(String inputString) {
		lookAheadBuffer = new int[lookAhead];
		this.input = new StringReader(inputString);
		fillLookAheadBuffer();
	}

	public void clear() {
		this.input = null;
		lookAheadBuffer = null;
	}

	protected void fillLookAheadBuffer() {
		try {
			lookAheadBuffer[0] = (char) input.read();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected char lookAhead(int position) {
		return (char) lookAheadBuffer[position - 1];
	}

	protected boolean isEndOfFile(int i) {
		return (-1 == i);
	}

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