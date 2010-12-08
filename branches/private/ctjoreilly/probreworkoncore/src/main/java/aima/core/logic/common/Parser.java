package aima.core.logic.common;

/**
 * @author Ravi Mohan
 * 
 */
public abstract class Parser {

	protected Lexer lexer;

	protected Token[] lookAheadBuffer;

	protected int lookAhead = 3;

	public abstract ParseTreeNode parse(String input);

	protected void fillLookAheadBuffer() {
		for (int i = 0; i < lookAhead; i++) {
			lookAheadBuffer[i] = lexer.nextToken();
		}
	}

	protected Token lookAhead(int i) {
		return lookAheadBuffer[i - 1];
	}

	protected void consume() {
		loadNextTokenFromInput();
	}

	protected void loadNextTokenFromInput() {

		boolean eoiEncountered = false;
		for (int i = 0; i < lookAhead - 1; i++) {

			lookAheadBuffer[i] = lookAheadBuffer[i + 1];
			if (isEndOfInput(lookAheadBuffer[i])) {
				eoiEncountered = true;
				break;
			}
		}
		if (!eoiEncountered) {
			try {
				lookAheadBuffer[lookAhead - 1] = lexer.nextToken();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	protected boolean isEndOfInput(Token t) {
		return (t.getType() == LogicTokenTypes.EOI);
	}

	protected void match(String terminalSymbol) {
		if (lookAhead(1).getText().equals(terminalSymbol)) {
			consume();
		} else {
			throw new RuntimeException(
					"Syntax error detected at match. Expected "
							+ terminalSymbol + " but got "
							+ lookAhead(1).getText());
		}

	}
}