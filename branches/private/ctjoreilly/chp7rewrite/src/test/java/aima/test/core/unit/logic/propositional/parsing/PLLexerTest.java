package aima.test.core.unit.logic.propositional.parsing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.common.LexerException;
import aima.core.logic.common.LogicTokenTypes;
import aima.core.logic.common.Token;
import aima.core.logic.propositional.parsing.PLLexer;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * 
 */
public class PLLexerTest {
	
	private PLLexer pllexer;
	
	@Before
	public void setUp() {
		pllexer = new PLLexer();
	}

	@Test
	public void testLexBasicExpression() {
		pllexer.setInput("(P)");
		Assert.assertEquals(new Token(LogicTokenTypes.LPAREN, "(", 0),
				pllexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.SYMBOL, "P", 1),
				pllexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.RPAREN, ")", 2),
				pllexer.nextToken());

		Assert.assertEquals(new Token(LogicTokenTypes.EOI, "EOI", 3),
				pllexer.nextToken());
	}

	@Test
	public void testLexNotExpression() {
		pllexer.setInput("(~ P)");
		Assert.assertEquals(new Token(LogicTokenTypes.LPAREN, "(", 0),
				pllexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.CONNECTIVE, "~", 1),
				pllexer.nextToken());

		Assert.assertEquals(new Token(LogicTokenTypes.SYMBOL, "P", 3),
				pllexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.RPAREN, ")", 4),
				pllexer.nextToken());

		Assert.assertEquals(new Token(LogicTokenTypes.EOI, "EOI", 5),
				pllexer.nextToken());
	}

	@Test
	public void testLexImpliesExpression() {
		pllexer.setInput("(P => Q)");
		Assert.assertEquals(new Token(LogicTokenTypes.LPAREN, "(", 0),
				pllexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.SYMBOL, "P", 1),
				pllexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.CONNECTIVE, "=>", 3),
				pllexer.nextToken());
	}

	@Test
	public void testLexBiCOnditionalExpression() {
		pllexer.setInput("(B11 <=> (P12 | P21))");
		Assert.assertEquals(new Token(LogicTokenTypes.LPAREN, "(", 0), pllexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.SYMBOL, "B11", 1),pllexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.CONNECTIVE, "<=>", 5),
				pllexer.nextToken());
	}
	
	@Test
	public void testChainedConnectiveExpression() {
		pllexer.setInput("~~&&||=>=><=><=>");
		Assert.assertEquals(new Token(LogicTokenTypes.CONNECTIVE, "~", 0), pllexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.CONNECTIVE, "~", 1), pllexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.CONNECTIVE, "&", 2), pllexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.CONNECTIVE, "&", 3), pllexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.CONNECTIVE, "|", 4), pllexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.CONNECTIVE, "|", 5), pllexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.CONNECTIVE, "=>", 6), pllexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.CONNECTIVE, "=>", 8), pllexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.CONNECTIVE, "<=>", 10), pllexer.nextToken());
	}
	
	@Test
	public void testLexerException() {
		try {
			pllexer.setInput("A & B.1 & C");
			pllexer.nextToken();
			pllexer.nextToken();
			pllexer.nextToken();
			// the , after 'B' is not a legal character
			pllexer.nextToken();
			Assert.fail("A LexerException should have been thrown here");
		} catch (LexerException le) {
			// Ensure the correct position in the input is identified.
			Assert.assertEquals(5, le.getCurrentPositionInInputExceptionThrown());
		}
	}
}
