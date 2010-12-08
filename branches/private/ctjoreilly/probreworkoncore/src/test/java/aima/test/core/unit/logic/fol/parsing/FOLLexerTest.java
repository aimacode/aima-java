package aima.test.core.unit.logic.fol.parsing;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.common.LogicTokenTypes;
import aima.core.logic.common.Token;
import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.parsing.FOLLexer;

/**
 * @author Ravi Mohan
 * 
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
		Assert.assertEquals(new Token(LogicTokenTypes.LPAREN, "("), lexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.CONSTANT, "P"), lexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.RPAREN, ")"), lexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.EOI, "EOI"), lexer
				.nextToken());
	}

	@Test
	public void testConnectors() {
		lexer.setInput(" p  AND q");
		Assert.assertEquals(new Token(LogicTokenTypes.VARIABLE, "p"), lexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.CONNECTOR, "AND"), lexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.VARIABLE, "q"), lexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.EOI, "EOI"), lexer
				.nextToken());
	}

	@Test
	public void testFunctions() {
		lexer.setInput(" LeftLeg(q)");
		Assert.assertEquals(new Token(LogicTokenTypes.FUNCTION, "LeftLeg"),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.LPAREN, "("), lexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.VARIABLE, "q"), lexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.RPAREN, ")"), lexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.EOI, "EOI"), lexer
				.nextToken());
	}

	@Test
	public void testPredicate() {
		lexer.setInput(" HasColor(r)");
		Assert.assertEquals(new Token(LogicTokenTypes.PREDICATE, "HasColor"),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.LPAREN, "("), lexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.VARIABLE, "r"), lexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.RPAREN, ")"), lexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.EOI, "EOI"), lexer
				.nextToken());
	}

	@Test
	public void testMultiArgPredicate() {
		lexer.setInput(" King(x,y)");
		Assert.assertEquals(new Token(LogicTokenTypes.PREDICATE, "King"), lexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.LPAREN, "("), lexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.VARIABLE, "x"), lexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.COMMA, ","), lexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.VARIABLE, "y"), lexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.RPAREN, ")"), lexer
				.nextToken());
	}

	@Test
	public void testQuantifier() {
		lexer.setInput("FORALL x,y");
		Assert.assertEquals(new Token(LogicTokenTypes.QUANTIFIER, "FORALL"),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.VARIABLE, "x"), lexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.COMMA, ","), lexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.VARIABLE, "y"), lexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.EOI, "EOI"), lexer
				.nextToken());
	}

	@Test
	public void testTermEquality() {
		lexer.setInput("BrotherOf(John) = EnemyOf(Saladin)");
		Assert.assertEquals(new Token(LogicTokenTypes.FUNCTION, "BrotherOf"),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.LPAREN, "("), lexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.CONSTANT, "John"), lexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.RPAREN, ")"), lexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.EQUALS, "="), lexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.FUNCTION, "EnemyOf"),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.LPAREN, "("), lexer
				.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.CONSTANT, "Saladin"),
				lexer.nextToken());
		Assert.assertEquals(new Token(LogicTokenTypes.RPAREN, ")"), lexer
				.nextToken());
	}
}
