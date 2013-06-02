package aima.core.logic.common;

/**
 * A runtime exception to be used to describe Lexer exceptions. In particular it
 * provides information to help in identifying where in the input character
 * sequence the exception occurred.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class LexerException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private int currentPositionInInput;

	public LexerException(String message, int currentPositionInInput) {
		super(message);
		this.currentPositionInInput = currentPositionInInput;
	}

	public LexerException(String message, int currentPositionInInput,
			Throwable cause) {
		super(message, cause);
		this.currentPositionInInput = currentPositionInInput;
	}

	/**
	 * 
	 * @return the current position in the input character stream that the lexer
	 *         was at before the exception was encountered.
	 */
	public int getCurrentPositionInInputExceptionThrown() {
		return currentPositionInInput;
	}
}
