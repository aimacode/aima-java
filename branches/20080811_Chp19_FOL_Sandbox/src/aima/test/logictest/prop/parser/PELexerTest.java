/*
 * Created on Aug 30, 2003 by Ravi Mohan
 *
 */
package aima.test.logictest.prop.parser;

import junit.framework.TestCase;
import aima.logic.common.LogicTokenTypes;
import aima.logic.common.Token;
import aima.logic.propositional.parsing.PELexer;

/**
 * @author Ravi Mohan
 * 
 */
public class PELexerTest extends TestCase {
	public void testLexBasicExpression() {
		PELexer pelexer = new PELexer();
		pelexer.setInput("(P)");
		assertEquals(new Token(LogicTokenTypes.LPAREN, "("), pelexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.SYMBOL, "P"), pelexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.RPAREN, ")"), pelexer
				.nextToken());

		assertEquals(new Token(LogicTokenTypes.EOI, "EOI"), pelexer.nextToken());
	}

	public void testLexNotExpression() {
		PELexer pelexer = new PELexer();
		pelexer.setInput("(NOT P)");
		assertEquals(new Token(LogicTokenTypes.LPAREN, "("), pelexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.CONNECTOR, "NOT"), pelexer
				.nextToken());

		assertEquals(new Token(LogicTokenTypes.SYMBOL, "P"), pelexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.RPAREN, ")"), pelexer
				.nextToken());

		assertEquals(new Token(LogicTokenTypes.EOI, "EOI"), pelexer.nextToken());
	}

	public void testLexImpliesExpression() {
		PELexer pelexer = new PELexer();
		pelexer.setInput("(P => Q)");
		assertEquals(new Token(LogicTokenTypes.LPAREN, "("), pelexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.SYMBOL, "P"), pelexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.CONNECTOR, "=>"), pelexer
				.nextToken());
	}

	public void testLexBiCOnditionalExpression() {
		PELexer pelexer = new PELexer();
		pelexer.setInput("(B11 <=> (P12 OR P21))");
		assertEquals(new Token(LogicTokenTypes.LPAREN, "("), pelexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.SYMBOL, "B11"), pelexer
				.nextToken());
		assertEquals(new Token(LogicTokenTypes.CONNECTOR, "<=>"), pelexer
				.nextToken());
	}
}
