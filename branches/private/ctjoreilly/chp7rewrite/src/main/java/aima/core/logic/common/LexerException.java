package aima.core.logic.common;

/**
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
	
	public LexerException(String message, int currentPositionInInput, Throwable cause) {
		super(message, cause);
		this.currentPositionInInput = currentPositionInInput;
	}
	
	public int getCurrentPositionInInputExceptionThrown() {
		return currentPositionInInput;
	}
 }
