package aima.test.core.unit.logic.propositional.parsing;

import org.junit.Assert;
import org.junit.Test;

import aima.core.logic.common.LogicTokenTypes;
import aima.core.logic.common.Token;
import aima.core.logic.propositional.parsing.PELexer;

/**
 * @author Ravi Mohan
 * 
 */
public class PELexerTest {

	@Test
	public void testLexBasicExpression() {
		PELexer pelexer = new PELexer();
		pelexer.setInput("(P)");
		Assert.assertEquals(new Token(LogicTokenTypes.LPAREN, "("), pelexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.SYMBOL, "P"), pelexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.RPAREN, ")"), pelexer
				.nextToken());

		Assert.assertEquals(new Token(LogicTokenTypes.EOI, "EOI"), pelexer
				.nextToken());
	}

	@Test
	public void testLexNotExpression() {
		PELexer pelexer = new PELexer();
		pelexer.setInput("(NOT P)");
		Assert.assertEquals(new Token(LogicTokenTypes.LPAREN, "("), pelexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.CONNECTOR, "NOT"),
				pelexer.nextToken());

		Assert.assertEquals(new Token(LogicTokenTypes.SYMBOL, "P"), pelexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.RPAREN, ")"), pelexer
				.nextToken());

		Assert.assertEquals(new Token(LogicTokenTypes.EOI, "EOI"), pelexer
				.nextToken());
	}

	@Test
	public void testLexImpliesExpression() {
		PELexer pelexer = new PELexer();
		pelexer.setInput("(P => Q)");
		Assert.assertEquals(new Token(LogicTokenTypes.LPAREN, "("), pelexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.SYMBOL, "P"), pelexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.CONNECTOR, "=>"), pelexer
				.nextToken());
	}

	@Test
	public void testLexBiCOnditionalExpression() {
		PELexer pelexer = new PELexer();
		pelexer.setInput("(B11 <=> (P12 OR P21))");
		Assert.assertEquals(new Token(LogicTokenTypes.LPAREN, "("), pelexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.SYMBOL, "B11"), pelexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.CONNECTOR, "<=>"),
				pelexer.nextToken());
	}
}
