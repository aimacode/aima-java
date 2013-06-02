package aima.core.logic.common;

import java.util.ArrayList;
import java.util.List;

/**
 * A runtime exception to be used to describe Parser exceptions. In particular
 * it provides information to help in identifying which tokens proved
 * problematic in the parse.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class ParserException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private List<Token> problematicTokens = new ArrayList<Token>();

	public ParserException(String message, Token... problematicTokens) {
		super(message);
		if (problematicTokens != null) {
			for (Token pt : problematicTokens) {
				this.problematicTokens.add(pt);
			}
		}
	}

	public ParserException(String message, Throwable cause, Token... problematicTokens) {
		super(message, cause);
		if (problematicTokens != null) {
			for (Token pt : problematicTokens) {
				this.problematicTokens.add(pt);
			}
		}
	}

	/**
	 * 
	 * @return a list of 0 or more tokens from the input stream that are
	 *         believed to have contributed to the parse exception.
	 */
	public List<Token> getProblematicTokens() {
		return problematicTokens;
	}
}
