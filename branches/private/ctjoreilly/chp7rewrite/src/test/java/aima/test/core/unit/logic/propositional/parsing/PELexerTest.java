package aima.test.core.unit.logic.propositional.parsing;

import org.junit.Assert;
import org.junit.Test;

import aima.core.logic.common.LogicTokenTypes;
import aima.core.logic.common.Token;
import aima.core.logic.propositional.parsing.PLLexer;

/**
 * @author Ravi Mohan
 * 
 */
public class PELexerTest {

	@Test
	public void testLexBasicExpression() {
		PLLexer pelexer = new PLLexer();
		pelexer.setInput("(P)");
		Assert.assertEquals(new Token(LogicTokenTypes.LPAREN, "(", 0),
				pelexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.SYMBOL, "P", 1),
				pelexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.RPAREN, ")", 2),
				pelexer.nextToken());

		Assert.assertEquals(new Token(LogicTokenTypes.EOI, "EOI", 3),
				pelexer.nextToken());
	}

	@Test
	public void testLexNotExpression() {
		PLLexer pelexer = new PLLexer();
		pelexer.setInput("(~ P)");
		Assert.assertEquals(new Token(LogicTokenTypes.LPAREN, "(", 0),
				pelexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.CONNECTIVE, "~", 1),
				pelexer.nextToken());

		Assert.assertEquals(new Token(LogicTokenTypes.SYMBOL, "P", 3),
				pelexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.RPAREN, ")", 4),
				pelexer.nextToken());

		Assert.assertEquals(new Token(LogicTokenTypes.EOI, "EOI", 5),
				pelexer.nextToken());
	}

	@Test
	public void testLexImpliesExpression() {
		PLLexer pelexer = new PLLexer();
		pelexer.setInput("(P => Q)");
		Assert.assertEquals(new Token(LogicTokenTypes.LPAREN, "(", 0),
				pelexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.SYMBOL, "P", 1),
				pelexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.CONNECTIVE, "=>", 3),
				pelexer.nextToken());
	}

	@Test
	public void testLexBiCOnditionalExpression() {
		PLLexer pelexer = new PLLexer();
		pelexer.setInput("(B11 <=> (P12 OR P21))");
		Assert.assertEquals(new Token(LogicTokenTypes.LPAREN, "(", 0),
				pelexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.SYMBOL, "B11", 1),
				pelexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.CONNECTIVE, "<=>", 5),
				pelexer.nextToken());
	}
}
