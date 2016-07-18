package aima.test.unit.antlr.propositional;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * Error handler which throws exception on any parsing error.
 */

public class ExceptionThrowingErrorListener extends BaseErrorListener {
	@Override
	public void syntaxError(Recognizer<?,?> recognizer,
            Object offendingSymbol,
            int line,
            int charPositionInLine,
            String msg,
            RecognitionException e) {
		throw new RuntimeException(e);
	}
}