package aima.test.unit.antlr.propositional;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import aima.extra.logic.propositional.parser.PropositionalLogicLexer;
import aima.extra.logic.propositional.parser.PropositionalLogicParser;

@RunWith(Parameterized.class)
public class AntlrParserTest {

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
			/* Valid Sentences */
			{ true, " ~  A" },
			{ true, " A  & B" },
			{ true, " A  | B" },
			{ true, " A  => B" },
			{ true, " A  <=> B" },
			{ true, "~~A" },
			{ true, "~~~A" },
			{ true, "~~A & B" },
			{ true, "~(A & B) => C" },
			{ true, "~(A & B) => (C)" },
			{ true, "(~~(A & B) => (C))" },
			/*
			{ false, " A & B C " }, 
			{ false, " ~~A & & B " }, 
			{ false, "(A & B) ( A | B) " },
			*/
		});
	}

	private final boolean testValid;
	private final String testString;

	public AntlrParserTest(boolean testValid, String testString) {
		this.testValid = testValid;
		this.testString = testString;
	}

	@Test
	public void testSentence() {
		ANTLRInputStream input = new ANTLRInputStream(this.testString);
		PropositionalLogicLexer lexer = new PropositionalLogicLexer(input);
		TokenStream tokens = new CommonTokenStream(lexer);

		PropositionalLogicParser parser = new PropositionalLogicParser(tokens);

		parser.removeErrorListeners();
		parser.setErrorHandler(new ExceptionThrowingErrorHandler());

		if (this.testValid) {
			ParserRuleContext ruleContext = parser.sentence();
			assertNull(ruleContext.exception);
		} else {
			try {
				parser.sentence();
				fail("Failed on \"" + this.testString + "\"");
			} catch (RuntimeException e) {
				// deliberately do nothing
			}
		}
	}
}