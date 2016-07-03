package aima.test.unit.logic.firstorder.parsing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.basic.common.LogicTokenTypes;
import aima.core.logic.basic.common.Token;
import aima.core.logic.basic.firstorder.domain.FOLDomain;
import aima.core.logic.basic.firstorder.parsing.FOLLexer;

/**
 * @author Ravi Mohan
 * @author Anurag Rai
 */
public class FOLLexerTest {
	FOLLexer lexer;

	@Before
	public void setUp() {
		FOLDomain domain = new FOLDomain();
		domain.addConstant("P");
		domain.addConstant("John");
		domain.addConstant("Saladin");
		domain.addFunction("LeftLeg");
		domain.addFunction("BrotherOf");
		domain.addFunction("EnemyOf");
		domain.addPredicate("HasColor");
		domain.addPredicate("King");
		lexer = new FOLLexer(domain);
	}

	@Test
	public void testLexBasicExpression() {
		lexer.setInput("( P )");
		Assert.assertEquals(new Token(LogicTokenTypes.LPAREN, "(", 0),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.CONSTANT, "P", 2),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.RPAREN, ")", 4),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.EOI, "EOI", 5),
				lexer.nextToken());
	}

	@Test
	public void testConnectors() {
		lexer.setInput(" p  & q");
		Assert.assertEquals(new Token(LogicTokenTypes.VARIABLE, "p", 1),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.CONNECTIVE, "&", 4),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.VARIABLE, "q", 6),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.EOI, "EOI", 7),
				lexer.nextToken());
	}

	@Test
	public void testFunctions() {
		lexer.setInput(" LeftLeg(q)");
		Assert.assertEquals(new Token(LogicTokenTypes.FUNCTION, "LeftLeg", 1),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.LPAREN, "(", 8),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.VARIABLE, "q", 9),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.RPAREN, ")", 10),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.EOI, "EOI", 11),
				lexer.nextToken());
	}

	@Test
	public void testPredicate() {
		lexer.setInput(" HasColor(r)");
		Assert.assertEquals(new Token(LogicTokenTypes.PREDICATE, "HasColor", 1),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.LPAREN, "(", 9),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.VARIABLE, "r", 10),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.RPAREN, ")", 11),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.EOI, "EOI", 12),
				lexer.nextToken());
	}

	@Test
	public void testMultiArgPredicate() {
		lexer.setInput(" King(x,y)");
		Assert.assertEquals(new Token(LogicTokenTypes.PREDICATE, "King", 1),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.LPAREN, "(", 5),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.VARIABLE, "x", 6),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.COMMA, ",", 7),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.VARIABLE, "y", 8),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.RPAREN, ")", 9),
				lexer.nextToken());
	}

	@Test
	public void testQuantifier() {
		lexer.setInput("FORALL x,y");
		Assert.assertEquals(new Token(LogicTokenTypes.QUANTIFIER, "FORALL", 0),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.VARIABLE, "x", 7),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.COMMA, ",", 8),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.VARIABLE, "y", 9),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.EOI, "EOI", 10),
				lexer.nextToken());
	}

	@Test
	public void testTermEquality() {
		lexer.setInput("BrotherOf(John) = EnemyOf(Saladin)");
		Assert.assertEquals(new Token(LogicTokenTypes.FUNCTION, "BrotherOf", 0),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.LPAREN, "(", 9),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.CONSTANT, "John", 10),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.RPAREN, ")", 14),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.EQUALS, "=", 16),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.FUNCTION, "EnemyOf", 18),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.LPAREN, "(", 25),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.CONSTANT, "Saladin", 26),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.RPAREN, ")", 33),
				lexer.nextToken());
	}
}
